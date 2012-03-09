/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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

/**
 *
 * @author EvandrO
 */
@Entity
@Table(name = "metric")
@NamedQueries({
    @NamedQuery(name = "Metric.findAll", query = "SELECT m FROM Metric m"),
    @NamedQuery(name = "Metric.findById", query = "SELECT m FROM Metric m WHERE m.id = :id"),
    @NamedQuery(name = "Metric.findByName", query = "SELECT m FROM Metric m WHERE m.name = :name")})
public class Metric implements Serializable {
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "metric")
    private List<SoftwareRate> softwareRateList;
    @OneToMany(mappedBy = "metric")
    private List<Session> sessionList;

    public Metric() {
    }

    public Metric(Integer id) {
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

    public List<SoftwareRate> getSoftwareRateList() {
        return softwareRateList;
    }

    public void setSoftwareRateList(List<SoftwareRate> softwareRateList) {
        this.softwareRateList = softwareRateList;
    }

    public List<Session> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<Session> sessionList) {
        this.sessionList = sessionList;
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
        if (!(object instanceof Metric)) {
            return false;
        }
        Metric other = (Metric) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Metric[id=" + id + "]";
    }

}
