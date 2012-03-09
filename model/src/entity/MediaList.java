/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author EvandrO
 */
@Entity
@Table(name = "medialist")
@NamedQueries({
    @NamedQuery(name = "MediaList.findAll", query = "SELECT m FROM MediaList m"),
    @NamedQuery(name = "MediaList.findById", query = "SELECT m FROM MediaList m WHERE m.id = :id"),
    @NamedQuery(name = "MediaList.findByPosition", query = "SELECT m FROM MediaList m WHERE m.position = :position")})
public class MediaList implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "position")
    private Integer position;
    @JoinColumn(name = "media", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Media media;
    @JoinColumn(name = "session", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Session session;

    public MediaList() {
    }

    public MediaList(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
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
        if (!(object instanceof MediaList)) {
            return false;
        }
        MediaList other = (MediaList) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.MediaList[id=" + id + "]";
    }

}
