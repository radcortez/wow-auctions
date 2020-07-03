package com.radcortez.wow.auctions.batch.process.data;

import com.google.common.collect.Lists;
import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.Auction;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.Realm;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Roberto Cortez
 */
@SuppressWarnings({"unchecked", "CdiInjectionPointsInspection"})
public class AuctionDataItemReaderTest {
    @Inject
    EntityManager em;
    @Inject
    WoWBusiness woWBusiness;

    @Inject
    AuctionDataItemReader itemReader;
    @Inject
    AuctionDataItemProcessor itemProcessor;
    @Inject
    AuctionDataItemWriter itemWriter;

    @BeforeEach
    public void setUp() {
        em.getTransaction().begin();

        Realm realm = new Realm();
        realm.setId(1L);
        realm.setName("Grim Batol");
        realm.setSlug("grimbatol");
        realm.setRegion(Realm.Region.EU);
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

    @AfterEach
    public void tearDown() {
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
