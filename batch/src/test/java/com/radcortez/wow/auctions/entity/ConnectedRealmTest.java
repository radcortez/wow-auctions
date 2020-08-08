package com.radcortez.wow.auctions.entity;

import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.wow.auctions.QuarkusDataSourceProvider;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@FlywayTest(@DataSource(QuarkusDataSourceProvider.class))
class ConnectedRealmTest {
    @Test
    @Transactional
    void create() {
        ConnectedRealm connectedRealm =
            ConnectedRealm.builder()
                          .id(UUID.randomUUID().toString())
                          .region(Region.EU)
                          .realm(Realm.builder()
                                      .id(UUID.randomUUID().toString())
                                      .name("Hellscream")
                                      .slug("hellscream")
                                      .build())
                          .build();

        ConnectedRealm createdConnectedRealm = connectedRealm.create();
        assertEquals(connectedRealm.getId(), createdConnectedRealm.getId());
        assertFalse(createdConnectedRealm.getRealms().isEmpty());

        Realm realm = connectedRealm.getRealms().iterator().next();
        Realm createdRealm = createdConnectedRealm.getRealms().iterator().next();
        assertEquals(realm.getId(), createdRealm.getId());
        assertEquals(realm.getName(), createdRealm.getName());
        assertEquals(realm.getSlug(), createdRealm.getSlug());

        List<Realm> realms = Realm.list("connectedRealm.id", createdConnectedRealm.getId());
        assertFalse(realms.isEmpty());
    }

    @Test
    void listAll() {
        List<ConnectedRealm> connectedRealms = ConnectedRealm.listAll();
        assertFalse(connectedRealms.isEmpty());
    }

    @Test
    void find() {
        Optional<ConnectedRealm> optionalConnectedRealm = ConnectedRealm.findByIdOptional("1");
        assertTrue(optionalConnectedRealm.isPresent());
        optionalConnectedRealm.ifPresent(connectedRealm -> {
            assertEquals("1", connectedRealm.getId());
            assertEquals(Region.EU, connectedRealm.getRegion());
            assertFalse(connectedRealm.getFolders().isEmpty());
            assertTrue(connectedRealm.getFolders().containsKey(FolderType.FI));
        });
    }

    @Test
    @Transactional
    void update() {
        ConnectedRealm connectedRealm =
            ConnectedRealm.builder()
                          .id(UUID.randomUUID().toString())
                          .region(Region.EU)
                          .realms(new HashSet<>())
                          .realm(Realm.builder()
                                      .id(UUID.randomUUID().toString())
                                      .name("Hellscream")
                                      .slug("hellscream")
                                      .build())
                          .build();
        connectedRealm.create();
        connectedRealm.flush();

        ConnectedRealm updateRealm =
            connectedRealm.toBuilder()
                          .realm(Realm.builder()
                                      .id(UUID.randomUUID().toString())
                                      .name("Thrall")
                                      .slug("thrall")
                                      .connectedRealm(connectedRealm)
                                      .build())
                          .build();

        Optional<ConnectedRealm> findConnectedRealm = ConnectedRealm.findByIdOptional(updateRealm.getId());
        assertTrue(findConnectedRealm.isPresent());
        assertEquals(1, findConnectedRealm.get().getRealms().size());
        findConnectedRealm.ifPresent(updateRealm::update);
        findConnectedRealm.ifPresent(updatedRealm -> {
            assertEquals(connectedRealm.getId(), updatedRealm.getId());
            assertEquals(2, updatedRealm.getRealms().size());
        });
    }
}
