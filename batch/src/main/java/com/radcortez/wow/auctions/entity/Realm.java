package com.radcortez.wow.auctions.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Realm implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "realmId")
    @SequenceGenerator(name = "realmId", sequenceName = "REALM_ID_SEQ", initialValue = 1, allocationSize = 1)
    private Long id;
    private String name;
    private String slug;
    private Region region;
    private boolean status;

    public void setRegion(String region) {
        this.region = Region.valueOf(region);
    }

    public String getRealmDetail() {
        return region + " " + name;
    }

    public static enum Region {
        US, EU
    }
}
