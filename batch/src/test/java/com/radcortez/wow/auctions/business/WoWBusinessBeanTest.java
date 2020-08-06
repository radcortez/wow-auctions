package com.radcortez.wow.auctions.business;

import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.wow.auctions.QuarkusDataSourceProvider;
import com.radcortez.wow.auctions.entity.ConnectedRealm;
import com.radcortez.wow.auctions.entity.FolderType;
import com.radcortez.wow.auctions.entity.Region;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

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
