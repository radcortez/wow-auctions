package com.radcortez.wow.auctions.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

/**
 * @author Ivan St. Ivanov
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = "connectedRealm")

@Entity
@NamedQueries({
    @NamedQuery(name = "AuctionItemStatistics.findByRealmsAndItem",
                query = "SELECT ais FROM AuctionStatistics ais " +
                        "WHERE ais.connectedRealm.id IN :realmIds AND ais.itemId = :itemId " +
                        "ORDER BY ais.timestamp ASC")
})
public class AuctionStatistics extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private Long id;
    private Integer itemId;
    private Long quantity;

    private Long bid;
    private Long minBid;
    private Long maxBid;

    private Long buyout;
    private Long minBuyout;
    private Long maxBuyout;

    private Double avgBid;
    private Double avgBuyout;
    private Long timestamp;

    @ManyToOne
    private ConnectedRealm connectedRealm;

    public AuctionStatistics create() {
        persist();
        return this;
    }
}
