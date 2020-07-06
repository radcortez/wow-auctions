package com.radcortez.wow.auctions.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@Data
@EqualsAndHashCode(of = "auctionId")

@Entity
@NamedQueries({
    @NamedQuery(name = "Auction.findByRealm",
              query = "SELECT a FROM Auction a WHERE a.realm.id = :realmId"),
    @NamedQuery(name = "Auction.deleteByAuctionFile",
              query = "DELETE FROM Auction a WHERE a.auctionFile.id = :fileId")
})
public class Auction implements Serializable {
    @Id
    private String id;
    @Id
    @ManyToOne
    private AuctionFile auctionFile;

    private Integer itemId;
    private String ownerRealm;
    private Long bid;
    private Long buyout;
    private Integer quantity;

    @ManyToOne
    private Realm realm;
}
