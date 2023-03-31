package com.radcortez.wow.auctions.batch.process.purge;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;

import jakarta.batch.api.Batchlet;
import jakarta.batch.runtime.BatchStatus;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

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
