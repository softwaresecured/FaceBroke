package facebroke;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.util.HibernateUtility;

public class Tester {
	
	private final static Logger log = LoggerFactory.getLogger(Tester.class);

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		log.info("Attempting to load hibernate config");
		SessionFactory sessionFactory = HibernateUtility.getSessionFactory();
		log.info("Finished loading hibernate config");
		
		System.exit(0);
	}

}
