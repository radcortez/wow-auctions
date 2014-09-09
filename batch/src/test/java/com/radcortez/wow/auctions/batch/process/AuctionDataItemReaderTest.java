package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.entity.Auction;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * @author Roberto Cortez
 */
@SuppressWarnings("CdiInjectionPointsInspection")
@RunWith(CdiTestRunner.class)
public class AuctionDataItemReaderTest {
    @Inject
    private AuctionDataItemReader itemReader;

    @Before
    public void setUp() throws Exception {
        itemReader.open(null);
    }

    @Test
    public void testReadItem() throws Exception {
        itemReader.open(null);
        Auction auction;
        while ((auction = ((Auction) itemReader.readItem())) != null) {
            System.out.println("auction = " + auction);
        }

    }
}
