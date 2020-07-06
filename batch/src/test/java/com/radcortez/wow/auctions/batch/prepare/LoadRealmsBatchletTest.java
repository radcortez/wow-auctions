package com.radcortez.wow.auctions.batch.prepare;

import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.wow.auctions.QuarkusDataSourceProvider;
import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.entity.Realm;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Roberto Cortez
 */
@QuarkusTest
@FlywayTest(@DataSource(QuarkusDataSourceProvider.class))
public class LoadRealmsBatchletTest {
    @Inject
    EntityManager em;
    @Inject
    LoadRealmsBatchlet loadRealmsBatchlet;
    @Inject
    WoWBusinessBean woWBusiness;

    @Test
    public void testCreateRealm() {
        Realm realm = new Realm();
        realm.setId(UUID.randomUUID().toString());
        realm.setName("Aggra");
        realm.setSlug("aggra");
        realm.setRegion(Realm.Region.EU);

        loadRealmsBatchlet.createRealmIfMissing(realm);

        realm = em.find(Realm.class, realm.getId());
        assertNotNull(realm.getId());
    }

    @Test
    public void testCreateExistentRealm() {
        Realm realm = new Realm();
        realm.setName("Hellscream");
        realm.setSlug("hellscream");
        realm.setRegion(Realm.Region.EU);

        assertTrue(woWBusiness.checkIfRealmExists(realm));
        loadRealmsBatchlet.createRealmIfMissing(realm);
        assertNull(realm.getId());
    }
}
