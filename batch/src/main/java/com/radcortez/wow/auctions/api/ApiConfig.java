package com.radcortez.wow.auctions.api;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "api.blizzard")
public interface ApiConfig {
    @ConfigProperty(name = "host")
    String host();
    // Defaults needs to be empty, so it doesn't fail Quarkus validation. This will be replaced by the real property when batch replaces it in QuarkusBatchConfigSource.
    @ConfigProperty(name = "region", defaultValue = "")
    String region();
    @ConfigProperty(name = "locale")
    String locale();
}
