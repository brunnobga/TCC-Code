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
@Table(name = "session")
@NamedQueries({
    @NamedQuery(name = "Session.findAll", query = "SELECT s FROM Session s"),
    @NamedQuery(name = "Session.findById", query = "SELECT s FROM Session s WHERE s.id = :id"),
    @NamedQuery(name = "Session.findByStartDate", query = "SELECT s FROM Session s WHERE s.startDate = :startDate"),
    @NamedQuery(name = "Session.findByEndDate", query = "SELECT s FROM Session s WHERE s.endDate = :endDate"),
    @NamedQuery(name = "Session.findBySpectorsCount", query = "SELECT s FROM Session s WHERE s.spectorsCount = :spectorsCount"),
    @NamedQuery(name = "Session.findByType", query = "SELECT s FROM Session s WHERE s.type = :type"),
    @NamedQuery(name = "Session.findByState", query = "SELECT s FROM Session s WHERE s.state = :state"),
    @NamedQuery(name = "Session.findByGradeOptions", query = "SELECT s FROM Session s WHERE s.gradeOptions = :gradeOptions"),
    @NamedQuery(name = "Session.findByTitle", query = "SELECT s FROM Session s WHERE s.title = :title")})
public class Session implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "startDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "endDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "spectorsCount")
    private Integer spectorsCount;
    @Column(name = "type")
    private Integer type;
    @Column(name = "state")
    private Integer state;
    @Column(name = "gradeOptions")
    private String gradeOptions;
    @Column(name = "title")
    private String title;
    @Lob
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "session")
    private List<UserList> userList;
    @JoinColumn(name = "metric", referencedColumnName = "id")
    @ManyToOne
    private Metric metric;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "session")
    private List<UserRate> userRateList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "session")
    private List<MediaList> mediaList;

    public Session() {
    }

    public Session(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getSpectorsCount() {
        return spectorsCount;
    }

    public void setSpectorsCount(Integer spectorsCount) {
        this.spectorsCount = spectorsCount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getGradeOptions() {
        return gradeOptions;
    }

    public void setGradeOptions(String gradeOptions) {
        this.gradeOptions = gradeOptions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<UserList> getUserList() {
        return userList;
    }

    public void setUserList(List<UserList> userList) {
        this.userList = userList;
    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Session)) {
            return false;
        }
        Session other = (Session) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Session[id=" + id + "]";
    }

}
