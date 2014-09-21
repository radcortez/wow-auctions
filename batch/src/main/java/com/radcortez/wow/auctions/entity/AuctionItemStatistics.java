package com.radcortez.wow.auctions.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

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
    private Integer minBid;
    private Double averageBid;
    private Integer maxBid;
    private Long bidTimestamp;

    @ManyToOne
    private AuctionItem item;

    @XmlTransient
    @OneToMany
    private List<Auction> auctions;
}
