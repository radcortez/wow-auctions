package com.radcortez.wow.auctions.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.json.bind.annotation.JsonbProperty;
import java.util.List;

@NoArgsConstructor
@Data
public class ConnectedRealms {
    @JsonbProperty("connected_realms")
    private List<Location> connectedRealms;
}
