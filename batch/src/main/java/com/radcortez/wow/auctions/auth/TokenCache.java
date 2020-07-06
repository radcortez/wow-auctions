package com.radcortez.wow.auctions.auth;

import io.quarkus.cache.CacheResult;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import java.net.URI;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@ApplicationScoped
public class TokenCache {
    @CacheResult(cacheName = "token")
    public Token getToken(final URI uri, final String credentials) {
        return ClientBuilder.newClient()
                            .target(uri)
                            .request(APPLICATION_JSON)
                            .header(AUTHORIZATION, credentials)
                            .post(Entity.form(new Form("grant_type", "client_credentials")), Token.class);
    }
}
