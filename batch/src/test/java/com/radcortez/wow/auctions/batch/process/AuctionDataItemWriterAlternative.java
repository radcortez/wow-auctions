package com.radcortez.wow.auctions.batch.process;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Roberto Cortez
 */
@Named
@Alternative
public class AuctionDataItemWriterAlternative extends AuctionDataItemWriter {
    @Inject
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }
}
