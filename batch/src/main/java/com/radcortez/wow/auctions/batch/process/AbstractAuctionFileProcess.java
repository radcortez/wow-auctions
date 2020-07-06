package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.ConnectedRealm;
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
    JobContext jobContext;
    @Inject
    WoWBusinessBean woWBusiness;

    @PostConstruct
    private void init() {
        String connectedRealmId = jobContext.getProperties().getProperty("connectedRealmId");

        if (jobContext.getTransientUserData() == null) {
            jobContext.setTransientUserData(new AuctionFileProcessContext(connectedRealmId, null));
        }
    }

    protected AuctionFileProcessContext getContext() {
        return (AuctionFileProcessContext) jobContext.getTransientUserData();
    }

    public class AuctionFileProcessContext {
        private final String connectedRealmId;
        private final String auctionFileId;

        private Realm realm;
        private ConnectedRealm connectedRealm;
        private AuctionFile fileToProcess;

        AuctionFileProcessContext(String connectedRealmId, String auctionFileId) {
            this.connectedRealmId = connectedRealmId;
            this.auctionFileId = auctionFileId;
        }

        @Deprecated
        public Realm getRealm() {
            throw new UnsupportedOperationException();
        }

        public ConnectedRealm getConnectedRealm() {
            if (connectedRealm == null) {
                connectedRealm = woWBusiness.findConnectedRealmById(connectedRealmId);
            }

            return connectedRealm;
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
