package com.radcortez.wow.auctions.batch.process.purge;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;
import com.radcortez.wow.auctions.business.WoWBusinessBean;

import javax.batch.api.Batchlet;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Ivan St. Ivanov
 */
@Dependent
@Named
public class PurgeRawAuctionDataBatchlet extends AbstractAuctionFileProcess implements Batchlet {
    @Inject
    WoWBusinessBean woWBusiness;

    @Override
    public String process() {
        woWBusiness.deleteAuctionDataByFile(getContext().getAuctionFile().getId());
        return "COMPLETED";
    }

    @Override
    public void stop() {}
}
