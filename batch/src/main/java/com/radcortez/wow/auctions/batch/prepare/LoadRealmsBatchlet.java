package com.radcortez.wow.auctions.batch.prepare;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.Realm;
import lombok.Data;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Level;

import static java.util.logging.Logger.getLogger;

/**
 * @author Roberto Cortez
 */
@Named
public class LoadRealmsBatchlet extends AbstractBatchlet {
    @Inject
    private WoWBusiness woWBusiness;

    @Inject
    @BatchProperty(name = "region")
    private String region;
    @Inject
    @BatchProperty(name = "target")
    private String target;

    @Override
    public String process() throws Exception {
        getLogger(this.getClass().getName()).log(Level.INFO, this.getClass().getSimpleName() + " running");
        Client client = ClientBuilder.newClient();
        Realms realms = client.target(target)
                              .request(MediaType.TEXT_PLAIN)
                              .get(Realms.class);

        realms.getRealms().forEach(this::createRealmIfMissing);

        getLogger(this.getClass().getName()).log(Level.INFO, this.getClass().getSimpleName() + " completed");
        return "COMPLETED";
    }

    void createRealmIfMissing(Realm realm) {
        realm.setRegion(region);

        if (woWBusiness.checkIfRealmExists(realm)) {
            getLogger(this.getClass().getName()).log(Level.INFO, "Verified Realm " + realm.getRealmDetail());
        } else {
            getLogger(this.getClass().getName()).log(Level.INFO, "Creating Realm " + realm.getRealmDetail());
            woWBusiness.createRealm(realm);
        }
    }

    @Data
    public static class Realms {
        private List<Realm> realms;
    }
}
