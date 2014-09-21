package com.radcortez.wow.auctions.batch;

import com.radcortez.wow.auctions.batch.util.BatchTestHelper;
import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.*;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.*;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.radcortez.wow.auctions.batch.util.BatchTestHelper.keepTestAlive;
import static org.apache.commons.io.FileUtils.copyInputStreamToFile;
import static org.apache.commons.io.FileUtils.getFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author Roberto Cortez
 */
@SuppressWarnings({"unchecked", "CdiInjectionPointsInspection"})
@RunWith(Arquillian.class)
public class JobTest {
    @PersistenceContext
    private EntityManager em;
    @Inject
    private WoWBusiness woWBusiness;

    @Deployment
    public static WebArchive createDeployment() {
        File[] requiredLibraries = Maven.resolver().loadPomFromFile("pom.xml")
                                        .resolve("commons-io:commons-io")
                                        .withTransitivity().asFile();

        WebArchive war = ShrinkWrap.create(WebArchive.class)
                                   .addAsLibraries(requiredLibraries)
                                   .addPackages(true, "com.radcortez.wow.auctions")
                                   .addAsWebInfResource("META-INF/beans.xml")
                                   .addAsResource("META-INF/persistence.xml")
                                   .addAsResource("META-INF/batch-jobs/prepare-job.xml")
                                   .addAsResource("META-INF/batch-jobs/files-job.xml")
                                   .addAsResource("META-INF/batch-jobs/process-job.xml")
                                   .addAsResource("samples/auction-data-sample.json");
        System.out.println(war.toString(true));
        return war;
    }

    @Test
    @InSequence(1)
    public void testPrepareJob() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("prepare-job", new Properties());

        JobExecution jobExecution = keepTestAlive(jobOperator, executionId);

        List<Realm> realms = woWBusiness.listRealms();
        assertFalse(realms.isEmpty());

        assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());
    }

    @Test
    @InSequence(2)
    public void testFilesJob() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("files-job", new Properties());

        JobExecution jobExecution = keepTestAlive(jobOperator, executionId);

        assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());
    }

    @Test
    @InSequence(3)
    public void testProcessJob() throws Exception {
        Realm realm = woWBusiness.findRealmByNameOrSlug("Hellscream", Realm.Region.EU).get();
        RealmFolder realmFolder = woWBusiness.findRealmFolderById(realm.getId(), FolderType.FI);
        AuctionFile auctionFile = new AuctionFile();
        auctionFile.setUrl("test");
        auctionFile.setLastModified(LocalDate.now().toEpochDay());
        auctionFile.setFileName("auction-data-sample.json");
        auctionFile.setFileStatus(FileStatus.LOADED);
        auctionFile.setRealm(realm);
        woWBusiness.createAuctionFile(auctionFile);

        copyInputStreamToFile(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("samples/auction-data-sample.json"),
                getFile(realmFolder.getPath() + "/auction-data-sample.json"));

        auctionFile = woWBusiness.findAuctionFilesByRealmToProcess(realm.getId()).get(0);

        Properties jobParameters = new Properties();
        jobParameters.setProperty("realmId", realm.getId().toString());
        jobParameters.setProperty("auctionFileId", auctionFile.getId().toString());

        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("process-job", jobParameters);

        JobExecution jobExecution = keepTestAlive(jobOperator, executionId);

        assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());
    }

    //@Test
    @InSequence(4)
    public void testProcessRealFile() throws Exception {
        List<AuctionFile> files = em.createQuery("SELECT af FROM AuctionFile af WHERE af.fileStatus = :status")
                                    .setParameter("status", FileStatus.LOADED)
                                    .getResultList();

        if (!files.isEmpty()) {
            AuctionFile auctionFile = files.get(0);

            Properties jobParameters = new Properties();
            jobParameters.setProperty("realmId", auctionFile.getRealm().getId().toString());
            jobParameters.setProperty("auctionFileId", auctionFile.getId().toString());

            JobOperator jobOperator = BatchRuntime.getJobOperator();
            Long executionId = jobOperator.start("process-job", jobParameters);

            long start = System.currentTimeMillis();
            JobExecution jobExecution = keepTestAlive(jobOperator, executionId);
            long end = System.currentTimeMillis();
            System.out.println("Time " + (end - start));

            StepExecution processFile = jobOperator.getStepExecutions(executionId)
                                                   .stream()
                                                   .filter(stepExecution -> stepExecution.getStepName()
                                                                                         .equals("processFile"))
                                                   .findFirst().get();

            Map<Metric.MetricType, Long> metricsMap = BatchTestHelper.getMetricsMap(processFile.getMetrics());
            System.out.println("Read: " + metricsMap.get(Metric.MetricType.READ_COUNT));
            System.out.println("Write = " + metricsMap.get(Metric.MetricType.WRITE_COUNT));
            System.out.println("Commit = " + metricsMap.get(Metric.MetricType.COMMIT_COUNT));

            assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());
        }
    }
}
