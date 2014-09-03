package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.FolderType;
import com.radcortez.wow.auctions.entity.Realm;
import com.radcortez.wow.auctions.entity.RealmFolder;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@SuppressWarnings("unchecked")
@Named
@Local
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class WoWBusinessBean {
    @PersistenceContext
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createRealm(Realm realm) {
        em.persist(realm);
    }

    public List<Realm> listReams() {
        CriteriaQuery<Realm> query = em.getCriteriaBuilder().createQuery(Realm.class);
        return em.createQuery(query.select(query.from(Realm.class))).getResultList();
    }

    public List<Realm> findRealmsByRegion(Realm.Region region) {
        return em.createQuery("SELECT r FROM Realm r WHERE r.region = :region")
                 .setParameter("region", region)
                 .setMaxResults(5)
                 .getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createRealmFolder(RealmFolder realmFolder) {
        em.persist(realmFolder);
    }

    public RealmFolder findRealmFolderById(Long realmId, FolderType folderType) {
        return em.find(RealmFolder.class, new RealmFolder.RealmFolderPK(realmId, folderType));
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createAuctionFile(AuctionFile auctionFile) {
        em.persist(auctionFile);
    }

    public List<AuctionFile> findAuctionFilesByRegionToLoad(Realm.Region region) {
        return em.createQuery("SELECT af FROM AuctionFile af WHERE af.loaded = false AND af.realm.region = :region")
                 .setParameter("region", region)
                 .getResultList();
    }
}
