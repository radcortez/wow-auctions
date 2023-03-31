package com.radcortez.wow.auctions.api;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

import jakarta.batch.api.BatchProperty;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriBuilder;

/**
 * We cannot use Rest Client CDI injection directly with @RestClient, because the host subdomain for the client
 * changes with the region (us. or eu.), so we need to build the client with the right configuration.
 */
@Dependent
public class ApiProducer {
    @Inject
    ApiConfig apiConfig;

    @Produces
    @Dependent
    ConnectedRealmsApi connectedRealmsApi() {
        return RestClientBuilder.newBuilder()
                                .baseUri(UriBuilder.fromUri(apiConfig.host()).build(apiConfig.region()))
                                .property("region", apiConfig.region())
                                .build(ConnectedRealmsApi.class);
    }

    @Produces
    @Dependent
    LocationApi locationApi() {
        return RestClientBuilder.newBuilder()
                                .baseUri(UriBuilder.fromUri(apiConfig.host()).build(apiConfig.region()))
                                .property("region", apiConfig.region())
                                .build(LocationApi.class);
    }
}
