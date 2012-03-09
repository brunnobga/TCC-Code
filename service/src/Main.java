

import communication.Connection;
import communication.Monitor;
import communication.Operations;

import entity.Device;
import facade.ModelImplFacade;
import bridge.MsgHandler;
import merapi.Bridge;


public class Main {

	/**
	 * @param args
	 */
	
	private static Monitor m;
	
	public static void main(String[] args) {
		Bridge.open();
		m = new Monitor();
		m.start();
		new MsgHandler(m);
	}
	
	public Monitor getMonitor() {
		return m;
	}
}
