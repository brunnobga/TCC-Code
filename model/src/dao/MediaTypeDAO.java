package dao;

import org.hibernate.Session;

import entity.MediaType;

public class MediaTypeDAO extends DAO<MediaType> {
	
	public MediaTypeDAO(Session s) {
		super(s, MediaType.class);
	}
}

