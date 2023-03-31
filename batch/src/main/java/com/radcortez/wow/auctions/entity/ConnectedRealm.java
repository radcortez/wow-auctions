package com.radcortez.wow.auctions.entity;

import com.radcortez.wow.auctions.mapper.ConnectedRealmMapper;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;

@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id", callSuper = false)

@Entity
public class ConnectedRealm extends PanacheEntityBase {
    @Id
    private String id;
    @Enumerated(EnumType.STRING)
    private Region region;
    @OneToMany(mappedBy = "connectedRealm", cascade = ALL, orphanRemoval = true)
    private Set<Realm> realms = new HashSet<>();
    @OneToMany(mappedBy = "connectedRealm", cascade = ALL, orphanRemoval = true, fetch = EAGER)
    @MapKey(name = "id.folderType")
    private Map<FolderType, Folder> folders = new HashMap<>();
    @OneToMany(mappedBy = "connectedRealm", cascade = ALL, orphanRemoval = true)
    private Set<AuctionFile> files = new HashSet<>();

    @Builder(toBuilder = true)
    public ConnectedRealm(
        final String id,
        final Region region,
        @Singular
        final Set<Realm> realms,
        final Map<FolderType, Folder> folders,
        final Set<AuctionFile> files) {

        this.id = id;
        this.region = region;
        this.realms = Optional.ofNullable(realms).map(HashSet::new).orElse(new HashSet<>());
        this.folders = Optional.ofNullable(folders).map(HashMap::new).orElse(new HashMap<>());
        this.files = Optional.ofNullable(files).map(HashSet::new).orElse(new HashSet<>());
    }

    public void addRealm(final Realm realm) {
        realm.setConnectedRealm(this);
        realms.add(realm);
    }

    public ConnectedRealm create() {
        realms.stream()
              .filter(realm -> realm.getConnectedRealm() == null)
              .forEach(realm -> realm.setConnectedRealm(this));

        persist();
        return this;
    }

    public ConnectedRealm update(final ConnectedRealm connectedRealm) {
        return ConnectedRealmMapper.INSTANCE.toEntity(this, connectedRealm);
    }
}
