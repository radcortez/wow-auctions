package com.radcortez.wow.auctions.batch.process.statistics;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;
import com.radcortez.wow.auctions.entity.AuctionItemStatistics;

import javax.batch.api.chunk.ItemProcessor;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import java.sql.ResultSet;

/**
 * @author Ivan St. Ivanov
 */
@Dependent
@Named
public class ProcessedAuctionsProcessor extends AbstractAuctionFileProcess implements ItemProcessor {
    @Override
    public Object processItem(Object item) throws Exception {
        ResultSet resultSet = (ResultSet) item;

        AuctionItemStatistics auctionItemStatistics = new AuctionItemStatistics();
        auctionItemStatistics.setItemId(resultSet.getInt(1));
        auctionItemStatistics.setQuantity(resultSet.getLong(2));
        auctionItemStatistics.setBid(resultSet.getLong(3));
        auctionItemStatistics.setBuyout(resultSet.getLong(4));
        auctionItemStatistics.setMinBid(resultSet.getLong(5));
        auctionItemStatistics.setMinBuyout(resultSet.getLong(6));
        auctionItemStatistics.setMaxBid(resultSet.getLong(7));
        auctionItemStatistics.setMaxBuyout(resultSet.getLong(8));

        auctionItemStatistics.setTimestamp(getContext().getFileToProcess().getLastModified());

        auctionItemStatistics.setAvgBid(
                (double) (auctionItemStatistics.getBid() / auctionItemStatistics.getQuantity()));
        auctionItemStatistics.setAvgBuyout(
                (double) (auctionItemStatistics.getBuyout() / auctionItemStatistics.getQuantity()));

        auctionItemStatistics.setRealm(getContext().getRealm());

        return auctionItemStatistics;
    }
}
