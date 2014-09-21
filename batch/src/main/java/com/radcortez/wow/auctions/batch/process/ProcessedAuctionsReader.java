package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.AuctionHouse;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemReader;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * @author Ivan St. Ivanov
 */
@Named
public class ProcessedAuctionsReader extends AbstractAuctionFileProcess implements ItemReader {
    @Inject
    private WoWBusiness wowBusiness;

    @Inject
    @BatchProperty(name = "auctionHouse")
    private String auctionHouse;

    private int reads;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        reads = 0;
    }

    @Override public void close() throws Exception {}

    @Override
    public Object readItem() throws Exception {
        List<Object> results =
                wowBusiness.findAuctionsAggregatedByFileAndHouse(getContext().getFileToProcess().getId(),
                                                                 AuctionHouse.valueOf(auctionHouse), reads++, 100);
        System.out.println("results.size() = " + results.size());
        System.out.println("reads = " + reads);
        return results.isEmpty() ? null : results;
    }

    @Override public Serializable checkpointInfo() throws Exception {
        return null;
    }
}
