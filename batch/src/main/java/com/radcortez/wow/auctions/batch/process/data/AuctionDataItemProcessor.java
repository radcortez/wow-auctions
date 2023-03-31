package com.radcortez.wow.auctions.batch.process.data;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;
import com.radcortez.wow.auctions.entity.Auction;

import jakarta.batch.api.chunk.ItemProcessor;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;

/**
 * @author Roberto Cortez
 */
@Dependent
@Named
public class AuctionDataItemProcessor extends AbstractAuctionFileProcess implements ItemProcessor {
    @Override
    public Object processItem(Object item) {
        Auction auction = (Auction) item;

        auction.setAuctionFile(getContext().getAuctionFile());

        return auction;
    }
}
