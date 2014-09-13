package com.radcortez.wow.auctions.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQueries({
      @NamedQuery(name = "Realm.listRealms",
                  query = "SELECT r FROM Realm r"),
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "realmId")
    @SequenceGenerator(name = "realmId", sequenceName = "REALM_ID_SEQ", initialValue = 1, allocationSize = 1)
    private Long id;
    private String name;
    private String nameAuction;
    private String slug;
    private Region region;
    private boolean status;

    public void setName(String name) {
        this.name = name;
        this.nameAuction = name.replaceAll(" ", "");
    }

    public void setRegion(String region) {
        this.region = Region.valueOf(region);
    }

    public String getRealmDetail() {
        return region + " " + name;
    }

    public static enum Region {
        US,
        EU
    }
}
