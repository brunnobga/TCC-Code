package dao;

import org.hibernate.Session;

import entity.Device;

public class DeviceDAO extends DAO<Device> {
	
	private Session s;
	
	public DeviceDAO(Session s) {
		super(s, Device.class);
		this.s = s;
	}
}
