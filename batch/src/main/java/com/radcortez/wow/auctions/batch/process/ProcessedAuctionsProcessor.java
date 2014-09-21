package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.entity.AuctionItemStatistics;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Named;
import java.sql.ResultSet;

/**
 * @author Ivan St. Ivanov
 */
@Named
public class ProcessedAuctionsProcessor implements ItemProcessor {
    @Override
    @SuppressWarnings("unchecked")
    public Object processItem(Object item) throws Exception {
        ResultSet resultSet = (ResultSet) item;

        AuctionItemStatistics auctionItemStatistics = new AuctionItemStatistics();
        auctionItemStatistics.setItemId(resultSet.getInt(1));
        auctionItemStatistics.setMinBid(resultSet.getInt(5));
        auctionItemStatistics.setMaxBid(resultSet.getInt(7));

        return auctionItemStatistics;
    }
}
