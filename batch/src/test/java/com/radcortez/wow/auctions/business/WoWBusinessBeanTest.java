package com.radcortez.wow.auctions.business;

import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.wow.auctions.QuarkusDataSourceProvider;
import com.radcortez.wow.auctions.entity.ConnectedRealm;
import com.radcortez.wow.auctions.entity.Realm;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Collections;
import java.util.UUID;

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
}
