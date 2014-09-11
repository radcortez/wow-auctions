package com.radcortez.wow.auctions.business.repository;

import com.radcortez.wow.auctions.entity.Realm;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * @author Roberto Cortez
 */
@Repository
public interface RealmRepository extends EntityRepository<Realm, Long> {
    @Query("SELECT r FROM Realm r WHERE (r.name = :name OR r.slug = :slug) AND r.region = :region ")
    Realm findByNameOrSlugInRegion(@QueryParam("name") String name,
                                   @QueryParam("slug") String slug,
                                   @QueryParam("region") Realm.Region region);

    List<Realm> findByRegion(Realm.Region region);
}
