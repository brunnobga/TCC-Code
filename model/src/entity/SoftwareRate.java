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
@Table(name = "softwarerate")
@NamedQueries({
    @NamedQuery(name = "SoftwareRate.findAll", query = "SELECT s FROM SoftwareRate s"),
    @NamedQuery(name = "SoftwareRate.findById", query = "SELECT s FROM SoftwareRate s WHERE s.id = :id"),
    @NamedQuery(name = "SoftwareRate.findByValue", query = "SELECT s FROM SoftwareRate s WHERE s.value = :value")})
public class SoftwareRate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "value")
    private Double value;
    @JoinColumn(name = "media", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Media media;
    @JoinColumn(name = "referenceMedia", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Media referenceMedia;
    @JoinColumn(name = "metric", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Metric metric;

    public SoftwareRate() {
    }

    public SoftwareRate(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Media getReferenceMedia() {
		return referenceMedia;
	}

	public void setReferenceMedia(Media referenceMedia) {
		this.referenceMedia = referenceMedia;
	}

	public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
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
        if (!(object instanceof SoftwareRate)) {
            return false;
        }
        SoftwareRate other = (SoftwareRate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SoftwareRate[id=" + id + "]";
    }

}
