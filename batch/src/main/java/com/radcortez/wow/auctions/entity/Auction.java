package com.radcortez.wow.auctions.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = "auctionFile")

@Entity
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
