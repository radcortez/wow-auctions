package com.radcortez.wow.auctions.batch.process.statistics;

import javax.annotation.Resource;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

/**
 * @author Ivan St. Ivanov
 */
@Alternative
@Named
public class ProcessedAuctionsReaderAlternative extends ProcessedAuctionsReader {

    @Inject
    @Resource
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
