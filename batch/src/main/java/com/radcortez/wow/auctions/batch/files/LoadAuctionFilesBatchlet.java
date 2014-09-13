package com.radcortez.wow.auctions.batch.files;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.FileStatus;
import com.radcortez.wow.auctions.entity.Realm;
import lombok.Data;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Level;

import static java.util.logging.Logger.getLogger;

/**
 * @author Roberto Cortez
 */
@Named
public class LoadAuctionFilesBatchlet extends AbstractBatchlet {
    @Inject
    private WoWBusiness woWBusiness;

    @Inject
    @BatchProperty(name = "region")
    private String region;
    @Inject
    @BatchProperty(name = "target")
    private String target;

    @Override
    public String process() throws Exception {
        getLogger(this.getClass().getName()).log(Level.INFO, this.getClass().getSimpleName() + " running");

        List<Realm> realmsByRegion = woWBusiness.findRealmsByRegion(Realm.Region.valueOf(region));
        realmsByRegion.parallelStream().forEach(this::getRealmAuctionFileInformation);

        getLogger(this.getClass().getName()).log(Level.INFO, this.getClass().getSimpleName() + " completed");
        return "COMPLETED";
    }

    private void getRealmAuctionFileInformation(Realm realm) {
        getLogger(this.getClass().getName()).log(Level.INFO, "Getting files for " + realm.getRealmDetail());

        Client client = ClientBuilder.newClient();
        Files files = client.target(target + realm.getSlug())
                             .request(MediaType.TEXT_PLAIN)
                             .get(Files.class);

        files.getFiles().forEach(auctionFile -> {
            auctionFile.setRealm(realm);
            auctionFile.setFileName("auctions." + auctionFile.getLastModified() + ".json");
            auctionFile.setFileStatus(FileStatus.LOADED);
            woWBusiness.createAuctionFile(auctionFile);
        });
    }

    @Data
    public static class Files {
        private List<AuctionFile> files;
    }
}
