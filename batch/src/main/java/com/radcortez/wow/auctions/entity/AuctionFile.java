package com.radcortez.wow.auctions.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author Roberto Cortez
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = "connectedRealm")

@Entity
public class AuctionFile extends PanacheEntityBase {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private String fileName;
    private FileStatus fileStatus;
    private Long timestamp;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private ConnectedRealm connectedRealm;

    @Builder
    public AuctionFile(
        final String fileName,
        final FileStatus fileStatus,
        final ConnectedRealm connectedRealm,
        final Long timestamp) {

        this.fileName = fileName;
        this.fileStatus = fileStatus;
        this.connectedRealm = connectedRealm;
        this.timestamp = timestamp;
    }

    public AuctionFile create() {
        persist();
        return this;
    }
}
