package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.business.repository.AuctionFileRepository;
import com.radcortez.wow.auctions.business.repository.RealmFolderRepository;
import com.radcortez.wow.auctions.business.repository.RealmRepository;
import com.radcortez.wow.auctions.entity.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@SuppressWarnings({"unchecked", "CdiInjectionPointsInspection"})
@Named
public class WoWBusinessBean implements WoWBusiness {
    @Inject
    private RealmRepository realmRepository;
    @Inject
    private RealmFolderRepository realmFolderRepository;
    @Inject
    private AuctionFileRepository auctionFileRepository;

    @Override
    public void createRealm(Realm realm) {
        realmRepository.save(realm);
    }

    @Override
    public List<Realm> listRealms() {
        return realmRepository.findAll();
    }

    public Realm findRealmByNameOrSlug(String name, Realm.Region region) {
        return realmRepository.findByNameOrSlugInRegion(name, name, region);
    }

    @Override
    public List<Realm> findRealmsByRegion(Realm.Region region) {
        return realmRepository.findByRegion(region);
    }

    @Override
    public boolean checkIfRealmExists(Realm realm) {
        return realmRepository.count(realm, Realm_.name, Realm_.region) > 0;
    }

    @Override
    public void createRealmFolder(RealmFolder realmFolder) {
        realmFolderRepository.save(realmFolder);
    }

    @Override
    public RealmFolder findRealmFolderById(Long realmId, FolderType folderType) {
        return realmFolderRepository.findBy(new RealmFolder.RealmFolderPK(realmId, folderType));
    }

    @Override
    public void createAuctionFile(AuctionFile auctionFile) {
        auctionFileRepository.save(auctionFile);
    }

    @Override
    public AuctionFile updateAuctionFile(AuctionFile auctionFile) {
        return auctionFileRepository.save(auctionFile);
    }

    @Override
    public List<AuctionFile> findAuctionFilesByRegionToDownload(Realm.Region region) {
        return auctionFileRepository.findByRealm_regionAndFileStatus(region, FileStatus.LOADED);
    }
}
