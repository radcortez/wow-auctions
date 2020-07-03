package com.radcortez.quarkus.jberet;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchStatus;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Dependent
@Named
public class QuarkusBatchlet extends AbstractBatchlet {
    @Override
    public String process() {
        return BatchStatus.COMPLETED.toString();
    }
}
