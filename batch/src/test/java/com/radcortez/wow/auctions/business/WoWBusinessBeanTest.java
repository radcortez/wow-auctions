package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.entity.*;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Roberto Cortez
 */
@RunWith(CdiTestRunner.class)
public class WoWBusinessBeanTest {
    @Inject
    private EntityManager em;
    @Inject
    private WoWBusiness woWBusiness;

    @Before
    public void setUp() throws Exception {
        em.getTransaction().begin();
        Realm realm = new Realm();
        realm.setName("Hellscream");
        realm.setSlug("hellscream");
        realm.setRegion("EU");
        realm.setStatus(true);
        em.persist(realm);

        Realm anotherRealm = new Realm();
        anotherRealm.setName("Hellscream");
        anotherRealm.setSlug("hellscream");
        anotherRealm.setRegion("US");
        anotherRealm.setStatus(true);
        em.persist(anotherRealm);

        Realm oneMoreRealm = new Realm();
        oneMoreRealm.setName("Grim Batol");
        oneMoreRealm.setSlug("grimbatol");
        oneMoreRealm.setRegion("EU");
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
        auctionFor33470.setBid(98);
        auctionFor33470.setBuyout(98);
        auctionFor33470.setQuantity(1);
        em.persist(auctionFor33470);

        Auction auctionFor43421 = new Auction();
        auctionFor43421.setAuctionId(297451708L);
        auctionFor43421.setAuctionFile(processedAuctionFile);
        auctionFor43421.setItemId(43421);
        auctionFor43421.setRealm(anotherRealm);
        auctionFor43421.setBid(12999);
        auctionFor43421.setBuyout(12999);
        auctionFor43421.setQuantity(5);
        em.persist(auctionFor43421);

        Auction auctionFor38845 = new Auction();
        auctionFor38845.setAuctionId(297838180L);
        auctionFor38845.setAuctionFile(auctionFile);
        auctionFor38845.setItemId(38845);
        auctionFor38845.setRealm(anotherRealm);
        auctionFor38845.setBid(5642);
        auctionFor38845.setBuyout(5642);
        auctionFor38845.setQuantity(2);
        em.persist(auctionFor38845);

        em.flush();
    }

    @After
    public void tearDown() throws Exception {
        em.getTransaction().rollback();
    }

    @Test
    public void testFindRealmByNameOrSlug() throws Exception {
        assertTrue(woWBusiness.findRealmByNameOrSlug("Hellscream", Realm.Region.EU).isPresent());
        assertTrue(woWBusiness.findRealmByNameOrSlug("hellscream", Realm.Region.US).isPresent());
        assertTrue(woWBusiness.findRealmByNameOrSlug("GrimBatol", Realm.Region.EU).isPresent());
    }

    @Test
    public void testFindAggregatedAuctions() throws Exception {
        Optional<Realm> realm = woWBusiness.findRealmByNameOrSlug("Hellscream", Realm.Region.EU);

        assertTrue(realm.isPresent());

        List<Object> aggregatedAuctions = woWBusiness.findAuctionsAggregatedByFileAndHouse(realm.get().getId(),
                                                                                           AuctionHouse.HORDE, 0, 10);
        assertFalse(aggregatedAuctions.isEmpty());
    }
}
