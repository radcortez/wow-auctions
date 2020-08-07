package com.radcortez.wow.auctions.api;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class ApiProducer {
    @Inject
    @ConfigProperty(name = "api.blizzard.host")
    String host;
    @Inject
    @ConfigProperty(name = "api.blizzard.locale")
    String locale;

}
