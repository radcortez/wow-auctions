package com.radcortez.wow.auctions.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@Data
@EqualsAndHashCode(of = "id")

@Entity
@NamedQueries({
      @NamedQuery(name = "Realm.listRealms",
                  query = "SELECT r FROM Realm r ORDER BY r.name, r.region"),
//      @NamedQuery(name = "Realm.findRealmsWithConnectionsById",
//                  query = "SELECT r FROM Realm r LEFT JOIN FETCH r.connectedRealms " +
//                          "WHERE r.id = :id ORDER BY r.name, r.region"),
      @NamedQuery(name = "Realm.findByNameOrSlugInRegion",
                  query = "SELECT r FROM Realm r " +
                          "WHERE (r.name = :name OR r.nameAuction = :name OR r.slug = :slug) AND r.region = :region"),
      @NamedQuery(name = "Realm.findByRegion",
                  query = "SELECT r FROM Realm r " +
                          "WHERE r.region = :region"),
      @NamedQuery(name = "Realm.exists",
                  query = "SELECT COUNT(r) FROM Realm r " +
                          "WHERE r.name = :name AND r.region = :region"),
})
public class Realm implements Serializable {
    @Id
    private String id;
    private String name;
    private String nameAuction;
    private String slug;
    private Region region;

    @ManyToOne
    private ConnectedRealm connectedRealm;
}
