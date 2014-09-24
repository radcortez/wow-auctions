package com.radcortez.wow.auctions.batch.process.purge;

import javax.batch.api.Decider;
import javax.batch.runtime.StepExecution;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

import static java.util.logging.Logger.getLogger;

/**
 * @author Ivan St. Ivanov
 */
@Named
public class PurgeRawAuctionDataDecider implements Decider {

    @Inject
    private JobContext jobContext;

    @Override
    public String decide(StepExecution[] stepExecutions) throws Exception {
        boolean doPurge = Boolean.parseBoolean(jobContext.getProperties().getProperty("purgeProcessedAuctions"));
        getLogger(this.getClass().getName()).info("Do purge: " + doPurge);
        return doPurge ? "purge" : "end";
    }
}
