package com.radcortez.wow.auctions.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author Roberto Cortez
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id", callSuper = false)
@Builder

@Entity
public class Realm extends PanacheEntityBase {
    @Id
    private String id;
    private String name;
    private String slug;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private ConnectedRealm connectedRealm;
}
