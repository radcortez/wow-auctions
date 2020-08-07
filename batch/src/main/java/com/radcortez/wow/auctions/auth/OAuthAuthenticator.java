package com.radcortez.wow.auctions.auth;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.Provider;
import java.net.URI;
import java.util.Base64;

// TODO - It seems that providers are only auto registed if we have a endpoint @Path registed. It doesn't make sense because with a simple client it should also work. Unless, it doesn't know about the client, so it doesn't do anything.
@Provider
@Priority(Priorities.AUTHENTICATION + 100)
public class OAuthAuthenticator implements ClientRequestFilter {
    @Inject
    TokenConfig tokenConfig;
    @Inject
    TokenCache tokenCache;

    @Override
    public void filter(final ClientRequestContext requestContext) {
        final String region = (String) requestContext.getProperty("region");
        if (region == null) {
            return;
        }

        final URI tokenEndpoint = UriBuilder.fromUri(tokenConfig.host()).resolveTemplate("region", region).build();
        final Token token = tokenCache.getToken(tokenEndpoint,
                                                createBasicAuthHeaderValue(tokenConfig.clientId(),
                                                                           tokenConfig.clientSecret()));
        requestContext.setUri(
            UriBuilder.fromUri(requestContext.getUri()).queryParam("access_token", token.getAccess_token()).build());
    }

    private static String createBasicAuthHeaderValue(final String username, final String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
