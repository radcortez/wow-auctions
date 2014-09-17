package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.business.WoWBusiness;

import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.api.chunk.ItemReader;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * @author Ivan St. Ivanov
 */
public class ProcessedAuctionsReader extends AbstractItemReader {

    @Inject
    private WoWBusiness wowBusiness;

    @Override
    public Object readItem() throws Exception {
        return wowBusiness.findAllProcessedAuctions();
    }
}
