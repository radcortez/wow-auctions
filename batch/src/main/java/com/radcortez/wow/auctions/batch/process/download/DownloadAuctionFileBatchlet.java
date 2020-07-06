package com.radcortez.wow.auctions.batch.process.download;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;
import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.FileStatus;
import com.radcortez.wow.auctions.entity.FolderType;
import com.radcortez.wow.auctions.entity.ConnectedRealmFolder;
import org.apache.commons.io.FileUtils;

import javax.batch.api.BatchProperty;
import javax.batch.api.Batchlet;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;

import static java.util.logging.Logger.getLogger;
import static org.apache.commons.io.FileUtils.getFile;

/**
 * @author Roberto Cortez
 */
@Named
public class DownloadAuctionFileBatchlet extends AbstractAuctionFileProcess implements Batchlet {
    @Inject
    WoWBusiness woWBusiness;

    @Inject
    @BatchProperty(name = "to")
    String to;

    @Override
    public String process() throws Exception {
        getLogger(this.getClass().getName()).log(Level.INFO, this.getClass().getSimpleName() + " running");

        downloadAuctionFile(getContext().getFileToProcess());

        getLogger(this.getClass().getName()).log(Level.INFO, this.getClass().getSimpleName() + " completed");
        return "COMPLETED";
    }

    @Override
    public void stop() throws Exception {}

    private void downloadAuctionFile(AuctionFile auctionFile) {
        ConnectedRealmFolder
            folder = woWBusiness.findRealmFolderById(auctionFile.getRealm().getId(), FolderType.valueOf(to));

        getLogger(this.getClass().getName()).log(Level.INFO,
                                                 "Downloadig Auction file " +
                                                 auctionFile.getUrl() +
                                                 " to " +
                                                 folder.getPath()
                                                );
        try {
            File finalFile = getFile(folder.getPath() + "/" + auctionFile.getFileName());
            if (!finalFile.exists()) {
                FileUtils.copyURLToFile(new URL(auctionFile.getUrl()), finalFile);
            }

            auctionFile.setFileStatus(FileStatus.DOWNLOADED);
            woWBusiness.updateAuctionFile(auctionFile);
        } catch (FileNotFoundException e) {
            getLogger(this.getClass().getName()).log(Level.INFO,
                                                     "Could not download Auction file " +
                                                     auctionFile.getUrl() +
                                                     " from " +
                                                     auctionFile.getRealm().getName()
                                                    );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
