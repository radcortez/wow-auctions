package com.radcortez.wow.auctions.batch.process.statistics;

import com.radcortez.wow.auctions.entity.AuctionItemStatistics;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Ivan St. Ivanov
 */
@Named
public class ProcessedAuctionsWriter extends AbstractItemWriter {
    @Inject
    protected EntityManager em;

    @Override
    @SuppressWarnings("unchecked")
    public void writeItems(List items) {
        List<AuctionItemStatistics> statistis = (List<AuctionItemStatistics>) items;
        statistis.forEach(em::persist);
    }
}
