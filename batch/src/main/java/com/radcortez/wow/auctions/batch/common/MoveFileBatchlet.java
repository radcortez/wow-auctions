package com.radcortez.wow.auctions.batch.common;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.FolderType;

import javax.annotation.PostConstruct;
import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.batch.api.Batchlet;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.logging.Level;

import static java.util.logging.Logger.getLogger;
import static org.apache.commons.io.FileUtils.getFile;
import static org.apache.commons.io.FileUtils.moveFileToDirectory;

/**
 * @author Roberto Cortez
 */
@Named
public class MoveFileBatchlet extends AbstractBatchlet implements Batchlet {
    private Long realmId;
    private String fileToProcess;

    @Inject
    private JobContext jobContext;
    @Inject
    private WoWBusiness woWBusiness;

    @Inject
    @BatchProperty(name = "from")
    private String from;
    @Inject
    @BatchProperty(name = "to")
    private String to;

    @Override
    public String process() throws Exception {
        File file = getFileToProcess(FolderType.valueOf(from));
        File destinationFolder = getFolder(FolderType.valueOf(to));

        getLogger(this.getClass().getName()).log(Level.INFO, "Moving file " + file + " to " + destinationFolder);
        moveFileToDirectory(file, destinationFolder, false);

        return "COMPLETED";
    }

    protected File getFileToProcess(FolderType folderType) {
        return getFile(woWBusiness.findRealmFolderById(realmId, folderType).getPath() + "/" + fileToProcess);
    }

    private File getFolder(FolderType folderType) {
        return getFile(woWBusiness.findRealmFolderById(realmId, folderType).getPath());
    }

    @PostConstruct
    private void init() {
        realmId = Long.valueOf(jobContext.getProperties().getProperty("companyId"));
        fileToProcess = jobContext.getProperties().getProperty("fileToProcess");
    }
}
