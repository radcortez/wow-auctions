package com.radcortez.wow.auctions.batch.prepare;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.Realm;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;

import static java.util.logging.Logger.getLogger;

/**
 * @author Roberto Cortez
 */
@Named
public class ConnectRealmsBatchlet extends AbstractBatchlet {
    @Inject
    WoWBusiness woWBusiness;

    @Inject
    @BatchProperty(name = "locale")
    String locale;
    @Inject
    @BatchProperty(name = "apikey")
    String apiKey;
    @Inject
    @BatchProperty(name = "region")
    String region;
    @Inject
    @BatchProperty(name = "target")
    String target;

    @Override
    public String process() throws Exception {
        getLogger(this.getClass().getName()).log(Level.INFO, this.getClass().getSimpleName() + " running");

        Client client = ClientBuilder.newClient();
        Realms realms = client.target(target)
                              .queryParam("locale", locale)
                              .queryParam("apikey", apiKey)
                              .request(MediaType.TEXT_PLAIN)
                              .get(Realms.class);

        realms.getRealms().forEach(this::createConnectedRealms);

        getLogger(this.getClass().getName()).log(Level.INFO, this.getClass().getSimpleName() + " completed");
        return "COMPLETED";
    }

    void createConnectedRealms(Realm realm) {
        Realm originalRealm =
                woWBusiness.findRealmByNameOrSlug(realm.getNameAuction(), Realm.Region.valueOf(region)).get();
        originalRealm.setConnectedRealms(new ArrayList<>());

        for (String slug : realm.getConnected_realms()) {
            Optional<Realm> connectedRealm = woWBusiness.findRealmByNameOrSlug(slug, originalRealm.getRegion());
            connectedRealm.ifPresent(value -> originalRealm.getConnectedRealms().add(value));
        }

        woWBusiness.updateRealm(originalRealm);
    }
}
