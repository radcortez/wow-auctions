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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

        ConnectedRealm createdConnectedRealm = woWBusiness.createConnectedRealm(connectedRealm);
        assertEquals(connectedRealm.getId(), createdConnectedRealm.getId());
        assertFalse(createdConnectedRealm.getRealms().isEmpty());
        assertEquals(realm.getId(), createdConnectedRealm.getRealms().get(0).getId());
        assertEquals(realm.getName(), createdConnectedRealm.getRealms().get(0).getName());
        assertEquals(realm.getSlug(), createdConnectedRealm.getRealms().get(0).getSlug());
    }

    @Test
    void listConnectedRealms() {
        List<ConnectedRealm> connectedRealms = woWBusiness.listConnectedRealms();
        assertFalse(connectedRealms.isEmpty());
    }

    @Test
    void findConnectedRealm() {
        Optional<ConnectedRealm> connectedRealm = woWBusiness.findConnectedRealm("1");
        assertTrue(connectedRealm.isPresent());
        connectedRealm.ifPresent(connectedRealm1 -> {
            assertEquals("1", connectedRealm1.getId());
            assertEquals(Region.EU, connectedRealm1.getRegion());
            assertFalse(connectedRealm1.getFolders().isEmpty());
            assertTrue(connectedRealm1.getFolders().containsKey(FolderType.FI));
        });
    }
}
