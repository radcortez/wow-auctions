package com.radcortez.wow.auctions.batch.util;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.Metric;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author Roberto Cortez
 */
public class BatchTestHelper {
    private BatchTestHelper() {
        throw new UnsupportedOperationException();
    }

    /**
     * We need to keep the test running because JobOperator runs the batch job in an asynchronous way, so the
     * JobExecution can be properly updated with the running job status.
     *
     * @param jobOperator the JobOperator of the job that is being executed.
     * @throws java.util.concurrent.TimeoutException if the job takes a long time to complete.
     */
    public static JobExecution keepTestAlive(JobOperator jobOperator, Long executionId) throws TimeoutException {
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        Date curDate = new Date();
        BatchStatus curBatchStatus = jobExecution.getBatchStatus();

        while (true) {
            if (curBatchStatus == BatchStatus.STOPPED ||
                curBatchStatus == BatchStatus.COMPLETED ||
                curBatchStatus == BatchStatus.FAILED) {
                break;
            }

            if (new Date().getTime() - curDate.getTime() > 1000000) {
                throw new TimeoutException("Job processing did not complete in time");
            }

            jobExecution = jobOperator.getJobExecution(executionId);
            curBatchStatus = jobExecution.getBatchStatus();
        }
        return jobExecution;
    }

    public static Map<Metric.MetricType, Long> getMetricsMap(Metric[] metrics) {
        Map<Metric.MetricType, Long> metricsMap = new HashMap<>();
        for (Metric metric : metrics) {
            metricsMap.put(metric.getType(), metric.getValue());
        }
        return metricsMap;
    }
}
