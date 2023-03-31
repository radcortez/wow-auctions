package com.radcortez.wow.auctions.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.io.InputStream;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/data/wow/connected-realm")
public interface ConnectedRealmsApi extends AutoCloseable {
    @GET
    @Path("/index")
    ConnectedRealms index();

    @GET
    @Path("/{connectedRealmId}/auctions")
    InputStream auctions(@PathParam("connectedRealmId") String connectedRealmId);
}
