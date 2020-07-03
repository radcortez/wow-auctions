package com.radcortez.wow.auctions.batch.process.statistics;

import com.radcortez.wow.auctions.batch.util.AuctionsBuilder;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.AuctionItemStatistics;
import com.radcortez.wow.auctions.entity.FileStatus;
import com.radcortez.wow.auctions.entity.Realm;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan St. Ivanov
 */
@QuarkusTest
public class AuctionsProcessorTest {
    @Inject
    EntityManager em;
    @Inject
    JobContext jobContext;
    @Inject
    ProcessedAuctionsReader processedAuctionsReader;
    @Inject
    ProcessedAuctionsProcessor processor;

    @BeforeEach
    public void setUp() {
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
                .withBid(125L)
                .withBuyout(160L)
                .withItemId(123)
                .withQuantity(1)
                .withRealm(realmGrimBatol)
                .get());
        em.persist(AuctionsBuilder.buildAuction()
                .withAuctionId(102L)
                .withAuctionFile(auctionFile)
                .withBid(213L)
                .withBuyout(255L)
                .withItemId(123)
                .withQuantity(3)
                .withRealm(realmGrimBatol)
                .get());
        em.persist(AuctionsBuilder.buildAuction()
                .withAuctionId(103L)
                .withAuctionFile(auctionFile)
                .withBid(595L)
                .withBuyout(700L)
                .withItemId(123)
                .withQuantity(5)
                .withRealm(realmGrimBatol)
                .get());
        em.persist(AuctionsBuilder.buildAuction()
                .withAuctionId(107L)
                .withAuctionFile(auctionFile)
                .withBid(26L)
                .withBuyout(66L)
                .withItemId(99)
                .withQuantity(1)
                .withRealm(realmEndTime)
                .get());
        em.persist(AuctionsBuilder.buildAuction()
                .withAuctionId(108L)
                .withAuctionFile(auctionFile)
                .withBid(185L)
                .withBuyout(205L)
                .withItemId(99)
                .withQuantity(5)
                .withRealm(realmEndTime)
                .get());
        em.persist(AuctionsBuilder.buildAuction()
                .withAuctionId(109L)
                .withAuctionFile(auctionFile)
                .withBid(54L)
                .withBuyout(54L)
                .withItemId(99)
                .withQuantity(18)
                .withRealm(realmEndTime)
                .get());
        em.persist(AuctionsBuilder.buildAuction()
                .withAuctionId(110L)
                .withAuctionFile(auctionFile)
                .withBid(125L)
                .withBuyout(220L)
                .withItemId(48)
                .withQuantity(1)
                .withRealm(realmEndTime)
                .get());

        em.getTransaction().commit();
    }

    @Test
    public void testProcessedAuctionsReader() throws Exception {
        processedAuctionsReader.open(null);
        ResultSet rs = (ResultSet) processedAuctionsReader.readItem();
        rs.last();
        assertEquals(3, rs.getRow());
    }

    @Test
    public void testProcessedAuctionsProcessor() throws Exception {
        processedAuctionsReader.open(null);
        ResultSet resultSet = (ResultSet) processedAuctionsReader.readItem();
        while (resultSet.next()) {
            AuctionItemStatistics auctionItemStatistics = (AuctionItemStatistics) processor.processItem(resultSet);
            assertAuctionItemStatistics(auctionItemStatistics);
        }
    }

    private void assertAuctionItemStatistics(AuctionItemStatistics auctionItemStatistics) {
        switch (auctionItemStatistics.getItemId()) {
            case 123:
                assertEquals(71, auctionItemStatistics.getMinBid().intValue());
                assertEquals(125, auctionItemStatistics.getMaxBid().intValue());
                assertEquals(85, auctionItemStatistics.getMinBuyout().intValue());
                assertEquals(160, auctionItemStatistics.getMaxBuyout().intValue());
                assertEquals(103d, auctionItemStatistics.getAvgBid(), 0.1d);
                assertEquals(123d, auctionItemStatistics.getAvgBuyout(), 0.1d);
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
                assertEquals(3, auctionItemStatistics.getMinBid().intValue());
                assertEquals(37, auctionItemStatistics.getMaxBid().intValue());
                assertEquals(3, auctionItemStatistics.getMinBuyout().intValue());
                assertEquals(66, auctionItemStatistics.getMaxBuyout().intValue());
                assertEquals(11d, auctionItemStatistics.getAvgBid(), 0.1d);
                assertEquals(13d, auctionItemStatistics.getAvgBuyout(), 0.1d);
        }
    }
}
