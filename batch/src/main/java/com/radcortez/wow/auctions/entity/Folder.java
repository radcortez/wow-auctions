package com.radcortez.wow.auctions.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
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
