package com.radcortez.wow.auctions.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author Ivan St. Ivanov
 */
@Data
@EqualsAndHashCode(of = "id")

@Entity
@NamedQueries({
    @NamedQuery(name = "AuctionItemStatistics.findByRealmsAndItem",
                query = "SELECT ais FROM AuctionItemStatistics ais " +
                        "WHERE ais.realm.id IN :realmIds AND ais.itemId = :itemId " +
                        "ORDER BY ais.timestamp ASC")
})
public class AuctionItemStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private Realm realm;
}
