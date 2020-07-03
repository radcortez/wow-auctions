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
public class RealmFolder implements Serializable {
    @EmbeddedId
    private RealmFolderPK id;
    private String path;

    public RealmFolder(final Long id, final FolderType folderType, final String path) {
        this(new RealmFolderPK(id, folderType), path);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class RealmFolderPK implements Serializable {
        @Basic
        private Long realmId;
        @Enumerated
        private FolderType folderType;
    }
}
