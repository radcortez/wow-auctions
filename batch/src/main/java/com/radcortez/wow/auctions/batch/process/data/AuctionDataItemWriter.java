package com.radcortez.wow.auctions.batch.process.data;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@Dependent
@Named
public class AuctionDataItemWriter extends AbstractItemWriter {
    @Inject
    EntityManager em;

    @Override
    public void writeItems(List<Object> items) {
        items.forEach(em::persist);
    }
}
