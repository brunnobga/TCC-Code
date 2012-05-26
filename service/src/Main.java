

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
		//PropertyConfigurator.configure("log4j.properties");
		
		
		m = new Monitor();
		m.start();
		ServiceBridge.init(m);
		//new MsgHandler(m);
		
		Device d = ServiceBridge.queryDeviceList().get(0);
		Connection c = new Connection(d);
		c.start();
		m.addConnection(c);
		ServiceBridge.operationDefineMetric(0);
		System.out.println("Fim!");
		
		System.exit(0);
	}
	
	public Monitor getMonitor() {
		return m;
	}
}
