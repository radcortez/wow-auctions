package com.radcortez.wow.auctions.entity;

import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.wow.auctions.QuarkusDataSourceProvider;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
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
        ConnectedRealm connectedRealm = new ConnectedRealm();
        connectedRealm.setId(UUID.randomUUID().toString());

        Realm realm = new Realm();
        realm.setId(UUID.randomUUID().toString());
        realm.setName("Hellscream");
        realm.setSlug("hellscream");
        connectedRealm.setRealms(Collections.singletonList(realm));

        ConnectedRealm createdConnectedRealm = connectedRealm.create();
        assertEquals(connectedRealm.getId(), createdConnectedRealm.getId());
        assertFalse(createdConnectedRealm.getRealms().isEmpty());
        assertEquals(realm.getId(), createdConnectedRealm.getRealms().get(0).getId());
        assertEquals(realm.getName(), createdConnectedRealm.getRealms().get(0).getName());
        assertEquals(realm.getSlug(), createdConnectedRealm.getRealms().get(0).getSlug());
    }

    @Test
    void listAll() {
        List<ConnectedRealm> connectedRealms = ConnectedRealm.listAll();
        assertFalse(connectedRealms.isEmpty());
    }

    @Test
    void find() {
        Optional<ConnectedRealm> connectedRealm = ConnectedRealm.findByIdOptional("1");
        assertTrue(connectedRealm.isPresent());
        connectedRealm.ifPresent(connectedRealm1 -> {
            assertEquals("1", connectedRealm1.getId());
            assertEquals(Region.EU, connectedRealm1.getRegion());
            assertFalse(connectedRealm1.getFolders().isEmpty());
            assertTrue(connectedRealm1.getFolders().containsKey(FolderType.FI));
        });
    }

    @Test
    @Transactional
    void update() {
        ConnectedRealm connectedRealm = new ConnectedRealm();
        connectedRealm.setId(UUID.randomUUID().toString());
        connectedRealm.setRealms(new ArrayList<>());
        Realm realm = new Realm();
        realm.setId(UUID.randomUUID().toString());
        realm.setName("Hellscream");
        realm.setSlug("hellscream");
        connectedRealm.getRealms().add(realm);
        connectedRealm.create();

        ConnectedRealm updateRealm = new ConnectedRealm();
        updateRealm.setId(connectedRealm.getId());
        updateRealm.setRealms(new ArrayList<>());
        Realm newRealm = new Realm();
        newRealm.setId(UUID.randomUUID().toString());
        newRealm.setName("Thrall");
        newRealm.setSlug("thrall");
        updateRealm.getRealms().addAll(connectedRealm.getRealms());
        updateRealm.getRealms().add(newRealm);

        Optional<ConnectedRealm> findConnectedRealm = ConnectedRealm.findByIdOptional(updateRealm.getId());
        findConnectedRealm.ifPresent(updateRealm::update);
        findConnectedRealm.ifPresent(new Consumer<ConnectedRealm>() {
            @Override
            public void accept(final ConnectedRealm updateRealm) {
                assertEquals(connectedRealm.getId(), updateRealm.getId());
                assertEquals(2, updateRealm.getRealms().size());
            }
        });
    }
}
