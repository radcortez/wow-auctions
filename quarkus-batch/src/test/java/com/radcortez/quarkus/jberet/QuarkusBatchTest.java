package com.radcortez.quarkus.jberet;

import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import javax.batch.api.AbstractBatchlet;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Properties;

import static org.awaitility.Awaitility.await;

@QuarkusTest
class QuarkusBatchTest {
    @Inject
    JobOperator jobOperator;

    @Test
    void batchlet() {
        long executionId = jobOperator.start("batchlet", new Properties());

        await().until(() -> {
            final JobExecution jobExecution = jobOperator.getJobExecution(executionId);
            return BatchStatus.COMPLETED.equals(jobExecution.getBatchStatus());
        });
    }

    @Test
    void partition() {
        long executionId = jobOperator.start("partition", new Properties());

        await().until(() -> {
            final JobExecution jobExecution = jobOperator.getJobExecution(executionId);
            return BatchStatus.COMPLETED.equals(jobExecution.getBatchStatus());
        });
    }

    @Dependent
    @Named
    public static class SimpleBatchlet extends AbstractBatchlet {
        @Override
        public String process() {
            return BatchStatus.COMPLETED.toString();
        }
    }

    @Dependent
    @Named
    public static class PartitionBatchlet extends AbstractBatchlet {
        @Inject
        @ConfigProperty(name = "region", defaultValue = "")
        String region;

        @Override
        public String process() {
            System.out.println("region = " + region);
            return BatchStatus.COMPLETED.toString();
        }
    }
}
