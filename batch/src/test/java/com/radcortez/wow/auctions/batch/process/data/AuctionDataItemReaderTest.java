package com.radcortez.wow.auctions.batch.process.data;

import com.google.common.collect.Lists;
import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.wow.auctions.QuarkusDataSourceProvider;
import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.entity.Auction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Roberto Cortez
 */
@QuarkusTest
@FlywayTest(@DataSource(QuarkusDataSourceProvider.class))
public class AuctionDataItemReaderTest {
    @Inject
    EntityManager em;
    @Inject
    WoWBusinessBean woWBusiness;

    @Inject
    AuctionDataItemReader itemReader;
    @Inject
    AuctionDataItemProcessor itemProcessor;
    @Inject
    AuctionDataItemWriter itemWriter;

    @Test
    @Transactional
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

        List<Auction> auctionsGrinBatol = woWBusiness.findAuctionsByRealm("1", 0, 10);
        assertEquals(8, auctionsGrinBatol.size());
    }
}
