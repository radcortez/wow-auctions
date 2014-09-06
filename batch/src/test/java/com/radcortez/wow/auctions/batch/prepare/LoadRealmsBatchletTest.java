package com.radcortez.wow.auctions.batch.prepare;

import com.radcortez.wow.auctions.business.repository.RealmRepository;
import com.radcortez.wow.auctions.entity.Realm;
import com.radcortez.wow.auctions.entity.Realm_;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Roberto Cortez
 */
@SuppressWarnings("CdiInjectionPointsInspection")
@RunWith(CdiTestRunner.class)
public class LoadRealmsBatchletTest {
    @Inject
    private RealmRepository realmRepository;
    @Inject
    private LoadRealmsBatchlet loadRealmsBatchlet;

    @Before
    public void setUp() {
        Realm realm = new Realm();
        realm.setName("Hellscream");
        realm.setSlug("hellscream");
        realm.setRegion("EU");
        realm.setStatus(true);
        realmRepository.save(realm);
    }

    @Test
    public void testCreateRealm() throws Exception {
        Realm realm = new Realm();
        realm.setName("Thrall");
        realm.setSlug("thrall");
        realm.setRegion("EU");
        realm.setStatus(true);

        loadRealmsBatchlet.createRealmIfMissing(realm);

        realm = realmRepository.findBy(realm.getId());
        assertNotNull(realm.getId());
    }

    @Test
    public void testCreateExistentRealm() throws Exception {
        Realm realm = new Realm();
        realm.setName("Hellscream");
        realm.setSlug("hellscream");
        realm.setRegion("EU");
        realm.setStatus(true);

        assertTrue(realmRepository.count(realm, Realm_.name, Realm_.region) > 0);

        loadRealmsBatchlet.createRealmIfMissing(realm);

        assertNull(realm.getId());
    }
}
