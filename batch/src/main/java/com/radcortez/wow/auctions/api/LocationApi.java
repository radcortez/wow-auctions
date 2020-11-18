package com.radcortez.wow.auctions.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface LocationApi extends AutoCloseable {
    @GET
    @Path("/{location}")
    ConnectedRealm getConnectedRealm(@PathParam ("location") String location);
}
