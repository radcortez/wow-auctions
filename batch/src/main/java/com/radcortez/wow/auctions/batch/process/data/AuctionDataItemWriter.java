package com.radcortez.wow.auctions.batch.process.data;

import com.radcortez.wow.auctions.entity.Auction;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@Dependent
@Named
public class AuctionDataItemWriter extends AbstractItemWriter {
    @Override
    public void writeItems(List<Object> items) {
        items.stream().map(Auction.class::cast).forEach(Auction::create);
    }
}
