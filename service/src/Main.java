

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import communication.Connection;
import communication.Monitor;
import communication.Operations;

import entity.Device;
import facade.ModelImplFacade;
import bridge.MsgHandler;
import bridge.ServiceBridge;
import merapi.Bridge;


public class Main {

	/**
	 * @param args
	 */
	
	private static Monitor m;
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		
		
		m = new Monitor();
		m.start();
		ServiceBridge.init(m);
		//new MsgHandler(m);
		
		//System.out.println("Numero de objetos em mydb.Artifact: " + ServiceBridge.queryArtifactList().size());
		System.out.println("Numero de objetos em mydb.Metric: " + ServiceBridge.queryMetricList().size());
		//System.out.println("Numero de objetos em mydb.SoftwareRate: " + ServiceBridge.querySoftwareRateList().size());
		//System.out.println("Numero de objetos em mydb.Media: " + ServiceBridge.queryMediaList(null).size());
		System.out.println("Fim!");
		
		System.exit(0);
	}
	
	public Monitor getMonitor() {
		return m;
	}
}
