package facebroke;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.User;
import facebroke.util.HibernateUtility;
import facebroke.util.ValidationSnipets;


/**
 * Handle /login endpoint that validates user login and sets required session parameters
 */
@WebServlet("/login")
public class Login extends HttpServlet {

	private static Logger log = LoggerFactory.getLogger(Login.class);
	private static final long serialVersionUID = 1L;

	private static final String INVALID_LOGIN_CREDS = "Invalid Login Credentials";

	
	/**
	 * Call parent Servlet constructor
	 */
	public Login() {
		super();
	}

	
	/**
	 * Respond to GET request. Forward control to the handleLogin method
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		handleLogin(req, res);
	}

	
	/**
	 * Respond to POST request. Forward control to the handleLogin method
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		handleLogin(req, res);
	}

	
	/**
	 * Process Login parameters and return the correct JSP depending on outcome
	 * 
	 * Accepts two parameters:
	 *   user_cred -> either registration email or username
	 *   password -> plaintext password for user account
	 * 
	 * @param req - HttpServletRequest from either GET or POST
	 * @param res - HttpServletResponse to pass info back to user
	 * @throws ServletException - Hiccups propagate up
	 * @throws IOException - Hiccups propagate up
	 */
	@SuppressWarnings("unchecked")
	protected void handleLogin(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// Redirect to index if we're already logged in
		if (ValidationSnipets.isValidSession(req.getSession())) {
			res.sendRedirect("index");
			return;
		}

		// Setup a dispatcher to return to login page on error
		RequestDispatcher reqDis = req.getRequestDispatcher("register.jsp");

		String user_cred = req.getParameter("user_cred");
		String pass = req.getParameter("password");

		if (user_cred == null || pass == null) {
			req.setAttribute("authMessage", INVALID_LOGIN_CREDS);
			reqDis.forward(req, res);
			return;
		}

		if (user_cred.equals("") || pass.equals("")) {
			req.setAttribute("authMessage", "Credentials can't be blank");
			reqDis.forward(req, res);
			return;
		}

		// Creds are not null, so need to try and validate against the db
		
		log.info("Trying to get session factory");

		Session sess = HibernateUtility.getSessionFactory().openSession();

		List<User> results = null;

		Query<User> query;
		
		log.info("Trying to validate login");

		if (ValidationSnipets.isValidEmail(user_cred)) {
			query = sess.createQuery("FROM User U WHERE U.email = :user_cred");
		} else {
			query = sess.createQuery("FROM User U WHERE U.username = :user_cred");
		}

		results = query.setParameter("user_cred", user_cred).list();

		log.info("Size of result list: {}",results.size());

		// Couldn't find the user_cred in DB
		if (results.size() < 1) {
			req.setAttribute("authMessage", INVALID_LOGIN_CREDS);
			reqDis.forward(req, res);
			sess.close();
			return;
		}

		// Got a match for username or email
		User candidate = results.get(0);

		// If password is valid then set up session parameters
		if (candidate.isPasswordValid(pass)) {
			req.getSession().setAttribute("valid", "true");
			req.getSession().setAttribute("user_id", candidate.getId());
			req.getSession().setAttribute("user_username", candidate.getUsername());
			req.getSession().setAttribute("user_fname", candidate.getFname());
			req.getSession().setAttribute("user_lname", candidate.getLname());
			req.getSession().setAttribute("user_wall_id", candidate.getWall().getId());
			
			if(candidate.getRole().equals(User.UserRole.ADMIN)) {
				req.getSession().setAttribute("user_role", "ADMIN");
			}else {
				req.getSession().setAttribute("user_role", "USER");
			}
			
			if(candidate.getProfilePicture() != null) {
				req.getSession().setAttribute("user_pic_id", candidate.getProfilePicture().getId());
			}else {
				req.getSession().setAttribute("user_pic_id", "default");
			}
			// All done authentication, so return user to index page
			res.sendRedirect("index");
		} else {
			log.info("Invalid password entered");
			req.setAttribute("authMessage", INVALID_LOGIN_CREDS);
			reqDis.forward(req, res);
		}

		sess.close();
	}
}
