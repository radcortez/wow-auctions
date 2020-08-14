package com.radcortez.wow.auctions.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.io.Serializable;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id", callSuper = false)

@Entity
@NamedQueries({
    @NamedQuery(name = "Auction.findByConnectedRealm",
              query = "SELECT a FROM Auction a WHERE a.auctionFile.connectedRealm.id = :connectedRealmId"),
    @NamedQuery(name = "Auction.deleteByAuctionFile",
              query = "DELETE FROM Auction a WHERE a.auctionFile.id = :fileId")
})
public class Auction extends PanacheEntityBase implements Serializable {
    @Id
    private Long id;
    @Id
    @ManyToOne(optional = false)
    private AuctionFile auctionFile;

    private Integer itemId;
    private Long bid;
    private Long buyout;
    private Integer quantity;

    public Auction create() {
        persist();
        return this;
    }

    public static List<Auction> findByConnectedRealm(final ConnectedRealm connectedRealm) {
        return list("auctionFile.connectedRealm.id", connectedRealm.getId());
    }
}
