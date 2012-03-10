package bridge;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import utils.ErrorUtils;
import utils.FilterUtils;
import utils.SessionUtils;

import communication.Connection;
import communication.Monitor;
import communication.Protocol;

import entity.Artifact;
import entity.Device;
import entity.Media;
import entity.MediaList;
import entity.Metric;
import entity.Session;
import entity.SoftwareRate;
import entity.UserRate;
import facade.ModelImplFacade;
import facade.ServiceFacade;

public class ServiceBridge {
	private static ModelImplFacade mif;
	private static ServiceFacade sf;
	private static Monitor monitor;

	public static void init(Monitor m){
		mif = new ModelImplFacade();
		sf = new ServiceFacade();
		monitor = m;
	}

	public static int operationMediaFilter(ArrayList<Artifact> filterList, ArrayList<Media> mediaList){
		int status = -1;
		for(Media media : mediaList) {
			String srcPath = media.getPath(), dstPath = "";
			for(Artifact artifact : filterList) {
				ArrayList filterParamList = artifact.getParameters();
				if(artifact.getType() == 1) {
					dstPath = FilterUtils.buildMediaDstPath(srcPath, "Block_S_" + (Integer)filterParamList.get(0));
					status = sf.blockFilterVideo(srcPath, dstPath, (Integer)filterParamList.get(0));
				} else if(artifact.getType() == 2) {
					String aux = "BoxBlur_VR_" + (Integer)filterParamList.get(0)
					+ "_HR_" + (Integer)filterParamList.get(1);
					if((Boolean)filterParamList.get(2)) {
						aux += "_PMA_I_" + (Integer)filterParamList.get(3);
					} else {
						aux += "_NoPMA_I_" + (Integer)filterParamList.get(3);
					}
					dstPath = FilterUtils.buildMediaDstPath(srcPath, aux);
					status = sf.boxBlurFilterVideo(srcPath, dstPath, (Integer)filterParamList.get(0), 
							(Integer)filterParamList.get(1), (Boolean)filterParamList.get(2), (Integer)filterParamList.get(3));
				} else if(artifact.getType() == 3) {
					String aux = "Convolve_KS_" + (Integer)filterParamList.get(0)
					+ "x" + (Integer)filterParamList.get(1);
					if((Boolean)filterParamList.get(3)) {
						aux += "_PMA";
					} else {
						aux += "_NoPMA";
					}
					if((Boolean)filterParamList.get(4)) {
						aux += "_AE";
					} else {
						aux += "_NoAE";
					}
					switch((Integer)filterParamList.get(5)) {
					case FilterUtils.CONVOLVE_ZERO_EDGES :
						aux += "_EA_ZE";
						break;
					case FilterUtils.CONVOLVE_CLAMP_EDGES :
						aux += "_EA_CE";
						break;
					case FilterUtils.CONVOLVE_WRAP_EDGES :
						aux += "_EA_WE";
						break;
					}
					dstPath = FilterUtils.buildMediaDstPath(srcPath, aux);
					status = sf.convolveFilterVideo(srcPath, dstPath, (Integer)filterParamList.get(0), 
							(Integer)filterParamList.get(1), (float[])filterParamList.get(2), (Boolean)filterParamList.get(3), 
							(Boolean)filterParamList.get(4), (Integer)filterParamList.get(5));
				}
				srcPath = dstPath;
				Media m = new Media();
				m.setTitle(media.getTitle());
				m.setArtifact(mif.findArtifact(artifact).get(0));
				m.setDate(Calendar.getInstance().getTime());
				m.setDescription("Vídeo degrado");
				m.setFormat("mp4");
				m.setHeigth(media.getHeigth());
				m.setLength(media.getLength());
				m.setNote(media.getNote());
				m.setPath(srcPath);
				m.setSize(media.getSize());
				m.setWidth(media.getWidth());
				m.setType(mif.findMediaTypeById(2));
				mif.saveOrUpdateMedia(m);
			}
		}
		return status;
	}
	
	public static Session operationStartSession(ArrayList sessionArgs, ArrayList medias, ArrayList devices){
		Session session = null;
		try {
			session = SessionUtils.buildSession(sessionArgs);
			boolean queryResult = 
				mif.saveOrUpdateSession(session);
			if(queryResult) {
				boolean queryResultAux = false;
				int index = 0;
				for(Object o : medias) {
					Media m = (Media)o;
					MediaList ml = new MediaList();
					ml.setSession(session);
					ml.setMedia(m);
					ml.setPosition(index++);
					queryResultAux = mif.saveOrUpdateMediaList(ml);
					if(queryResultAux == false) {
						queryResult = false;
					}
				}
				if(queryResult) {
					for(Object o : devices) {
						Device d = (Device) o;
						Connection c = new Connection(d);
						c.start();
						monitor.addConnection(c);
					}
				} else {
				}
			} else {
			}
		} catch (Exception ex) {}
		return session;
	}
	
