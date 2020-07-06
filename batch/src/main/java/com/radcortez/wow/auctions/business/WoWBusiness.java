package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.entity.Auction;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.AuctionItemStatistics;
import com.radcortez.wow.auctions.entity.FolderType;
import com.radcortez.wow.auctions.entity.Realm;
import com.radcortez.wow.auctions.entity.ConnectedRealmFolder;
import com.radcortez.wow.auctions.entity.Region;

import java.util.List;
import java.util.Optional;

/**
 * @author Roberto Cortez
 */
public interface WoWBusiness {
    void createRealm(Realm realm);

    Realm updateRealm(Realm realm);

    List<Realm> listRealms();

    Realm findRealmById(Long realmId);

    Optional<Realm> findRealmByNameOrSlug(String name, Region region);

    List<Realm> findRealmsByRegion(Region region);

    boolean checkIfRealmExists(Realm realm);

    void createRealmFolder(ConnectedRealmFolder connectedRealmFolder);

    ConnectedRealmFolder findRealmFolderById(String realmId, FolderType folderType);

    boolean checkIfAuctionFileExists(AuctionFile auctionFile);

    void createAuctionFile(AuctionFile auctionFile);

    AuctionFile updateAuctionFile(AuctionFile auctionFile);

    List<AuctionFile> findAuctionFilesByRealmToProcess(Long realmId);

    AuctionFile findAuctionFileById(Long auctionFileId);

    List<Auction> findAuctionsByRealm(Long realmId, int start, int max);

    List<AuctionItemStatistics> findAuctionItemStatisticsByRealmAndItem(Long realmId, Integer itemId);

    void deleteAuctionDataByFile(Long fileId);
}
