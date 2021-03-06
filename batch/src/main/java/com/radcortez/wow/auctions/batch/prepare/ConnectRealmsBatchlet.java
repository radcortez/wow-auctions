package com.radcortez.wow.auctions.batch.prepare;

import com.radcortez.wow.auctions.api.ConnectedRealmsApi;
import com.radcortez.wow.auctions.api.LocationApi;
import com.radcortez.wow.auctions.entity.ConnectedRealm;
import lombok.extern.java.Log;
import org.eclipse.microprofile.config.Config;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchStatus;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.net.URI;

/**
 * @author Roberto Cortez
 */
@Dependent
@Named
@Log
public class ConnectRealmsBatchlet extends AbstractBatchlet {
    @Inject
    Config config;

    @Inject
    ConnectedRealmsApi connectedRealmsApi;
    @Inject
    LocationApi locationApi;

    @Override
    @Transactional
    public String process() {
        log.info(ConnectRealmsBatchlet.class.getSimpleName() + " running");

        connectedRealmsApi.index()
                          .getConnectedRealms()
                          .forEach(location -> createConnectedRealmFromUri(location.getHref()));

        log.info(ConnectRealmsBatchlet.class.getSimpleName() + " completed");
        return BatchStatus.COMPLETED.toString();
    }

    private void createConnectedRealmFromUri(final URI connectedRealmUri) {
        com.radcortez.wow.auctions.api.ConnectedRealm connectedRealm =
            locationApi.getConnectedRealm(connectedRealmUri.getPath());

        log.info("Connected Realm " + connectedRealm);
        if (connectedRealm.isDown()) {
            return;
        }

        ConnectedRealm connectedRealmEntity =
            connectedRealm.toEntity(config.getConfigValue("api.blizzard.region").getValue());
        ConnectedRealm.<ConnectedRealm>findByIdOptional(connectedRealm.getId())
            .ifPresentOrElse(connectedRealmEntity::update, connectedRealmEntity::create);
    }
}
