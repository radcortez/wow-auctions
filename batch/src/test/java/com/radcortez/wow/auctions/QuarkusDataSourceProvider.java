package com.radcortez.wow.auctions;

import com.radcortez.flyway.test.junit.DataSourceInfo;
import com.radcortez.flyway.test.junit.DataSourceProvider;
import io.smallrye.config.PropertiesConfigSource;
import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.net.URL;

public class QuarkusDataSourceProvider implements DataSourceProvider {
    @Override
    @SneakyThrows
    public DataSourceInfo getDatasourceInfo(final ExtensionContext extensionContext) {
        // We don't have access to the Quarkus CL here, so we cannot use ConfigProvider.getConfig() to retrieve the same configuration.

        URL properties = Thread.currentThread().getContextClassLoader().getResource("application.properties");
        assert properties != null;

        SmallRyeConfig config = new SmallRyeConfigBuilder()
            .withSources(new PropertiesConfigSource(properties))
            .withProfile("test")
            .build();

        return DataSourceInfo.config(config.getRawValue("quarkus.datasource.jdbc.url"));
    }
}
