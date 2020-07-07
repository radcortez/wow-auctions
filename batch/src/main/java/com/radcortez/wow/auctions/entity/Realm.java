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
public class Realm implements Serializable {
    @Id
    private String id;
    private String name;
    private String slug;

    @ManyToOne
    private ConnectedRealm connectedRealm;
}
