package dao;

import org.hibernate.Session;

public class SessionDAO extends DAO<entity.Session> {
	
	public SessionDAO(Session s) {
		super(s, entity.Session.class);
	}
}

