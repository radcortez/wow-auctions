package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.entity.FolderType;
import com.radcortez.wow.auctions.entity.RealmFolder;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Roberto Cortez
 */
@SuppressWarnings("unchecked")
@Named
@Local
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class BatchBusinessBean {
    @PersistenceContext
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createRealmFolder(RealmFolder realmFolder) {
        em.persist(realmFolder);
    }

    public RealmFolder findRealmFolderById(Long realmId, FolderType folderType) {
        return em.find(RealmFolder.class, new RealmFolder.RealmFolderPK(realmId, folderType));
    }

    /*

    public List<RealmFolder> findCompanyFolders() {
        return em.createQuery("SELECT cf FROM RealmFolder cf").getResultList();
    }

    public List<RealmFolder> findCompanyFoldersByType(FolderType type) {
        return em.createQuery("SELECT cf FROM RealmFolder cf WHERE cf.id.folderType = :type")
                            .setParameter("type", type)
                            .getResultList();
    }
    */
}
