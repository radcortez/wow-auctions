package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.FolderType;

import javax.annotation.PostConstruct;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import java.io.File;

import static org.apache.commons.io.FileUtils.getFile;

/**
 * @author Roberto Cortez
 */
public abstract class AbstractAuctionFileProcess {
    private Long realmId;
    private String fileToProcess;

    @Inject
    private JobContext jobContext;
    @Inject
    private WoWBusiness woWBusiness;

    protected Long getRealmId() {
        return realmId;
    }

    protected File getFileToProcess(FolderType folderType) {
        return getFile(woWBusiness.findRealmFolderById(realmId, folderType).getPath() + "/" + fileToProcess);
    }

    protected File getFolder(FolderType folderType) {
        return getFile(woWBusiness.findRealmFolderById(realmId, folderType).getPath());
    }

    @PostConstruct
    private void init() {
        realmId = Long.valueOf(jobContext.getProperties().getProperty("realmId"));
        fileToProcess = jobContext.getProperties().getProperty("fileToProcess");
    }
}
