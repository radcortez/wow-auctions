package com.radcortez.wow.auctions.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Ivan St. Ivanov
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionItem {
    /*
      TODO design the auction item
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String itemName;

}
