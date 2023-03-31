package com.radcortez.wow.auctions.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface LocationApi extends AutoCloseable {
    @GET
    @Path("/{location}")
    ConnectedRealm getConnectedRealm(@PathParam ("location") String location);
}
