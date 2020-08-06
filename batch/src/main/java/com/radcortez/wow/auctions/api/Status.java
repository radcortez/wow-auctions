package com.radcortez.wow.auctions.api;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Status {
    private Type type;
    private String name;

    public enum Type {
        UP, DOWN
    }
}
