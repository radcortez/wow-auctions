package com.radcortez.wow.auctions.auth;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Path("/oauth/token")
public interface TokenApi extends AutoCloseable {
    @POST
    Token token(@HeaderParam(value = HttpHeaders.AUTHORIZATION) String authorizationHeader, Form form);
}
