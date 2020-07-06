package com.radcortez.wow.auctions.business;

import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.wow.auctions.QuarkusDataSourceProvider;
import com.radcortez.wow.auctions.entity.ConnectedRealm;
import com.radcortez.wow.auctions.entity.Realm;
import com.radcortez.wow.auctions.entity.Region;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.Collections;
import java.util.UUID;

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
        assertTrue(woWBusiness.findRealmByNameOrSlug("Hellscream", Region.EU).isPresent());
        assertTrue(woWBusiness.findRealmByNameOrSlug("hellscream", Region.US).isPresent());
        assertTrue(woWBusiness.findRealmByNameOrSlug("Grim Batol", Region.EU).isPresent());
    }

    @Test
    void createConnectedRealm() {
        ConnectedRealm connectedRealm = new ConnectedRealm();
        connectedRealm.setId(UUID.randomUUID().toString());

        Realm realm = new Realm();
        realm.setId(UUID.randomUUID().toString());
        realm.setName("Hellscream");
        realm.setSlug("hellscream");
        realm.setRegion(Region.EU);

        connectedRealm.setRealms(Collections.singletonList(realm));

        woWBusiness.createConnectedRealm(connectedRealm);
    }
}
