package facebroke.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtility {
	
	public static SessionFactory factory;
	
	public static synchronized SessionFactory getSessionFactory() {
		if (factory == null) {
			StandardServiceRegistry reg = new StandardServiceRegistryBuilder()
					.configure()
					.build();
			
			try {
				factory = new MetadataSources(reg).buildMetadata().buildSessionFactory();
			} catch (Exception e) {
				StandardServiceRegistryBuilder.destroy(reg);
				
				System.err.println("\nCaught the following error in HibernateUtil, Dying...:\n"+e);
				System.exit(1);
			}
		}
		
		return factory;
	}
}
