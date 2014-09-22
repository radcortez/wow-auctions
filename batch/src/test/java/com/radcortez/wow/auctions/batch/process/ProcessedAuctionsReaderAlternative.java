package com.radcortez.wow.auctions.batch.process;

import javax.enterprise.inject.Alternative;
import javax.inject.Named;
import javax.sql.DataSource;
import javax.annotation.Resource;
import javax.inject.Inject;

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
