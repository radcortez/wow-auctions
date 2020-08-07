package com.radcortez.quarkus.jberet;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.jberet.creation.ArtifactCreationContext;
import org.jberet.job.model.Properties;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class QuarkusBatchConfigSource implements ConfigSource {
    @Override
    public Map<String, String> getProperties() {
        return Collections.emptyMap();
    }

    @Override
    public String getValue(final String propertyName) {
        final ArtifactCreationContext ac = ArtifactCreationContext.getCurrentArtifactCreationContext();

        // TODO - change
        Properties reflectiveProperties = null;
        try {
            Field propertiesField = ac.getClass().getDeclaredField("properties");
            propertiesField.setAccessible(true);
            reflectiveProperties = (Properties) propertiesField.get(ac);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(reflectiveProperties).map(props -> props.get(propertyName)).orElse(null);
    }

    @Override
    public String getName() {
        return QuarkusBatchConfigSource.class.getName();
    }

    @Override
    public int getOrdinal() {
        return ConfigSource.DEFAULT_ORDINAL + 50;
    }
}
