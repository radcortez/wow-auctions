package com.radcortez.wow.auctions.auth;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "api.battle.net")
public interface TokenConfig {
    @WithName("host")
    String host();
    @WithName("clientId")
    String clientId();
    @WithName("clientSecret")
    String clientSecret();
}
