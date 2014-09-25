package com.radcortez.wow.auctions.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Roberto Cortez
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQueries({
      @NamedQuery(name = "AuctionFile.exists",
                  query = "SELECT COUNT(af) FROM AuctionFile af " +
                          "WHERE af.url = :url AND af.lastModified = :lastModified"),
      @NamedQuery(name = "AuctionFile.findByRealmAndFileStatus",
                  query = "SELECT af FROM AuctionFile af " +
                          "WHERE af.realm.id = :id AND af.fileStatus = :fileStatus ORDER BY af.id"),
})
public class AuctionFile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auctionFileId")
    @SequenceGenerator(name = "auctionFileId",
                       sequenceName = "AUCTIONFILE_ID_SEQ",
                       initialValue = 1,
                       allocationSize = 1)
    private Long id;
    private String url;
    private Long lastModified;
    private String fileName;
    private FileStatus fileStatus;

    @ManyToOne
    private Realm realm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        AuctionFile that = (AuctionFile) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "AuctionFile{" +
               "id=" + id +
               ", url='" + url + '\'' +
               ", lastModified=" + lastModified +
               ", fileName='" + fileName + '\'' +
               ", fileStatus=" + fileStatus +
               ", realm=" + realm +
               '}';
    }
}
