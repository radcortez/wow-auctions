package com.radcortez.wow.auctions.entity;

import lombok.Data;

/**
 * @author Roberto Cortez
 */
@Data
public class Auction {
    private Long auctionId;
    private AuctionHouse auctionHouse;
    private Integer itemId;
    private String owner;
    private String ownerRealm;
    private Integer bid;
    private Integer buyout;
    private Integer quantity;
    private String timeLeft;
    private Integer rand;
    private Long seed;

}
