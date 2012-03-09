package dao;

import org.hibernate.Session;

import entity.UserRate;

public class UserRateDAO extends DAO<UserRate> {
	
	public UserRateDAO(Session s) {
		super(s, UserRate.class);
	}
}

