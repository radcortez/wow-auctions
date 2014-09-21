package com.radcortez.wow.auctions.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Ivan St. Ivanov
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
    @NamedQuery(name = "AuctionItemStatistics.getAllItemsStatistics",
                query = "SELECT ais FROM AuctionItemStatistics ais")
})
@XmlRootElement
public class AuctionItemStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer itemId;
    private Integer quantity;

    private Integer bid;
    private Integer minBid;
    private Integer maxBid;

    private Integer buyout;
    private Integer minBuyout;
    private Integer maxBuyout;

    @Transient
    private Double avgBid;
    @Transient
    private Double avgBuyout;
}
