package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.ConnectedRealm;
import com.radcortez.wow.auctions.entity.FolderType;
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

    @PostConstruct
    void init() {
        final String connectedRealmId = jobContext.getProperties().getProperty("connectedRealmId");

        if (jobContext.getTransientUserData() == null) {
            jobContext.setTransientUserData(new AuctionFileProcessContext(connectedRealmId));
        }
    }

    public AuctionFileProcessContext getContext() {
        return (AuctionFileProcessContext) jobContext.getTransientUserData();
    }

    @RequiredArgsConstructor
    public static class AuctionFileProcessContext {
        private final String connectedRealmId;

        private ConnectedRealm connectedRealm;
        private AuctionFile auctionFile;

        public ConnectedRealm getConnectedRealm() {
            if (connectedRealm == null) {
                connectedRealm = ConnectedRealm.findById(connectedRealmId);
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
            return getFile(connectedRealm.getFolders().get(folderType).getPath() + "/" + getAuctionFile().getFileName());
        }

        public File getFolder(FolderType folderType) {
            return getFile(connectedRealm.getFolders().get(folderType).getPath());
        }
    }
}
