package com.radcortez.wow.auctions.batch.itest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.inject.Inject;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@QuarkusTest
public class ProcessJobTest {
    @Inject
    JobOperator jobOperator;

    @Test
    void prepareJob() {
        final Properties jobParameters = new Properties();
        jobParameters.setProperty("connectedRealmId", "11");
        long executionId = jobOperator.start("process-job", jobParameters);

        await().atMost(60, TimeUnit.SECONDS).until(() -> {
            final JobExecution jobExecution = jobOperator.getJobExecution(executionId);
            return BatchStatus.COMPLETED.equals(jobExecution.getBatchStatus());
        });
    }
}
