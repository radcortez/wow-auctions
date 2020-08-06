package com.radcortez.wow.auctions.batch.prepare;

import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.entity.ConnectedRealm;
import com.radcortez.wow.auctions.entity.Folder;
import com.radcortez.wow.auctions.entity.FolderType;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.batch.api.AbstractBatchlet;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.radcortez.wow.auctions.entity.FolderType.FI;
import static com.radcortez.wow.auctions.entity.FolderType.FI_TMP;
import static com.radcortez.wow.auctions.entity.FolderType.FO;
import static com.radcortez.wow.auctions.entity.FolderType.FO_TMP;
import static com.radcortez.wow.auctions.entity.FolderType.FP;

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
    @Inject
    WoWBusinessBean woWBusiness;

    @Override
    public String process() {
        log.info(this.getClass().getSimpleName() + " running");

        // TODO - Move this to configuration?
        Map<String, List<FolderType>> folders = new HashMap<>();
        folders.put("batch/work/", Arrays.asList(FI_TMP, FO_TMP));
        folders.put("batch/files/", Arrays.asList(FI, FO, FP));

        woWBusiness.listConnectedRealms()
                       .forEach(connectedRealm -> folders
                               .forEach((folderRoot, folderTypes) -> folderTypes
                                       .forEach(folderType ->
                                                        verifyAndCreateFolder(folderRoot, connectedRealm, folderType))));


        log.info(this.getClass().getSimpleName() + " completed");
        return "COMPLETED";
    }

    private void verifyAndCreateFolder(String folderRoot, ConnectedRealm connectedRealm, FolderType folderType) {
        File folder = new File(
                batchHome + "/" + folderRoot + "/" + connectedRealm.getRegion() + "/" + connectedRealm.getId() + "/" + folderType);

        if (!folder.exists()) {
            try {
                log.info("Creating folder " + folder);
                FileUtils.forceMkdir(folder);
                woWBusiness.createRealmFolder(new Folder(connectedRealm.getId(), folderType, folder.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (woWBusiness.findRealmFolderById(connectedRealm.getId(), folderType) == null) {
            woWBusiness.createRealmFolder(new Folder(connectedRealm.getId(), folderType, folder.getPath()));
        }
    }
}
