package bridge;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import communication.Connection;
import communication.Monitor;
import communication.Protocol;
import dao.ArtifactDAO;

import utils.ErrorUtils;
import utils.FilterUtils;
import utils.SessionUtils;
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
import merapi.handlers.MessageHandler;
import merapi.messages.IMessage;

public class MsgHandler extends MessageHandler {

	public static final String MESSAGE = "Message";
	
	public static final String SERVICE_OPERATION = "ServiceOperation";
	
	public static final String OPERATION_MEDIA_FILTER = "OperationMediaFilter";
	
	public static final String OPERATION_START_SESSION = "OperationStartSession";
	public static final String OPERATION_DEFINE_METRIC = "OperationDefineMetric";
	public static final String OPERATION_ENABLE_RATE = "OperationEnableRate";
	public static final String OPERATION_DISABLE_RATE = "OperationDisableRate";
	public static final String OPERATION_SOFTWARE_RATE = "OperationSoftwareRate";
	
	public static final String MODEL_QUERY = "ModelQuery";

	public static final String QUERY_ARTIFACT_LIST = "QueryArtifactList";
	
	public static final String QUERY_DEVICE_LIST = "QueryDeviceList";
	
	public static final String QUERY_MEDIA_LIST = "QueryMediaList";
	public static final String QUERY_MEDIA_FIND = "QueryMediaFind";
	
	public static final String QUERY_METRIC_LIST = "QueryMetricList";
	
	public static final String QUERY_SOFTWARE_RATE_LIST = "QuerySoftwareRateList";
	
	public static final String QUERY_USER_RATE_LIST = "QueryUserRateList";
	
	public static final String QUERY_RESULT = "QueryResult";

	public static final String OPERATION_RESULT = "OperationResult";
	
	public static final String SESSION_DEVICE_MONITOR = "SessionDeviceMonitor";
	
	private ModelImplFacade mif;
	private ServiceFacade sf;
	private Monitor monitor;
	
	private Logger loggger = Logger.getLogger(this.getClass());

