package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.entity.AuctionHouse;
import com.radcortez.wow.auctions.entity.AuctionItemStatistics;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import javax.inject.Named;
import java.sql.ResultSet;

/**
 * @author Ivan St. Ivanov
 */
@Named
public class ProcessedAuctionsProcessor extends AbstractAuctionFileProcess implements ItemProcessor {
    @Inject
    @BatchProperty(name = "auctionHouse")
    private String auctionHouse;

    @Override
    @SuppressWarnings("unchecked")
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

        auctionItemStatistics.setAuctionHouse(AuctionHouse.valueOf(auctionHouse));
        auctionItemStatistics.setRealm(getContext().getRealm());

        return auctionItemStatistics;
    }
}
