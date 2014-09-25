package com.radcortez.wow.auctions.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQueries({
      @NamedQuery(name = "Realm.listRealms",
                  query = "SELECT r FROM Realm r ORDER BY r.name, r.region"),
      @NamedQuery(name = "Realm.findRealmsWithConnectionsById",
                  query = "SELECT r FROM Realm r LEFT JOIN FETCH r.connectedRealms " +
                          "WHERE r.id = :id ORDER BY r.name, r.region"),
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

    @Transient
    private String[] connected_realms;

    @ManyToMany
    @JsonIgnore
    private List<Realm> connectedRealms;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.nameAuction = name.replaceAll(" ", "");
    }

    public String getNameAuction() {
        return nameAuction;
    }

    public void setNameAuction(String nameAuction) {
        this.nameAuction = nameAuction;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = Region.valueOf(region);
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String[] getConnected_realms() {
        return connected_realms;
    }

    public void setConnected_realms(String[] connected_realms) {
        this.connected_realms = connected_realms;
    }

    public List<Realm> getConnectedRealms() {
        return connectedRealms;
    }

    public void setConnectedRealms(List<Realm> connectedRealms) {
        this.connectedRealms = connectedRealms;
    }

    public String getRealmDetail() {
        return region + " " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        Realm realm = (Realm) o;

        return id.equals(realm.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Realm{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", nameAuction='" + nameAuction + '\'' +
               ", slug='" + slug + '\'' +
               ", region=" + region +
               ", status=" + status +
               ", connected_realms=" + Arrays.toString(connected_realms) +
               ", connectedRealms=" + connectedRealms +
               '}';
    }

    public static enum Region {
        US,
        EU
    }
}
