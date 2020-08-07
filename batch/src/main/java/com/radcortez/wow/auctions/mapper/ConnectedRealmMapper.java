package com.radcortez.wow.auctions.mapper;

import com.radcortez.wow.auctions.entity.ConnectedRealm;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

// Mapstruct doesn't support builder adder
@Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ConnectedRealmMapper {
    ConnectedRealmMapper INSTANCE = Mappers.getMapper(ConnectedRealmMapper.class);

    ConnectedRealm toEntity(com.radcortez.wow.auctions.api.ConnectedRealm connectedRealm, String region);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "folders", ignore = true)
    ConnectedRealm toConnectedRealm(ConnectedRealm source, @MappingTarget ConnectedRealm target);
}
