package facebroke;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.User;
import facebroke.util.HibernateUtility;
import facebroke.util.ValidationSnipets;

public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(Register.class);

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		handleRegistration(req,res);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		handleRegistration(req,res);
	}

	@SuppressWarnings("unchecked")
	private void handleRegistration(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		if (ValidationSnipets.isValidSession(req.getSession())) {
			res.sendRedirect("index.jsp");
			return;
		}
		
		RequestDispatcher reqDis = req.getRequestDispatcher("index.jsp");
		Session sess = HibernateUtility.getSessionFactory().openSession();
		
		
		String username = req.getParameter("regUsername");
		String email = req.getParameter("regEmail");
		String fname = req.getParameter("regFirstName");
		String lname = req.getParameter("regLasttName");
		String dob = req.getParameter("regDOB");
		String pass1 = req.getParameter("regPassword");
		String pass2 = req.getParameter("regPasswordConfirm");
		
		
		
		
		// Validate the user name
		if (username == null) {
			req.setAttribute("errorMessage", "Username is required");
			reqDis.forward(req, res);
			sess.close();
			return;
		}
		log.info("Received register request with username \""+username+"\"");
		
		sess.beginTransaction();
		
		List<User> results = null;
		results = (List<User>)sess.createQuery("FROM User U WHERE U.username = :username")
						.setParameter("username", username).list();
		sess.getTransaction().commit();
		
		if(results.size() > 0) {
			req.setAttribute("errorMessage", "Username already taken");
			reqDis.forward(req, res);
			sess.close();
			return;
		}
		
		
		// Validate the email
		if(email == null || !ValidationSnipets.isValidEmail(email)) {
			req.setAttribute("errorMessage", "Invalid email address");
			reqDis.forward(req, res);
			sess.close();
			return;
		}
		
		sess.beginTransaction();
		
		results = (List<User>)sess.createQuery("FROM User U WHERE U.email = :email")
						.setParameter("email", email).list();
		sess.getTransaction().commit();
		
		if(results.size() > 0) {
			req.setAttribute("errorMessage", "Email already taken");
			reqDis.forward(req, res);
			sess.close();
			return;
		}
	}
}
