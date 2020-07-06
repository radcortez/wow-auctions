package com.radcortez.wow.auctions.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@Data
@EqualsAndHashCode(of = "id")

@Entity
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
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private String name;
    private String nameAuction;
    private String slug;
    @XmlElement
    private Region region;
    private boolean status;

    @Transient
    private String realmDetail;
    @Transient
    private String[] connected_realms;

    @ManyToMany
    private List<Realm> connectedRealms;

    public enum Region {
        US,
        EU
    }
}
