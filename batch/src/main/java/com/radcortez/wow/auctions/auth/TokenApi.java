package com.radcortez.wow.auctions.auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Path("/oauth/token")
public interface TokenApi {
    @POST
    Token token(@HeaderParam(value = HttpHeaders.AUTHORIZATION) String authorizationHeader, Form form);
}
