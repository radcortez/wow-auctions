package com.radcortez.wow.auctions.api;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.ext.Provider;
import java.net.URI;

@Provider
@Priority(Priorities.USER + 100)
public class ApiQueryParamFilter implements ClientRequestFilter {
    @Inject
    Instance<ApiConfig> apiConfig;

    @Override
    public void filter(final ClientRequestContext requestContext) {
        final URI uriWithQueryParams =
            UriBuilder.fromUri(requestContext.getUri())
                      .queryParam("namespace", "dynamic-" + requestContext.getProperty("region"))
                      .queryParam("locale", apiConfig.get().locale())
                      .build();
        requestContext.setUri(uriWithQueryParams);
    }
}

