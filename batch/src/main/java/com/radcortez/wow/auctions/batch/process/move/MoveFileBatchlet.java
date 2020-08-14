package com.radcortez.wow.auctions.batch.process.move;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;
import com.radcortez.wow.auctions.entity.FolderType;
import lombok.extern.java.Log;
import org.apache.commons.io.FileExistsException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.batch.api.Batchlet;
import javax.batch.runtime.BatchStatus;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
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
    @ConfigProperty(name = "wow.batch.download.from", defaultValue = "")
    String from;
    @Inject
    @ConfigProperty(name = "wow.batch.download.to", defaultValue = "")
    String to;

    @Override
    public String process() throws Exception {
        File file = getContext().getAuctionFile(FolderType.valueOf(from));
        File destinationFolder = getContext().getFolder(FolderType.valueOf(to));

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
