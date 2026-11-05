package com.radcortez.wow.auctions;

import com.radcortez.flyway.test.junit.DataSourceInfo;
import com.radcortez.flyway.test.junit.DataSourceProvider;
import io.smallrye.config.PropertiesConfigSource;
import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.net.URL;
import java.util.Collections;
import java.util.List;

public class QuarkusDataSourceProvider implements DataSourceProvider {
    @Override
    @SneakyThrows
    public DataSourceInfo getDatasourceInfo(final ExtensionContext extensionContext) {
        // We don't have access to the Quarkus CL here, so we cannot use ConfigProvider.getConfig() to retrieve the same configuration.

        // Quarkus may add a temporary application.properties with overrides before the project one, so load all of them (first wins).
        List<URL> resources = Collections.list(Thread.currentThread().getContextClassLoader().getResources("application.properties"));
        assert !resources.isEmpty();

        SmallRyeConfigBuilder builder = new SmallRyeConfigBuilder().withProfile("test");
        for (int i = 0; i < resources.size(); i++) {
            builder.withSources(new PropertiesConfigSource(resources.get(i), 250 - i));
        }
        SmallRyeConfig config = builder.build();

        return DataSourceInfo.config(config.getConfigValue("quarkus.datasource.jdbc.url").getValue());
    }
}
