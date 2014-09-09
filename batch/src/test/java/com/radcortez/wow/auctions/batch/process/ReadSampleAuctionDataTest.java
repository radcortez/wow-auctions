package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.entity.Auction;
import com.radcortez.wow.auctions.entity.AuctionHouse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@SuppressWarnings("CdiInjectionPointsInspection")
@RunWith(JUnit4.class)
public class ReadSampleAuctionDataTest {
    private TestableAuctionDataItemReader itemReader;

    @Before
    public void setUp() throws Exception {
        itemReader = new TestableAuctionDataItemReader();
        itemReader.open(null);
    }

    @Test
    public void testReadSampleAuctionDataTest() throws Exception {
        Auction auction;
        while ((auction = ((Auction) itemReader.readItem())) != null) {
            System.out.println("auction = " + auction);
        }
    }

    private class TestableAuctionDataItemReader extends AuctionDataItemReader {
        @Override
        public void open(Serializable checkpoint) throws Exception {
            setParser(Json.createParser(Thread.currentThread()
                                              .getContextClassLoader()
                                              .getResourceAsStream("samples/auction-data-sample.json")));
        }
    }
}
