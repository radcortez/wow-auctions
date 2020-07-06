package com.radcortez.wow.auctions.batch.files;

import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.wow.auctions.QuarkusDataSourceProvider;
import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.Realm;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Roberto Cortez
 */
@QuarkusTest
@FlywayTest(@DataSource(QuarkusDataSourceProvider.class))
public class LoadAuctionFilesBatchetTest {
    @Inject
    LoadAuctionFilesBatchlet loadAuctionFilesBatchlet;
    @Inject
    WoWBusinessBean woWBusiness;

    @Test
    public void testValidateDuplicatedFiles() {
        Realm hellscream = woWBusiness.findRealmById("1");

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
