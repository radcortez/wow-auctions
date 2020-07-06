package com.radcortez.wow.auctions.batch.process.statistics;

import io.quarkus.arc.AlternativePriority;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

/**
 * @author Ivan St. Ivanov
 */
@Dependent
@Named
@AlternativePriority(Integer.MAX_VALUE)
public class ProcessedAuctionsReaderAlternative extends ProcessedAuctionsReader {
    @Inject
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
