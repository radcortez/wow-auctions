package com.radcortez.wow.auctions.batch.prepare;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.FolderType;
import com.radcortez.wow.auctions.entity.Realm;
import com.radcortez.wow.auctions.entity.RealmFolder;
import org.apache.commons.io.FileUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.batch.api.AbstractBatchlet;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static com.radcortez.wow.auctions.entity.FolderType.FI;
import static com.radcortez.wow.auctions.entity.FolderType.FI_TMP;
import static com.radcortez.wow.auctions.entity.FolderType.FO;
import static com.radcortez.wow.auctions.entity.FolderType.FO_TMP;
import static com.radcortez.wow.auctions.entity.FolderType.FP;
import static java.util.logging.Logger.getLogger;

/**
 * @author Roberto Cortez
 */
@Named
public class FolderCreationBatchlet extends AbstractBatchlet {
    @Inject
    @ConfigProperty(name = "wow.batch.home")
    String batchHome;
    @Inject
    WoWBusiness woWBusiness;

    @Override
    public String process() {
        getLogger(this.getClass().getName()).log(Level.INFO, this.getClass().getSimpleName() + " running");

        // TODO - Move this to configuration?
        Map<String, List<FolderType>> folders = new HashMap<>();
        folders.put("batch/work/", Arrays.asList(FI_TMP, FO_TMP));
        folders.put("batch/files/", Arrays.asList(FI, FO, FP));

        woWBusiness.listRealms()
                       .forEach(realm -> folders
                               .forEach((folderRoot, folderTypes) -> folderTypes
                                       .forEach(folderType ->
                                                        verifyAndCreateFolder(folderRoot, realm, folderType))));


        getLogger(this.getClass().getName()).log(Level.INFO, this.getClass().getSimpleName() + " completed");
        return "COMPLETED";
    }

    private void verifyAndCreateFolder(String folderRoot, Realm realm, FolderType folderType) {
        File folder = new File(
                batchHome + "/" + folderRoot + "/" + realm.getRegion() + "/" + realm.getName() + "/" + folderType);

        if (!folder.exists()) {
            try {
                getLogger(this.getClass().getName()).log(Level.INFO, "Creating folder " + folder);
                FileUtils.forceMkdir(folder);
                woWBusiness.createRealmFolder(new RealmFolder(realm.getId(), folderType, folder.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (woWBusiness.findRealmFolderById(realm.getId(), folderType) == null) {
            woWBusiness.createRealmFolder(new RealmFolder(realm.getId(), folderType, folder.getPath()));
        }
    }
}
