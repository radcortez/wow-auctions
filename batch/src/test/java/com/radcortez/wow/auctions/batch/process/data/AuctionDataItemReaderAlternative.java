package com.radcortez.wow.auctions.batch.process.data;

import io.quarkus.arc.AlternativePriority;

import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.json.Json;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@Dependent
@Named
@AlternativePriority(Integer.MAX_VALUE)
public class AuctionDataItemReaderAlternative extends AuctionDataItemReader {
    @Override
    public void open(Serializable checkpoint) {
        setParser(Json.createParser(Thread.currentThread()
                                          .getContextClassLoader()
                                          .getResourceAsStream("samples/auction-data-sample.json")));
    }
}
