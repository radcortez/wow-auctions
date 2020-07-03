package com.radcortez.wow.auctions.batch.process.purge;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;
import com.radcortez.wow.auctions.business.WoWBusiness;

import javax.batch.api.Batchlet;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Ivan St. Ivanov
 */
@Named
public class PurgeRawAuctionDataBatchlet extends AbstractAuctionFileProcess implements Batchlet {
    @Inject
    WoWBusiness woWBusiness;

    @Override
    public String process() {
        woWBusiness.deleteAuctionDataByFile(getContext().getFileToProcess().getId());
        return "COMPLETED";
    }

    @Override
    public void stop() {}
}
