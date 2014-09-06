package com.radcortez.wow.auctions.business.repository;

import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.Realm;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

/**
 * @author Roberto Cortez
 */
@Repository
public interface AuctionFileRepository extends EntityRepository<AuctionFile, Long> {
    List<AuctionFile> findByRealm_regionAndDownloaded(Realm.Region region, boolean downloaded);
}
