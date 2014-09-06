package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.FolderType;
import com.radcortez.wow.auctions.entity.Realm;
import com.radcortez.wow.auctions.entity.RealmFolder;

import java.util.List;

/**
 * @author Roberto Cortez
 */
public interface WoWBusiness {
    void createRealm(Realm realm);

    List<Realm> listRealms();

    List<Realm> findRealmsByRegion(Realm.Region region);

    boolean checkIfRealmExists(Realm realm);

    void createRealmFolder(RealmFolder realmFolder);

    RealmFolder findRealmFolderById(Long realmId, FolderType folderType);

    void createAuctionFile(AuctionFile auctionFile);

    AuctionFile updateAuctionFile(AuctionFile auctionFile);

    List<AuctionFile> findAuctionFilesByRegionToDownload(Realm.Region region);
}
