package com.radcortez.wow.auctions.batch.process.data;

import com.radcortez.wow.auctions.entity.Auction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Roberto Cortez
 */
public class ReadSampleAuctionDataTest {
    private TestableAuctionDataItemReader itemReader;

    @BeforeEach
    public void setUp() {
        itemReader = new TestableAuctionDataItemReader();
        itemReader.open(null);
    }

    @Test
    public void testReadSampleAuctionDataTest() {
        int count = 0;
        Auction auction;
        while ((auction = ((Auction) itemReader.readItem())) != null) {
            count ++;
            assertNotNull(auction);
            System.out.println("auction = " + auction);
        }
        assertEquals(8, count);
    }

    private static class TestableAuctionDataItemReader extends AuctionDataItemReader {
        @Override
        public void open(Serializable checkpoint) {
            setParser(Json.createParser(Thread.currentThread()
                                              .getContextClassLoader()
                                              .getResourceAsStream("samples/auction-data-sample.json")));
        }
    }
}
