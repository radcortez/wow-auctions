package com.radcortez.wow.auctions.business.repository;

import com.radcortez.wow.auctions.entity.Realm;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * @author Roberto Cortez
 */
@Repository
public interface RealmRepository extends EntityRepository<Realm, Long> {
    List<Realm> findByRegion(Realm.Region region);
}
