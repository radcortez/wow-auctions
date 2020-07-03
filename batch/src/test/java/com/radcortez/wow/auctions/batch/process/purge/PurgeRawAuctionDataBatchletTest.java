package com.radcortez.wow.auctions.batch.process.purge;

import com.radcortez.wow.auctions.batch.util.AuctionsBuilder;
import com.radcortez.wow.auctions.entity.Auction;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.FileStatus;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan St. Ivanov
 */
public class PurgeRawAuctionDataBatchletTest {
    @Inject
    EntityManager em;
    @Inject
    PurgeRawAuctionDataBatchlet batchlet;
    @Inject
    JobContext jobContext;

    @BeforeEach
    public void setUp() {
        em.getTransaction().begin();

        AuctionFile processedFile = new AuctionFile();
        processedFile.setFileStatus(FileStatus.PROCESSED);

        AuctionFile otherFile = new AuctionFile();
        otherFile.setFileStatus(FileStatus.PROCESSED);

        em.persist(processedFile); em.persist(otherFile);

        jobContext.getProperties().setProperty("auctionFileId", processedFile.getId().toString());

        Auction processedAuction1 = AuctionsBuilder.buildAuction()
                            .withAuctionId(101L)
                            .withAuctionFile(processedFile)
                            .get();

        Auction processedAuction2 = AuctionsBuilder.buildAuction()
                            .withAuctionId(102L)
                            .withAuctionFile(processedFile)
                            .get();

        Auction otherAuction = AuctionsBuilder.buildAuction()
                            .withAuctionId(103L)
                            .withAuctionFile(otherFile)
                            .get();

        em.persist(processedAuction1); em.persist(processedAuction2); em.persist(otherAuction);

        em.flush();
    }

    @AfterEach
    public void tearDown() {
        em.getTransaction().rollback();
    }

    @Test
    public void testPurgeRawAuctionData() {
        batchlet.process();
        assertEquals(1, em.createQuery("SELECT a FROM Auction a").getResultList().size());
    }
}
