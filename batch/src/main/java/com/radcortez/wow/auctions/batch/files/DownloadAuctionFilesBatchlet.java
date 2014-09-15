package com.radcortez.wow.auctions.batch.files;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.*;
import org.apache.commons.io.FileUtils;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;

import static java.util.logging.Logger.getLogger;
import static org.apache.commons.io.FileUtils.getFile;

/**
 * @author Roberto Cortez
 */
@Named
public class DownloadAuctionFilesBatchlet extends AbstractBatchlet {
    @Inject
    private WoWBusiness woWBusiness;

    @Inject
    @BatchProperty(name = "region")
    private String region;
    @Inject
    @BatchProperty(name = "to")
    private String to;

    @Override
    public String process() throws Exception {
        getLogger(this.getClass().getName()).log(Level.INFO, this.getClass().getSimpleName() + " running");

        List<AuctionFile> files = woWBusiness.findAuctionFilesByRegionToDownload(Realm.Region.valueOf(region));
        files.parallelStream().forEach(this::downloadAuctionFile);

        getLogger(this.getClass().getName()).log(Level.INFO, this.getClass().getSimpleName() + " completed");
        return "COMPLETED";
    }

    private void downloadAuctionFile(AuctionFile auctionFile) {
        RealmFolder folder = woWBusiness.findRealmFolderById(auctionFile.getRealm().getId(), FolderType.valueOf(to));

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
