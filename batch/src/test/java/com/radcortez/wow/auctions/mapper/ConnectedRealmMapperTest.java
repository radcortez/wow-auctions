package com.radcortez.wow.auctions.mapper;

import com.radcortez.wow.auctions.api.Realm;
import com.radcortez.wow.auctions.entity.ConnectedRealm;
import com.radcortez.wow.auctions.entity.Region;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ConnectedRealmMapperTest {
    @Test
    void toConnectedRealm() {
        com.radcortez.wow.auctions.api.ConnectedRealm connectedRealm = new com.radcortez.wow.auctions.api.ConnectedRealm();
        connectedRealm.setId("1");
        connectedRealm.setRealms(new ArrayList<>());
        com.radcortez.wow.auctions.api.Realm realm = new Realm();
        realm.setId("1");
        realm.setName("Grim Batol");
        realm.setSlug("grim-batol");
        connectedRealm.getRealms().add(realm);

        final ConnectedRealm toConnectedRealm = ConnectedRealmMapper.INSTANCE.toConnectedRealm(connectedRealm, "EU");
        assertEquals(connectedRealm.getId(), toConnectedRealm.getId());
        assertEquals(Region.EU, toConnectedRealm.getRegion());
        assertFalse(connectedRealm.getRealms().isEmpty());
        assertEquals(realm.getId(), toConnectedRealm.getRealms().get(0).getId());
        assertEquals(realm.getName(), toConnectedRealm.getRealms().get(0).getName());
        assertEquals(realm.getSlug(), toConnectedRealm.getRealms().get(0).getSlug());
    }
}
