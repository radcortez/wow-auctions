package com.radcortez.wow.auctions.batch.prepare;

import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.Realm;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import static org.junit.Assert.*;

/**
 * @author Roberto Cortez
 */
@SuppressWarnings("CdiInjectionPointsInspection")
@RunWith(CdiTestRunner.class)
public class LoadRealmsBatchletTest {
    @Inject
    private EntityManager em;
    @Inject
    private LoadRealmsBatchlet loadRealmsBatchlet;
    @Inject
    private WoWBusiness woWBusiness;

    @Before
    public void setUp() {
        em.getTransaction().begin();
        Realm realm = new Realm();
        realm.setName("Hellscream");
        realm.setSlug("hellscream");
        realm.setRegion("EU");
        realm.setStatus(true);
        em.persist(realm);
        em.flush();
    }

    @After
    public void tearDown() throws Exception {
        em.getTransaction().rollback();
    }

    @Test
    public void testCreateRealm() throws Exception {
        Realm realm = new Realm();
        realm.setName("Thrall");
        realm.setSlug("thrall");
        realm.setRegion("EU");
        realm.setStatus(true);

        loadRealmsBatchlet.createRealmIfMissing(realm);

        realm = em.find(Realm.class, realm.getId());
        assertNotNull(realm.getId());
    }

    @Test
    public void testCreateExistentRealm() throws Exception {
        Realm realm = new Realm();
        realm.setName("Hellscream");
        realm.setSlug("hellscream");
        realm.setRegion("EU");
        realm.setStatus(true);

        assertTrue(woWBusiness.checkIfRealmExists(realm));

        loadRealmsBatchlet.createRealmIfMissing(realm);

        assertNull(realm.getId());
    }
}
