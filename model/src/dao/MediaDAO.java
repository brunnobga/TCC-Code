package dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import entity.Media;

public class MediaDAO extends DAO<Media> {
	
	private Session s;
	
	public MediaDAO(Session s) {
		super(s, Media.class);
		this.s = s;
	}
	
	@SuppressWarnings("unchecked")
	public List<Media> findMedia(Media m) {
        if (m != null) {
            Query q;
            String query = "where";
            if (m.getDate() != null) {
                query += " obj.date like :date";
            }
            if (m.getDescription() != null && m.getDescription().length() > 0) {
                query += " and obj.description like :description";
            }
            if (m.getFormat() != null && m.getFormat().length() > 0) {
                query += " and obj.format like :format";
            }
            if (m.getHeigth() > 0) {
                query += " and obj.heigth like :heigth";
            }
            if (m.getLength() != null) {
                query += " and obj.length like :length";
            }
            if (m.getType() != null) {
                query += " and obj.mediatype like :mediatype";
            }
            if (m.getNote() != null && m.getNote().length() > 0) {
                query += " and obj.note like :note";
            }
            if (m.getPath() != null && m.getPath().length() > 0) {
                query += " and obj.path like :path";
            }
            if (m.getSize() > 0) {
                query += " and obj.size like :size";
            }
            if (m.getTags() != null && m.getTags().length() > 0) {
                query += " and obj.tags like :tags";
            }
            if (m.getTitle() != null && m.getTitle().length() > 0) {
                query += " and obj.title like :title";
            }
            if (m.getWidth() > 0) {
                query += " and obj.width like :width";
            }
            if (query.contains("where and")) {
                query = query.replace("where and", "where");
            }
            if (!query.equals("where")) {
                q = s.createQuery("from Media obj " + query);
            } else {
                q = s.createQuery("from Media obj ");
            }
            if (m.getDate() != null) {
            	q.setDate("date", m.getDate());
            }
            if (m.getDescription() != null && m.getDescription().length() > 0) {
                q.setString("description", "%" + m.getDescription() + "%");
            }
            if (m.getFormat() != null && m.getFormat().length() > 0) {
            	q.setString("format", "%" + m.getFormat() + "%");
            }
            if (m.getHeigth() > 0) {
            	q.setInteger("heigth", m.getHeigth());
            }
            if (m.getLength() != null) {
            	q.setDate("length", m.getLength());
            }
            if (m.getType() != null) {
            	q.setInteger("mediatype", m.getType().getId());
            }
            if (m.getNote() != null && m.getNote().length() > 0) {
            	q.setString("note", "%" + m.getNote() + "%");
            }
            if (m.getPath() != null && m.getPath().length() > 0) {
            	q.setString("path", "%" + m.getPath() + "%");
            }
            if (m.getSize() > 0) {
            	q.setLong("size", m.getSize());
            }
            if (m.getTags() != null && m.getTags().length() > 0) {
            	q.setString("tags", "%" + m.getTags() + "%");
            }
            if (m.getTitle() != null && m.getTitle().length() > 0) {
            	q.setString("title", "%" + m.getTitle() + "%");
            }
            if (m.getWidth() > 0) {
            	q.setInteger("width", m.getWidth());
            }
            return q.list();
        } else {
            return null;
        }
    }
}

