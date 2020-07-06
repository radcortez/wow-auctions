package com.radcortez.wow.auctions.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@Data
@EqualsAndHashCode(of = "id")

@Entity
@NamedQueries({
      @NamedQuery(name = "AuctionFile.exists",
                  query = "SELECT COUNT(af) FROM AuctionFile af " +
                          "WHERE af.url = :url AND af.lastModified = :lastModified"),
      @NamedQuery(name = "AuctionFile.findByRealmAndFileStatus",
                  query = "SELECT af FROM AuctionFile af " +
                          "WHERE af.realm.id = :id AND af.fileStatus = :fileStatus ORDER BY af.id"),
})
public class AuctionFile implements Serializable {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private String url;
    private Long lastModified;
    private String fileName;
    private FileStatus fileStatus;

    @ManyToOne
    private Realm realm;
}
