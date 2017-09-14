package FaceBroke;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Tester {

	public static void main(String[] args) {
		System.out.println("Attempting to load hibernate config");
		SessionFactory sessionFactory = HibernateUtility.getSessionFactory();
		System.out.println("Finished loading hiberante config");
		
		System.out.println("here");
		Session s = sessionFactory.openSession();
		System.out.println("here2");
		s.beginTransaction();
		System.out.println("here3");
		s.getTransaction().commit();
		System.out.println("here4");
		s.close();
		System.out.println("here5");
		
		System.exit(0);
	}

}
