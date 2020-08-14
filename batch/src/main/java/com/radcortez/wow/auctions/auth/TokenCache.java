package com.radcortez.wow.auctions.auth;

import io.quarkus.cache.CacheResult;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Form;
import java.net.URI;

@ApplicationScoped
public class TokenCache {
    // Tokens are valid for 24 hours
    @CacheResult(cacheName = "token", lockTimeout = 24 * 60 * 60 * 1000 - 60 * 1000)
    public Token getToken(final URI uri, final String credentials) {
        return RestClientBuilder.newBuilder()
                                .baseUri(uri)
                                .build(TokenApi.class)
                                .token(credentials, new Form("grant_type", "client_credentials"));
    }
}
