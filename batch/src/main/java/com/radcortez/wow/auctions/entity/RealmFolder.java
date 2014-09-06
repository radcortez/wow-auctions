package com.radcortez.wow.auctions.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealmFolder implements Serializable {
    @EmbeddedId
    private RealmFolderPK id;
    private String path;

    public RealmFolder(Long realmId, FolderType folderyType, String path) {
        this.id = new RealmFolderPK(realmId, folderyType);
        this.path = path;
    }

    @SuppressWarnings("JpaObjectClassSignatureInspection")
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RealmFolderPK implements Serializable {
        @Basic
        private Long realmId;
        @Enumerated
        private FolderType folderType;
    }
}
