package com.radcortez.wow.auctions.api;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.batch.api.BatchProperty;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;

/**
 * We cannot use Rest Client CDI injection directly with @RestClient, because the host subdomain for the client
 * changes with the region (us. or eu.), so we need to build the client with the right configuration.
 */
@Dependent
public class ApiProducer {
    @Inject
    ApiConfig apiConfig;
    @Inject
    @BatchProperty(name = "region")
    String region; // TODO - Batch only supports field injection. It should support method and constructor.

    @Produces
    @Dependent
    ConnectedRealmsApi connectedRealmsApi() {
        return RestClientBuilder.newBuilder()
                                .baseUri(UriBuilder.fromUri(apiConfig.host()).build(region))
                                .property("region", region)
                                .build(ConnectedRealmsApi.class);
    }

    @Produces
    @Dependent
    LocationApi locationApi() {
        return RestClientBuilder.newBuilder()
                                .baseUri(UriBuilder.fromUri(apiConfig.host()).build(region))
                                .property("region", region)
                                .build(LocationApi.class);
    }
}
