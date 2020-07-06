package com.radcortez.wow.auctions.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ConnectedRealmFolder implements Serializable {
    @EmbeddedId
    private ConnectedRealmFolderPK id;
    private String path;

    public ConnectedRealmFolder(final String id, final FolderType folderType, final String path) {
        this.id = new ConnectedRealmFolderPK(id, folderType);
        this.path = path;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class ConnectedRealmFolderPK implements Serializable {
        @Basic
        private String connectedRealmId;
        @Enumerated
        private FolderType folderType;
    }
}
