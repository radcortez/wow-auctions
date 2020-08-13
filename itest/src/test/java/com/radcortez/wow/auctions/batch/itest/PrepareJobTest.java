package com.radcortez.wow.auctions.batch.itest;

import com.radcortez.wow.auctions.business.WoWBusinessBean;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.inject.Inject;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;

@QuarkusTest
public class PrepareJobTest {
    @Inject
    JobOperator jobOperator;
    @Inject
    WoWBusinessBean woWBusiness;

    @Test
    void prepareJob() {
        long executionId = jobOperator.start("prepare-job", new Properties());

        await().atMost(60, TimeUnit.SECONDS).until(() -> {
            final JobExecution jobExecution = jobOperator.getJobExecution(executionId);
            return BatchStatus.COMPLETED.equals(jobExecution.getBatchStatus());
        });

        assertFalse(woWBusiness.listConnectedRealms().isEmpty());
    }
}
