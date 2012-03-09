package communication;

import java.util.ArrayList;

import bridge.MsgData;
import bridge.MsgHandler;

import entity.Device;

public class Monitor extends Thread {
	
	private ArrayList<Connection> connectionList;
	private ArrayList<Integer> connectedDeviceList;
	
	public Monitor(){	
		connectionList = new ArrayList<Connection>();
	}
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			if(connectionList.size() > 0) {
				connectedDeviceList = new ArrayList<Integer>();
				for(Connection c : connectionList) {
					if(c.getDevice().isConnected()) {
						connectedDeviceList.add(c.getDevice().getId());
					}
				}
			}
		}
	}
	
	public void addConnection(Connection c) {
		if(!connectionList.contains(c)) {
			connectionList.add(c);
		}
	}
	
	public boolean removeConnection(Connection c) {
		return connectionList.remove(c);
	}
	
	public void clearConnectionList() {
		connectionList.clear();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Connection> getSessionDevices() {
		return (ArrayList<Connection>) connectionList.clone();
	}
}
