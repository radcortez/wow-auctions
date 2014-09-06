package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.business.repository.AuctionFileRepository;
import com.radcortez.wow.auctions.business.repository.RealmRepository;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.FileStatus;
import com.radcortez.wow.auctions.entity.Realm;
import junit.framework.Assert;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@RunWith(CdiTestRunner.class)
public class WoWBusinessBeanTest {
    @Inject
    private RealmRepository realmRepository;
    @Inject
    AuctionFileRepository auctionFileRepository;

    @Inject
    private WoWBusiness woWBusiness;

    @Before
    public void setUp() throws Exception {
        Realm realm = new Realm();
        realm.setName("Hellscream");
        realm.setSlug("hellscream");
        realm.setRegion("EU");
        realm.setStatus(true);
        realmRepository.save(realm);

        AuctionFile auctionFile = new AuctionFile();
        auctionFile.setId(1L);
        auctionFile.setFileName("test.json");
        auctionFile.setUrl("test.json");
        auctionFile.setLastModified(0L);
        auctionFile.setFileStatus(FileStatus.DOWNLOADED);
        auctionFile.setRealm(realm);
        auctionFileRepository.save(auctionFile);
    }

    @Test
    public void testFindAuctionFilesByRegionToDownload() throws Exception {
        List<AuctionFile> files = woWBusiness.findAuctionFilesByRegionToDownload(Realm.Region.EU);
        Assert.assertFalse(files.isEmpty());
    }
}
