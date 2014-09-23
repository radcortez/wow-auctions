package com.radcortez.wow.auctions.batch.process;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Ivan St. Ivanov
 */
@Named
@Alternative
public class PurgeRawAuctionDataBatchletAlternative extends PurgeRawAuctionDataBatchlet {

    @Inject
    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
