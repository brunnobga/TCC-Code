package facade;

import java.util.List;

import entity.Artifact;
import entity.Device;
import entity.Media;
import entity.MediaList;
import entity.MediaType;
import entity.Metric;
import entity.Session;
import entity.SoftwareRate;
import entity.UserRate;

public interface ModelFacade {
	
	public boolean saveOrUpdateArtifact(Artifact artifact);
	public boolean removeArtifact(Artifact artifact);
	public List<Artifact> listAllArtifats();
	public List<Artifact> findArtifact(Artifact artifact);
	
	public boolean saveOrUpdateMedia(Media media);
	public boolean removeMedia(Media media);
	public List<Media> listAllMedias();
	public List<Media> findMedia(Media media);
	
	public List<Metric> listAllMetrics();
	
	public List<Device> listAllDevices();
	
	public List<SoftwareRate> listAllSoftwareRates();
	
	public List<UserRate> listAllUserRates();
	
	public boolean saveOrUpdateSession(Session session);
	
	public boolean saveOrUpdateMediaList(MediaList mediaList);
	
	public boolean saveOrUpdateUserRate(UserRate userRate);
	
	public boolean saveOrUpdateSoftwareRate(SoftwareRate softwareRate);
	
	public MediaType findMediaTypeById(int id);
}
