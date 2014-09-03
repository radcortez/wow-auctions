package com.radcortez.wow.auctions.batch.files;

import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.entity.AuctionFile;
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
    private WoWBusinessBean woWBusinessBean;

    @Inject
    @BatchProperty(name = "region")
    private String region;
    @Inject
    @BatchProperty(name = "target")
    private String target;

    @Override
    public String process() throws Exception {
        List<Realm> realmsByRegion = woWBusinessBean.findRealmsByRegion(Realm.Region.valueOf(region));

        realmsByRegion.parallelStream().forEach(this::getRealmAuctionFileInformation);

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
            woWBusinessBean.createAuctionFile(auctionFile);
        });
    }

    @Data
    public static class Files {
        private List<AuctionFile> files;
    }
}
