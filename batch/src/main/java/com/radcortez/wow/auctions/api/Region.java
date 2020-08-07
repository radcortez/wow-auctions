package com.radcortez.wow.auctions.api;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Region {
    private Integer id;
    private String name;
    private Location key;
}
