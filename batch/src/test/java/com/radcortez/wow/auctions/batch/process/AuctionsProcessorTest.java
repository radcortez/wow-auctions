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
                .withBuyout(125)
                .withItemId(123)
                .withQuantity(1)
                .withRealm(realmGrimBatol)
                .withAuctionHouse(AuctionHouse.ALLIANCE)
                .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(102L)
                        .withAuctionFile(auctionFile)
                        .withBid(212)
                        .withBuyout(212)
                        .withItemId(123)
                        .withQuantity(3)
                        .withRealm(realmGrimBatol)
                        .withAuctionHouse(AuctionHouse.ALLIANCE)
                        .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(103L)
                        .withAuctionFile(auctionFile)
                        .withBid(98)
                        .withBuyout(98)
                        .withItemId(123)
                        .withQuantity(5)
                        .withRealm(realmGrimBatol)
                        .withAuctionHouse(AuctionHouse.ALLIANCE)
                        .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(104L)
                        .withAuctionFile(auctionFile)
                        .withBid(26)
                        .withBuyout(26)
                        .withItemId(123)
                        .withQuantity(1)
                        .withRealm(realmGrimBatol)
                        .withAuctionHouse(AuctionHouse.HORDE)
                        .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(105L)
                        .withAuctionFile(auctionFile)
                        .withBid(78)
                        .withBuyout(78)
                        .withItemId(123)
                        .withQuantity(5)
                        .withRealm(realmGrimBatol)
                        .withAuctionHouse(AuctionHouse.HORDE)
                        .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(106L)
                        .withAuctionFile(auctionFile)
                        .withBid(22)
                        .withBuyout(22)
                        .withItemId(99)
                        .withQuantity(10)
                        .withRealm(realmGrimBatol)
                        .withAuctionHouse(AuctionHouse.HORDE)
                        .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(107L)
                        .withAuctionFile(auctionFile)
                        .withBid(266)
                        .withBuyout(266)
                        .withItemId(99)
                        .withQuantity(1)
                        .withRealm(realmEndTime)
                        .withAuctionHouse(AuctionHouse.ALLIANCE)
                        .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(108L)
                        .withAuctionFile(auctionFile)
                        .withBid(188)
                        .withBuyout(188)
                        .withItemId(99)
                        .withQuantity(5)
                        .withRealm(realmEndTime)
                        .withAuctionHouse(AuctionHouse.ALLIANCE)
                        .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(109L)
                        .withAuctionFile(auctionFile)
                        .withBid(56)
                        .withBuyout(56)
                        .withItemId(99)
                        .withQuantity(18)
                        .withRealm(realmEndTime)
                        .withAuctionHouse(AuctionHouse.ALLIANCE)
                        .get());
        em.persist(AuctionsBuilder.buildAuction()
                        .withAuctionId(110L)
                        .withAuctionFile(auctionFile)
                        .withBid(125)
                        .withBuyout(125)
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

    @Test
    public void testProcessedAuctionsProcessor() throws Exception {
        ProcessedAuctionsProcessor processor = new ProcessedAuctionsProcessor();
        ResultSet resultSetForAlliance = getResultSetForAuctionHouse(AuctionHouse.ALLIANCE.name());
        while (resultSetForAlliance.next()) {
            AuctionItemStatistics auctionItemStatistics = (AuctionItemStatistics) processor.processItem(resultSetForAlliance);
            assertAuctionItemStatistics(auctionItemStatistics, AuctionHouse.ALLIANCE);
        }
    }

    private void assertAuctionItemStatistics(AuctionItemStatistics auctionItemStatistics, AuctionHouse auctionHouse) {
        switch (auctionItemStatistics.getItemId()) {
            case 123:
                if (auctionHouse == AuctionHouse.ALLIANCE) {
                    assertEquals(98, auctionItemStatistics.getMinBid().intValue());
                    assertEquals(212, auctionItemStatistics.getMaxBid().intValue());
//                    assertEquals(145d, auctionItemStatistics.getAvgBid(), 0.1d);
                } else {
                    assertEquals(26, auctionItemStatistics.getMinBid().intValue());
                    assertEquals(78, auctionItemStatistics.getMaxBid().intValue());
//                    assertEquals(52d, auctionItemStatistics.getAvgBid(), 0.1d);
                }
                break;
            case 48:
                assertEquals(116, auctionItemStatistics.getMinBid().intValue());
                assertEquals(116, auctionItemStatistics.getMaxBid().intValue());
//                assertEquals(116d, auctionItemStatistics.getAvgBid(), 0.1d);
                break;
            case 99:
                if (auctionItemStatistics.getMinBid().equals(auctionItemStatistics.getMaxBid())) {
                    assertEquals(22, auctionItemStatistics.getMinBid().intValue());
                    assertEquals(22, auctionItemStatistics.getMaxBid().intValue());
//                    assertEquals(22d, auctionItemStatistics.getAvgBid(), 0.1d);
                } else {
                    assertEquals(56, auctionItemStatistics.getMinBid().intValue());
                    assertEquals(266, auctionItemStatistics.getMaxBid().intValue());
//                    assertEquals(170d, auctionItemStatistics.getAvgBid(), 0.1d);
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
