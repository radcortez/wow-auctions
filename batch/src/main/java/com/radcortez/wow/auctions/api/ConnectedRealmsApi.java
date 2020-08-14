package com.radcortez.wow.auctions.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/data/wow/connected-realm")
public interface ConnectedRealmsApi {
    @GET
    @Path("/index")
    ConnectedRealms index();

    @GET
    @Path("/{connectedRealmId}/auctions")
    InputStream auctions(@PathParam("connectedRealmId") String connectedRealmId);
}
