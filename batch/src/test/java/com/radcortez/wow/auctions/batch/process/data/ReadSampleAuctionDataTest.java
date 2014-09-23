package com.radcortez.wow.auctions.batch.process.data;

import com.radcortez.wow.auctions.entity.Auction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.json.Json;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        int count = 0;
        Auction auction;
        while ((auction = ((Auction) itemReader.readItem())) != null) {
            count ++;
            assertNotNull(auction);
            System.out.println("auction = " + auction);
        }
        assertEquals(8, count);
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
