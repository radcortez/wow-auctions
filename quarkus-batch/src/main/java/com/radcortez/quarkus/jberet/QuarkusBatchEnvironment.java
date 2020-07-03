package com.radcortez.quarkus.jberet;

import io.quarkus.arc.Arc;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jberet.creation.AbstractArtifactFactory;
import org.jberet.repository.InMemoryRepository;
import org.jberet.repository.JobRepository;
import org.jberet.spi.ArtifactFactory;
import org.jberet.spi.BatchEnvironment;
import org.jberet.spi.JobExecutor;
import org.jberet.spi.JobTask;
import org.jberet.spi.JobXmlResolver;
import org.jberet.tools.ChainedJobXmlResolver;
import org.jberet.tools.MetaInfBatchJobsJobXmlResolver;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.transaction.TransactionManager;
import java.util.Properties;
import java.util.ServiceLoader;

public class QuarkusBatchEnvironment implements BatchEnvironment {
    private final Properties configProperties;
    private final JobExecutor jobExecutor;
    private final JobRepository jobRepository;

    public QuarkusBatchEnvironment() {
        configProperties = new Properties();

        final ManagedExecutor managedExecutor = ManagedExecutor.builder().build();
        jobExecutor = new JobExecutor(managedExecutor) {
            @Override
            protected int getMaximumPoolSize() {
                return 10;
            }
        };

        jobRepository = new InMemoryRepository();
    }

    @Override
    public ClassLoader getClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = QuarkusBatchEnvironment.class.getClassLoader();
        }
        return cl;
    }

    @Override
    public ArtifactFactory getArtifactFactory() {
        return new AbstractArtifactFactory() {
            @Override
            public Object create(final String ref, final Class<?> cls, final ClassLoader classLoader) {
                final BeanManager beanManager = Arc.container().beanManager();
                final Bean<?> bean = beanManager.resolve(beanManager.getBeans(ref));
                return bean == null ? null : beanManager.getReference(bean, bean.getBeanClass(), beanManager.createCreationalContext(bean));
            }

            @Override
            public Class<?> getArtifactClass(final String ref, final ClassLoader classLoader) {
                final BeanManager beanManager = Arc.container().beanManager();
                final Bean<?> bean = beanManager.resolve(beanManager.getBeans(ref));
                return bean == null ? null : bean.getBeanClass();
            }
        };
    }

    @Override
    public void submitTask(final JobTask task) {
        jobExecutor.execute(task);
    }

    @Override
    public TransactionManager getTransactionManager() {
        // TODO - not sure if this is right, since Arc may not be available at this time in a proper Quarkus Extension
        return Arc.container().instance(TransactionManager.class).get();
    }

    @Override
    public JobRepository getJobRepository() {
        return jobRepository;
    }

    @Override
    public JobXmlResolver getJobXmlResolver() {
        final ServiceLoader<JobXmlResolver> userJobXmlResolvers = ServiceLoader.load(JobXmlResolver.class, getClassLoader());
        return new ChainedJobXmlResolver(userJobXmlResolvers, new MetaInfBatchJobsJobXmlResolver());
    }

    @Override
    public Properties getBatchConfigurationProperties() {
        return configProperties;
    }
}
