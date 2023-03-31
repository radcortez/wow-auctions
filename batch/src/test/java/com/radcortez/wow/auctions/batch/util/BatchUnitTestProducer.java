package com.radcortez.wow.auctions.batch.util;


import jakarta.annotation.Priority;
import jakarta.batch.api.BatchProperty;
import jakarta.batch.runtime.BatchStatus;
import jakarta.batch.runtime.context.JobContext;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Singleton;

import java.util.Properties;

/**
 * @author Roberto Cortez
 */
public class BatchUnitTestProducer {
    @Produces
    @Singleton
    @Alternative
    @Priority(Integer.MAX_VALUE)
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
                properties.setProperty("connectedRealmId", "1");
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
    @Alternative
    @Priority(Integer.MAX_VALUE)
    public String getString(InjectionPoint injectionPoint) {
        BatchProperty annotation = injectionPoint.getAnnotated().getAnnotation(BatchProperty.class);

        if (annotation.name().equals("region")) {
            return "EU";
        }

        return "test";
    }
}
