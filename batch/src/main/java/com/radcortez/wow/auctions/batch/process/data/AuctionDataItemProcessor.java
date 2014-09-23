package com.radcortez.wow.auctions.batch.process.data;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;
import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.Auction;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Roberto Cortez
 */
@Named
public class AuctionDataItemProcessor extends AbstractAuctionFileProcess implements ItemProcessor {
    @Inject
    private WoWBusiness woWBusiness;

    @Override
    public Object processItem(Object item) throws Exception {
        Auction auction = (Auction) item;

        auction.setRealm(getContext().getRealm());
        auction.setAuctionFile(getContext().getFileToProcess());

        return auction;
    }
}
