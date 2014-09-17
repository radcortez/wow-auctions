package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.Auction;
import com.radcortez.wow.auctions.entity.AuctionItemStatistics;
import com.radcortez.wow.auctions.entity.FileStatus;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Ivan St. Ivanov
 */
public class ProcessedAuctionsWriter extends AbstractItemWriter {

    @PersistenceContext
    protected EntityManager em;

    @Inject
    private WoWBusiness wowBusiness;

    @Override
    public void writeItems(List<Object> statsList) throws Exception {
        statsList.forEach(stats -> ((List<AuctionItemStatistics>) stats)
                    .forEach(singleItemStats -> {
                        em.persist(singleItemStats);
                        singleItemStats.getAuctions()
                                .forEach(auction -> auction.getAuctionFile().setFileStatus(FileStatus.DW));
                    }));
    }
}
