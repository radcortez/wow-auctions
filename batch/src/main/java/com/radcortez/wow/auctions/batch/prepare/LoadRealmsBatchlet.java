package com.radcortez.wow.auctions.batch.prepare;

import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.entity.Realm;
import lombok.extern.java.Log;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

/**
 * @author Roberto Cortez
 */
@Dependent
@Named
@Log
public class LoadRealmsBatchlet extends AbstractBatchlet {
    @Inject
    WoWBusinessBean woWBusiness;

    @Inject
    @BatchProperty(name = "locale")
    String locale;
    @Inject
    @BatchProperty(name = "host")
    String host;
    @Inject
    @BatchProperty(name = "endpoint")
    String endpoint;
    @Inject
    @BatchProperty(name = "region")
    String region;

    @Override
    public String process() {
        log.info(this.getClass().getSimpleName() + " running for region " + region);

        Client client = ClientBuilder.newClient();
        Realms realms = client.target(UriBuilder.fromUri(host).resolveTemplate("region", region))
                              .path(endpoint)
                              .queryParam("namespace", "dynamic-" + region)
                              .queryParam("locale", locale)
                              .request(MediaType.APPLICATION_JSON)
                              .property("region", region)
                              .get(Realms.class);

        realms.getRealms().forEach(this::createRealmIfMissing);

        log.info(this.getClass().getSimpleName() + " completed");
        return "COMPLETED";
    }

    void createRealmIfMissing(Realm realm) {
        realm.setRegion(Realm.Region.valueOf(region.toUpperCase()));

        if (woWBusiness.checkIfRealmExists(realm)) {
            log.info("Verified Realm " + realm.getName());
        } else {
            log.info("Creating Realm " + realm.getName());
            woWBusiness.createRealm(realm);
        }
    }
}
