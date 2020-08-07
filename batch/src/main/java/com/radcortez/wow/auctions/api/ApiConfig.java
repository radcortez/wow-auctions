package com.radcortez.wow.auctions.api;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "api.blizzard")
public interface ApiConfig {
    @ConfigProperty(name = "host")
    String host();
    @ConfigProperty(name = "locale")
    String locale();
}
