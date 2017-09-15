package FaceBroke;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tester {
	
	private final static Logger log = LoggerFactory.getLogger(Tester.class);

	public static void main(String[] args) {
		log.info("Attempting to load hibernate config");
		SessionFactory sessionFactory = HibernateUtility.getSessionFactory();
		log.info("Finished loading hibernate config");
		
		log.info("here");
		Session s = sessionFactory.openSession();
		log.info("here2");
		s.beginTransaction();
		log.info("here3");
		s.getTransaction().commit();
		log.info("here4");
		s.close();
		log.info("here5");
		
		System.exit(0);
	}

}
