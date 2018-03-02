package facebroke.util;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.Loader;
import facebroke.model.User;


/**
 * FaceBroke uses Hibernate as it's ORM. To simplify things, every FaceBroke method must
 * get a Hibernate Session 
 * 
 * 
 * 
 */
public class HibernateUtility {

	private static SessionFactory factory = null;
	private static EntityManagerFactory emf = null;
	private static String urlParam = "hibernate.connection.url";
	private final static Logger log = LoggerFactory.getLogger(HibernateUtility.class);

	public static synchronized SessionFactory getSessionFactory() {
		
		
		if (factory == null) {
			factory = buildSessionFactory();
			
			log.info("Building session factory");
			
			// Now, we need to check if the DB is empty and load the dummy data if it is
			try {
				Session sess = factory.openSession();
				@SuppressWarnings("unchecked")
				List<User> results = sess.createQuery("From DummyDataInfo").list();
				sess.close();
				if(results.size() < 1) {
					throw new Exception("Empty DB");
				}
				
			// Should error out if the initial mapping hasn't occurred (i.e. DB is empty)
			}catch(Exception e){
				log.info("{}",ValidationSnipets.sanitizeCRLF(e.getMessage()));
				log.info("Trying to create DB");
				Loader.generateDummyDB();
			}
		}

		return factory;
	}
	
	private static synchronized SessionFactory buildSessionFactory() {
		
		SessionFactory result = null;
		
		
		StandardServiceRegistryBuilder regBuilder = new StandardServiceRegistryBuilder().configure();
		
		/*
		 * Read an ENV var. If it's set, use that as DB URL (helps with Docker).
		 * By default, use localhost (as in cfg file)
		 */
		String targetUrl = System.getenv("HIBERNATE_FB_URL");
		if(targetUrl != null && targetUrl.length() > 0) {
			regBuilder.applySetting(HibernateUtility.urlParam, targetUrl);
		}
		
		
		StandardServiceRegistry reg = regBuilder.build();

		try {
			result = new MetadataSources(reg).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			StandardServiceRegistryBuilder.destroy(reg);
			throw new RuntimeException("Caught the following error in HibernateUtil, Dying...:" + e);
		}
		
		return result;
	}
	
	public static synchronized void destroySessionFactory() {
		if(factory.isOpen()) {
			factory.close();
		}
		
		factory = null;
	}
	
	
	public static synchronized EntityManagerFactory getEntityManagerFactory() {
		
		if(emf == null) {
			emf = Persistence.createEntityManagerFactory("EMF");
		}
		
		return emf;
	}
}
