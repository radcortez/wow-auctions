package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.entity.*;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@SuppressWarnings({"unchecked"})
@Named
@Local
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class WoWBusinessBean implements WoWBusiness {
    @PersistenceContext
    protected EntityManager em;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createRealm(Realm realm) {
        em.persist(realm);
    }

    @Override
    public List<Realm> listRealms() {
        return em.createNamedQuery("Realm.listRealms").getResultList();
    }

    @Override
    public Realm findRealmByNameOrSlug(String name, Realm.Region region) {
        return ((Realm) em.createNamedQuery("Realm.findByNameOrSlugInRegion")
                          .setParameter("name", name)
                          .setParameter("slug", name)
                          .setParameter("region", region)
                          .getSingleResult());
    }

    @Override
    public List<Realm> findRealmsByRegion(Realm.Region region) {
        return em.createNamedQuery("Realm.findByRegion").setParameter("region", region).getResultList();
    }

    @Override
    public boolean checkIfRealmExists(Realm realm) {
        return ((Long) em.createNamedQuery("Realm.exists")
                         .setParameter("name", realm.getName())
                         .setParameter("region", realm.getRegion())
                         .getSingleResult()) > 0;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createRealmFolder(RealmFolder realmFolder) {
        em.persist(realmFolder);
    }

    @Override
    public RealmFolder findRealmFolderById(Long realmId, FolderType folderType) {
        return em.find(RealmFolder.class, new RealmFolder.RealmFolderPK(realmId, folderType));
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createAuctionFile(AuctionFile auctionFile) {
        em.persist(auctionFile);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public AuctionFile updateAuctionFile(AuctionFile auctionFile) {
        return em.merge(auctionFile);
    }

    @Override
    public List<AuctionFile> findAuctionFilesByRegionToDownload(Realm.Region region) {
        return em.createNamedQuery("AuctionFile.findByRealmRegionAndFileStatus")
                 .setParameter("region", region)
                 .setParameter("fileStatus", FileStatus.LOADED)
                 .getResultList();
    }
}
