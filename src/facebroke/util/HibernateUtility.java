package facebroke.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.Loader;
import facebroke.model.User;

public class HibernateUtility {

	private static SessionFactory factory;
	private static String urlParam = "hibernate.connection.url";
	private final static Logger log = LoggerFactory.getLogger(HibernateUtility.class);

	public static synchronized SessionFactory getSessionFactory() {
		
		
		if (factory == null) {
			factory = buildSessionFactory();
			
			log.info("Building session factory");
			
			// Now, we need to check if the DB is empty and load the dummy data if it is
			try {
				Session sess = factory.openSession();
				List<User> results = sess.createQuery("From DummyDataInfo").list();
				sess.close();
				if(results.size() < 1) {
					throw new Exception("Empty DB");
				}
				
			// Should error out if the initial mapping hasn't occurred (i.e. DB is empty)
			}catch(Exception e){
				log.info(e.getMessage());
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

			System.err.println("\nCaught the following error in HibernateUtil, Dying...:\n" + e);
			System.exit(1);
		}
		
		return result;
	}
	
	public static synchronized void destroySessionFactory() {
		if(factory.isOpen()) {
			factory.close();
		}
		
		factory = null;
	}
}
