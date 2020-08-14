package com.radcortez.wow.auctions.resource;

import javax.batch.operations.JobOperator;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Properties;

@Path("/batch")
@Produces(MediaType.APPLICATION_JSON)
public class BatchResource {
    @Inject
    JobOperator jobOperator;

    @GET
    @Path("/prepare")
    public Response prepare() {
        long executionId = jobOperator.start("prepare-job", new Properties());
        return Response.ok(executionId).build();
    }

    @GET
    @Path("/process/{region}/{connectedRealmId}")
    public Response process(
        @PathParam("region") final String region,
        @PathParam("connectedRealmId") final String connectedRealmId) {

        final Properties jobParameters = new Properties();
        jobParameters.setProperty("api.blizzard.region", region);
        jobParameters.setProperty("connectedRealmId", connectedRealmId);
        long executionId = jobOperator.start("process-job", jobParameters);

        return Response.ok(executionId).build();
    }
}
