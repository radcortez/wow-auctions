package com.radcortez.wow.auctions.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
        @Enumerated
        private FolderType folderType;
    }
}
