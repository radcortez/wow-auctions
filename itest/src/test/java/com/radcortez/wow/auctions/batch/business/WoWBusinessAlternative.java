package com.radcortez.wow.auctions.batch.business;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.Realm;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;
import javax.inject.Named;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Roberto Cortez
 */
@Named
@Alternative
@Local
@Stateless
public class WoWBusinessAlternative extends WoWBusinessBean implements WoWBusiness {
    @Override
    public List<Realm> findRealmsByRegion(Realm.Region region) {
        return super.findRealmsByRegion(region).stream().limit(2).collect(toList());
    }

    @Override
    public List<AuctionFile> findAuctionFilesByRegionToDownload(Realm.Region region) {
        return super.findAuctionFilesByRegionToDownload(region).stream().limit(1).collect(toList());
    }
}
