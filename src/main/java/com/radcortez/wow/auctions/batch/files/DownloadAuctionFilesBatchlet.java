package com.radcortez.wow.auctions.batch.files;

import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.Realm;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@Named
public class DownloadAuctionFilesBatchlet extends AbstractBatchlet {
    @Inject
    private WoWBusinessBean woWBusinessBean;

    @Inject
    @BatchProperty(name = "region")
    private String region;

    @Override
    public String process() throws Exception {
        List<AuctionFile> auctionFilesByRegionToLoad =
                woWBusinessBean.findAuctionFilesByRegionToLoad(Realm.Region.valueOf(region));



        return "COMPLETED";
    }
}
