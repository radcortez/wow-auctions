package com.radcortez.quarkus.jberet;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.inject.Inject;
import java.util.Properties;

import static org.awaitility.Awaitility.await;

@QuarkusTest
class QuarkusBatchTest {
    @Inject
    JobOperator jobOperator;

    @Test
    void batch() {
        long executionId = jobOperator.start("batchlet", new Properties());

        await().until(() -> {
            final JobExecution jobExecution = jobOperator.getJobExecution(executionId);
            return BatchStatus.COMPLETED.equals(jobExecution.getBatchStatus());
        });
    }
}
