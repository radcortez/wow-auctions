package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.FileStatus;
import com.radcortez.wow.auctions.entity.Realm;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

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

        AuctionFile auctionFile = new AuctionFile();
        auctionFile.setFileName("test.json");
        auctionFile.setUrl("test.json");
        auctionFile.setLastModified(0L);
        auctionFile.setFileStatus(FileStatus.LOADED);
        auctionFile.setRealm(realm);
        em.persist(auctionFile);
        em.flush();
    }

    @After
    public void tearDown() throws Exception {
        em.getTransaction().rollback();
    }

    @Test
    public void testFindAuctionFilesByRegionToDownload() throws Exception {
        List<AuctionFile> files = woWBusiness.findAuctionFilesByRegionToDownload(Realm.Region.EU);
        assertFalse(files.isEmpty());
    }

    @Test
    public void testFindRealmByNameOrSlug() throws Exception {
        assertNotNull(woWBusiness.findRealmByNameOrSlug("Hellscream", Realm.Region.EU));
        assertNotNull(woWBusiness.findRealmByNameOrSlug("hellscream", Realm.Region.US));
    }
}
