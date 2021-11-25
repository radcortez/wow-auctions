package com.radcortez.wow.auctions.api;

import io.smallrye.config.ConfigMapping;
import org.eclipse.microprofile.config.ConfigProvider;

@ConfigMapping(prefix = "api.blizzard")
public interface ApiConfig {
    String host();
    String locale();

    default String region() {
        return ConfigProvider.getConfig().getConfigValue("api.blizzard.region").getRawValue();
    }
}
