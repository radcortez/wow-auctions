package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.AuctionItem;
import com.radcortez.wow.auctions.entity.AuctionItemStatistics;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan St. Ivanov
 */
@Named
public class ProcessedAuctionsProcessor implements ItemProcessor {
    @Inject
    private WoWBusiness wowBusiness;

    @Override
    @SuppressWarnings("unchecked")
    public Object processItem(Object auctions) throws Exception {
        List<Object> results = (List<Object>) auctions;

        List<AuctionItemStatistics> statisticsList = new ArrayList<>();
        for (Object result : results) {
            Object[] auctionData = (Object[]) result;

            AuctionItemStatistics statistics = new AuctionItemStatistics();
            statistics.setItemId((Integer) auctionData[0]);
            statistics.setMinBid((Integer) auctionData[4]);
            statistics.setMaxBid((Integer) auctionData[6]);
            statisticsList.add(statistics);
        }

        System.out.println("statisticsList.size() = " + statisticsList.size());
        return statisticsList;
    }

    protected AuctionItem findItemById(Long itemId) {
        return wowBusiness.findAuctionItemById(itemId);
    }
}
