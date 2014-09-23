package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.entity.FileStatus;

import javax.batch.api.Batchlet;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Ivan St. Ivanov
 */
@Named
public class PurgeRawAuctionDataBatchlet extends AbstractAuctionFileProcess implements Batchlet {

    @PersistenceContext
    protected EntityManager em;

    @Override
    public String process() throws Exception {
        Query deleteQuery = em.createNamedQuery("Auction.deleteByAuctionFile");
        deleteQuery.setParameter("auctionFile", getContext().getFileToProcess());
        deleteQuery.executeUpdate();
        return "COMPLETED";
    }

    @Override
    public void stop() throws Exception {}
}
