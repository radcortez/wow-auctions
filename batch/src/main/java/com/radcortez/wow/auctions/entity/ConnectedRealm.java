package com.radcortez.wow.auctions.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@NoArgsConstructor
@Data

@Entity
public class ConnectedRealm {
    @Id
    private String id;
    private Region region;
    @OneToMany(mappedBy = "connectedRealm", cascade = CascadeType.ALL)
    private List<Realm> realms;
}
