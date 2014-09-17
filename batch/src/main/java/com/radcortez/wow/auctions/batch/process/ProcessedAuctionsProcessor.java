package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.*;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Ivan St. Ivanov
 */
public class ProcessedAuctionsProcessor implements ItemProcessor {

    @Inject
    private WoWBusiness wowBusiness;

    @Override
    public Object processItem(Object auctions) throws Exception {
        List<Auction> auctionList = (List<Auction>) auctions;

        Collection<List<Auction>> auctionsGroupedByRealm = getAuctionsGroupedByRealms(auctionList).values();

        Collection<Collection<List<Auction>>> auctionGroupedByRealmAndHouse = auctionsGroupedByRealm
                .stream()
                .map(auctionsForRealm -> getAuctionsGroupedByHouse(auctionsForRealm).values())
                .collect(Collectors.toList());
        Collection<List<Auction>> flattenedAuctionsGroupedByRealmAndHouse = flattenAuctionsList(auctionGroupedByRealmAndHouse);

        Collection<Collection<List<Auction>>> auctionsGroupedByRealmHouseAndItemId = flattenedAuctionsGroupedByRealmAndHouse
                .stream()
                .map(auctionsForRealm -> getAuctionsGroupedByItems(auctionsForRealm).values())
                .collect(Collectors.toList());
        Collection<List<Auction>> flattenedAuctionsGroupedByRealmHouseAndItemId = flattenAuctionsList(auctionsGroupedByRealmHouseAndItemId);

        return flattenedAuctionsGroupedByRealmHouseAndItemId
                .stream()
                .map(auctionsForItemInRealm -> toAuctionItemStatistics(auctionsForItemInRealm))
                .collect(Collectors.toList());
    }

    private Map<Realm, List<Auction>> getAuctionsGroupedByRealms(List<Auction> auctions) {
        return auctions
                .stream()
                .collect(Collectors.groupingBy(Auction::getRealm));
    }

    private Map<AuctionHouse, List<Auction>> getAuctionsGroupedByHouse(List<Auction> auctions) {
        return auctions
                .stream()
                .collect(Collectors.groupingBy(Auction::getAuctionHouse));
    }

    private Map<Integer, List<Auction>> getAuctionsGroupedByItems(List<Auction> auctions) {
        return auctions
                .stream()
                .collect(Collectors.groupingBy(Auction::getItemId));
    }

    private AuctionItemStatistics toAuctionItemStatistics(List<Auction> auctions) {
        AuctionItemStatistics stats = new AuctionItemStatistics();

        if (auctions != null && !auctions.isEmpty()) {
            stats.setMaxBid(auctions.stream().mapToInt(Auction::getBid).max().getAsInt());
            stats.setMinBid(auctions.stream().mapToInt(Auction::getBid).min().getAsInt());
            stats.setAverageBid(auctions.stream().mapToInt(Auction::getBid).average().getAsDouble());
            stats.setItem(findItemById(auctions.get(0).getItemId()));
            stats.setBidTimestamp(auctions.get(0).getAuctionFile().getLastModified());
            stats.setAuctions(auctions);
        }

        return stats;
    }

    private Collection<List<Auction>> flattenAuctionsList(Collection<Collection<List<Auction>>> listToFlatten) {
        return listToFlatten
                .stream()
                .flatMap(item -> item.stream())
                .collect(Collectors.toList());
    }

    protected AuctionItem findItemById(Integer itemId) {
        return wowBusiness.findAuctionItemById(new Long(itemId));
    }
}
