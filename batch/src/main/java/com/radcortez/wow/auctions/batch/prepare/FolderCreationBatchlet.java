package com.radcortez.wow.auctions.batch.prepare;

import com.radcortez.wow.auctions.entity.ConnectedRealm;
import com.radcortez.wow.auctions.entity.Folder;
import com.radcortez.wow.auctions.entity.FolderType;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.batch.api.AbstractBatchlet;
import jakarta.batch.runtime.BatchStatus;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;

/**
 * @author Roberto Cortez
 */
@Dependent
@Named
@Log
public class FolderCreationBatchlet extends AbstractBatchlet {
    @Inject
    @ConfigProperty(name = "wow.batch.home")
    String batchHome;

    @Override
    @Transactional
    public String process() {
        log.info(FolderCreationBatchlet.class.getSimpleName() + " running");

        ConnectedRealm.<ConnectedRealm>listAll().forEach(this::verifyAndCreateFolder);

        log.info(FolderCreationBatchlet.class.getSimpleName() + " completed");
        return BatchStatus.COMPLETED.toString();
    }

    private void verifyAndCreateFolder(final ConnectedRealm connectedRealm) {
        for (FolderType folderType : FolderType.values()) {
            File folder = FileUtils.getFile(batchHome, connectedRealm.getRegion().toString(), connectedRealm.getId(), folderType.toString());
            if (!folder.exists()) {
                try {
                    log.info("Creating folder " + folder);
                    FileUtils.forceMkdir(folder);
                } catch (IOException e) {
                    // Ignore
                    continue;
                }
            }

            if (!connectedRealm.getFolders().containsKey(folderType)) {
                connectedRealm.getFolders().put(folderType, new Folder(connectedRealm, folderType, folder.getPath()));
            }
        }
        connectedRealm.flush();
    }
}
