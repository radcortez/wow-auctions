package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.batch.util.AuctionsBuilder;
import com.radcortez.wow.auctions.entity.*;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;


/**
 * @author Ivan St. Ivanov
 */
@RunWith(CdiTestRunner.class)
public class AuctionsProcessorTest {

    @Inject
    private EntityManager em;

    @Inject
    private JobContext jobContext;

    @Before
    public void setUp() throws Exception {
        em.getTransaction().begin();

        AuctionFile auctionFile = new AuctionFile();
        auctionFile.setFileStatus(FileStatus.PROCESSED);
        auctionFile.setLastModified(155555L);
        em.persist(auctionFile);

        Realm realmGrimBatol = new Realm();
        realmGrimBatol.setName("Grim Batol");
        realmGrimBatol.setSlug("grim-batol");
        em.persist(realmGrimBatol);

        Realm realmEndTime = new Realm();
        realmEndTime.setName("End Time");
        realmEndTime.setSlug("end-time");
        em.persist(realmEndTime);

        jobContext.getProperties().setProperty("auctionFileId", auctionFile.getId().toString());

        em.persist(AuctionsBuilder.buildAuction()
                .withAuctionId(101L)
                .withAuctionFile(auctionFile)
                .withBid(125)
                .withBuyout(160)
                .withItemId(123)
                .withQuantity(1)
                .withRealm(realmGrimBatol)
                .withAuctionHouse(AuctionHouse.ALLIANCE)
                .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(102L)
                        .withAuctionFile(auctionFile)
                        .withBid(213)
                        .withBuyout(255)
                        .withItemId(123)
                        .withQuantity(3)
                        .withRealm(realmGrimBatol)
                        .withAuctionHouse(AuctionHouse.ALLIANCE)
                        .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(103L)
                        .withAuctionFile(auctionFile)
                        .withBid(595)
                        .withBuyout(700)
                        .withItemId(123)
                        .withQuantity(5)
                        .withRealm(realmGrimBatol)
                        .withAuctionHouse(AuctionHouse.ALLIANCE)
                        .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(104L)
                        .withAuctionFile(auctionFile)
                        .withBid(26)
                        .withBuyout(80)
                        .withItemId(123)
                        .withQuantity(1)
                        .withRealm(realmGrimBatol)
                        .withAuctionHouse(AuctionHouse.HORDE)
                        .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(105L)
                        .withAuctionFile(auctionFile)
                        .withBid(75)
                        .withBuyout(75)
                        .withItemId(123)
                        .withQuantity(5)
                        .withRealm(realmGrimBatol)
                        .withAuctionHouse(AuctionHouse.HORDE)
                        .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(106L)
                        .withAuctionFile(auctionFile)
                        .withBid(220)
                        .withBuyout(420)
                        .withItemId(99)
                        .withQuantity(10)
                        .withRealm(realmGrimBatol)
                        .withAuctionHouse(AuctionHouse.HORDE)
                        .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(107L)
                        .withAuctionFile(auctionFile)
                        .withBid(26)
                        .withBuyout(66)
                        .withItemId(99)
                        .withQuantity(1)
                        .withRealm(realmEndTime)
                        .withAuctionHouse(AuctionHouse.ALLIANCE)
                        .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(108L)
                        .withAuctionFile(auctionFile)
                        .withBid(185)
                        .withBuyout(205)
                        .withItemId(99)
                        .withQuantity(5)
                        .withRealm(realmEndTime)
                        .withAuctionHouse(AuctionHouse.ALLIANCE)
                        .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(109L)
                        .withAuctionFile(auctionFile)
                        .withBid(54)
                        .withBuyout(54)
                        .withItemId(99)
                        .withQuantity(18)
                        .withRealm(realmEndTime)
                        .withAuctionHouse(AuctionHouse.ALLIANCE)
                        .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(110L)
                        .withAuctionFile(auctionFile)
                        .withBid(125)
                        .withBuyout(220)
                        .withItemId(48)
                        .withQuantity(1)
                        .withRealm(realmEndTime)
                        .withAuctionHouse(AuctionHouse.ALLIANCE)
                        .get());

        em.getTransaction().commit();
    }

    @Inject
    private ProcessedAuctionsReader processedAuctionsReader;

