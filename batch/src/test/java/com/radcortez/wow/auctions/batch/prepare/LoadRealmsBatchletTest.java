package com.radcortez.wow.auctions.batch.prepare;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.Realm;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Roberto Cortez
 */
public class LoadRealmsBatchletTest {
    @Inject
    EntityManager em;
    @Inject
    LoadRealmsBatchlet loadRealmsBatchlet;
    @Inject
    WoWBusiness woWBusiness;

    @BeforeEach
    public void setUp() {
        em.getTransaction().begin();
        Realm realm = new Realm();
        realm.setName("Hellscream");
        realm.setSlug("hellscream");
        realm.setRegion(Realm.Region.EU);
        realm.setStatus(true);
        em.persist(realm);
        em.flush();
    }

    @AfterEach
    public void tearDown() {
        em.getTransaction().rollback();
    }

    @Test
    public void testCreateRealm() {
        Realm realm = new Realm();
        realm.setName("Thrall");
        realm.setSlug("thrall");
        realm.setRegion(Realm.Region.EU);
        realm.setStatus(true);

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
        realm.setStatus(true);

        assertTrue(woWBusiness.checkIfRealmExists(realm));
        loadRealmsBatchlet.createRealmIfMissing(realm);
        assertNull(realm.getId());
    }
}
