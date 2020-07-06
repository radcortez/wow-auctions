package com.radcortez.wow.auctions.batch.process.statistics;

import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.wow.auctions.QuarkusDataSourceProvider;
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
@FlywayTest(@DataSource(QuarkusDataSourceProvider.class))
public class AuctionsProcessorTest {
    @Inject
    JobContext jobContext;
    @Inject
    ProcessedAuctionsReader processedAuctionsReader;
    @Inject
    ProcessedAuctionsProcessor processor;

    @BeforeEach
    public void setUp() {
        jobContext.getProperties().setProperty("auctionFileId", "1");
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
