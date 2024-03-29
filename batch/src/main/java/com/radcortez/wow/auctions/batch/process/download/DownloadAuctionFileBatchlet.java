package com.radcortez.wow.auctions.batch.process.download;

import com.radcortez.wow.auctions.api.ApiConfig;
import com.radcortez.wow.auctions.api.ConnectedRealmsApi;
import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.ConnectedRealm;
import com.radcortez.wow.auctions.entity.FileStatus;
import com.radcortez.wow.auctions.entity.Folder;
import com.radcortez.wow.auctions.entity.FolderType;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.batch.api.Batchlet;
import jakarta.batch.runtime.BatchStatus;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
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
    Config config;
    @Inject
    ConnectedRealmsApi connectedRealmsApi;

    @Override
    @Transactional
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
        final Folder folder = connectedRealm.getFolders()
                                            .get(FolderType.valueOf(
                                                config.getConfigValue("wow.batch.download.to").getValue()));

        log.info("Downloading Auction data for connected realm " + connectedRealm.getId());
        // TODO - register file download and check if already process before downloading a new one
        final long timestamp = System.currentTimeMillis();
        final String fileName = "payload-" + timestamp + ".json";
        final File finalFile = getFile(folder.getPath() + "/" + fileName);
        if (!finalFile.exists()) {
            try (InputStream payload = connectedRealmsApi.auctions(connectedRealm.getId())) {
                FileUtils.copyInputStreamToFile(payload, finalFile);
                log.info("Copied Auction data to " + finalFile);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }

            final AuctionFile auctionFile =
                AuctionFile.builder()
                           .fileName(fileName)
                           .fileStatus(FileStatus.DOWNLOADED)
                           .connectedRealm(connectedRealm)
                           .timestamp(timestamp)
                           .build();

            getContext().setAuctionFile(auctionFile.create());
        }
    }
}
