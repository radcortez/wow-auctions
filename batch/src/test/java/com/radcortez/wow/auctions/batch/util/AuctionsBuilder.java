package com.radcortez.wow.auctions.batch.util;

import com.radcortez.wow.auctions.entity.Auction;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.AuctionHouse;
import com.radcortez.wow.auctions.entity.Realm;

/**
 * @author Ivan St. Ivanov
 */
public class AuctionsBuilder {

    private Auction auction;

    private AuctionsBuilder() {
        auction = new Auction();
    }

    public static AuctionsBuilder buildAuction() {
        return new AuctionsBuilder();
    }

    public AuctionsBuilder withAuctionFile(AuctionFile auctionFile) {
        auction.setAuctionFile(auctionFile);
        return this;
    }

    public AuctionsBuilder withAuctionHouse(AuctionHouse auctionHouse) {
        auction.setAuctionHouse(auctionHouse);
        return this;
    }

    public AuctionsBuilder withItemId(Integer itemId) {
        auction.setItemId(itemId);
        return this;
    }

    public AuctionsBuilder withOwnerRealm(String ownerRealm) {
        auction.setOwnerRealm(ownerRealm);
        return this;
    }

    public AuctionsBuilder withBid(Integer bid) {
        auction.setBid(bid);
        return this;
    }

    public AuctionsBuilder withBuyout(Integer buyout) {
        auction.setBuyout(buyout);
        return this;
    }

    public AuctionsBuilder withQuantity(Integer quantity) {
        auction.setQuantity(quantity);
        return this;
    }

    public AuctionsBuilder withRealm(Realm realm) {
        auction.setRealm(realm);
        return this;
    }

    public Auction get() {
        return auction;
    }
}
