package com.radcortez.wow.auctions.business.repository;

import com.radcortez.wow.auctions.entity.RealmFolder;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

/**
 * @author Roberto Cortez
 */
@Repository
public interface RealmFolderRepository extends EntityRepository<RealmFolder, RealmFolder.RealmFolderPK> {}
