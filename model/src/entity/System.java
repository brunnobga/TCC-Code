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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author EvandrO
 */
@Entity
@Table(name = "system")
@NamedQueries({
    @NamedQuery(name = "System.findAll", query = "SELECT s FROM System s"),
    @NamedQuery(name = "System.findById", query = "SELECT s FROM System s WHERE s.id = :id"),
    @NamedQuery(name = "System.findByLocale", query = "SELECT s FROM System s WHERE s.locale = :locale"),
    @NamedQuery(name = "System.findByStyle", query = "SELECT s FROM System s WHERE s.style = :style")})
public class System implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "locale")
    private String locale;
    @Column(name = "style")
    private Integer style;

    public System() {
    }

    public System(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Integer getStyle() {
        return style;
    }

    public void setStyle(Integer style) {
        this.style = style;
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
        if (!(object instanceof System)) {
            return false;
        }
        System other = (System) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.System[id=" + id + "]";
    }

}
