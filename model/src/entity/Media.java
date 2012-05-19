/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author EvandrO
 */
@Entity
@Table(name = "Media")
@NamedQueries({
    @NamedQuery(name = "Media.findAll", query = "SELECT m FROM Media m"),
    @NamedQuery(name = "Media.findById", query = "SELECT m FROM Media m WHERE m.id = :id"),
    @NamedQuery(name = "Media.findByTitle", query = "SELECT m FROM Media m WHERE m.title = :title"),
    @NamedQuery(name = "Media.findBySize", query = "SELECT m FROM Media m WHERE m.size = :size"),
    @NamedQuery(name = "Media.findByLength", query = "SELECT m FROM Media m WHERE m.length = :length"),
    @NamedQuery(name = "Media.findByFormat", query = "SELECT m FROM Media m WHERE m.format = :format"),
    @NamedQuery(name = "Media.findByWidth", query = "SELECT m FROM Media m WHERE m.width = :width"),
    @NamedQuery(name = "Media.findByHeigth", query = "SELECT m FROM Media m WHERE m.heigth = :heigth"),
    @NamedQuery(name = "Media.findByDate", query = "SELECT m FROM Media m WHERE m.date = :date")})
public class Media implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "size")
    private long size;
    @Column(name = "length")
    @Temporal(TemporalType.TIME)
    private Date length;
    @Lob
    @Column(name = "path")
    private String path;
    @Column(name = "format")
    private String format;
    @Column(name = "width")
    private Integer width;
    @Column(name = "heigth")
    private Integer heigth;
    @Lob
    @Column(name = "note")
    private String note;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Lob
    @Column(name = "description")
    private String description;
    @Lob
    @Column(name = "tags")
    private String tags;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "media")
    private List<SoftwareRate> softwareRateList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "media")
    private List<UserRate> userRateList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "media")
    private List<MediaList> mediaList;
    @JoinColumn(name = "artifact", referencedColumnName = "id")
    @ManyToOne
    private Artifact artifact;
    @JoinColumn(name = "type", referencedColumnName = "id")
    @ManyToOne
    private MediaType type;

    public Media() {
    	this.heigth = 0;
    	this.width = 0;
    	this.size = 0;
    }

    public Media(Integer id) {
        this.id = id;
    }
    
    public Media(Media m){
    	this.size = m.getSize();
    	this.length = m.getLength();
    	this.format = m.getFormat();
    	this.width = m.getWidth();
    	this.heigth = m.getHeigth();
    	this.type = m.getType();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getLength() {
        return length;
    }

    public void setLength(Date length) {
        this.length = length;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeigth() {
        return heigth;
    }

    public void setHeigth(Integer heigth) {
        this.heigth = heigth;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<SoftwareRate> getSoftwareRateList() {
        return softwareRateList;
    }

    public void setSoftwareRateList(List<SoftwareRate> softwareRateList) {
        this.softwareRateList = softwareRateList;
    }

    public List<UserRate> getUserRateList() {
        return userRateList;
    }

    public void setUserRateList(List<UserRate> userRateList) {
        this.userRateList = userRateList;
    }

    public List<MediaList> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<MediaList> mediaList) {
        this.mediaList = mediaList;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Media)) {
            return false;
        }
        Media other = (Media) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Media[id=" + id + "]";
    }

}
