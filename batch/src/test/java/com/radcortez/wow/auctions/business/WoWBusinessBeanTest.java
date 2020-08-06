package com.radcortez.wow.auctions.business;

import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.wow.auctions.QuarkusDataSourceProvider;
import com.radcortez.wow.auctions.entity.ConnectedRealm;
import com.radcortez.wow.auctions.entity.FolderType;
import com.radcortez.wow.auctions.entity.Realm;
import com.radcortez.wow.auctions.entity.Region;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void createConnectedRealm() {
        ConnectedRealm connectedRealm = new ConnectedRealm();
        connectedRealm.setId(UUID.randomUUID().toString());

        Realm realm = new Realm();
        realm.setId(UUID.randomUUID().toString());
        realm.setName("Hellscream");
        realm.setSlug("hellscream");

        connectedRealm.setRealms(Collections.singletonList(realm));

        woWBusiness.createConnectedRealm(connectedRealm);
    }

    @Test
    void listConnectedRealms() {
        List<ConnectedRealm> connectedRealms = woWBusiness.listConnectedRealms();
        assertFalse(connectedRealms.isEmpty());
    }

    @Test
    void findConnectedRealmById() {
        ConnectedRealm connectedRealm = woWBusiness.findConnectedRealmById("1");
        assertNotNull(connectedRealm);
        assertEquals("1", connectedRealm.getId());
        assertEquals(Region.EU, connectedRealm.getRegion());
        assertFalse(connectedRealm.getFolders().isEmpty());
        assertTrue(connectedRealm.getFolders().containsKey(FolderType.FI));
    }
}
