package dao;

import org.hibernate.Session;

import entity.SoftwareRate;

public class SoftwareRateDAO extends DAO<SoftwareRate> {
	
	public SoftwareRateDAO(Session s) {
		super(s, SoftwareRate.class);
	}
}

