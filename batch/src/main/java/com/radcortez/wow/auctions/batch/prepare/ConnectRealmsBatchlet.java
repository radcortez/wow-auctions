package com.radcortez.wow.auctions.batch.prepare;

import com.radcortez.wow.auctions.api.ConnectedRealm;
import com.radcortez.wow.auctions.api.ConnectedRealms;
import com.radcortez.wow.auctions.api.Realm;
import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.entity.Region;
import com.radcortez.wow.auctions.mapper.ConnectedRealmMapper;
import lombok.extern.java.Log;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @author Roberto Cortez
 */
@Dependent
@Named
@Log
public class ConnectRealmsBatchlet extends AbstractBatchlet {
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

    Client client;

    @PostConstruct
    void init() {
        client = ClientBuilder.newClient();
    }

    @Override
    public String process() {
        log.info(this.getClass().getSimpleName() + " running");

        ConnectedRealms connectedRealms =
            client.target(UriBuilder.fromUri(host).resolveTemplate("region", region))
                  .path(endpoint)
                  .queryParam("namespace", "dynamic-" + region)
                  .queryParam("locale", locale)
                  .request(MediaType.APPLICATION_JSON)
                  .property("region", region)
                  .get(ConnectedRealms.class);

        connectedRealms.getConnectedRealms().forEach(location -> createConnectedRealmFromUri(location.getHref()));

        log.info(this.getClass().getSimpleName() + " completed");
        return "COMPLETED";
    }

    private void createConnectedRealmFromUri(final URI connectedRealmUri) {
        final ConnectedRealm connectedRealm =
            client.target(connectedRealmUri)
                  .queryParam("namespace", "dynamic-" + region)
                  .queryParam("locale", locale)
                  .request(MediaType.APPLICATION_JSON)
                  .property("region", region)
                  .get(ConnectedRealm.class);

        //woWBusiness.findConnectedRealm(connectedRealm.getId());

        final com.radcortez.wow.auctions.entity.ConnectedRealm connectedRealmEntity =
            ConnectedRealmMapper.INSTANCE.toConnectedRealm(connectedRealm, region.toUpperCase());

        connectedRealm.getRealms().stream().map(Realm::getName).forEach(realm -> log.info("Creating Realm " + realm));
        // TODO - Missing update
        woWBusiness.createConnectedRealm(connectedRealmEntity);
    }

    @PreDestroy
    void destroy() {
        client.close();
    }
}
