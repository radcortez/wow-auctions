package com.radcortez.wow.auctions.batch.process.purge;

import com.radcortez.wow.auctions.batch.util.AuctionsBuilder;
import com.radcortez.wow.auctions.entity.Auction;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.FileStatus;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;

/**
 * @author Ivan St. Ivanov
 */
@RunWith(CdiTestRunner.class)
public class PurgeRawAuctionDataBatchletTest {
    @Inject
    private EntityManager em;
    @Inject
    private PurgeRawAuctionDataBatchlet batchlet;
    @Inject
    private JobContext jobContext;

    @Before
    public void setUp() throws Exception {
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

    @After
    public void tearDown() throws Exception {
        em.getTransaction().rollback();
    }

    @Test
    public void testPurgeRawAuctionData() throws Exception {
        batchlet.process();
        assertEquals(1, em.createQuery("SELECT a FROM Auction a").getResultList().size());
    }
}
