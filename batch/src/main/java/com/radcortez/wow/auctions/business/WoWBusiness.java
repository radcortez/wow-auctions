package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.entity.*;

import javax.ejb.Local;
import java.util.List;
import java.util.Optional;

/**
 * @author Roberto Cortez
 */
@Local
public interface WoWBusiness {
    void createRealm(Realm realm);

    Realm updateRealm(Realm realm);

    List<Realm> listRealms();

    Realm findRealmById(Long realmId);

    Optional<Realm> findRealmByNameOrSlug(String name, Realm.Region region);

    List<Realm> findRealmsByRegion(Realm.Region region);

    boolean checkIfRealmExists(Realm realm);

    void createRealmFolder(RealmFolder realmFolder);

    RealmFolder findRealmFolderById(Long realmId, FolderType folderType);

    boolean checkIfAuctionFileExists(AuctionFile auctionFile);

    void createAuctionFile(AuctionFile auctionFile);

    AuctionFile updateAuctionFile(AuctionFile auctionFile);

    List<AuctionFile> findAuctionFilesByRealmToProcess(Long realmId);

    AuctionFile findAuctionFileById(Long auctionFileId);

    List<Auction> findAuctionsByRealm(Long realmId, int start, int max);

    List<AuctionItemStatistics> findAuctionItemStatisticsByRealmAndItem(Long realmId, Integer itemId);
}