    @Test
    public void testProcessedAuctionsReader() throws Exception {
        ResultSet rs = getResultSetForAuctionHouse(AuctionHouse.ALLIANCE.name());
        rs.last();
        assertEquals(3, rs.getRow());

        rs = getResultSetForAuctionHouse(AuctionHouse.HORDE.name());
        rs.last();
        assertEquals(2, rs.getRow());
    }

    @Inject
    private ProcessedAuctionsProcessor processor;

    @Test
    public void testProcessedAuctionsProcessor() throws Exception {
        assertForAuctionHouse(AuctionHouse.ALLIANCE);
        assertForAuctionHouse(AuctionHouse.HORDE);
    }

    private void assertForAuctionHouse(AuctionHouse testAuctionHouse) throws Exception {
        processor.auctionHouse = testAuctionHouse.name();
        ResultSet resultSetForAlliance = getResultSetForAuctionHouse(testAuctionHouse.name());
        while (resultSetForAlliance.next()) {
            AuctionItemStatistics auctionItemStatistics = (AuctionItemStatistics) processor.processItem(resultSetForAlliance);
            assertAuctionItemStatistics(auctionItemStatistics, testAuctionHouse);
        }
    }

    private void assertAuctionItemStatistics(AuctionItemStatistics auctionItemStatistics, AuctionHouse auctionHouse) {
        switch (auctionItemStatistics.getItemId()) {
            case 123:
                if (auctionHouse == AuctionHouse.ALLIANCE) {
                    assertEquals(71, auctionItemStatistics.getMinBid().intValue());
                    assertEquals(125, auctionItemStatistics.getMaxBid().intValue());
                    assertEquals(85, auctionItemStatistics.getMinBuyout().intValue());
                    assertEquals(160, auctionItemStatistics.getMaxBuyout().intValue());
                    assertEquals(103d, auctionItemStatistics.getAvgBid(), 0.1d);
                    assertEquals(123d, auctionItemStatistics.getAvgBuyout(), 0.1d);
                } else {
                    assertEquals(15, auctionItemStatistics.getMinBid().intValue());
                    assertEquals(26, auctionItemStatistics.getMaxBid().intValue());
                    assertEquals(15, auctionItemStatistics.getMinBuyout().intValue());
                    assertEquals(80, auctionItemStatistics.getMaxBuyout().intValue());
                    assertEquals(16d, auctionItemStatistics.getAvgBid(), 0.1d);
                    assertEquals(25d, auctionItemStatistics.getAvgBuyout(), 0.1d);
                }
                break;
            case 48:
                assertEquals(125, auctionItemStatistics.getMinBid().intValue());
                assertEquals(125, auctionItemStatistics.getMaxBid().intValue());
                assertEquals(220, auctionItemStatistics.getMinBuyout().intValue());
                assertEquals(220, auctionItemStatistics.getMaxBuyout().intValue());
                assertEquals(116d, auctionItemStatistics.getAvgBid(), 0.1d);
                assertEquals(220d, auctionItemStatistics.getAvgBuyout(), 0.1d);
                break;
            case 99:
                if (auctionHouse == AuctionHouse.ALLIANCE) {
                    assertEquals(3, auctionItemStatistics.getMinBid().intValue());
                    assertEquals(37, auctionItemStatistics.getMaxBid().intValue());
                    assertEquals(3, auctionItemStatistics.getMinBuyout().intValue());
                    assertEquals(66, auctionItemStatistics.getMaxBuyout().intValue());
                    assertEquals(11d, auctionItemStatistics.getAvgBid(), 0.1d);
                    assertEquals(13d, auctionItemStatistics.getAvgBuyout(), 0.1d);
                } else {
                    assertEquals(22, auctionItemStatistics.getMinBid().intValue());
                    assertEquals(42, auctionItemStatistics.getMaxBid().intValue());
                    assertEquals(22, auctionItemStatistics.getMinBuyout().intValue());
                    assertEquals(42, auctionItemStatistics.getMaxBuyout().intValue());
                    assertEquals(22d, auctionItemStatistics.getAvgBid(), 0.1d);
                    assertEquals(42d, auctionItemStatistics.getAvgBuyout(), 0.1d);
                }
                break;
        }

    }

    private ResultSet getResultSetForAuctionHouse(String auctonHouseName) throws Exception {
        processedAuctionsReader.auctionHouse = auctonHouseName;
        processedAuctionsReader.open(null);
        return (ResultSet) processedAuctionsReader.readItem();
    }
}
