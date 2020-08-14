package com.radcortez.wow.auctions.batch.process.data;

import com.google.common.collect.Lists;
import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.wow.auctions.QuarkusDataSourceProvider;
import com.radcortez.wow.auctions.entity.Auction;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.ConnectedRealm;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
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

            AuctionFile auctionFile = AuctionFile.findById("1");
            itemProcessor.getContext().setAuctionFile(auctionFile);
            auction = (Auction) itemProcessor.processItem(auction);

            itemWriter.writeItems(Lists.newArrayList(auction));
        }
        assertEquals(8, count);

        List<Auction> auctions = Auction.findByConnectedRealm(ConnectedRealm.builder().id("1").build());
        assertEquals(8, auctions.size());
    }
}
