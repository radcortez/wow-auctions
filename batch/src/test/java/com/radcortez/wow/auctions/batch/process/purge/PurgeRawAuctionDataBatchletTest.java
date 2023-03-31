package com.radcortez.wow.auctions.batch.process.purge;

import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.wow.auctions.QuarkusDataSourceProvider;
import com.radcortez.wow.auctions.entity.AuctionFile;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import jakarta.batch.runtime.context.JobContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan St. Ivanov
 */
@QuarkusTest
@FlywayTest(@DataSource(QuarkusDataSourceProvider.class))
@Transactional
@Disabled
public class PurgeRawAuctionDataBatchletTest {
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
        assertEquals(1, AuctionFile.listAll().size());
    }
}
