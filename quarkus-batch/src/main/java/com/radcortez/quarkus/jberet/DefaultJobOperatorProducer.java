package com.radcortez.quarkus.jberet;

import io.quarkus.arc.DefaultBean;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@Dependent
public class DefaultJobOperatorProducer {
    @Produces
    @DefaultBean
    @Singleton
    public JobOperator jobOperator() {
        return BatchRuntime.getJobOperator();
    }
}
