package com.radcortez.wow.auctions.api;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Realm {
    private String id;
    private String name;
    private String slug;
}
