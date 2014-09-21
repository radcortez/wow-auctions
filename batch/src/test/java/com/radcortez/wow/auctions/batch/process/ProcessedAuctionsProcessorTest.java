package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.entity.AuctionItemStatistics;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Ivan St. Ivanov
 */
@RunWith(CdiTestRunner.class)
public class ProcessedAuctionsProcessorTest extends AbstractAuctionsProcessingTestFixture {

    @Inject
    private ProcessedAuctionsProcessor processor;

    @Test
    @Ignore
    @SuppressWarnings("unchecked")
    public void testProcessedAuctionsProcessor() throws Exception {
        List<AuctionItemStatistics> stats = (List<AuctionItemStatistics>) processor.processItem(auctions);
        assertEquals(5, stats.size());
        /*
        stats.stream().forEach(singleItemStats -> {
            assertEquals(155555L, singleItemStats.getBidTimestamp().longValue());
            switch (singleItemStats.getItem().getItemName()) {
                case "Volatile Air":
                    if (singleItemStats.getAuctions().get(0).getAuctionHouse() == AuctionHouse.ALLIANCE) {
                        assertEquals(98, singleItemStats.getMinBid().intValue());
                        assertEquals(212, singleItemStats.getMaxBid().intValue());
                        assertEquals(145d, singleItemStats.getAverageBid(), 0.1d);
                        assertEquals(3, singleItemStats.getAuctions().size());
                    } else {
                        assertEquals(26, singleItemStats.getMinBid().intValue());
                        assertEquals(78, singleItemStats.getMaxBid().intValue());
                        assertEquals(52d, singleItemStats.getAverageBid(), 0.1d);
                        assertEquals(2, singleItemStats.getAuctions().size());
                    }
                    break;
                case "Mogu Pumpkin":
                    assertEquals(116, singleItemStats.getMinBid().intValue());
                    assertEquals(116, singleItemStats.getMaxBid().intValue());
                    assertEquals(116d, singleItemStats.getAverageBid(), 0.1d);
                    assertEquals(1, singleItemStats.getAuctions().size());
                    break;
                case "Obsidium Bar":
                    if (singleItemStats.getMinBid().equals(singleItemStats.getMaxBid())) {
                        assertEquals(22, singleItemStats.getMinBid().intValue());
                        assertEquals(22, singleItemStats.getMaxBid().intValue());
                        assertEquals(22d, singleItemStats.getAverageBid(), 0.1d);
                        assertEquals(1, singleItemStats.getAuctions().size());
                    } else {
                        assertEquals(56, singleItemStats.getMinBid().intValue());
                        assertEquals(266, singleItemStats.getMaxBid().intValue());
                        assertEquals(170d, singleItemStats.getAverageBid(), 0.1d);
                        assertEquals(3, singleItemStats.getAuctions().size());
                    }
                    break;
            }
        });
        */
    }
}
