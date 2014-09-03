package com.radcortez.wow.auctions.business.repository;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 * @author Roberto Cortez
 */
public class EntityManagerProducer {
    @PersistenceUnit
    private EntityManagerFactory emf;

    @Produces
    public EntityManager create() {
        return emf.createEntityManager();
    }
    public void close( @Disposes EntityManager em) {
        if (em.isOpen()) {
            em.close();
        }
    }
}
