package com.radcortez.wow.auctions.batch.process;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Named;

/**
 * @author Roberto Cortez
 */
@Named
public class AuctionDataItemProcessor implements ItemProcessor {
    @Override
    public Object processItem(Object item) throws Exception {
        System.out.println(item);
        return item;
    }
}
