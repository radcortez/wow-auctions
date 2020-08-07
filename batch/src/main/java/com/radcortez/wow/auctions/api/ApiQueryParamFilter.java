package com.radcortez.wow.auctions.api;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.Provider;
import java.net.URI;

@Provider
@Priority(Priorities.USER + 100)
public class ApiQueryParamFilter implements ClientRequestFilter {
    @Inject
    ApiConfig apiConfig;

    @Override
    public void filter(final ClientRequestContext requestContext) {
        final URI uriWithQueryParams =
            UriBuilder.fromUri(requestContext.getUri())
                      .queryParam("namespace", "dynamic-" + requestContext.getProperty("region"))
                      .queryParam("locale", apiConfig.locale())
                      .build();
        requestContext.setUri(uriWithQueryParams);
    }
}

