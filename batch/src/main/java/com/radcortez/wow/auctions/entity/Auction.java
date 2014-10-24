package com.radcortez.wow.auctions.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Auction.findByRealm",
              query = "SELECT a FROM Auction a WHERE a.realm.id = :realmId"),
    @NamedQuery(name = "Auction.deleteByAuctionFile",
              query = "DELETE FROM Auction a WHERE a.auctionFile.id = :fileId")
})
public class Auction implements Serializable {
    @Id
    private Long auctionId;
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

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public AuctionFile getAuctionFile() {
        return auctionFile;
    }

    public void setAuctionFile(AuctionFile auctionFile) {
        this.auctionFile = auctionFile;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getOwnerRealm() {
        return ownerRealm;
    }

    public void setOwnerRealm(String ownerRealm) {
        this.ownerRealm = ownerRealm;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public Long getBuyout() {
        return buyout;
    }

    public void setBuyout(Long buyout) {
        this.buyout = buyout;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

        Auction auction = (Auction) o;

        return auctionFile.equals(auction.auctionFile) && auctionId.equals(auction.auctionId);
    }

    @Override
    public int hashCode() {
        int result = auctionId.hashCode();
        result = 31 * result + auctionFile.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Auction{" +
               "auctionId=" + auctionId +
               ", auctionFile=" + auctionFile +
               ", itemId=" + itemId +
               ", ownerRealm='" + ownerRealm + '\'' +
               ", bid=" + bid +
               ", buyout=" + buyout +
               ", quantity=" + quantity +
               ", realm=" + realm +
               '}';
    }
}
