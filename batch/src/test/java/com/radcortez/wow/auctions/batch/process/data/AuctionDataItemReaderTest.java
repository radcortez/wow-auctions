package com.radcortez.wow.auctions.batch.process.data;

import com.google.common.collect.Lists;
import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.Auction;
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
import static org.junit.Assert.assertNotNull;

/**
 * @author Roberto Cortez
 */
@SuppressWarnings({"unchecked", "CdiInjectionPointsInspection"})
@RunWith(CdiTestRunner.class)
public class AuctionDataItemReaderTest {
    @Inject
    private EntityManager em;
    @Inject
    private WoWBusiness woWBusiness;

    @Inject
    private AuctionDataItemReader itemReader;
    @Inject
    private AuctionDataItemProcessor itemProcessor;
    @Inject
    private AuctionDataItemWriter itemWriter;

    @Before
    public void setUp() throws Exception {
        em.getTransaction().begin();

        Realm realm = new Realm();
        realm.setId(1L);
        realm.setName("Grim Batol");
        realm.setSlug("grimbatol");
        realm.setRegion("EU");
        realm.setStatus(true);
        em.merge(realm);

        AuctionFile auctionFile = new AuctionFile();
        auctionFile.setId(1L);
        auctionFile.setFileName("auction-data-sample.json");
        auctionFile.setLastModified(1L);
        auctionFile.setRealm(realm);
        em.merge(auctionFile);

        em.flush();
    }

    @After
    public void tearDown() throws Exception {
        em.getTransaction().rollback();
    }

    @Test
    public void testReadItem() throws Exception {
        itemReader.open(null);

        int count = 0;
        Auction auction;
        while ((auction = ((Auction) itemReader.readItem())) != null) {
            count ++;
            assertNotNull(auction);

            auction = (Auction) itemProcessor.processItem(auction);

            itemWriter.writeItems(Lists.newArrayList(auction));
            em.flush();
        }
        assertEquals(8, count);

        List<Auction> auctionsGrinBatol = woWBusiness.findAuctionsByRealm(1L, 0, 10);
        assertEquals(8, auctionsGrinBatol.size());
    }
}
