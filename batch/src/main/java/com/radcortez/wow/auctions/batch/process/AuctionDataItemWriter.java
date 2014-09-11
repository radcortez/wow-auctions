package com.radcortez.wow.auctions.batch.process;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Named;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@Named
public class AuctionDataItemWriter extends AbstractItemWriter {
    @Override
    public void writeItems(List<Object> items) throws Exception {
        items.forEach(System.out::println);
    }
}
