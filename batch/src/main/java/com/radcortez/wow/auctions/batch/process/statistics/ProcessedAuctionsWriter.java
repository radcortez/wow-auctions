package com.radcortez.wow.auctions.batch.process.statistics;

import com.radcortez.wow.auctions.entity.AuctionStatistics;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Named;
import java.util.List;

/**
 * @author Ivan St. Ivanov
 */
@Named
public class ProcessedAuctionsWriter extends AbstractItemWriter {
    @Override
    @SuppressWarnings("unchecked")
    public void writeItems(List<Object> items) {
        items.stream().map(AuctionStatistics.class::cast).forEach(AuctionStatistics::create);
    }
}
