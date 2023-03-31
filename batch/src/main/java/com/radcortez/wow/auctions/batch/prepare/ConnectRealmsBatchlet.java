package com.radcortez.wow.auctions.batch.prepare;

import com.radcortez.wow.auctions.api.ApiConfig;
import com.radcortez.wow.auctions.api.ConnectedRealmsApi;
import com.radcortez.wow.auctions.api.LocationApi;
import com.radcortez.wow.auctions.entity.ConnectedRealm;
import lombok.extern.java.Log;
import org.eclipse.microprofile.config.Config;

import jakarta.batch.api.AbstractBatchlet;
import jakarta.batch.runtime.BatchStatus;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import java.net.URI;

/**
 * @author Roberto Cortez
 */
@Dependent
@Named
@Log
public class ConnectRealmsBatchlet extends AbstractBatchlet {
    @Inject
    ApiConfig apiConfig;
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

        ConnectedRealm connectedRealmEntity = connectedRealm.toEntity(apiConfig.region());
        ConnectedRealm.<ConnectedRealm>findByIdOptional(connectedRealm.getId())
            .ifPresentOrElse(connectedRealmEntity::update, connectedRealmEntity::create);
    }
}
