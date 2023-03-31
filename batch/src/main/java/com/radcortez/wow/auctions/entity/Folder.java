package com.radcortez.wow.auctions.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.Basic;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = "connectedRealm")

@Entity
public class Folder implements Serializable {
    @EmbeddedId
    private FolderPK id;
    private String path;

    @ManyToOne
    @MapsId("connectedRealmId")
    private ConnectedRealm connectedRealm;

    @Deprecated
    public Folder(final String id, final FolderType folderType, final String path) {
        this.id = new FolderPK(id, folderType);
        this.path = path;
    }

    @Builder
    public Folder(final ConnectedRealm connectedRealm, final FolderType folderType, final String path) {
        this.id = new FolderPK(connectedRealm.getId(), folderType);
        this.connectedRealm = connectedRealm;
        this.path = path;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class FolderPK implements Serializable {
        @Basic
        private String connectedRealmId;
        @Enumerated(EnumType.STRING)
        private FolderType folderType;
    }
}
