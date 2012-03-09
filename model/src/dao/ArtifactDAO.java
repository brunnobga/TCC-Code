package dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import entity.Artifact;
import entity.Media;

public class ArtifactDAO extends DAO<Artifact> {
	
	private Session s;
	
	public ArtifactDAO(Session s) {
		super(s, Artifact.class);
		this.s = s;
	}
	
	@SuppressWarnings("unchecked")
	public List<Artifact> findArtifact(Artifact f) {
        if (f != null) {
            Query q;
            String query = "where";
            if (f.getName() != null && f.getName().length() > 0) {
                query += " obj.name like :name";
            }
            query += " and obj.type like :type";
            if (f.getDescription() != null && f.getDescription().length() > 0) {
                query += " and obj.description like :description";
            }
            if (query.contains("where and")) {
                query = query.replace("where and", "where");
            }
            if (!query.equals("where")) {
                q = s.createQuery("from Artifact obj " + query);
            } else {
                q = s.createQuery("from Artifact obj ");
            }
            if (f.getName() != null && f.getName().length() > 0) {
                q.setString("name", "%" + f.getName() + "%");
            }
            q.setInteger("type", f.getType());
            if (f.getDescription() != null && f.getDescription().length() > 0) {
                q.setString("description", "%" + f.getDescription() + "%");
            }
            return q.list();
        } else {
            return null;
        }
    }
}

