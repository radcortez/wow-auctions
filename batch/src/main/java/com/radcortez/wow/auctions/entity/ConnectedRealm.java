package com.radcortez.wow.auctions.entity;

import com.radcortez.wow.auctions.mapper.ConnectedRealmMapper;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Map;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id", callSuper = false)

@Entity
public class ConnectedRealm extends PanacheEntityBase {
    @Id
    private String id;
    private Region region;
    @OneToMany(mappedBy = "connectedRealm", cascade = ALL, orphanRemoval = true)
    private List<Realm> realms;
    @OneToMany(mappedBy = "connectedRealm", cascade = ALL, orphanRemoval = true, fetch = EAGER)
    @MapKey(name = "id.folderType")
    private Map<FolderType, Folder> folders ;

    public ConnectedRealm create() {
        persist();
        return this;
    }

    public ConnectedRealm update(final ConnectedRealm connectedRealm) {
        return ConnectedRealmMapper.INSTANCE.toConnectedRealm(this, connectedRealm);
    }

    @Deprecated
    public ConnectedRealm toConnectedRealm(final ConnectedRealm connectedRealm) {
        return ConnectedRealmMapper.INSTANCE.toConnectedRealm(connectedRealm, this);
    }
}
