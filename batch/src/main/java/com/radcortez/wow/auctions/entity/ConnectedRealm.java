package com.radcortez.wow.auctions.entity;

import com.radcortez.wow.auctions.mapper.ConnectedRealmMapper;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id", callSuper = false)
@Builder

@Entity
public class ConnectedRealm extends PanacheEntityBase {
    @Id
    private String id;
    private Region region;
    @OneToMany(mappedBy = "connectedRealm", cascade = ALL, orphanRemoval = true)
    private Set<Realm> realms = new HashSet<>();
    @OneToMany(mappedBy = "connectedRealm", cascade = ALL, orphanRemoval = true, fetch = EAGER)
    @MapKey(name = "id.folderType")
    private Map<FolderType, Folder> folders = new HashMap<>();

    public void addRealm(final Realm realm) {
        realm.setConnectedRealm(this);
        realms.add(realm);
    }

    public ConnectedRealm create() {
        persist();
        return this;
    }

    public ConnectedRealm update(final ConnectedRealm connectedRealm) {
        return ConnectedRealmMapper.INSTANCE.toConnectedRealm(this, connectedRealm);
    }
}
