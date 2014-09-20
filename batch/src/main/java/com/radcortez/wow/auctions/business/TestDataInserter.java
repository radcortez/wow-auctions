package com.radcortez.wow.auctions.business;

import com.radcortez.wow.auctions.entity.AuctionItem;
import com.radcortez.wow.auctions.entity.AuctionItemStatistics;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

/**
 * @author Ivan St. Ivanov
 */
@Singleton
@Startup
public class TestDataInserter {

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void insertItems() {
        AuctionItem ai1 = new AuctionItem();
        ai1.setItemName("Volatile Air");

        AuctionItem ai2 = new AuctionItem();
        ai2.setItemName("Obsidium Bar");

        em.persist(ai1); em.persist(ai2);

        AuctionItemStatistics ais1 = new AuctionItemStatistics();
        ais1.setItem(ai1);
        ais1.setMinBid(10);
        ais1.setAverageBid(15d);
        ais1.setMaxBid(20);
        ais1.setAuctions(new ArrayList<>());

        AuctionItemStatistics ais2 = new AuctionItemStatistics();
        ais2.setItem(ai2);
        ais2.setMinBid(100);
        ais2.setAverageBid(150d);
        ais2.setMaxBid(200);
        ais2.setAuctions(new ArrayList<>());

        em.persist(ais1); em.persist(ais2);
    }
}