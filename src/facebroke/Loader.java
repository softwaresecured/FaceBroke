package facebroke;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.Comment;
import facebroke.model.DummyDataInfo;
import facebroke.model.Post;
import facebroke.model.User;
import facebroke.model.User.UserRole;
import facebroke.model.Wall;
import facebroke.util.HibernateUtility;
import facebroke.util.LoremGenerator;
import facebroke.util.ValidationSnipets;

/**
 * Utility class to load the DB with varying kinds of generated data
 *
 */
public class Loader {

	// The sample names below were taken from
	// https://github.com/fivethirtyeight/data/tree/master/most-common-name
	private final static String[] firstNames = { "Michael", "James", "John", "Robert", "David", "William", "Mary",
			"Christopher", "Joseph", "Richard", "Daniel", "Thomas", "Matthew", "Jennifer", "Charles", "Anthony",
			"Patricia", "Linda", "Mark", "Elizabeth", "Joshua", "Steven", "Andrew", "Kevin", "Brian", "Barbara",
			"Jessica", "Jason", "Susan", "Timothy", "Paul", "Kenneth", "Lisa", "Ryan", "Sarah", "Karen", "Jeffrey",
			"Donald", "Ashley", "Eric", "Jacob", "Nicholas", "Jonathan", "Ronald", "Michelle", "Kimberly", "Nancy",
			"Justin", "Sandra", "Amanda", "Brandon", "Stephanie", "Emily", "Melissa", "Gary", "Edward", "Stephen",
			"Scott", "George", "Donna", "Jose", "Rebecca", "Deborah", "Laura", "Cynthia", "Carol", "Amy", "Margaret",
			"Gregory", "Sharon", "Larry", "Angela", "Maria", "Alexander", "Benjamin", "Nicole", "Kathleen", "Patrick",
			"Samantha", "Tyler", "Samuel", "Betty", "Brenda", "Pamela", "Aaron", "Kelly", "Heather", "Rachel", "Adam",
			"Christine", "Zachary", "Debra", "Katherine", "Dennis", "Nathan", "Christina", "Julie", "Jordan", "Kyle",
			"Anna" };

	private final static String[] lastNames = { "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Rodriguez",
			"Miller", "Martinez", "Davis", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor",
			"Lee", "Moore", "Jackson", "Perez", "Martin", "Thompson", "White", "Sanchez", "Harris", "Ramirez", "Clark",
			"Lewis", "Robinson", "Walker", "Young", "Hall", "Allen", "Torres", "Nguyen", "Wright", "Flores", "King",
			"Scott", "Rivera", "Green", "Hill", "Adams", "Baker", "Nelson", "Mitchell", "Campbell", "Gomez", "Carter",
			"Roberts", "Diaz", "Phillips", "Evans", "Turner", "Reyes", "Cruz", "Parker", "Edwards", "Collins",
			"Stewart", "Morris", "Morales", "Ortiz", "Gutierrez", "Murphy", "Rogers", "Cook", "Kim", "Morgan", "Cooper",
			"Ramos", "Peterson", "Gonzales", "Bell", "Reed", "Bailey", "Chavez", "Kelly", "Howard", "Richardson",
			"Ward", "Cox", "Ruiz", "Brooks", "Watson", "Wood", "James", "Mendoza", "Gray", "Bennett", "Alvarez",
			"Castillo", "Price", "Hughes", "Vasquez", "Sanders", "Jimenez", "Long", "Foster" };
	private final static Logger log = LoggerFactory.getLogger(Loader.class);
	private final static int NUMNAMES = 100;
	private final static int NUM_USERS = 1000;
	private final static long SEED = 1877;
	private final static int LOWER_YEAR = 1950;
	private final static int RANGE_YEAR = 75;
	private final static int MAX_RANDOM_POSTS = 4;
	private final static int MAX_RANDOM_COMMENTS = 6;
	private static final String version = "0.1";
	
	
	public static void generateDummyDB() {
		loadRandomUsers(NUM_USERS, SEED);
		loadRandomPosts(MAX_RANDOM_POSTS, SEED);
		loadRandomComments(MAX_RANDOM_COMMENTS, SEED);
		loadVersionInfo(version);
	}

	public static void loadVersionInfo(String ver) {
		Session sess = HibernateUtility.getSessionFactory().openSession();
		
		sess.beginTransaction();
		DummyDataInfo info = new DummyDataInfo(ver);
		sess.save(info);
		sess.getTransaction().commit();
		sess.close();
	}


