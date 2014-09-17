package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.Realm;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Ivan St. Ivanov
 * @author Roberto Cortez
 */
@Path("/batchexecution")
public class BatchExecutionBean {

    @Inject
    private WoWBusiness woWBusiness;

    @GET
    public void execute(@QueryParam("action") String action) {
        JobOperator jobOperator = BatchRuntime.getJobOperator();

        switch (action) {
            case "prepare":
                jobOperator.start("prepare-job", new Properties());
                break;
            case "files":
                jobOperator.start("files-job", new Properties());
                break;
            case "process":
                Realm realm = woWBusiness.findRealmByNameOrSlug("grim-batol", Realm.Region.EU).get();
                woWBusiness.findAuctionFilesByRealmToProcess(realm.getId())
                        .stream()
                        .forEach(this::processJob);
                break;
            case "multiple-process":
                woWBusiness.listRealms().forEach(realm1 -> woWBusiness.findAuctionFilesByRealmToProcess(realm1.getId())
                        .parallelStream()
                        .limit(10)
                        .forEach(this::processJob));

                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void processJob(AuctionFile auctionFile) {
        Properties jobParameters = new Properties();
        JobOperator jobOperator = BatchRuntime.getJobOperator();

        jobParameters.setProperty("realmId", auctionFile.getRealm().getId().toString());
        jobParameters.setProperty("auctionFileId", auctionFile.getId().toString());

        jobOperator.start("process-job", jobParameters);
    }

}
