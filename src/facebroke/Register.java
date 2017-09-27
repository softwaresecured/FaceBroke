package facebroke;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.User;
import facebroke.model.Wall;
import facebroke.util.HibernateUtility;
import facebroke.util.ValidationSnipets;

@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(Register.class);
	
	public Register() {
		super();
	}

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
		String lname = req.getParameter("regLastName");
		String dob_raw = req.getParameter("regDOB");
		String pass1 = req.getParameter("regPassword");
		String pass2 = req.getParameter("regPasswordConfirm");
		Date dob;
		
		log.info(username);
		log.info(email);
		log.info(fname);
		log.info(lname);
		log.info(dob_raw);
		log.info(pass1);
		log.info(pass2);
		
		
		
		// Validate the user name
		if (username == null) {
			req.setAttribute("authMessage", "Username is required");
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
			req.setAttribute("authMessage", "Username already taken");
			reqDis.forward(req, res);
			sess.close();
			return;
		}
		
		
		// Validate the email
		if(email == null || !ValidationSnipets.isValidEmail(email)) {
			req.setAttribute("authMessage", "Invalid email address");
			reqDis.forward(req, res);
			sess.close();
			return;
		}
		
		
		results = (List<User>)sess.createQuery("FROM User U WHERE U.email = :email")
						.setParameter("email", email).list();
		
		if(results.size() > 0) {
			req.setAttribute("authMessage", "Email already taken");
			reqDis.forward(req, res);
			sess.close();
			return;
		}
		
		
		// Validate first and last names
		if (fname == null || fname.length() < 1) {
			req.setAttribute("authMessage", "First name can't be blank. If you have a mononym, leave Last Name blank");
			reqDis.forward(req, res);
			sess.close();
			return;
		}
		
		if(lname == null) {
			lname = "";
		}
		
		
		// Validate DOB
		if (dob_raw == null) {
			req.setAttribute("authMessage", "Date of Bith can't be blank");
			reqDis.forward(req, res);
			sess.close();
			return;
		}
		try {
			dob = ValidationSnipets.parseDate(dob_raw);
		} catch (ParseException e) {
			req.setAttribute("authMessage", "Invalid Date of Birth Format. Need yyyy-mm-dd");
			reqDis.forward(req, res);
			sess.close();
			return;
		}
		
		
		// Validate Password
		if (pass1 == null || pass1.length() < 1 || pass2 ==null || pass2.length() < 1) {
			req.setAttribute("authMessage", "Password can't be blank");
			reqDis.forward(req, res);
			sess.close();
			return;
		}
		
		if (!ValidationSnipets.passwordFormatValid(pass1)) {
			req.setAttribute("authMessage", "Password must be at least 8 characters long and contain only a-z,A-z,0-9,!,#,$,^");
			reqDis.forward(req, res);
			sess.close();
			return;
		}
		
		if (!pass1.equals(pass2)) {
			req.setAttribute("authMessage", "Passwords don't match");
			reqDis.forward(req, res);
			sess.close();
			return;
		}
		
		
		// All input is valid. Time to build our new user
		User u = new User(fname,lname, username, email, dob);
		Wall w = new Wall(u);
		
		u.updatePassword(pass1);
		u.setWall(w);
		
		sess.beginTransaction();
		sess.save(u);
		sess.save(w);
		sess.getTransaction().commit();
		
		// Finally
		req.setAttribute("authMessage", "Registration Successful, go ahead and login!");
		reqDis.forward(req, res);
		sess.close();
	}
}
