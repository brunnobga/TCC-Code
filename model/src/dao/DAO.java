package dao;

import java.util.List;

import org.hibernate.Session;

/**
 *
 * @author EvandrO
 */
public class DAO<T> {

    private Session session;
    @SuppressWarnings("rawtypes")
	private Class persistentClass;
    
    @SuppressWarnings("rawtypes")
    protected DAO(Session session, Class persistentClass) {
        this.session = session;
        this.persistentClass = persistentClass;
    }
    
    public boolean saveOrUpdate(T t) {
	    try {    
	    	if (t != null) {
	        	session.flush();
	            session.clear();
	            session.beginTransaction();
	            session.saveOrUpdate(t);
	            session.getTransaction().commit();
	            return true;
	        } else {
	        	return false;
	        }
        } catch(Exception ex) {
        	ex.printStackTrace();
            session.getTransaction().rollback();
            return false;
        }
    }
    
    public boolean remove(T t) {
        try {
            session.flush();
            session.clear();
            session.beginTransaction();
            session.delete(t);
            session.getTransaction().commit();
            return true;
        } catch(Exception ex) {
            session.getTransaction().rollback();
            return false;
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<T> list() {
        return session.createCriteria(persistentClass).list();
    }

    @SuppressWarnings("unchecked")
	public T findById(int pk) {
        return (T) session.load(persistentClass, pk);
    }
    
    public Session getSession() {
        return session;
    }
}
