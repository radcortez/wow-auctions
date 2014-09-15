package com.radcortez.wow.auctions.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Roberto Cortez
 */
@Data
@Entity
@NamedQueries({
      @NamedQuery(name = "Auction.findByRealm",
                  query = "SELECT a FROM Auction a WHERE a.realm.id = :realmId"),
})
public class Auction {
    @Id
    private Long auctionId;
    private AuctionHouse auctionHouse;
    private Integer itemId;
    private String ownerRealm;
    private Integer bid;
    private Integer buyout;
    private Integer quantity;

    @ManyToOne
    private Realm realm;
}
