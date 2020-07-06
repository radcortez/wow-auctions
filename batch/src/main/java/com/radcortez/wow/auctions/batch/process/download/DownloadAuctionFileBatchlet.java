package com.radcortez.wow.auctions.batch.process.download;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;
import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.ConnectedRealm;
import com.radcortez.wow.auctions.entity.ConnectedRealmFolder;
import com.radcortez.wow.auctions.entity.FileStatus;
import com.radcortez.wow.auctions.entity.FolderType;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;

import javax.batch.api.BatchProperty;
import javax.batch.api.Batchlet;
import javax.batch.runtime.BatchStatus;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.InputStream;

import static org.apache.commons.io.FileUtils.getFile;

/**
 * @author Roberto Cortez
 */
@Dependent
@Named
@Log
public class DownloadAuctionFileBatchlet extends AbstractAuctionFileProcess implements Batchlet {
    @Inject
    WoWBusinessBean woWBusiness;

    @Inject
    @BatchProperty(name = "to")
    String to;
    @Inject
    @BatchProperty(name = "host")
    String host;
    @Inject
    @BatchProperty(name = "endpoint")
    String endpoint;

    @Override
    public String process() {
        log.info(this.getClass().getSimpleName() + " running");

        downloadAuctionFile();

        log.info(this.getClass().getSimpleName() + " completed");

        return BatchStatus.COMPLETED.toString();
    }

    @Override
    public void stop() {}

    private void downloadAuctionFile() {
        final ConnectedRealm connectedRealm = getContext().getConnectedRealm();
        final ConnectedRealmFolder folder = woWBusiness.findRealmFolderById(connectedRealm.getId(), FolderType.valueOf(to));

        log.info("Downloading Auction data for connected realm " + connectedRealm.getId());
        try {
            // TODO - register file download and check if already process before downloading a new one
            final String fileName = "payload-" + System.currentTimeMillis() + ".json";
            final File finalFile = getFile(folder.getPath() + "/" + fileName);
            System.out.println(finalFile);
            if (!finalFile.exists()) {
                final Client client = ClientBuilder.newClient();
                final String region = connectedRealm.getRegion().toString().toLowerCase();

                final InputStream payload =
                    client.target(UriBuilder.fromUri(host).resolveTemplate("region", region))
                          .path(endpoint).resolveTemplate("connectedRealmId", connectedRealm.getId())
                          .queryParam("namespace", "dynamic-" + region)
                          .queryParam("locale", "en_US")
                          .request(MediaType.APPLICATION_JSON)
                          .property("region", region)
                          .get(InputStream.class);

                FileUtils.copyInputStreamToFile(payload, finalFile);

                AuctionFile auctionFile = new AuctionFile();
                auctionFile.setFileName(fileName);
                auctionFile.setFileStatus(FileStatus.DOWNLOADED);
                auctionFile.setConnectedRealm(connectedRealm);

                woWBusiness.createAuctionFile(auctionFile);
                getContext().setFileToProcess(auctionFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