	public static void loadRandomUsers(int numUsers, long seed) {
		log.info("Attempting to load hibernate config");
		SessionFactory sessionFactory = HibernateUtility.getSessionFactory();
		log.info("Finished loading hibernate config");

		Session sess = sessionFactory.openSession();

		Random r = new Random(seed);
		
		HashSet<String> takenNames = new HashSet<>();
		
		log.info("Creating users....");

		sess.beginTransaction();

		for (int i = 0; i < numUsers; ++i) {

			String f = firstNames[r.nextInt(NUMNAMES)];
			String l = lastNames[r.nextInt(NUMNAMES)];
			String username;
			
			do {
				username = f + l + r.nextInt(NUMNAMES);
			} while (takenNames.contains(username));
			
			takenNames.add(username);
			
			String email = username + "@fake.ca";
			Calendar dob;
			
			try {
				dob = ValidationSnipets.parseDate(String.format("%d-%d-%d", LOWER_YEAR+r.nextInt(RANGE_YEAR), 1+r.nextInt(12), 1+r.nextInt(29)));
			}catch (ParseException e) {
				dob = new GregorianCalendar(1950,2,17);
			}


			User u = new User(f, l, username, email.toLowerCase(), dob);
			u.updatePassword(f);

			Wall w = new Wall(u);
			u.setWall(w);

			sess.save(u);
			sess.save(w);

		}

		sess.getTransaction().commit();
		
		
		/* Adding a tester account that will always be created */
		sess.beginTransaction();
		
		
		User matt = new User("Matt","Yaraskavitch","jarusk","myaraskavitch@dummy.ca", new GregorianCalendar(1992,3,14));
		matt.updatePassword("password");
		matt.setRole(UserRole.ADMIN);
		Wall w = new Wall(matt);
		matt.setWall(w);
		sess.save(matt);
		sess.save(w);
		sess.getTransaction().commit();

		sess.close();
		
		log.info("Total num of users created: " + (takenNames.size() + 1));
	}

	
	public static void loadRandomPosts(int maxNumPosts, long seed) {
		log.info("Creating Random Posts");
		log.info("Attempting to load Hibernate SessionFactory");
		SessionFactory sessionFactory = HibernateUtility.getSessionFactory();
		log.info("Finished loading Hibernate SessionFactory");

		Session sess = sessionFactory.openSession();

		Random r = new Random(seed*3);
		LoremGenerator lg = new LoremGenerator(seed);


		@SuppressWarnings("unchecked")
		List<Wall> walls = (List<Wall>)sess.createQuery("FROM Wall w").list();
		
		sess.beginTransaction();
		
		int totalCreated = 0;
		
		for (int i = 0; i < walls.size(); i++) {
			Wall w = walls.get(i);
			
			for (int j = 0; j < r.nextInt(maxNumPosts + 1); j++) {
				//String title = lg.getWords(4 + r.nextInt(5));
				String content = lg.getSentences(2);
				User creator = walls.get(r.nextInt(walls.size())).getUser();
				
				Post p = new Post(w, creator, Post.PostType.TEXT, content);
				
				sess.save(p);
				totalCreated += 1;
			}
		}
		
		sess.getTransaction().commit();
		
		sess.close();
		log.info("Have "+walls.size()+" walls");
		log.info("Total num of posts created: "+totalCreated);
	}
	
	
	public static void loadRandomComments(int maxNumComments, long seed) {
		log.info("Creating Random Comments");
		log.info("Attempting to load Hibernate Session");
		Session sess = HibernateUtility.getSessionFactory().openSession();
		log.info("Finished loading Hibernate Session");
		
		Random r = new Random(seed*5);
		LoremGenerator lg = new LoremGenerator(seed*4);
		
		@SuppressWarnings("unchecked")
		List<Post> posts = (List<Post>)sess.createQuery("FROM Post").list();
		
		sess.beginTransaction();
		int totalComments = 0;
		
		for (int i = 0; i < posts.size(); i++) {
			Post p = posts.get(i);
			
			for (int j = 0; j < r.nextInt(maxNumComments + 1); j++) {
				String content = lg.getWords(4 + r.nextInt(5));
				User creator = posts.get(r.nextInt(posts.size())).getCreator();
				
				Comment c = new Comment(creator, p, content);
				sess.save(c);
				totalComments += 1;
			}
		}
		
		sess.getTransaction().commit();
		sess.close();
		log.info("Have "+posts.size()+" posts");
		log.info("Total num of comments created: "+totalComments);
	}
}
