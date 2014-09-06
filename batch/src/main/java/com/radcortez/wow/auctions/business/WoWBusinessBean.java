package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.business.repository.AuctionFileRepository;
import com.radcortez.wow.auctions.business.repository.RealmFolderRepository;
import com.radcortez.wow.auctions.business.repository.RealmRepository;
import com.radcortez.wow.auctions.entity.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@SuppressWarnings({"unchecked", "CdiInjectionPointsInspection"})
@Named
public class WoWBusinessBean {
    @Inject
    private RealmRepository realmRepository;
    @Inject
    private RealmFolderRepository realmFolderRepository;
    @Inject
    private AuctionFileRepository auctionFileRepository;

    public void createRealm(Realm realm) {
        realmRepository.save(realm);
    }

    public List<Realm> listRealms() {
        return realmRepository.findAll();
    }

    public List<Realm> findRealmsByRegion(Realm.Region region) {
        return realmRepository.findByRegion(region);
    }

    public boolean checkIfRealmExists(Realm realm) {
        return realmRepository.count(realm, Realm_.name, Realm_.region) > 0;
    }

    public void createRealmFolder(RealmFolder realmFolder) {
        realmFolderRepository.save(realmFolder);
    }

    public RealmFolder findRealmFolderById(Long realmId, FolderType folderType) {
        return realmFolderRepository.findBy(new RealmFolder.RealmFolderPK(realmId, folderType));
    }

    public void createAuctionFile(AuctionFile auctionFile) {
        auctionFileRepository.save(auctionFile);
    }

    public AuctionFile updateAuctionFile(AuctionFile auctionFile) {
        return auctionFileRepository.save(auctionFile);
    }

    public List<AuctionFile> findAuctionFilesByRegionToDownload(Realm.Region region) {
        return auctionFileRepository.findByRealm_regionAndDownloaded(region, false);
    }
}
