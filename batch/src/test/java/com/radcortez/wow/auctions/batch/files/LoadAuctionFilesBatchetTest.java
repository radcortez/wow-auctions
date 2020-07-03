package com.radcortez.wow.auctions.batch.files;

import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.Realm;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Roberto Cortez
 */
//@QuarkusTest
public class LoadAuctionFilesBatchetTest {
    @Inject
    EntityManager em;
    @Inject
    LoadAuctionFilesBatchlet loadAuctionFilesBatchlet;
    @Inject
    WoWBusinessBean woWBusiness;

    public void setUp() {
        em.getTransaction().begin();
        Realm realm = new Realm();
        realm.setId(1L);
        realm.setName("Hellscream");
        realm.setSlug("hellscream");
        realm.setRegion(Realm.Region.EU);
        realm.setStatus(true);
        em.merge(realm);

        Realm anotherRealm = new Realm();
        anotherRealm.setId(2L);
        anotherRealm.setName("Thrall");
        anotherRealm.setSlug("thrall");
        anotherRealm.setRegion(Realm.Region.EU);
        anotherRealm.setStatus(true);
        em.merge(anotherRealm);

        em.flush();
    }

    @AfterEach
    public void tearDown() {
        em.getTransaction().rollback();
    }

    @Test
    public void testValidateDuplicatedFiles() {
        Realm hellscream = woWBusiness.findRealmById(1L);

        AuctionFile auctionFile = new AuctionFile();
        auctionFile.setUrl("http://eu.battle.net/auction-data/1878bff06a82775ebf6438e312cd2682/auctions.json");
        auctionFile.setLastModified(1L);

        loadAuctionFilesBatchlet.createAuctionFile(hellscream, auctionFile);
        assertFalse(woWBusiness.findAuctionFilesByRealmToProcess(hellscream.getId()).isEmpty());

        auctionFile = new AuctionFile();
        auctionFile.setUrl("http://eu.battle.net/auction-data/1878bff06a82775ebf6438e312cd2682/auctions.json");
        auctionFile.setLastModified(1L);

        loadAuctionFilesBatchlet.createAuctionFile(hellscream, auctionFile);
        List<AuctionFile> auctionFilesByRegionToDownload =
                woWBusiness.findAuctionFilesByRealmToProcess(hellscream.getId());
        assertFalse(auctionFilesByRegionToDownload.isEmpty());
        assertEquals(1, auctionFilesByRegionToDownload.size());
    }
}
