package com.radcortez.wow.auctions.batch.process.statistics;

import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.wow.auctions.QuarkusDataSourceProvider;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.AuctionStatistics;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Ivan St. Ivanov
 */
@QuarkusTest
@FlywayTest(@DataSource(QuarkusDataSourceProvider.class))
@Transactional
public class AuctionsProcessorTest {
    @Inject
    ProcessedAuctionsReader processedAuctionsReader;
    @Inject
    ProcessedAuctionsProcessor processor;

    @Test
    public void testProcessedAuctionsReader() throws Exception {
        processedAuctionsReader.getContext().setAuctionFile(AuctionFile.findById(1L));
        processedAuctionsReader.open(null);

        assertNotNull(processedAuctionsReader.readItem());
        assertNotNull(processedAuctionsReader.readItem());
        assertNotNull(processedAuctionsReader.readItem());
        assertNull(processedAuctionsReader.readItem());
    }

    @Test
    public void testProcessedAuctionsProcessor() throws Exception {
        processedAuctionsReader.getContext().setAuctionFile(AuctionFile.findById(1L));
        processedAuctionsReader.open(null);
        Object auction;
        while ((auction = processedAuctionsReader.readItem()) != null) {
            AuctionStatistics auctionStatistics = (AuctionStatistics) processor.processItem(auction);
            assertAuctionItemStatistics(auctionStatistics);
        }
    }

    private void assertAuctionItemStatistics(AuctionStatistics auctionStatistics) {
        // TODO - not working
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
