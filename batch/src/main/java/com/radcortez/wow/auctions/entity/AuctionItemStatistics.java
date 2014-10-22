package com.radcortez.wow.auctions.entity;

import javax.persistence.*;

/**
 * @author Ivan St. Ivanov
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "AuctionItemStatistics.findByRealmsAndItem",
                query = "SELECT ais FROM AuctionItemStatistics ais " +
                        "WHERE ais.realm.id IN (:realmIds) AND ais.itemId = :itemId " +
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public Long getMinBid() {
        return minBid;
    }

    public void setMinBid(Long minBid) {
        this.minBid = minBid;
    }

    public Long getMaxBid() {
        return maxBid;
    }

    public void setMaxBid(Long maxBid) {
        this.maxBid = maxBid;
    }

    public Long getBuyout() {
        return buyout;
    }

    public void setBuyout(Long buyout) {
        this.buyout = buyout;
    }

    public Long getMinBuyout() {
        return minBuyout;
    }

    public void setMinBuyout(Long minBuyout) {
        this.minBuyout = minBuyout;
    }

    public Long getMaxBuyout() {
        return maxBuyout;
    }

    public void setMaxBuyout(Long maxBuyout) {
        this.maxBuyout = maxBuyout;
    }

    public Double getAvgBid() {
        return avgBid;
    }

    public void setAvgBid(Double avgBid) {
        this.avgBid = avgBid;
    }

    public Double getAvgBuyout() {
        return avgBuyout;
    }

    public void setAvgBuyout(Double avgBuyout) {
        this.avgBuyout = avgBuyout;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        AuctionItemStatistics that = (AuctionItemStatistics) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "AuctionItemStatistics{" +
               "id=" + id +
               ", itemId=" + itemId +
               ", quantity=" + quantity +
               ", bid=" + bid +
               ", minBid=" + minBid +
               ", maxBid=" + maxBid +
               ", buyout=" + buyout +
               ", minBuyout=" + minBuyout +
               ", maxBuyout=" + maxBuyout +
               ", avgBid=" + avgBid +
               ", avgBuyout=" + avgBuyout +
               ", timestamp=" + timestamp +
               ", realm=" + realm +
               '}';
    }
}
