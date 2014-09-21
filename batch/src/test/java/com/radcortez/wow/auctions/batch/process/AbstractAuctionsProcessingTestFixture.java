package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.batch.util.AuctionsBuilder;
import com.radcortez.wow.auctions.entity.*;
import org.junit.Before;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ivan St. Ivanov
 */
public class AbstractAuctionsProcessingTestFixture {

    protected List<Auction> auctions;

    @Before
    public void setUp() throws Exception {
        AuctionFile auctionFile = new AuctionFile();
        auctionFile.setFileStatus(FileStatus.PROCESSED);
        auctionFile.setLastModified(155555L);

        Realm realmGrimBatol = new Realm();
        realmGrimBatol.setName("Grim Batol");
        realmGrimBatol.setSlug("grim-batol");

        Realm realmEndTime = new Realm();
        realmEndTime.setName("End Time");
        realmEndTime.setSlug("end-time");

        auctions = Arrays.asList(
                AuctionsBuilder.buildAuction()
                               .withAuctionFile(auctionFile)
                               .withBid(125)
                               .withItemId(123)
                               .withQuantity(1)
                               .withRealm(realmGrimBatol)
                               .withAuctionHouse(AuctionHouse.ALLIANCE)
                               .get(),
                AuctionsBuilder.buildAuction()
                               .withAuctionFile(auctionFile)
                               .withBid(212)
                               .withItemId(123)
                               .withQuantity(3)
                               .withRealm(realmGrimBatol)
                               .withAuctionHouse(AuctionHouse.ALLIANCE)
                               .get(),
                AuctionsBuilder.buildAuction()
                               .withAuctionFile(auctionFile)
                               .withBid(98)
                               .withItemId(123)
                               .withQuantity(5)
                               .withRealm(realmGrimBatol)
                               .withAuctionHouse(AuctionHouse.ALLIANCE)
                               .get(),
                AuctionsBuilder.buildAuction()
                               .withAuctionFile(auctionFile)
                               .withBid(26)
                               .withItemId(123)
                               .withQuantity(1)
                               .withRealm(realmGrimBatol)
                               .withAuctionHouse(AuctionHouse.HORDE)
                               .get(),
                AuctionsBuilder.buildAuction()
                               .withAuctionFile(auctionFile)
                               .withBid(78)
                               .withItemId(123)
                               .withQuantity(5)
                               .withRealm(realmGrimBatol)
                               .withAuctionHouse(AuctionHouse.HORDE)
                               .get(),
                AuctionsBuilder.buildAuction()
                               .withAuctionFile(auctionFile)
                               .withBid(22)
                               .withItemId(99)
                               .withQuantity(10)
                               .withRealm(realmGrimBatol)
                               .withAuctionHouse(AuctionHouse.HORDE)
                               .get(),
                AuctionsBuilder.buildAuction()
                               .withAuctionFile(auctionFile)
                               .withBid(266)
                               .withItemId(99)
                               .withQuantity(1)
                               .withRealm(realmEndTime)
                               .withAuctionHouse(AuctionHouse.ALLIANCE)
                               .get(),
                AuctionsBuilder.buildAuction()
                               .withAuctionFile(auctionFile)
                               .withBid(188)
                               .withItemId(99)
                               .withQuantity(5)
                               .withRealm(realmEndTime)
                               .withAuctionHouse(AuctionHouse.ALLIANCE)
                               .get(),
                AuctionsBuilder.buildAuction()
                               .withAuctionFile(auctionFile)
                               .withBid(56)
                               .withItemId(99)
                               .withQuantity(18)
                               .withRealm(realmEndTime)
                               .withAuctionHouse(AuctionHouse.ALLIANCE)
                               .get(),
                AuctionsBuilder.buildAuction()
                               .withAuctionFile(auctionFile)
                               .withBid(116)
                               .withItemId(48)
                               .withQuantity(1)
                               .withRealm(realmEndTime)
                               .withAuctionHouse(AuctionHouse.ALLIANCE)
                               .get()
                                );

    }

}
