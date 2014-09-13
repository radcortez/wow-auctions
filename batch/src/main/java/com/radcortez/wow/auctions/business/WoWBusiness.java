package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.entity.*;

import java.util.List;

/**
 * @author Roberto Cortez
 */
public interface WoWBusiness {
    void createRealm(Realm realm);

    List<Realm> listRealms();

    Realm findRealmById(Long realmId);

    Realm findRealmByNameOrSlug(String name, Realm.Region region);

    List<Realm> findRealmsByRegion(Realm.Region region);

    boolean checkIfRealmExists(Realm realm);

    void createRealmFolder(RealmFolder realmFolder);

    RealmFolder findRealmFolderById(Long realmId, FolderType folderType);

    void createAuctionFile(AuctionFile auctionFile);

    AuctionFile updateAuctionFile(AuctionFile auctionFile);

    List<AuctionFile> findAuctionFilesByRegionToDownload(Realm.Region region);

    List<AuctionFile> findAuctionFilesByRealmToProcess(Long realmId);

    AuctionFile findAuctionFileById(Long auctionFileId);

    void createAuction(Auction auction);

    List<Auction> findAuctionsByRealm(Long realmId, int start, int max);
}
