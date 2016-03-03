package com.radcortez.wow.auctions.batch.files;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.FileStatus;
import com.radcortez.wow.auctions.entity.Realm;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
    @BatchProperty(name = "locale")
    private String locale;
    @Inject
    @BatchProperty(name = "apikey")
    private String apiKey;
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

    void getRealmAuctionFileInformation(Realm realm) {
        getLogger(this.getClass().getName()).log(Level.INFO, "Getting files for " + realm.getRealmDetail());

        Client client = ClientBuilder.newClient();
        try {
            Files files = client.target(target + realm.getSlug())
                                .queryParam("locale", locale)
                                .queryParam("apikey", apiKey)
                                .request(MediaType.TEXT_PLAIN).async()
                                .get(Files.class)
                                .get(5, TimeUnit.SECONDS);

            files.getFiles().forEach(auctionFile -> createAuctionFile(realm, auctionFile));
        } catch (Exception e) {
            getLogger(this.getClass().getName()).log(Level.INFO, "Could not get files for " + realm.getRealmDetail());
        } finally {
            client.close();
        }
    }

    void createAuctionFile(Realm realm, AuctionFile auctionFile) {
        auctionFile.setRealm(realm);
        auctionFile.setFileName("auctions." + auctionFile.getLastModified() + ".json");
        auctionFile.setFileStatus(FileStatus.LOADED);

        if (!woWBusiness.checkIfAuctionFileExists(auctionFile)) {
            woWBusiness.createAuctionFile(auctionFile);
            getLogger(this.getClass().getName()).log(Level.INFO, "Created Auction File " +
                                                                 auctionFile.getUrl() +
                                                                 " for Realm " + realm.getName());
        } else {
            getLogger(this.getClass().getName()).log(Level.INFO, "Auction File " +
                                                                 auctionFile.getUrl() +
                                                                 " already loaded by another Realm");
        }
    }

    public static class Files {
        private List<AuctionFile> files;

        public List<AuctionFile> getFiles() {
            return files;
        }

        public void setFiles(List<AuctionFile> files) {
            this.files = files;
        }
    }
}