	public MsgHandler(Monitor monitor) {
		super(MESSAGE);
		this.monitor = monitor;
		mif = new ModelImplFacade();
		sf = new ServiceFacade();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void handleMessage(IMessage message) {
		MsgData msgData = (MsgData) message;
		ArrayList parameters = (ArrayList) msgData.getObjectData();
		msgData.setType(msgData.getSender());
		String type = (String) parameters.get(0);
		System.out.println(type);
		ModelImplFacade mif = new ModelImplFacade();
		ServiceFacade sf = new ServiceFacade();
		if (type.equals(SERVICE_OPERATION)) {
			if(parameters.get(1).equals(OPERATION_MEDIA_FILTER)) {
				ArrayList<Artifact> filterList = (ArrayList<Artifact>) parameters.get(2);
				ArrayList<Media> mediaList = (ArrayList<Media>) parameters.get(3);
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
				parameters = new ArrayList();
				parameters.add(OPERATION_RESULT);
				parameters.add(status);
				msgData.setStatus(ErrorUtils.ERR_NONE);
				msgData.clearObjectData();
				msgData.setObjectData(parameters);
			} else if(parameters.get(1).equals(OPERATION_START_SESSION)) {
				int status = -1;
				Session session = null;
				try {
					session = SessionUtils.buildSession((ArrayList)parameters.get(2));
					boolean queryResult = 
						mif.saveOrUpdateSession(session);
					if(queryResult) {
						boolean queryResultAux = false;
						int index = 0;
						for(Object o : (ArrayList) parameters.get(3)) {
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
							for(Object o : (ArrayList) parameters.get(4)) {
								Device d = (Device) o;
								Connection c = new Connection(d);
								c.start();
								monitor.addConnection(c);
							}
							status = ErrorUtils.ERR_NONE;
						} else {
							status = ErrorUtils.ERR_HIBERNATE_QUERY_SAVE;
						}
					} else {
						status = ErrorUtils.ERR_HIBERNATE_QUERY_SAVE;
					}
				} catch (Exception ex) {
					msgData.setStatus(ErrorUtils.ERR_HIBERNATE_QUERY_SAVE);
					loggger.log(Level.ERROR, ErrorUtils
							.getErrorMsg(ErrorUtils.ERR_HIBERNATE_QUERY_SAVE),
							ex);
				}
				parameters = new ArrayList();
				parameters.add(OPERATION_RESULT);
				parameters.add(OPERATION_START_SESSION);
				parameters.add(session);
				msgData.setStatus(status);
				msgData.clearObjectData();
				msgData.setObjectData(parameters);
			} else if(parameters.get(1).equals(OPERATION_DEFINE_METRIC)) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
				int status = -1, metric = ((Metric)parameters.get(2)).getType();
				parameters = new ArrayList();
				parameters.add(OPERATION_RESULT);
				parameters.add(OPERATION_DEFINE_METRIC);
				for(Connection c : monitor.getSessionDevices()) {
					status = sf.defineMetricOnDevice(c, metric);
					parameters.add(status);
				}
				msgData.setStatus(ErrorUtils.ERR_NONE);
				msgData.clearObjectData();
				msgData.setObjectData(parameters);
			} else if(parameters.get(1).equals(OPERATION_ENABLE_RATE)) {
				Session session = (Session)parameters.get(2);
				Metric metric = session.getMetric();
				ArrayList<UserRate> userRates = new ArrayList<UserRate>();
				int status = -1;
				for(Connection c : monitor.getSessionDevices()) {
					status = sf.enableRateOnDevice(c);
					if(metric.getType() == 0) { // DSIS
						Media media = (Media)parameters.get(3);
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
				
				
				
					//Remover 
					/*
					Media media = (Media)parameters.get(3);
					UserRate ur = new UserRate();
					ur.setSession(session);
					ur.setMedia(media);
					ur.setValue(4d);
					mif.saveOrUpdateUserRate(ur);
					userRates.add(ur);
					int status = Protocol.STATUS_OK;
					// Remover
					*/
					parameters = new ArrayList();
					parameters.add(OPERATION_RESULT);
					parameters.add(OPERATION_ENABLE_RATE);
					parameters.add(status);
					if(metric.getType() == 0) { // DSIS
						parameters.add(userRates);
					}
				}
				msgData.setStatus(ErrorUtils.ERR_NONE);
				msgData.clearObjectData();
				msgData.setObjectData(parameters);
			} else if(parameters.get(1).equals(OPERATION_SOFTWARE_RATE)) {
				int status = -1;
				double value = -1;
				SoftwareRate sr = null;
				try {
					Media referenceVideo = ((ArrayList<Media>)parameters.get(3)).get(0), video = ((ArrayList<Media>)parameters.get(3)).get(1);
					if(((Metric)parameters.get(2)).getType() == 10) {
						value = sf.calculateMSEFromVideo(referenceVideo.getPath(), video.getPath(), 
								referenceVideo.getWidth(), referenceVideo.getHeigth());
					} else if(((Metric)parameters.get(2)).getType() == 11) {
						value = sf.calculatePSNRFromMSE((sf.calculateMSEFromVideo(referenceVideo.getPath(), video.getPath(), 
								referenceVideo.getWidth(), referenceVideo.getHeigth())), 16);
					} else if(((Metric)parameters.get(2)).getType() == 12) {
						value = sf.calculateMSSIMFromVideo(referenceVideo.getPath(), video.getPath(), 16, 
								referenceVideo.getWidth(), referenceVideo.getHeigth());
					}
					sr = new SoftwareRate();
					sr.setMedia(video);
					sr.setReferenceMedia(referenceVideo);
					sr.setMetric((Metric)parameters.get(2));
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
					msgData.setStatus(ErrorUtils.ERR_HIBERNATE_QUERY_SAVE);
					loggger.log(Level.ERROR, ErrorUtils
							.getErrorMsg(ErrorUtils.ERR_HIBERNATE_QUERY_SAVE),
							ex);
				}
				parameters = new ArrayList();
				parameters.add(OPERATION_RESULT);
				parameters.add(OPERATION_SOFTWARE_RATE);
				parameters.add(sr);
				msgData.setStatus(status);
				msgData.clearObjectData();
				msgData.setObjectData(parameters);
			}
		} else if (type.equals(MODEL_QUERY)) {
			if (parameters.get(1).equals(QUERY_ARTIFACT_LIST)) {
				try {
					ArrayList queryResult = (ArrayList) mif.listAllArtifats();
					parameters = new ArrayList();
					parameters.add(QUERY_RESULT);
					parameters.add(QUERY_ARTIFACT_LIST);
					parameters.add(queryResult);
					msgData.setStatus(ErrorUtils.ERR_NONE);
					msgData.clearObjectData();
					msgData.setObjectData(parameters);
				} catch (Exception ex) {
					msgData.setStatus(ErrorUtils.ERR_HIBERNATE_QUERY_LIST);
					loggger.log(Level.ERROR, ErrorUtils
							.getErrorMsg(ErrorUtils.ERR_HIBERNATE_QUERY_LIST),
							ex);
				}
			} else if (parameters.get(1).equals(QUERY_MEDIA_FIND)) {
				try {
					ArrayList queryResult = (ArrayList) mif.findMedia((Media)parameters.get(2));
					parameters = new ArrayList();
					parameters.add(QUERY_RESULT);
					parameters.add(QUERY_MEDIA_FIND);
					parameters.add(queryResult);
					msgData.setStatus(ErrorUtils.ERR_NONE);
					msgData.clearObjectData();
					msgData.setObjectData(parameters);
				} catch (Exception ex) {
					msgData.setStatus(ErrorUtils.ERR_HIBERNATE_QUERY_LIST);
					loggger.log(Level.ERROR, ErrorUtils
							.getErrorMsg(ErrorUtils.ERR_HIBERNATE_QUERY_LIST),
							ex);
				}
			} else if (parameters.get(1).equals(QUERY_METRIC_LIST)) {
				try {
					if(msgData.getSender().contains("Tools")) {
						Thread.sleep(2000);
					}
					ArrayList queryResult = (ArrayList) mif.listAllMetrics();
					parameters = new ArrayList();
					parameters.add(QUERY_RESULT);
					parameters.add(QUERY_METRIC_LIST);
					parameters.add(queryResult);
					msgData.setStatus(ErrorUtils.ERR_NONE);
					msgData.clearObjectData();
					msgData.setObjectData(parameters);
				} catch (Exception ex) {
					msgData.setStatus(ErrorUtils.ERR_HIBERNATE_QUERY_LIST);
					loggger.log(Level.ERROR, ErrorUtils
							.getErrorMsg(ErrorUtils.ERR_HIBERNATE_QUERY_LIST),
							ex);
				}
			} else if (parameters.get(1).equals(QUERY_DEVICE_LIST)) {
				try {
					ArrayList queryResult = (ArrayList) mif.listAllDevices();
					parameters = new ArrayList();
					parameters.add(QUERY_RESULT);
					parameters.add(QUERY_DEVICE_LIST);
					parameters.add(queryResult);
					msgData.setStatus(ErrorUtils.ERR_NONE);
					msgData.clearObjectData();
					msgData.setObjectData(parameters);
				} catch (Exception ex) {
					msgData.setStatus(ErrorUtils.ERR_HIBERNATE_QUERY_LIST);
					loggger.log(Level.ERROR, ErrorUtils
							.getErrorMsg(ErrorUtils.ERR_HIBERNATE_QUERY_LIST),
							ex);
				}
			} else if (parameters.get(1).equals(QUERY_SOFTWARE_RATE_LIST)) {
				try {
					ArrayList queryResult = (ArrayList) mif.listAllSoftwareRates();
					parameters = new ArrayList();
					parameters.add(QUERY_RESULT);
					parameters.add(QUERY_SOFTWARE_RATE_LIST);
					parameters.add(queryResult);
					msgData.setStatus(ErrorUtils.ERR_NONE);
					msgData.clearObjectData();
					msgData.setObjectData(parameters);
				} catch (Exception ex) {
					msgData.setStatus(ErrorUtils.ERR_HIBERNATE_QUERY_LIST);
					loggger.log(Level.ERROR, ErrorUtils
							.getErrorMsg(ErrorUtils.ERR_HIBERNATE_QUERY_LIST),
							ex);
				}
			} else if (parameters.get(1).equals(QUERY_USER_RATE_LIST)) {
				try {
					ArrayList queryResult = (ArrayList) mif.listAllUserRates();
					parameters = new ArrayList();
					parameters.add(QUERY_RESULT);
					parameters.add(QUERY_USER_RATE_LIST);
					parameters.add(queryResult);
					msgData.setStatus(ErrorUtils.ERR_NONE);
					msgData.clearObjectData();
					msgData.setObjectData(parameters);
				} catch (Exception ex) {
					msgData.setStatus(ErrorUtils.ERR_HIBERNATE_QUERY_LIST);
					loggger.log(Level.ERROR, ErrorUtils
							.getErrorMsg(ErrorUtils.ERR_HIBERNATE_QUERY_LIST),
							ex);
				}
			}
		}
		try {
			msgData.send();
		} catch (Exception ex) {
			loggger.log(Level.ERROR,
					ErrorUtils.getErrorMsg(ErrorUtils.ERR_MERAPI_SEND_MESSAGE),
					ex);
		}
	}
}