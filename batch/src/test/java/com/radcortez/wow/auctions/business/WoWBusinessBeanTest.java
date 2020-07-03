package com.radcortez.wow.auctions.business;

import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.wow.auctions.QuarkusDataSourceProvider;
import com.radcortez.wow.auctions.entity.Realm;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Roberto Cortez
 */
@QuarkusTest
@FlywayTest(@DataSource(QuarkusDataSourceProvider.class))
public class WoWBusinessBeanTest {
    @Inject
    WoWBusinessBean woWBusiness;

    @Test
    public void testFindRealmByNameOrSlug() {
        assertTrue(woWBusiness.findRealmByNameOrSlug("Hellscream", Realm.Region.EU).isPresent());
        assertTrue(woWBusiness.findRealmByNameOrSlug("hellscream", Realm.Region.US).isPresent());
        assertTrue(woWBusiness.findRealmByNameOrSlug("Grim Batol", Realm.Region.EU).isPresent());
    }
}
