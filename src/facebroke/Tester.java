package facebroke;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.util.HibernateUtility;

import facebroke.model.User;

public class Tester {
	
	private final static Logger log = LoggerFactory.getLogger(Tester.class);

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		log.info("Attempting to load hibernate config");
		SessionFactory sessionFactory = HibernateUtility.getSessionFactory();
		log.info("Finished loading hibernate config");
		
		
		Session s = sessionFactory.openSession();
		s.beginTransaction();
		List result = s.createQuery("from User").list();
		
		for (User u : (List<User>)result) {
			System.out.println("User: "+u.getId()+" - "+u.getFname()+" - "+u.getLname());
		}
		
		System.exit(0);
	}

}
