package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.entity.Auction;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.AuctionItemStatistics;
import com.radcortez.wow.auctions.entity.FileStatus;
import com.radcortez.wow.auctions.entity.FolderType;
import com.radcortez.wow.auctions.entity.Realm;
import com.radcortez.wow.auctions.entity.RealmFolder;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Roberto Cortez
 */
@SuppressWarnings({"unchecked"})
@ApplicationPath("/resources")
@Path("wowauctions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WoWBusinessBean extends Application implements WoWBusiness {
    @Inject
    protected EntityManager em;

    @Override
    @Transactional
    public void createRealm(Realm realm) {
        em.persist(realm);
    }

    @Override
    @Transactional
    public Realm updateRealm(Realm realm) {
        return em.merge(realm);
    }

    @Override
    @GET
    @Path("realms")
    public List<Realm> listRealms() {
        return em.createNamedQuery("Realm.listRealms").getResultList();
    }

    @Override
    public Realm findRealmById(Long realmId) {
        return em.find(Realm.class, realmId);
    }

    @Override
    public Optional<Realm> findRealmByNameOrSlug(String name, Realm.Region region) {
        Optional<Realm> realm;
        try {
            realm = Optional.of((Realm) em.createNamedQuery("Realm.findByNameOrSlugInRegion")
                                          .setParameter("name", name)
                                          .setParameter("slug", name)
                                          .setParameter("region", region)
                                          .getSingleResult());
        } catch (NoResultException e) {
            realm = Optional.empty();
        }
        return realm;
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
    @Transactional
    public void createRealmFolder(RealmFolder realmFolder) {
        em.persist(realmFolder);
    }

    @Override
    public RealmFolder findRealmFolderById(Long realmId, FolderType folderType) {
        return em.find(RealmFolder.class, new RealmFolder.RealmFolderPK(realmId, folderType));
    }

    @Override
    public boolean checkIfAuctionFileExists(AuctionFile auctionFile) {
        return ((Long) em.createNamedQuery("AuctionFile.exists")
                         .setParameter("url", auctionFile.getUrl())
                         .setParameter("lastModified", auctionFile.getLastModified())
                         .getSingleResult()) > 0;
    }

    @Override
    @Transactional
    public void createAuctionFile(AuctionFile auctionFile) {
        em.persist(auctionFile);
    }

    @Override
    @Transactional
    public AuctionFile updateAuctionFile(AuctionFile auctionFile) {
        return em.merge(auctionFile);
    }

    @Override
    public List<AuctionFile> findAuctionFilesByRealmToProcess(Long realmId) {
        return em.createNamedQuery("AuctionFile.findByRealmAndFileStatus")
                 .setParameter("id", realmId)
                 .setParameter("fileStatus", FileStatus.LOADED)
                 .getResultList();
    }

    @Override
    public AuctionFile findAuctionFileById(Long auctionFileId) {
        return em.find(AuctionFile.class, auctionFileId);
    }

    @Override
    public List<Auction> findAuctionsByRealm(Long realmId, int start, int max) {
        return em.createNamedQuery("Auction.findByRealm")
                 .setParameter("realmId", realmId)
                 .setFirstResult(start)
                 .setMaxResults(max)
                 .getResultList();
    }

    @Override @GET
    @Path("items")
    public List<AuctionItemStatistics> findAuctionItemStatisticsByRealmAndItem(@QueryParam("realmId") Long realmId,
                                                                               @QueryParam("itemId") Integer itemId) {

        Realm realm = (Realm) em.createNamedQuery("Realm.findRealmsWithConnectionsById")
                                .setParameter("id", realmId)
                                .getSingleResult();

        List<Realm> connectedRealms = new ArrayList<>(realm.getConnectedRealms());
        List<Long> ids = connectedRealms.stream().map(Realm::getId).collect(Collectors.toList());
        ids.add(realmId);

        return em.createNamedQuery("AuctionItemStatistics.findByRealmsAndItem")
                 .setParameter("realmIds", ids)
                 .setParameter("itemId", itemId)
                 .getResultList();
    }

    @Override
    @Transactional
    public void deleteAuctionDataByFile(Long fileId) {
        Query deleteQuery = em.createNamedQuery("Auction.deleteByAuctionFile");
        deleteQuery.setParameter("fileId", fileId);
        deleteQuery.executeUpdate();
    }
}
