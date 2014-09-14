package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.Auction;
import com.radcortez.wow.auctions.entity.Realm;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Roberto Cortez
 */
@Named
public class AuctionDataItemProcessor extends AbstractAuctionFileProcess implements ItemProcessor {
    @Inject
    private WoWBusiness woWBusiness;

    @Override
    public Object processItem(Object item) throws Exception {
        Auction auction = (Auction) item;

        Realm fileRealm = getContext().getRealm();
        Optional<Realm> ownerRealm =
                woWBusiness.findRealmByNameOrSlug(auction.getOwnerRealm(), fileRealm.getRegion());

        if (ownerRealm.isPresent()) {
            auction.setRealm(ownerRealm.get());

            if (!ownerRealm.get().equals(fileRealm)) {
                auction.setAdditionalRealms(new ArrayList<>());
                auction.getAdditionalRealms().add(fileRealm);
            }

            return auction;
        } else {
            return null;
        }
    }
}
