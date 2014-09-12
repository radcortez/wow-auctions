package com.radcortez.wow.auctions.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQueries({
      @NamedQuery(name = "AuctionFile.findByRealmRegionAndFileStatus",
                  query = "SELECT af FROM AuctionFile af " +
                          "WHERE af.realm.region = :region AND af.fileStatus = :fileStatus")
})
public class AuctionFile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auctionFileId")
    @SequenceGenerator(name = "auctionFileId",
                       sequenceName = "AUCTIONFILE_ID_SEQ",
                       initialValue = 1,
                       allocationSize = 1)
    private Long id;
    private String url;
    private Long lastModified;
    private String fileName;
    private FileStatus fileStatus;

    @ManyToOne
    private Realm realm;

    public AuctionFile() {
        this.fileName = "auctions." + lastModified + ".json";
    }
}
