package com.radcortez.wow.auctions.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ConnectedRealm {
    private String id;
    private List<Realm> realms;
}
