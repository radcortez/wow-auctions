package com.radcortez.wow.auctions.batch;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.batch.operations.JobOperator;
import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class JobOperatorInjectionTest {
    @Inject
    JobOperator jobOperator;

    @Test
    void jobOperator() {
        assertNotNull(jobOperator);
    }
}
