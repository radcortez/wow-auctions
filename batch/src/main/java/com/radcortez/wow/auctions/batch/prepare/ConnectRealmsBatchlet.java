package com.radcortez.wow.auctions.batch.prepare;

import com.radcortez.wow.auctions.api.ConnectedRealmsApi;
import com.radcortez.wow.auctions.api.LocationApi;
import com.radcortez.wow.auctions.entity.ConnectedRealm;
import lombok.extern.java.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.annotation.PostConstruct;
import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.batch.runtime.BatchStatus;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * @author Roberto Cortez
 */
@Dependent
@Named
@Log
public class ConnectRealmsBatchlet extends AbstractBatchlet {
    @Inject
    @ConfigProperty(name = "api.blizzard.locale")
    String locale;
    @Inject
    @ConfigProperty(name = "api.blizzard.host")
    String host;
    @Inject
    @BatchProperty(name = "region")
    String region;

    ConnectedRealmsApi connectedRealmsApi;
    LocationApi locationApi;

    @PostConstruct
    void init() {
        connectedRealmsApi =
            RestClientBuilder.newBuilder()
                             .baseUri(UriBuilder.fromUri(host).build(region))
                             .property("region", region)
                             .build(ConnectedRealmsApi.class);

        locationApi =
            RestClientBuilder.newBuilder()
                             .baseUri(UriBuilder.fromUri(host).build(region))
                             .property("region", region)
                             .build(LocationApi.class);
    }

    @Override
    @Transactional
    public String process() {
        log.info(ConnectRealmsBatchlet.class.getSimpleName() + " running");

        connectedRealmsApi.index("dynamic-" + region, locale)
                          .getConnectedRealms()
                          .forEach(location -> createConnectedRealmFromUri(location.getHref()));

        log.info(ConnectRealmsBatchlet.class.getSimpleName() + " completed");
        return BatchStatus.COMPLETED.toString();
    }

    private void createConnectedRealmFromUri(final URI connectedRealmUri) {
        com.radcortez.wow.auctions.api.ConnectedRealm connectedRealm =
            locationApi.getConnectedRealm(connectedRealmUri.getPath(), "dynamic-" + region, locale);

        log.info("Connected Realm " + connectedRealm);
        if (connectedRealm.isDown()) {
            return;
        }

        ConnectedRealm connectedRealmEntity = connectedRealm.toEntity(region);
        ConnectedRealm.<ConnectedRealm>findByIdOptional(connectedRealm.getId())
            .ifPresentOrElse(connectedRealmEntity::update, connectedRealmEntity::create);
    }
}
