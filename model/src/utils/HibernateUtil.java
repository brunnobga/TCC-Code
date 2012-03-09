package utils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * 
 * @author EvandrO
 */
public class HibernateUtil {

	private static final SessionFactory sessionFactory;
    private static ThreadLocal<Session> sessions = new ThreadLocal<Session>();
    
	private static Logger loggger = Logger.getLogger(HibernateUtil.class);
	
	static {
		try {
			sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory(); 
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

    public static Session openSession() {
        Session session = sessions.get();
        if(session != null && sessions.get().isOpen()) {
        	loggger.log(Level.DEBUG, "Opened session not closed!");
        } else {
            session = sessionFactory.openSession();
            sessions.set(session);
        }
        return sessions.get();
    }

    public static void closeCurrentSession() {
        if (sessions.get() != null && sessions.get().isOpen()) {
            sessions.get().close();
            sessions.set(null);
            sessionFactory.close();
        }
    }
    
    public static Session currentSession() {
        return sessions.get();
    }
}
