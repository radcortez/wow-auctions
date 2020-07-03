package com.radcortez.wow.auctions;

import com.radcortez.flyway.test.junit.DataSourceInfo;
import com.radcortez.flyway.test.junit.DataSourceProvider;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.extension.ExtensionContext;

public class QuarkusDataSourceProvider implements DataSourceProvider {
    @Override
    public DataSourceInfo getDatasourceInfo(final ExtensionContext extensionContext) {
        return DataSourceInfo.config(ConfigProvider.getConfig().getValue("quarkus.datasource.jdbc.url", String.class));
    }
}
