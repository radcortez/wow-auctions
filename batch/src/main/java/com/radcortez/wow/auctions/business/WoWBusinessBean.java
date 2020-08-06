package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.entity.Auction;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.ConnectedRealm;
import com.radcortez.wow.auctions.entity.Folder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

/**
 * @author Roberto Cortez
 */
@ApplicationScoped
@ApplicationPath("/resources")
@Path("wowauctions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WoWBusinessBean extends Application {
    @Inject
    protected EntityManager em;

    public Optional<ConnectedRealm> findConnectedRealm(String connectedRealmId) {
        return Optional.ofNullable(em.find(ConnectedRealm.class, connectedRealmId));
    }

    @Transactional
    public Optional<ConnectedRealm> createConnectedRealm(ConnectedRealm connectedRealm) {
        em.persist(connectedRealm);
        return Optional.of(connectedRealm);
    }

    @Path("connectedRealms")
    public List<ConnectedRealm> listConnectedRealms() {
        return em.createQuery("SELECT cr FROM ConnectedRealm cr", ConnectedRealm.class).getResultList();
    }

    @Transactional
    public void createRealmFolder(Folder folder) {
        em.merge(folder);
    }

    @Transactional
    public void createAuctionFile(AuctionFile auctionFile) {
        em.persist(auctionFile);
    }

    @Transactional
    public AuctionFile updateAuctionFile(AuctionFile auctionFile) {
        return em.merge(auctionFile);
    }

//    public List<AuctionFile> findAuctionFilesByRealmToProcess(String  realmId) {
//        return em.createNamedQuery("AuctionFile.findByRealmAndFileStatus")
//                 .setParameter("id", realmId)
//                 .setParameter("fileStatus", FileStatus.LOADED)
//                 .getResultList();
//    }

    public AuctionFile findAuctionFileById(String auctionFileId) {
        return em.find(AuctionFile.class, auctionFileId);
    }

    public List<Auction> findAuctionsByRealm(String realmId, int start, int max) {
        return em.createNamedQuery("Auction.findByConnectedRealm", Auction.class)
                 .setParameter("connectedRealmId", realmId)
                 .setFirstResult(start)
                 .setMaxResults(max)
                 .getResultList();
    }

    /*
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
     */

    @Transactional
    public void deleteAuctionDataByFile(String fileId) {
        Query deleteQuery = em.createNamedQuery("Auction.deleteByAuctionFile");
        deleteQuery.setParameter("fileId", fileId);
        deleteQuery.executeUpdate();
    }
}
