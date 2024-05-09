package com.radcortez.wow.auctions.batch.process.statistics;

import com.radcortez.wow.auctions.entity.AuctionStatistics;
import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;

import java.util.List;

/**
 * @author Ivan St. Ivanov
 */
@Dependent
@Named
public class ProcessedAuctionsWriter extends AbstractItemWriter {
    @Override
    public void writeItems(List<Object> items) {
        items.stream().map(AuctionStatistics.class::cast).forEach(AuctionStatistics::create);
    }
}
