package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.entity.AuctionItemStatistics;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * @author Ivan St. Ivanov
 */
@Stateless
@Path("/itemsstats")
public class ItemsStatisticsBean {

    @PersistenceContext
    private EntityManager em;

    @GET
    @Produces("application/json")
    public List<AuctionItemStatistics> getItemsStatistics() {
        Query getAllItemsStatisticsQuery = em.createNamedQuery("AuctionItemStatistics.getAllItemsStatistics");
        List resultList = getAllItemsStatisticsQuery.getResultList();
        System.out.println(resultList);
        return resultList;
    }
}
