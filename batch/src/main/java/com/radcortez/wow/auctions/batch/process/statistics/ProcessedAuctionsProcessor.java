package com.radcortez.wow.auctions.batch.process.statistics;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;
import com.radcortez.wow.auctions.entity.AuctionStatistics;

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

        AuctionStatistics auctionStatistics = new AuctionStatistics();
        auctionStatistics.setItemId(resultSet.getInt(1));
        auctionStatistics.setQuantity(resultSet.getLong(2));
        auctionStatistics.setBid(resultSet.getLong(3));
        auctionStatistics.setBuyout(resultSet.getLong(4));
        auctionStatistics.setMinBid(resultSet.getLong(5));
        auctionStatistics.setMinBuyout(resultSet.getLong(6));
        auctionStatistics.setMaxBid(resultSet.getLong(7));
        auctionStatistics.setMaxBuyout(resultSet.getLong(8));

        auctionStatistics.setTimestamp(getContext().getAuctionFile().getTimestamp());

        auctionStatistics.setAvgBid(
                (double) (auctionStatistics.getBid() / auctionStatistics.getQuantity()));
        auctionStatistics.setAvgBuyout(
                (double) (auctionStatistics.getBuyout() / auctionStatistics.getQuantity()));

        auctionStatistics.setConnectedRealm(getContext().getConnectedRealm());

        return auctionStatistics;
    }
}
