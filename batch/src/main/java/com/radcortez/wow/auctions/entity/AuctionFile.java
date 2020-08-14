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
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    @OneToMany(mappedBy = "auctionFile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Auction> auctions = new HashSet<>();
    @ManyToOne(optional = false)
    private ConnectedRealm connectedRealm;

    @Builder
    public AuctionFile(
        final String fileName,
        final FileStatus fileStatus,
        final Long timestamp,
        final Set<Auction> auctions,
        final ConnectedRealm connectedRealm) {

        this.fileName = fileName;
        this.fileStatus = fileStatus;
        this.timestamp = timestamp;
        this.auctions = Optional.ofNullable(auctions).map(HashSet::new).orElse(new HashSet<>());
        this.connectedRealm = connectedRealm;
    }

    public AuctionFile create() {
        persist();
        return this;
    }
}
