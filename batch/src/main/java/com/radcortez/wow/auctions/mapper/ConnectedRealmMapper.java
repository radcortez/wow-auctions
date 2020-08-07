package com.radcortez.wow.auctions.mapper;

import com.radcortez.wow.auctions.entity.ConnectedRealm;
import com.radcortez.wow.auctions.entity.Realm;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    builder = @Builder(disableBuilder = true) // TODO - Figure out if we should use builder or not
)
public interface ConnectedRealmMapper {
    ConnectedRealmMapper INSTANCE = Mappers.getMapper(ConnectedRealmMapper.class);

    ConnectedRealm toEntity(com.radcortez.wow.auctions.api.ConnectedRealm connectedRealm, String region);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "folders", ignore = true)
    ConnectedRealm toEntity(ConnectedRealm source, @MappingTarget ConnectedRealm target);

    // TODO - This is only here due to https://github.com/mapstruct/mapstruct/issues/1477
    Realm realm(com.radcortez.wow.auctions.api.Realm realm);
}
