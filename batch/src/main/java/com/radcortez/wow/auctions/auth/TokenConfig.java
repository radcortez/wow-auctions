package com.radcortez.wow.auctions.auth;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "api.battle.net")
public interface TokenConfig {
    @ConfigProperty(name = "host")
    String host();
    @ConfigProperty(name = "clientId")
    String clientId();
    @ConfigProperty(name = "clientSecret")
    String clientSecret();
}
