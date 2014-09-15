package com.radcortez.wow.auctions.batch.files;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.Realm;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author Roberto Cortez
 */
@SuppressWarnings("CdiInjectionPointsInspection")
@RunWith(CdiTestRunner.class)
public class LoadAuctionFilesBatchetTest {
    @Inject
    private EntityManager em;
    @Inject
    private LoadAuctionFilesBatchlet loadAuctionFilesBatchlet;
    @Inject
    private WoWBusiness woWBusiness;

    @Before
    public void setUp() {
        em.getTransaction().begin();
        Realm realm = new Realm();
        realm.setId(1L);
        realm.setName("Hellscream");
        realm.setSlug("hellscream");
        realm.setRegion("EU");
        realm.setStatus(true);
        em.merge(realm);

        Realm anotherRealm = new Realm();
        anotherRealm.setId(2L);
        anotherRealm.setName("Thrall");
        anotherRealm.setSlug("thrall");
        anotherRealm.setRegion("EU");
        anotherRealm.setStatus(true);
        em.merge(anotherRealm);

        em.flush();
    }

    @After
    public void tearDown() throws Exception {
        em.getTransaction().rollback();
    }

    @Test
    public void testValidateDuplicatedFiles() throws Exception {
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
