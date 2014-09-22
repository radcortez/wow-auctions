package com.radcortez.wow.auctions.servlet;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.Realm;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * @author Roberto Cortez
 */
@WebServlet(urlPatterns = {"/BatchExecutionServlet"})
public class BatchExecutionServlet extends HttpServlet {
    @Inject
    private WoWBusiness woWBusiness;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String batch = req.getParameter("batch");

        JobOperator jobOperator = BatchRuntime.getJobOperator();

        switch (batch) {
            case "prepare":
                jobOperator.start("prepare-job", new Properties());
                break;
            case "files":
                jobOperator.start("files-job", new Properties());
                break;
            case "process":
                Realm realm = woWBusiness.findRealmByNameOrSlug("grim-batol", Realm.Region.EU).get();
                List<AuctionFile> auctionFilesByRealmToProcess =
                        woWBusiness.findAuctionFilesByRealmToProcess(realm.getId());

                auctionFilesByRealmToProcess.stream().forEach(this::processJob);

                if (auctionFilesByRealmToProcess.isEmpty()) {
                    realm = woWBusiness.findRealmByNameOrSlug("aggra-portugues", Realm.Region.EU).get();
                    woWBusiness.findAuctionFilesByRealmToProcess(realm.getId()).stream().forEach(this::processJob);
                }

                break;
/*            case "multiple-process":
                woWBusiness.listRealms().forEach(realm1 -> woWBusiness.findAuctionFilesByRealmToProcess(realm1.getId())
                                                                      .parallelStream()
                                                                      .limit(10)
                                                                      .forEach(this::processJob));

                break;*/
            default:
                throw new UnsupportedOperationException();
        }

        resp.sendRedirect("batchs.html");
    }

    private void processJob(AuctionFile auctionFile) {
        Properties jobParameters = new Properties();
        JobOperator jobOperator = BatchRuntime.getJobOperator();

        jobParameters.setProperty("realmId", auctionFile.getRealm().getId().toString());
        jobParameters.setProperty("auctionFileId", auctionFile.getId().toString());

        jobOperator.start("process-job", jobParameters);
    }
}
