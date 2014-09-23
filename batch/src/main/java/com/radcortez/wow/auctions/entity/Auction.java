package com.radcortez.wow.auctions.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@Data
@Entity
@NamedQueries({
    @NamedQuery(name = "Auction.findByRealm",
              query = "SELECT a FROM Auction a WHERE a.realm.id = :realmId"),
    @NamedQuery(name = "Auction.deleteByAuctionFile",
              query = "DELETE FROM Auction a WHERE a.auctionFile = :auctionFile")
})
public class Auction implements Serializable {
    @Id
    private Long auctionId;
    @Id
    @ManyToOne
    private AuctionFile auctionFile;

    private AuctionHouse auctionHouse;
    private Integer itemId;
    private String ownerRealm;
    private Integer bid;
    private Integer buyout;
    private Integer quantity;

    @ManyToOne
    private Realm realm;
}
