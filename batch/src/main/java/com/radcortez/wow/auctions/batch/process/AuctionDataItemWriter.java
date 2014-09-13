package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.Auction;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@Named
public class AuctionDataItemWriter extends AbstractItemWriter {
    @Inject
    private WoWBusiness woWBusiness;

    @Override
    public void writeItems(List<Object> items) throws Exception {
        items.forEach(auction -> woWBusiness.createAuction((Auction) auction));
    }
}
