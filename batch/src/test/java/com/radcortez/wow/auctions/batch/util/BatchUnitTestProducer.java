package com.radcortez.wow.auctions.batch.util;

import javax.batch.api.BatchProperty;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Roberto Cortez
 */
@Named
@Alternative
public class BatchUnitTestProducer {
    @Produces
    @BatchProperty
    public String getString(InjectionPoint injectionPoint) {
        BatchProperty annotation = injectionPoint.getAnnotated().getAnnotation(BatchProperty.class);

        if (annotation.name().equals("region")) {
            return "EU";
        }

        return "test";
    }

    @Produces
    @Singleton
    public EntityManager create() {
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.transactionType", "RESOURCE_LOCAL");
        properties.put("hibernate.show_sql", "true");
        return Persistence.createEntityManagerFactory("wowAuctions", properties).createEntityManager();
    }

    public void close(@Disposes
                      EntityManager em) {
        if (em.isOpen()) {
            em.close();
        }
    }
}
