package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.entity.Auction;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.FileStatus;
import com.radcortez.wow.auctions.entity.Realm;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Roberto Cortez
 */
@QuarkusTest
public class WoWBusinessBeanTest {
    @Inject
    EntityManager em;
    @Inject
    WoWBusiness woWBusiness;

    @BeforeEach
    public void setUp() {
        em.getTransaction().begin();
        Realm realm = new Realm();
        realm.setName("Hellscream");
        realm.setSlug("hellscream");
        realm.setRegion(Realm.Region.EU);
        realm.setStatus(true);
        em.persist(realm);

        Realm anotherRealm = new Realm();
        anotherRealm.setName("Hellscream");
        anotherRealm.setSlug("hellscream");
        anotherRealm.setRegion(Realm.Region.EU);
        anotherRealm.setStatus(true);
        em.persist(anotherRealm);

        Realm oneMoreRealm = new Realm();
        oneMoreRealm.setName("Grim Batol");
        oneMoreRealm.setSlug("grimbatol");
        oneMoreRealm.setRegion(Realm.Region.EU);
        oneMoreRealm.setStatus(true);
        em.persist(oneMoreRealm);

        AuctionFile auctionFile = new AuctionFile();
        auctionFile.setFileName("test.json");
        auctionFile.setUrl("test.json");
        auctionFile.setLastModified(0L);
        auctionFile.setFileStatus(FileStatus.LOADED);
        auctionFile.setRealm(realm);
        em.persist(auctionFile);

        AuctionFile processedAuctionFile = new AuctionFile();
        processedAuctionFile.setFileName("processed.json");
        processedAuctionFile.setUrl("processed.json");
        processedAuctionFile.setLastModified(0L);
        processedAuctionFile.setFileStatus(FileStatus.PROCESSED);
        processedAuctionFile.setRealm(realm);
        em.persist(processedAuctionFile);

        Auction auctionFor33470 = new Auction();
        auctionFor33470.setAuctionId(297747945L);
        auctionFor33470.setAuctionFile(processedAuctionFile);
        auctionFor33470.setItemId(33470);
        auctionFor33470.setRealm(anotherRealm);
        auctionFor33470.setBid(98L);
        auctionFor33470.setBuyout(98L);
        auctionFor33470.setQuantity(1);
        em.persist(auctionFor33470);

        Auction auctionFor43421 = new Auction();
        auctionFor43421.setAuctionId(297451708L);
        auctionFor43421.setAuctionFile(processedAuctionFile);
        auctionFor43421.setItemId(43421);
        auctionFor43421.setRealm(anotherRealm);
        auctionFor43421.setBid(12999L);
        auctionFor43421.setBuyout(12999L);
        auctionFor43421.setQuantity(5);
        em.persist(auctionFor43421);

        Auction auctionFor38845 = new Auction();
        auctionFor38845.setAuctionId(297838180L);
        auctionFor38845.setAuctionFile(auctionFile);
        auctionFor38845.setItemId(38845);
        auctionFor38845.setRealm(anotherRealm);
        auctionFor38845.setBid(5642L);
        auctionFor38845.setBuyout(5642L);
        auctionFor38845.setQuantity(2);
        em.persist(auctionFor38845);

        em.flush();
    }

    @AfterEach
    public void tearDown() {
        em.getTransaction().rollback();
    }

    @Test
    public void testFindRealmByNameOrSlug() {
        assertTrue(woWBusiness.findRealmByNameOrSlug("Hellscream", Realm.Region.EU).isPresent());
        assertTrue(woWBusiness.findRealmByNameOrSlug("hellscream", Realm.Region.US).isPresent());
        assertTrue(woWBusiness.findRealmByNameOrSlug("GrimBatol", Realm.Region.EU).isPresent());
    }
}
