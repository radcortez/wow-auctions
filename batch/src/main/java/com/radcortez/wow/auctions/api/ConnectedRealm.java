package com.radcortez.wow.auctions.api;

import com.radcortez.wow.auctions.mapper.ConnectedRealmMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ConnectedRealm {
    private String id;
    private List<Realm> realms;

    public com.radcortez.wow.auctions.entity.ConnectedRealm toEntity(final String region) {
        return ConnectedRealmMapper.INSTANCE.toEntity(this, region.toUpperCase());
    }
}
