package dao;

import org.hibernate.Session;

import entity.MediaList;

public class MediaListDAO extends DAO<MediaList> {
	
	public MediaListDAO(Session s) {
		super(s, MediaList.class);
	}
}

