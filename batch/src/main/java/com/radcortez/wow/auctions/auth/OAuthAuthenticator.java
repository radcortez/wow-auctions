package com.radcortez.wow.auctions.auth;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.Provider;
import java.util.Base64;

// TODO - It seems that providers are only auto registed if we have a endpoint @Path registed. It doesn't make sense because with a simple client it should also work. Unless, it doesn't know about the client, so it doesn't do anything.
@Provider
@Priority(Priorities.AUTHENTICATION + 100)
public class OAuthAuthenticator implements ClientRequestFilter {
    @Inject
    @ConfigProperty(name = "api.battle.net.host")
    String host;
    @Inject
    @ConfigProperty(name = "api.battle.net.clientId")
    String clientId;
    @Inject
    @ConfigProperty(name = "api.battle.net.clientSecret")
    String clientSecret;

    @Inject
    TokenCache tokenCache;

    @Override
    public void filter(final ClientRequestContext requestContext) {
        final String region = (String) requestContext.getProperty("region");
        if (region == null) {
            return;
        }

        final Token token = tokenCache.getToken(UriBuilder.fromUri(host).resolveTemplate("region", region).build(),
                                                createBasicAuthHeaderValue(clientId, clientSecret));

        requestContext.setUri(
            UriBuilder.fromUri(requestContext.getUri()).queryParam("access_token", token.getAccess_token()).build());
    }

    private String createBasicAuthHeaderValue(final String username, final String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
