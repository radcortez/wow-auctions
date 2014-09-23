package com.radcortez.wow.auctions.batch.process.data;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@Named
public class AuctionDataItemWriter extends AbstractItemWriter {
    @PersistenceContext
    protected EntityManager em;

    @Override
    public void writeItems(List<Object> items) throws Exception {
        items.forEach(em::persist);
    }
}
