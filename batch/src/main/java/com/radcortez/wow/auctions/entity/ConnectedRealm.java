package com.radcortez.wow.auctions.entity;

import com.radcortez.wow.auctions.mapper.ConnectedRealmMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Map;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@NoArgsConstructor
@Data

@Entity
public class ConnectedRealm {
    @Id
    private String id;
    private Region region;
    @OneToMany(mappedBy = "connectedRealm", cascade = ALL, orphanRemoval = true)
    private List<Realm> realms;
    @OneToMany(mappedBy = "connectedRealm", cascade = ALL, orphanRemoval = true, fetch = EAGER)
    @MapKey(name = "id.folderType")
    private Map<FolderType, Folder> folders ;

    public ConnectedRealm toConnectedRealm(final ConnectedRealm connectedRealm) {
        return ConnectedRealmMapper.INSTANCE.toConnectedRealm(connectedRealm, this);
    }
}
