package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.entity.Auction;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.AuctionItemStatistics;
import com.radcortez.wow.auctions.entity.FileStatus;
import com.radcortez.wow.auctions.entity.FolderType;
import com.radcortez.wow.auctions.entity.Realm;
import com.radcortez.wow.auctions.entity.RealmFolder;

import javax.enterprise.context.ApplicationScoped;
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
@ApplicationScoped
@ApplicationPath("/resources")
@Path("wowauctions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WoWBusinessBean extends Application {
    @Inject
    protected EntityManager em;

    @Transactional
    public void createRealm(Realm realm) {
        em.persist(realm);
    }

    @Transactional
    public Realm updateRealm(Realm realm) {
        return em.merge(realm);
    }

    @GET
    @Path("realms")
    public List<Realm> listRealms() {
        return em.createNamedQuery("Realm.listRealms").getResultList();
    }

    public Realm findRealmById(String  realmId) {
        return em.find(Realm.class, realmId);
    }

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

    public List<Realm> findRealmsByRegion(Realm.Region region) {
        return em.createNamedQuery("Realm.findByRegion").setParameter("region", region).getResultList();
    }

    public boolean checkIfRealmExists(Realm realm) {
        return ((Long) em.createNamedQuery("Realm.exists")
                         .setParameter("name", realm.getName())
                         .setParameter("region", realm.getRegion())
                         .getSingleResult()) > 0;
    }

    @Transactional
    public void createRealmFolder(RealmFolder realmFolder) {
        em.persist(realmFolder);
    }

    public RealmFolder findRealmFolderById(String realmId, FolderType folderType) {
        return em.find(RealmFolder.class, new RealmFolder.RealmFolderPK(realmId, folderType));
    }

    public boolean checkIfAuctionFileExists(AuctionFile auctionFile) {
        return ((Long) em.createNamedQuery("AuctionFile.exists")
                         .setParameter("url", auctionFile.getUrl())
                         .setParameter("lastModified", auctionFile.getLastModified())
                         .getSingleResult()) > 0;
    }

    @Transactional
    public void createAuctionFile(AuctionFile auctionFile) {
        em.persist(auctionFile);
    }

    @Transactional
    public AuctionFile updateAuctionFile(AuctionFile auctionFile) {
        return em.merge(auctionFile);
    }

    public List<AuctionFile> findAuctionFilesByRealmToProcess(String  realmId) {
        return em.createNamedQuery("AuctionFile.findByRealmAndFileStatus")
                 .setParameter("id", realmId)
                 .setParameter("fileStatus", FileStatus.LOADED)
                 .getResultList();
    }

    public AuctionFile findAuctionFileById(String auctionFileId) {
        return em.find(AuctionFile.class, auctionFileId);
    }

    public List<Auction> findAuctionsByRealm(String realmId, int start, int max) {
        return em.createNamedQuery("Auction.findByRealm")
                 .setParameter("realmId", realmId)
                 .setFirstResult(start)
                 .setMaxResults(max)
                 .getResultList();
    }

    @Path("items")
    public List<AuctionItemStatistics> findAuctionItemStatisticsByRealmAndItem(@QueryParam("realmId") String realmId,
                                                                               @QueryParam("itemId") Integer itemId) {

        Realm realm = (Realm) em.createNamedQuery("Realm.findRealmsWithConnectionsById")
                                .setParameter("id", realmId)
                                .getSingleResult();

        List<Realm> connectedRealms = new ArrayList<>(realm.getConnectedRealms());
        List<String> ids = connectedRealms.stream().map(Realm::getId).collect(Collectors.toList());
        ids.add(realmId);

        return em.createNamedQuery("AuctionItemStatistics.findByRealmsAndItem")
                 .setParameter("realmIds", ids)
                 .setParameter("itemId", itemId)
                 .getResultList();
    }

    @Transactional
    public void deleteAuctionDataByFile(String fileId) {
        Query deleteQuery = em.createNamedQuery("Auction.deleteByAuctionFile");
        deleteQuery.setParameter("fileId", fileId);
        deleteQuery.executeUpdate();
    }
}
