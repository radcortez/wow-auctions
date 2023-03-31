package com.radcortez.wow.auctions.batch.process.data;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import jakarta.json.Json;

import jakarta.enterprise.inject.Alternative;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@Dependent
@Named
@Alternative
@Priority(Integer.MAX_VALUE)
public class AuctionDataItemReaderAlternative extends AuctionDataItemReader {
    @Override
    public void open(Serializable checkpoint) {
        setParser(Json.createParser(Thread.currentThread()
                                          .getContextClassLoader()
                                          .getResourceAsStream("samples/auction-data-sample.json")));
    }
}
