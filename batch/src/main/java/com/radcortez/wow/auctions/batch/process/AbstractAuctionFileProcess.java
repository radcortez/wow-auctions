package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.FolderType;
import com.radcortez.wow.auctions.entity.Realm;

import javax.annotation.PostConstruct;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import java.io.File;

import static org.apache.commons.io.FileUtils.getFile;

/**
 * @author Roberto Cortez
 */
public abstract class AbstractAuctionFileProcess {
    @Inject
    private JobContext jobContext;
    @Inject
    private WoWBusiness woWBusiness;

    @PostConstruct
    private void init() {
        Long realmId = Long.valueOf(jobContext.getProperties().getProperty("realmId"));
        Long auctionFileId = Long.valueOf(jobContext.getProperties().getProperty("auctionFileId"));

        if (jobContext.getTransientUserData() == null) {
            jobContext.setTransientUserData(new AuctionFileProcessContext(realmId, auctionFileId));
        }
    }

    protected AuctionFileProcessContext getContext() {
        return (AuctionFileProcessContext) jobContext.getTransientUserData();
    }

    public class AuctionFileProcessContext {
        private Long realmId;
        private Long auctionFileId;

        private Realm realm;
        private AuctionFile fileToProcess;

        private AuctionFileProcessContext(Long realmId, Long auctionFileId) {
            this.realmId = realmId;
            this.auctionFileId = auctionFileId;
        }

        public Realm getRealm() {
            if (realm == null) {
                realm = woWBusiness.findRealmById(realmId);
            }

            return realm;
        }

        public AuctionFile getFileToProcess() {
            if (fileToProcess == null) {
                this.fileToProcess = woWBusiness.findAuctionFileById(auctionFileId);
            }

            return fileToProcess;
        }

        public File getFileToProcess(FolderType folderType) {
            return getFile(woWBusiness.findRealmFolderById(getRealm().getId(), folderType).getPath() +
                           "/" +
                           getFileToProcess().getFileName());
        }

        public File getFolder(FolderType folderType) {
            return getFile(woWBusiness.findRealmFolderById(getRealm().getId(), folderType).getPath());
        }
    }
}
