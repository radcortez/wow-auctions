package com.radcortez.wow.auctions.batch.business;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.entity.Realm;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;
import javax.inject.Named;
import java.util.ArrayList;
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
        if (region.equals(Realm.Region.EU)) {
            List<Realm> realms = new ArrayList<>();
            realms.add(findRealmByNameOrSlug("grim-batol", region).get());
            realms.add(findRealmByNameOrSlug("aggra-portugues", region).get());
            return realms;
        } else {
            return super.findRealmsByRegion(region).stream().limit(2).collect(toList());
        }
    }

}
