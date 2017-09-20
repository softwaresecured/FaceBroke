package facebroke;

import java.util.List;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.User;
import facebroke.model.Wall;
import facebroke.util.HibernateUtility;

public class Loader {
	

	private final static Logger log = LoggerFactory.getLogger(Loader.class);

	// The sample names below were taken from https://github.com/fivethirtyeight/data/tree/master/most-common-name
	private final static String[] firstNames = {"Michael", "James", "John", "Robert", "David", "William", "Mary", "Christopher", "Joseph", "Richard", "Daniel", "Thomas", "Matthew", "Jennifer", "Charles", "Anthony", "Patricia", "Linda", "Mark", "Elizabeth", "Joshua", "Steven", "Andrew", "Kevin", "Brian", "Barbara", "Jessica", "Jason", "Susan", "Timothy", "Paul", "Kenneth", "Lisa", "Ryan", "Sarah", "Karen", "Jeffrey", "Donald", "Ashley", "Eric", "Jacob", "Nicholas", "Jonathan", "Ronald", "Michelle", "Kimberly", "Nancy", "Justin", "Sandra", "Amanda", "Brandon", "Stephanie", "Emily", "Melissa", "Gary", "Edward", "Stephen", "Scott", "George", "Donna", "Jose", "Rebecca", "Deborah", "Laura", "Cynthia", "Carol", "Amy", "Margaret", "Gregory", "Sharon", "Larry", "Angela", "Maria", "Alexander", "Benjamin", "Nicole", "Kathleen", "Patrick", "Samantha", "Tyler", "Samuel", "Betty", "Brenda", "Pamela", "Aaron", "Kelly", "Heather", "Rachel", "Adam", "Christine", "Zachary", "Debra", "Katherine", "Dennis", "Nathan", "Christina", "Julie", "Jordan", "Kyle", "Anna"};
	private final static String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Rodriguez", "Miller", "Martinez", "Davis", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Lee", "Moore", "Jackson", "Perez", "Martin", "Thompson", "White", "Sanchez", "Harris", "Ramirez", "Clark", "Lewis", "Robinson", "Walker", "Young", "Hall", "Allen", "Torres", "Nguyen", "Wright", "Flores", "King", "Scott", "Rivera", "Green", "Hill", "Adams", "Baker", "Nelson", "Mitchell", "Campbell", "Gomez", "Carter", "Roberts", "Diaz", "Phillips", "Evans", "Turner", "Reyes", "Cruz", "Parker", "Edwards", "Collins", "Stewart", "Morris", "Morales", "Ortiz", "Gutierrez", "Murphy", "Rogers", "Cook", "Kim", "Morgan", "Cooper", "Ramos", "Peterson", "Gonzales", "Bell", "Reed", "Bailey", "Chavez", "Kelly", "Howard", "Richardson", "Ward", "Cox", "Ruiz", "Brooks", "Watson", "Wood", "James", "Mendoza", "Gray", "Bennett", "Alvarez", "Castillo", "Price", "Hughes", "Vasquez", "Sanders", "Jimenez", "Long", "Foster"};
	private final static int NUMNAMES = 100;
	private final static int NUMROUNDS = 1000;
	private final static long seed = 1877;

	@SuppressWarnings({"unchecked" })
	public static void main(String[] args) {
		log.info("Attempting to load hibernate config");
		SessionFactory sessionFactory = HibernateUtility.getSessionFactory();
		log.info("Finished loading hibernate config");


		Session s = sessionFactory.openSession();
		
		
		Random r = new Random(seed);
		
		s.beginTransaction();
		
		for(int i=0; i<NUMROUNDS; ++i) {
			
			String f = firstNames[r.nextInt(NUMNAMES)];
			String l = lastNames[r.nextInt(NUMNAMES)];
			String username = f + l + r.nextInt(NUMNAMES);
			String email = username + "@fake.ca";
			
			User u = new User(f,l,username,email.toLowerCase());
			u.updatePassword(f);
			
			Wall w = new Wall(u);
			u.setWall(w);
			
			s.save(u);
			s.save(w);
			
		}
		
		s.getTransaction().commit();

		List<User> result = s.createQuery("from User").list();

		for (User u : result) {
			System.out.println("User: "+u.getId()+" - "+u.getFname()+" - "+u.getLname());
		}

		System.exit(0);
	}

}
