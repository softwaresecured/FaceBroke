package facebroke;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.User;
import facebroke.util.HibernateUtility;
import facebroke.util.ValidationSnipets;

/**
 * A simple demo page for SQL injection.
 * Not going to remain in final version, simply a place to hold code during dev
 * 
 * @author matt @ Software Secured
 *
 */
@WebServlet("/demo")
public class Demo extends HttpServlet {

	private static Logger log = LoggerFactory.getLogger(Demo.class);
	private static final long serialVersionUID = 1L;

	
	/**
	 * Call parent constructor
	 */
	public Demo() {
		super();
	}

	
	/**
	 * Handle the submission of a request for User ID.
	 * Uses the following parameters:
	 *   userid -> the target userid to fetch from the DB
	 *   injection -> if equals "allow" then allow easy SQL injection
	 */
	@Override
	@SuppressWarnings({ "deprecation", "unchecked" })
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		if(!ValidationSnipets.isValidSession(req.getSession())){
			res.sendRedirect("index");
			return;
		}

		Session sess = HibernateUtility.getSessionFactory().openSession();

		String userid = req.getParameter("userid");

		List<User> results = null;

		if (userid != null && userid.length() > 0) {

			if (req.getParameter("injection").equals("allow")) {
				results = sess.createSQLQuery("select * from Users WHERE ID = " + userid).addEntity(User.class).list();
				log.info("Allowed raw SQL query without validation");
			} else {
				try {
				results = sess.createQuery("FROM User U WHERE U.id = :user_id")
						.setParameter("user_id", Long.parseLong(userid)).list();
				}catch(NumberFormatException e) {
					req.setAttribute("serverMessage","That's not a valid number........");
					req.getRequestDispatcher("error.jsp").forward(req, res);
					sess.close();
					return;
				}
				log.info("Executed SQL using prepared statements and Hibernate");
			}
		}

		req.setAttribute("rows", results);

		log.info("Starting JSP");
		req.getRequestDispatcher("user_dump.jsp").forward(req, res);
		log.info("Gave up control");

		sess.close();
	}

	
	/**
	 * Handle GET. Doesn't take parameters, just passes control to the Demo JSP
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		log.info("Received GET request");
		if(!ValidationSnipets.isValidSession(req.getSession())){
			res.sendRedirect("index");
			return;
		}

		req.getRequestDispatcher("demo.jsp").forward(req, res);
		log.info("Handed control to JSP");
	}
}