	public static ArrayList<Integer> operationDefineMetric(int metricArg){
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		int status = -1, metric = metricArg;
		ArrayList<Integer> parameters = new ArrayList();
		for(Connection c : monitor.getSessionDevices()) {
			status = sf.defineMetricOnDevice(c, metric);
			parameters.add(status);
		}
		return parameters;
	}
	
	public static ArrayList<UserRate> operatioEnableRate(Session sessionArg, Media mediaArg){
		Session session = sessionArg;
		Metric metric = session.getMetric();
		ArrayList<UserRate> userRates = new ArrayList<UserRate>();
		int status = -1;
		for(Connection c : monitor.getSessionDevices()) {
			status = sf.enableRateOnDevice(c);
			if(metric.getType() == 0) { // DSIS
				Media media = mediaArg;
				int[][] receivedRates = sf.getReceivedRatesFromDevice(c);
				while(receivedRates == null) {
					receivedRates = sf.getReceivedRatesFromDevice(c);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
				status = sf.disableRateOnDevice(c);
				if(status == Protocol.STATUS_OK) { 
					for(int i = 0; i < receivedRates.length; i++) {
						UserRate ur = new UserRate();
						ur.setSession(session);
						ur.setMedia(media);
						ur.setValue((double)receivedRates[i][0]);
						mif.saveOrUpdateUserRate(ur);
						userRates.add(ur);
					}
				}
			}
		}
		return userRates;
	} 
	
	public static ArrayList<SoftwareRate> operationSoftwareRate(Metric metric, ArrayList<Media> medias){
		int status = -1;
		double value = -1;
		SoftwareRate sr = null;
		try {
			Media referenceVideo = medias.get(0), video = medias.get(1);
			if(metric.getType() == 10) {
				value = sf.calculateMSEFromVideo(referenceVideo.getPath(), video.getPath(), 
						referenceVideo.getWidth(), referenceVideo.getHeigth());
			} else if(metric.getType() == 11) {
				value = sf.calculatePSNRFromMSE((sf.calculateMSEFromVideo(referenceVideo.getPath(), video.getPath(), 
						referenceVideo.getWidth(), referenceVideo.getHeigth())), 16);
			} else if(metric.getType() == 12) {
				value = sf.calculateMSSIMFromVideo(referenceVideo.getPath(), video.getPath(), 16, 
						referenceVideo.getWidth(), referenceVideo.getHeigth());
			}
			sr = new SoftwareRate();
			sr.setMedia(video);
			sr.setReferenceMedia(referenceVideo);
			sr.setMetric(metric);
			sr.setValue(Double.valueOf(value / 50d));
			boolean queryResult = 
				mif.saveOrUpdateSoftwareRate(sr);
			if(queryResult) {
				status = ErrorUtils.ERR_NONE;
			} else {
				status = ErrorUtils.ERR_HIBERNATE_QUERY_SAVE;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		ArrayList<SoftwareRate> parameters = new ArrayList();
		parameters.add(sr);
		return parameters;
	}




	/*if (parameters.get(1).equals(QUERY_ARTIFACT_LIST)) {
		ArrayList queryResult = (ArrayList) mif.listAllArtifats();
	} else if (parameters.get(1).equals(QUERY_MEDIA_FIND)) {
		ArrayList queryResult = (ArrayList) mif.findMedia((Media)parameters.get(2));
	} else if (parameters.get(1).equals(QUERY_METRIC_LIST)) {
		ArrayList queryResult = (ArrayList) mif.listAllMetrics();
	} else if (parameters.get(1).equals(QUERY_DEVICE_LIST)) {
		ArrayList queryResult = (ArrayList) mif.listAllDevices();
	} else if (parameters.get(1).equals(QUERY_SOFTWARE_RATE_LIST)) {
		ArrayList queryResult = (ArrayList) mif.listAllSoftwareRates();
	} else if (parameters.get(1).equals(QUERY_USER_RATE_LIST)) {
		ArrayList queryResult = (ArrayList) mif.listAllUserRates();
	}*/
}
