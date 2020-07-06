package com.radcortez.wow.auctions.batch.process.purge;

import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.wow.auctions.QuarkusDataSourceProvider;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan St. Ivanov
 */
@QuarkusTest
@FlywayTest(@DataSource(QuarkusDataSourceProvider.class))
@Transactional
public class PurgeRawAuctionDataBatchletTest {
    @Inject
    EntityManager em;
    @Inject
    PurgeRawAuctionDataBatchlet batchlet;
    @Inject
    JobContext jobContext;

    @BeforeEach
    public void setUp() {
        jobContext.getProperties().setProperty("auctionFileId", "1");
    }

    @Test
    public void testPurgeRawAuctionData() {
        batchlet.process();
        assertEquals(1, em.createQuery("SELECT a FROM Auction a").getResultList().size());
    }
}
