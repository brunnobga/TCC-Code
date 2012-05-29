package bridge;

import java.util.ArrayList;
import java.util.Calendar;

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
				System.out.println("OK!");
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
					System.out.println("OK!!");
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return session;
	}
	
	public static ArrayList<Integer> operationDefineMetric(int metricArg){
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		int status = -1, metric = metricArg;
		ArrayList<Integer> result = new ArrayList();
		for(Connection c : monitor.getSessionDevices()) {
			status = sf.defineMetricOnDevice(c, metric);
			result.add(status);
		}
		return result;
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
	
	public static SoftwareRate operationSoftwareRate(Metric metric, Media reference, Media media){
		double value = -1;
		SoftwareRate sr = null;
		try {
			Media referenceVideo = reference, video = media;
			if(metric.getType() == 10) { //MSE
				value = sf.calculateMSEFromVideo(referenceVideo.getPath(), video.getPath(), 
						referenceVideo.getWidth(), referenceVideo.getHeigth());
			} else if(metric.getType() == 11) { //PSNR
				value = sf.calculatePSNRFromMSE((sf.calculateMSEFromVideo(referenceVideo.getPath(), video.getPath(), 
						referenceVideo.getWidth(), referenceVideo.getHeigth())), 16);
			} else if(metric.getType() == 12) { //MSSIM
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sr;
	}




	public static ArrayList<Artifact> queryArtifactList(){
		return (ArrayList<Artifact>) mif.listAllArtifats();
	}
	
	public static ArrayList<Media> queryMediaList(Media media){
		return (ArrayList<Media>) mif.findMedia(media == null ? new Media() : media);
	}
	
	public static ArrayList<Metric> queryMetricList(){
		return (ArrayList<Metric>) mif.listAllMetrics();
	}
	
	public static ArrayList<Device> queryDeviceList(){
		return (ArrayList<Device>) mif.listAllDevices();
	}
	
	public static ArrayList<SoftwareRate> querySoftwareRateList(){
		return (ArrayList<SoftwareRate>) mif.listAllSoftwareRates();
	}
	
	public static ArrayList<UserRate> queryUserRateList(){
		return (ArrayList<UserRate>) mif.listAllUserRates();
	}
	
	public static ArrayList<Session> querySessionList(){
		return (ArrayList<Session>) mif.listAllSessions();
	}
	
	public static Media queryMediaById(int pk){
		return mif.findMediaById(pk);
	}
	
	public static void SaveOrUpdateSoftRate(SoftwareRate sr){
		mif.saveOrUpdateSoftwareRate(sr);
	}
	
	public static void SaveOrUpdateUserRate(UserRate ur){
		mif.saveOrUpdateUserRate(ur);
	}
	
	public static void SaveOrUpdateMedia(Media m){
		mif.saveOrUpdateMedia(m);
	}
}
