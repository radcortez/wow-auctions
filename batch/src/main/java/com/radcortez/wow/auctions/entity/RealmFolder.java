package com.radcortez.wow.auctions.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@Entity
public class RealmFolder implements Serializable {
    @EmbeddedId
    private RealmFolderPK id;
    private String path;

    public RealmFolder() {}

    public RealmFolder(Long realmId, FolderType folderyType, String path) {
        this.id = new RealmFolderPK(realmId, folderyType);
        this.path = path;
    }

    public RealmFolderPK getId() {
        return id;
    }

    public void setId(RealmFolderPK id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        RealmFolder that = (RealmFolder) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "RealmFolder{" +
               "id=" + id +
               ", path='" + path + '\'' +
               '}';
    }

    @SuppressWarnings("JpaObjectClassSignatureInspection")
    @Embeddable
    public static class RealmFolderPK implements Serializable {
        @Basic
        private Long realmId;
        @Enumerated
        private FolderType folderType;

        public RealmFolderPK() {}

        public RealmFolderPK(Long realmId, FolderType folderType) {
            this.realmId = realmId;
            this.folderType = folderType;
        }

        public Long getRealmId() {
            return realmId;
        }

        public void setRealmId(Long realmId) {
            this.realmId = realmId;
        }

        public FolderType getFolderType() {
            return folderType;
        }

        public void setFolderType(FolderType folderType) {
            this.folderType = folderType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (o == null || getClass() != o.getClass()) { return false; }

            RealmFolderPK that = (RealmFolderPK) o;

            return folderType == that.folderType && realmId.equals(that.realmId);
        }

        @Override
        public int hashCode() {
            int result = realmId.hashCode();
            result = 31 * result + folderType.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "RealmFolderPK{" +
                   "realmId=" + realmId +
                   ", folderType=" + folderType +
                   '}';
        }
    }
}
