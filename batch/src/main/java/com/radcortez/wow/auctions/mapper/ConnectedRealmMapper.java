package com.radcortez.wow.auctions.mapper;

import com.radcortez.wow.auctions.entity.ConnectedRealm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ConnectedRealmMapper {
    ConnectedRealmMapper INSTANCE = Mappers.getMapper(ConnectedRealmMapper.class);

    ConnectedRealm toConnectedRealm(com.radcortez.wow.auctions.api.ConnectedRealm connectedRealm, String region);
}
