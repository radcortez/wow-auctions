package com.radcortez.wow.auctions.batch.process.purge;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;

import javax.batch.api.Batchlet;
import javax.batch.runtime.BatchStatus;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.transaction.Transactional;

/**
 * @author Ivan St. Ivanov
 */
@Dependent
@Named
public class PurgeRawAuctionDataBatchlet extends AbstractAuctionFileProcess implements Batchlet {
    @Override
    @Transactional
    public String process() {
        getContext().getAuctionFile().delete();

        return BatchStatus.COMPLETED.toString();
    }

    @Override
    public void stop() {}
}
