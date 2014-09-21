package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.entity.AuctionItem;

import javax.enterprise.inject.Alternative;
import javax.inject.Named;

/**
 * @author Ivan St. Ivanov
 */
@Named
@Alternative
public class ProcessedAuctionsProcessorAlternative extends ProcessedAuctionsProcessor {
    @Override
    protected AuctionItem findItemById(Long itemId) {
        switch (itemId.intValue()) {
            case 123: return new AuctionItem(123L, "Volatile Air");
            case 99: return new AuctionItem(99L, "Obsidium Bar");
            case 48: return new AuctionItem(48L, "Mogu Pumpkin");
            default: return null;
        }
    }
}
