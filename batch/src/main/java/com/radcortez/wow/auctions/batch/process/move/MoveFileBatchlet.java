package com.radcortez.wow.auctions.batch.process.move;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;
import com.radcortez.wow.auctions.entity.FolderType;
import lombok.extern.java.Log;
import org.apache.commons.io.FileExistsException;
import org.eclipse.microprofile.config.Config;

import jakarta.batch.api.Batchlet;
import jakarta.batch.runtime.BatchStatus;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.File;

import static org.apache.commons.io.FileUtils.moveFileToDirectory;

/**
 * @author Roberto Cortez
 */
@Dependent
@Named
@Log
public class MoveFileBatchlet extends AbstractAuctionFileProcess implements Batchlet {
    @Inject
    Config config;

    @Override
    public String process() throws Exception {
        File file = getContext().getAuctionFile(
            FolderType.valueOf(config.getConfigValue("wow.batch.download.from").getValue()));
        File destinationFolder =
            getContext().getFolder(FolderType.valueOf(config.getConfigValue("wow.batch.download.to").getValue()));

        try {
            log.info("Moving file " + file + " to " + destinationFolder);
            moveFileToDirectory(file, destinationFolder, false);
        } catch (FileExistsException e) {
            log.warning("File " + file + " already exists at " + destinationFolder);
        }

        return BatchStatus.COMPLETED.toString();
    }

    @Override
    public void stop() {}
}
