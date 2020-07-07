package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.ConnectedRealm;
import com.radcortez.wow.auctions.entity.FolderType;
import com.radcortez.wow.auctions.entity.Realm;
import lombok.RequiredArgsConstructor;

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
    void init() {
        String connectedRealmId = jobContext.getProperties().getProperty("connectedRealmId");

        if (jobContext.getTransientUserData() == null) {
            jobContext.setTransientUserData(new AuctionFileProcessContext(connectedRealmId));
        }
    }

    public AuctionFileProcessContext getContext() {
        return (AuctionFileProcessContext) jobContext.getTransientUserData();
    }

    @RequiredArgsConstructor
    public class AuctionFileProcessContext {
        private final String connectedRealmId;

        private ConnectedRealm connectedRealm;
        private AuctionFile auctionFile;

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

        public AuctionFile getAuctionFile() {
            if (auctionFile == null) {
                throw new IllegalStateException();
            }
            return auctionFile;
        }

        public void setAuctionFile(AuctionFile auctionFile) {
            this.auctionFile = auctionFile;
        }

        public File getAuctionFile(FolderType folderType) {
            return getFile(woWBusiness.findRealmFolderById(connectedRealmId, folderType).getPath() +
                           "/" +
                           getAuctionFile().getFileName());
        }

        public File getFolder(FolderType folderType) {
            return getFile(woWBusiness.findRealmFolderById(connectedRealmId, folderType).getPath());
        }
    }
}
