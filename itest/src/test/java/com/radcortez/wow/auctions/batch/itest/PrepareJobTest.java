package com.radcortez.wow.auctions.batch.itest;

import com.radcortez.wow.auctions.entity.ConnectedRealm;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import jakarta.batch.operations.JobOperator;
import jakarta.batch.runtime.BatchStatus;
import jakarta.batch.runtime.JobExecution;
import jakarta.inject.Inject;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;

@QuarkusTest
public class PrepareJobTest {
    @Inject
    JobOperator jobOperator;

    @Test
    void prepareJob() {
        long executionId = jobOperator.start("prepare-job", new Properties());

        await().atMost(60, TimeUnit.SECONDS).until(() -> {
            final JobExecution jobExecution = jobOperator.getJobExecution(executionId);
            return BatchStatus.COMPLETED.equals(jobExecution.getBatchStatus());
        });

        assertFalse(ConnectedRealm.listAll().isEmpty());
    }
}
