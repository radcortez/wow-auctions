package com.radcortez.wow.auctions.batch.process.data;

import javax.enterprise.inject.Alternative;
import javax.inject.Named;
import javax.json.Json;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@Named
@Alternative
public class AuctionDataItemReaderAlternative extends AuctionDataItemReader {
    @Override public void open(Serializable checkpoint) {
        setParser(Json.createParser(Thread.currentThread()
                                          .getContextClassLoader()
                                          .getResourceAsStream("samples/auction-data-sample.json")));

    }
}
