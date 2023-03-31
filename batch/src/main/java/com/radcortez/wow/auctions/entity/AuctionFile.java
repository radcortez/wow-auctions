package com.radcortez.wow.auctions.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auction_file_id")
    @SequenceGenerator(name = "auction_file_id", sequenceName = "auction_file_id")
    private Long id;
    private String fileName;
    @Enumerated(EnumType.STRING)
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
