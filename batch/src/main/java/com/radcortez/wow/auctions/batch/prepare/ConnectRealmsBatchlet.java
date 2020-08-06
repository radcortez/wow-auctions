package com.radcortez.wow.auctions.batch.prepare;

import com.radcortez.wow.auctions.api.ConnectedRealm;
import com.radcortez.wow.auctions.api.ConnectedRealms;
import com.radcortez.wow.auctions.api.ConnectedRealmsApi;
import com.radcortez.wow.auctions.api.Realm;
import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.mapper.ConnectedRealmMapper;
import lombok.extern.java.Log;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.batch.runtime.BatchStatus;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
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
    ConnectedRealmsApi connectedRealmsApi;

    @PostConstruct
    void init() {
        client = ClientBuilder.newClient().property("region", region);
        connectedRealmsApi =
            RestClientBuilder.newBuilder()
                             .baseUri(UriBuilder.fromUri(host).build(region))
                             .property("region", region)
                             .build(ConnectedRealmsApi.class);
    }

    @Override
    public String process() {
        log.info(this.getClass().getSimpleName() + " running");

        ConnectedRealms connectedRealms = connectedRealmsApi.index("dynamic-" + region, locale);
        connectedRealms.getConnectedRealms().forEach(location -> createConnectedRealmFromUri(location.getHref()));

        log.info(this.getClass().getSimpleName() + " completed");
        return BatchStatus.COMPLETED.toString();
    }

    private void createConnectedRealmFromUri(final URI connectedRealmUri) {
        final ConnectedRealm connectedRealm =
            client.target(connectedRealmUri)
                  .queryParam("namespace", "dynamic-" + region)
                  .queryParam("locale", locale)
                  .request(MediaType.APPLICATION_JSON)
                  .get(ConnectedRealm.class);

        log.info("Connected Realm " + connectedRealm);

        final com.radcortez.wow.auctions.entity.ConnectedRealm connectedRealmEntity =
            ConnectedRealmMapper.INSTANCE.toConnectedRealm(connectedRealm, region.toUpperCase());

        woWBusiness.findConnectedRealm(connectedRealm.getId())
                   .ifPresentOrElse(woWBusiness::updateConnectedRealm, () -> woWBusiness.createConnectedRealm(connectedRealmEntity));
    }

    @PreDestroy
    void destroy() {
        client.close();
    }
}
