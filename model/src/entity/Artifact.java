/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author EvandrO
 */
@Entity
@Table(name = "artifact")
@NamedQueries({
    @NamedQuery(name = "Artifact.findAll", query = "SELECT a FROM Artifact a"),
    @NamedQuery(name = "Artifact.findById", query = "SELECT a FROM Artifact a WHERE a.id = :id"),
    @NamedQuery(name = "Artifact.findByName", query = "SELECT a FROM Artifact a WHERE a.name = :name"),
    @NamedQuery(name = "Artifact.findByType", query = "SELECT a FROM Artifact a WHERE a.type = :type")})
public class Artifact implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private Integer type;
    @Lob
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "artifact")
    private List<Media> mediaList;
    @Transient
    private ArrayList parameters;
    
    public Artifact() {
    }

    public Artifact(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Media> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<Media> mediaList) {
        this.mediaList = mediaList;
    }
    
    public ArrayList getParameters() {
		return parameters;
	}

	public void setParameters(ArrayList parameters) {
		this.parameters = parameters;
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
        if (!(object instanceof Artifact)) {
            return false;
        }
        Artifact other = (Artifact) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Artifact[id=" + id + "]";
    }

}
