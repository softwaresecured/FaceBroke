package facebroke.util;

import java.util.Iterator;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtility {

	private static SessionFactory factory;

	public static synchronized SessionFactory getSessionFactory() {
		String urlParam = "hibernate.connection.url";
		
		if (factory == null) {
			StandardServiceRegistryBuilder regBuilder = new StandardServiceRegistryBuilder().configure();
			
			/*
			 * Read and ENV var. If its set, use that as DB URL (helps with Docker).
			 * By default, use localhost (as in cfg file)
			 */
			String targetUrl = System.getenv("HIBERNATE_FB_URL");
			if(targetUrl != null && targetUrl.length() > 0) {
				regBuilder.applySetting(urlParam, targetUrl);
			}
			
			
			StandardServiceRegistry reg = regBuilder.build();

			try {
				factory = new MetadataSources(reg).buildMetadata().buildSessionFactory();
			} catch (Exception e) {
				StandardServiceRegistryBuilder.destroy(reg);

				System.err.println("\nCaught the following error in HibernateUtil, Dying...:\n" + e);
				System.exit(1);
			}
		}

		return factory;
	}
}
