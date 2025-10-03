package com.radcortez.wow.auctions.auth;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.ext.Provider;
import java.net.URI;
import java.util.Base64;

// TODO - It seems that providers are only auto registed if we have a endpoint @Path registed. It doesn't make sense because with a simple client it should also work. Unless, it doesn't know about the client, so it doesn't do anything.
@Provider
@Priority(Priorities.AUTHENTICATION + 100)
public class OAuthAuthenticator implements ClientRequestFilter {
    @Inject
    Instance<TokenConfig> tokenConfig;
    @Inject
    TokenCache tokenCache;

    @Override
    public void filter(final ClientRequestContext requestContext) {
        final String region = (String) requestContext.getProperty("region");
        // The filter calls itself in TokenCache, since the Provider is registered for all REST calls.
        if (region == null) {
            return;
        }

        final URI tokenEndpoint = UriBuilder.fromUri(tokenConfig.get().host()).resolveTemplate("region", region).build();
        final Token token = tokenCache.getToken(tokenEndpoint,
                                                createBasicAuthHeaderValue(tokenConfig.get().clientId(),
                                                                           tokenConfig.get().clientSecret()));

        requestContext.getHeaders().add("Authorization", "Bearer " + token.getAccess_token());
    }

    private static String createBasicAuthHeaderValue(final String username, final String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
