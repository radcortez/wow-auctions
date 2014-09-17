package com.radcortez.wow.auctions.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;
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
@Table(name = "STATS")
public class AuctionItemStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private AuctionItem item;
    private Integer minBid;
    private Double averageBid;
    private Integer maxBid;
    private Long bidTimestamp;
    @OneToMany
    @XmlTransient
    private List<Auction> auctions;
}
