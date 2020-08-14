package com.radcortez.wow.auctions.batch.process.statistics;

import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.wow.auctions.QuarkusDataSourceProvider;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.AuctionStatistics;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan St. Ivanov
 */
@QuarkusTest
@FlywayTest(@DataSource(QuarkusDataSourceProvider.class))
public class AuctionsProcessorTest {
    @Inject
    ProcessedAuctionsReader processedAuctionsReader;
    @Inject
    ProcessedAuctionsProcessor processor;

    @Test
    public void testProcessedAuctionsReader() throws Exception {
        processedAuctionsReader.getContext().setAuctionFile(AuctionFile.findById(1L));
        processedAuctionsReader.open(null);
        ResultSet rs = (ResultSet) processedAuctionsReader.readItem();
        rs.last();
        assertEquals(3, rs.getRow());
    }

    @Test
    public void testProcessedAuctionsProcessor() throws Exception {
        processedAuctionsReader.getContext().setAuctionFile(AuctionFile.findById(1L));
        processedAuctionsReader.open(null);
        ResultSet resultSet = (ResultSet) processedAuctionsReader.readItem();
        while (resultSet.next()) {
            AuctionStatistics auctionStatistics = (AuctionStatistics) processor.processItem(resultSet);
            assertAuctionItemStatistics(auctionStatistics);
        }
    }

    private void assertAuctionItemStatistics(AuctionStatistics auctionStatistics) {
        switch (auctionStatistics.getItemId()) {
            case 123:
                assertEquals(71, auctionStatistics.getMinBid().intValue());
                assertEquals(125, auctionStatistics.getMaxBid().intValue());
                assertEquals(85, auctionStatistics.getMinBuyout().intValue());
                assertEquals(160, auctionStatistics.getMaxBuyout().intValue());
                assertEquals(103d, auctionStatistics.getAvgBid(), 0.1d);
                assertEquals(123d, auctionStatistics.getAvgBuyout(), 0.1d);
                break;
            case 48:
                assertEquals(125, auctionStatistics.getMinBid().intValue());
                assertEquals(125, auctionStatistics.getMaxBid().intValue());
                assertEquals(220, auctionStatistics.getMinBuyout().intValue());
                assertEquals(220, auctionStatistics.getMaxBuyout().intValue());
                assertEquals(116d, auctionStatistics.getAvgBid(), 0.1d);
                assertEquals(220d, auctionStatistics.getAvgBuyout(), 0.1d);
                break;
            case 99:
                assertEquals(3, auctionStatistics.getMinBid().intValue());
                assertEquals(37, auctionStatistics.getMaxBid().intValue());
                assertEquals(3, auctionStatistics.getMinBuyout().intValue());
                assertEquals(66, auctionStatistics.getMaxBuyout().intValue());
                assertEquals(11d, auctionStatistics.getAvgBid(), 0.1d);
                assertEquals(13d, auctionStatistics.getAvgBuyout(), 0.1d);
        }
    }
}
