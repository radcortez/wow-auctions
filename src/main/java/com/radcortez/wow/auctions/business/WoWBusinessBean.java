package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.entity.Realm;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@SuppressWarnings("unchecked")
@Named
@Local
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class WoWBusinessBean {
    @PersistenceContext
    private EntityManager em;

    public List<Realm> listReams() {
        CriteriaQuery<Realm> query = em.getCriteriaBuilder().createQuery(Realm.class);
        return em.createQuery(query.select(query.from(Realm.class))).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createRealm(Realm realm) {
        em.persist(realm);
    }
}
