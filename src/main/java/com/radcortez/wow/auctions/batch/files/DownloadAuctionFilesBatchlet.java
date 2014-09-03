package com.radcortez.wow.auctions.batch.files;

import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.FolderType;
import com.radcortez.wow.auctions.entity.Realm;
import com.radcortez.wow.auctions.entity.RealmFolder;
import org.apache.commons.io.FileUtils;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.inject.Inject;
import javax.inject.Named;
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
    private WoWBusinessBean woWBusinessBean;

    @Inject
    @BatchProperty(name = "region")
    private String region;
    @Inject
    @BatchProperty(name = "to")
    private String to;

    @Override
    public String process() throws Exception {
        List<AuctionFile> files =
                woWBusinessBean.findAuctionFilesByRegionToDownload(Realm.Region.valueOf(region));

        files.stream().forEach(this::downloadAuctionFile);

        return "COMPLETED";
    }

    private void downloadAuctionFile(AuctionFile auctionFile) {
        RealmFolder folder =
                woWBusinessBean.findRealmFolderById(auctionFile.getRealm().getId(), FolderType.valueOf(to));

        getLogger(this.getClass().getName()).log(Level.INFO,
                                                 "Downloadig Auction file " + auctionFile.getUrl() +
                                                 " to " + folder.getPath());
        try {
            FileUtils.copyURLToFile(new URL(auctionFile.getUrl()), getFile(folder.getPath() + "/" + auctionFile.getFileName()));
            auctionFile.setDownloaded(true);
            woWBusinessBean.updateAuctionFile(auctionFile);
        } catch (FileNotFoundException e) {
            getLogger(this.getClass().getName()).log(Level.INFO,
                                                     "Could not download Auction file " + auctionFile.getUrl() +
                                                     " from " + auctionFile.getRealm().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
