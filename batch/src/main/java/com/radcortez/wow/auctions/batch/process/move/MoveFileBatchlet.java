package com.radcortez.wow.auctions.batch.process.move;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;
import com.radcortez.wow.auctions.entity.FolderType;
import org.apache.commons.io.FileExistsException;

import javax.batch.api.BatchProperty;
import javax.batch.api.Batchlet;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.logging.Level;

import static java.util.logging.Logger.getLogger;
import static org.apache.commons.io.FileUtils.moveFileToDirectory;

/**
 * @author Roberto Cortez
 */
@Named
public class MoveFileBatchlet extends AbstractAuctionFileProcess implements Batchlet {
    @Inject
    @BatchProperty(name = "from")
    private String from;
    @Inject
    @BatchProperty(name = "to")
    private String to;

    @Override
    public String process() throws Exception {
        File file = getContext().getFileToProcess(FolderType.valueOf(from));
        File destinationFolder = getContext().getFolder(FolderType.valueOf(to));

        try {
            getLogger(this.getClass().getName()).log(Level.INFO, "Moving file " + file + " to " + destinationFolder);
            moveFileToDirectory(file, destinationFolder, false);
        } catch (FileExistsException e) {
            getLogger(this.getClass().getName()).log(Level.WARNING,
                                                     "File " + file + " already exists at " + destinationFolder);
        }

        return "COMPLETED";
    }

    @Override
    public void stop() {}
}
