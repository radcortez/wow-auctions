package com.radcortez.wow.auctions.auth;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.Provider;
import java.util.Base64;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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

    @Override
    public void filter(final ClientRequestContext requestContext) {
        final String region = (String) requestContext.getProperty("region");
        if (region == null) {
            return;
        }

        Token token = ClientBuilder.newClient()
                                   .target(UriBuilder.fromUri(host).resolveTemplate("region", region))
                                   .request(APPLICATION_JSON)
                                   .header(AUTHORIZATION, createBasicAuthHeaderValue(clientId, clientSecret))
                                   .post(Entity.form(new Form("grant_type", "client_credentials")), Token.class);

        requestContext.setUri(
            UriBuilder.fromUri(requestContext.getUri()).queryParam("access_token", token.getAccess_token()).build());
    }

    private String createBasicAuthHeaderValue(final String username, final String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

}
