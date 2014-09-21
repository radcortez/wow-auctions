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
    @NamedQuery(name = "Auction.findByAuctionFileStatus",
              query = "SELECT a FROM Auction a WHERE a.auctionFile.fileStatus = :fileStatus"),
    @NamedQuery(name = "Auction.aggregateByFileAndHouse",
              query = "SELECT a.itemId, SUM(a.quantity), " +
                      "SUM(a.bid), SUM(a.buyout), MIN(a.bid), MIN(a.buyout), MAX(a.bid), MAX(a.buyout)" +
                      "FROM Auction a " +
                      "WHERE a.auctionFile.id = :id " +
                      "AND a.auctionHouse = :auctionHouse " +
                      "AND a.auctionFile.fileStatus = :fileStatus " +
                      "GROUP BY a.itemId " +
                      "ORDER BY a.itemId")
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
