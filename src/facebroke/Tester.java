package facebroke;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.User;
import facebroke.model.Wall;
import facebroke.util.HibernateUtility;

public class Tester {

	private final static Logger log = LoggerFactory.getLogger(Tester.class);

	@SuppressWarnings({ "unchecked" })
	public static void main(String[] args) {
		log.info("Attempting to load hibernate config");
		SessionFactory sessionFactory = HibernateUtility.getSessionFactory();
		log.info("Finished loading hibernate config");

		Session s = sessionFactory.openSession();
		s.beginTransaction();

		User a = new User("Bob", "Mckenzie", "bob01", "bob@fake.ca");
		User b = new User("Doug", "Mckenzie", "DougDoug", "doug@fake.ca");
		a.updatePassword("badPass");
		b.updatePassword("password");

		Wall wa = new Wall();
		Wall wb = new Wall();

		a.setWall(wa);
		wa.setUser(a);
		b.setWall(wb);
		wb.setUser(b);

		s.save(a);
		s.save(wa);
		s.save(b);
		s.save(wb);

		s.getTransaction().commit();

		List<User> result = s.createQuery("from User").list();

		for (User u : result) {
			System.out.println("User: " + u.getId() + " - " + u.getFname() + " - " + u.getLname());
		}

		System.exit(0);
	}

}
