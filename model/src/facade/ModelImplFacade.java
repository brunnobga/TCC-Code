package facade;
import java.util.List;

import org.hibernate.Session;

import utils.HibernateUtil;
import dao.ArtifactDAO;
import dao.DeviceDAO;
import dao.MediaDAO;
import dao.MediaListDAO;
import dao.MediaTypeDAO;
import dao.MetricDAO;
import dao.SessionDAO;
import dao.SoftwareRateDAO;
import dao.UserRateDAO;
import entity.Artifact;
import entity.Device;
import entity.Media;
import entity.MediaList;
import entity.MediaType;
import entity.Metric;
import entity.SoftwareRate;
import entity.UserRate;


public class ModelImplFacade implements ModelFacade {
	
	public static HibernateUtil hibernateUtil;
	
	public ModelImplFacade() {
	}
	
	@SuppressWarnings("static-access")
	private Session openSession() {
		return hibernateUtil.openSession();
	}
	
	@SuppressWarnings("static-access")
	public Session currentSession() {
		return hibernateUtil.openSession(); 
	}
	
	@SuppressWarnings("static-access")
	private void closeSession() {
		hibernateUtil.closeCurrentSession();
	}
	
	public boolean saveOrUpdateArtifact(Artifact artifact) {
		return new ArtifactDAO(openSession()).saveOrUpdate(artifact);
	}
	
	public boolean removeArtifact(Artifact artifact) {
		return new ArtifactDAO(openSession()).remove(artifact);
	}
	
	public List<Artifact> listAllArtifats() {
		return new ArtifactDAO(openSession()).list();
	}
	
	public List<Artifact> findArtifact(Artifact artifact) {
		return new ArtifactDAO(openSession()).findArtifact(artifact);
	}
	
	public boolean saveOrUpdateMedia(Media media) {
		return new MediaDAO(openSession()).saveOrUpdate(media);
	}
	
	public boolean removeMedia(Media media) {
		return new MediaDAO(openSession()).remove(media);
	}
	
	public List<Media> listAllMedias() {
		return new MediaDAO(openSession()).list();
	}
	
	public List<Media> findMedia(Media media) {
		return new MediaDAO(openSession()).findMedia(media);
	}
	
	public List<Metric> listAllMetrics() {
		return new MetricDAO(openSession()).list();
	}
	
	public List<Device> listAllDevices() {
		return new DeviceDAO(openSession()).list();
	}
	
	public List<SoftwareRate> listAllSoftwareRates() {
		return new SoftwareRateDAO(openSession()).list();
	}
	
	public List<UserRate> listAllUserRates() {
		return new UserRateDAO(openSession()).list();
	}
	
	public List<entity.Session> listAllSessions(){
		return new SessionDAO(openSession()).list();
	}
	
	public boolean saveOrUpdateSession(entity.Session session) {
		return new SessionDAO(openSession()).saveOrUpdate(session);
	}
	
	public boolean saveOrUpdateMediaList(MediaList mediaList) {
		return new MediaListDAO(openSession()).saveOrUpdate(mediaList);
	}
	
	public boolean saveOrUpdateUserRate(UserRate userRate) {
		return new UserRateDAO(openSession()).saveOrUpdate(userRate);
	}

	public boolean saveOrUpdateSoftwareRate(SoftwareRate softwareRate) {
		return new SoftwareRateDAO(openSession()).saveOrUpdate(softwareRate);
	}
	
	public MediaType findMediaTypeById(int value) {
		return new MediaTypeDAO(openSession()).findById(value);
	}
}
