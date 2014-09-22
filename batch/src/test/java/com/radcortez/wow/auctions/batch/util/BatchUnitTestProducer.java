package com.radcortez.wow.auctions.batch.util;

import org.apache.commons.dbcp.BasicDataSource;

import javax.annotation.Resource;
import javax.batch.api.BatchProperty;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Roberto Cortez
 */
@Named
@Alternative
public class BatchUnitTestProducer {
    @Produces
    @Singleton
    public JobContext getJobContext() {
        return new JobContext() {
            private Object transientData;

            @Override
            public String getJobName() {
                return null;
            }

            @Override
            public Object getTransientUserData() {
                return transientData;
            }

            @Override
            public void setTransientUserData(Object data) {
                this.transientData = data;
            }

            @Override
            public long getInstanceId() {
                return 0;
            }

            @Override
            public long getExecutionId() {
                return 0;
            }

            @Override
            public Properties getProperties() {
                Properties properties = new Properties();
                properties.setProperty("realmId", "1");
                properties.setProperty("auctionFileId", "1");
                return properties;
            }

            @Override
            public BatchStatus getBatchStatus() {
                return null;
            }

            @Override
            public String getExitStatus() {
                return null;
            }

            @Override
            public void setExitStatus(String status) {

            }
        };
    }

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
    @PersistenceContext
    @Singleton
    public EntityManager create() {
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.transactionType", "RESOURCE_LOCAL");
        properties.put("hibernate.show_sql", "true");
        return Persistence.createEntityManagerFactory("wowAuctions", properties).createEntityManager();
    }

    public void close(@Disposes EntityManager em) {
        if (em.isOpen()) {
            em.close();
        }
    }

    @Produces
    @Resource
    @Singleton
    public DataSource getDatasource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:h2:target/data/repository");
        dataSource.setDriverClassName("org.h2.Driver");
        return dataSource;
    }
}
