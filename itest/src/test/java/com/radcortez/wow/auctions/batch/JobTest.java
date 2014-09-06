package com.radcortez.wow.auctions.batch;

import com.radcortez.wow.auctions.batch.util.BatchTestHelper;
import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.Realm;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.inject.Inject;
import java.io.File;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertFalse;

/**
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class JobTest {
    @Inject
    private WoWBusiness woWBusiness;

    @Deployment
    public static WebArchive createDeployment() {
        File[] requiredLibraries = Maven.resolver().loadPomFromFile("pom.xml")
                                        .resolve("commons-io:commons-io",
                                                 "org.apache.deltaspike.modules:deltaspike-data-module-api",
                                                 "org.apache.deltaspike.modules:deltaspike-data-module-impl")
                                        .withTransitivity().asFile();

        WebArchive war = ShrinkWrap.create(WebArchive.class)
                                   .addAsLibraries(requiredLibraries)
                                   .addPackages(true, "com.radcortez.wow.auctions")
                                   .addAsWebInfResource("META-INF/beans.xml")
                                   .addAsResource("META-INF/apache-deltaspike.properties")
                                   .addAsResource("META-INF/persistence.xml")
                                   .addAsResource("META-INF/sql/create.sql")
                                   .addAsResource("META-INF/sql/drop.sql")
                                   .addAsResource("META-INF/sql/load.sql")
                                   .addAsResource("META-INF/batch-jobs/prepare-job.xml")
                                   .addAsResource("META-INF/batch-jobs/files-job.xml");
        System.out.println(war.toString(true));
        return war;
    }

    @Test
    @InSequence(1)
    public void testPrepareJob() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("prepare-job", new Properties());

        JobExecution jobExecution = BatchTestHelper.keepTestAlive(jobOperator, executionId);

        List<Realm> realms = woWBusiness.listRealms();
        assertFalse(realms.isEmpty());

        Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());
    }

    @Test
    @InSequence(2)
    public void testFilesJob() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("files-job", new Properties());

        JobExecution jobExecution = BatchTestHelper.keepTestAlive(jobOperator, executionId);

        List<AuctionFile> auctionFilesEU = woWBusiness.findAuctionFilesByRegionToDownload(Realm.Region.EU);
        //assertFalse(auctionFilesEU.isEmpty());

        Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());
    }
}
