package com.radcortez.wow.auctions.batch.prepare;

import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.entity.Realm;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@Named
public class LoadRealmsBatchlet extends AbstractBatchlet {
    @Inject
    private WoWBusinessBean woWBusinessBean;

    @Inject
    @BatchProperty(name = "region")
    private String region;
    @Inject
    @BatchProperty(name = "target")
    private String target;

    @Override
    public String process() throws Exception {
        Client client = ClientBuilder.newClient();
        Realms realms = client.target(target)
                              .request(MediaType.TEXT_PLAIN)
                              .get(Realms.class);

        realms.getRealms().forEach(realm -> {
            realm.setRegion(region);
            woWBusinessBean.createRealm(realm);
        });

        return "COMPLETED";
    }

    @Data
    public static class Realms {
        private List<Realm> realms;
    }
}
