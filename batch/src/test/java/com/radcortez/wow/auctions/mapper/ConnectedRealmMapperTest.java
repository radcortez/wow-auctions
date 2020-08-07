package com.radcortez.wow.auctions.mapper;

import com.radcortez.wow.auctions.entity.ConnectedRealm;
import com.radcortez.wow.auctions.entity.Folder;
import com.radcortez.wow.auctions.entity.FolderType;
import com.radcortez.wow.auctions.entity.Realm;
import com.radcortez.wow.auctions.entity.Region;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ConnectedRealmMapperTest {
    @Test
    void toConnectedRealm() {
        com.radcortez.wow.auctions.api.ConnectedRealm connectedRealm = new com.radcortez.wow.auctions.api.ConnectedRealm();
        connectedRealm.setId("1");
        connectedRealm.setRealms(new HashSet<>());
        com.radcortez.wow.auctions.api.Realm realm = new com.radcortez.wow.auctions.api.Realm();
        realm.setId("1");
        realm.setName("Grim Batol");
        realm.setSlug("grim-batol");
        connectedRealm.getRealms().add(realm);

        ConnectedRealm toConnectedRealm = ConnectedRealmMapper.INSTANCE.toEntity(connectedRealm, "EU");
        assertEquals(connectedRealm.getId(), toConnectedRealm.getId());
        assertEquals(Region.EU, toConnectedRealm.getRegion());
        assertFalse(connectedRealm.getRealms().isEmpty());
        Realm toRealm = toConnectedRealm.getRealms().iterator().next();
        assertEquals(realm.getId(), toRealm.getId());
        assertEquals(realm.getName(), toRealm.getName());
        assertEquals(realm.getSlug(), toRealm.getSlug());
    }

    @Test
    void toConnectedRealmEntity() {
        ConnectedRealm source = new ConnectedRealm();
        source.setId(UUID.randomUUID().toString());
        source.setRegion(Region.EU);

        ConnectedRealm target = new ConnectedRealm();
        target.setFolders(new HashMap<>());
        target.getFolders().put(FolderType.FI, new Folder("1", FolderType.FI, "path"));

        ConnectedRealm toConnectedRealm = ConnectedRealmMapper.INSTANCE.toEntity(source, target);
        assertNull(toConnectedRealm.getId());
        assertEquals(source.getRegion(), toConnectedRealm.getRegion());
        assertNotNull(toConnectedRealm.getFolders());
        assertFalse(toConnectedRealm.getFolders().isEmpty());
    }
}
