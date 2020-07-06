package com.radcortez.wow.auctions.batch.process.data;

import io.quarkus.arc.AlternativePriority;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Roberto Cortez
 */
@Dependent
@Named
@AlternativePriority(Integer.MAX_VALUE)
public class AuctionDataItemWriterAlternative extends AuctionDataItemWriter {
    @Inject
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }
}
