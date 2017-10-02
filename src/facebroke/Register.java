package facebroke;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

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
		req.getRequestDispatcher("register.jsp").forward(req, res);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		handleRegistration(req,res);
	}


	private void handleRegistration(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		if (ValidationSnipets.isValidSession(req.getSession())) {
			res.sendRedirect("index");
			return;
		}
		
		RequestDispatcher reqDis = req.getRequestDispatcher("index");
		
		
		String username = req.getParameter("regUsername");
		String email = req.getParameter("regEmail");
		String fname = req.getParameter("regFirstName");
		String lname = req.getParameter("regLastName");
		String dob_raw = req.getParameter("regDOB");
		String pass1 = req.getParameter("regPassword");
		String pass2 = req.getParameter("regPasswordConfirm");
		Calendar dob;
		
		
		
		// Validate the user name
		if (username == null) {
			req.getSession().setAttribute("authMessage", "Username is required");
			res.sendRedirect("register");
			return;
		}
		log.info("Received register request with username \""+username+"\"");
		log.info("Email: "+email);
		log.info("First Name: "+fname);
		log.info("Last Name: "+lname);
		log.info("DOB: "+dob_raw);
		
		
		if(ValidationSnipets.isUsernameTaken(username)) {
			req.getSession().setAttribute("authMessage", "Username already taken");
			res.sendRedirect("register");
			return;
		}
		
		
		// Validate the email
		if(email == null || !ValidationSnipets.isValidEmail(email)) {
			req.getSession().setAttribute("authMessage", "Invalid email address");
			res.sendRedirect("register");
			return;
		}
		
		
		if(ValidationSnipets.isEmailTaken(email)) {
			req.getSession().setAttribute("authMessage", "Email already taken");
			res.sendRedirect("register");
			return;
		}
		
		
		// Validate first and last names
		if (fname == null || fname.length() < 1) {
			req.getSession().setAttribute("authMessage", "First name can't be blank. If you have a mononym, leave Last Name blank");
			res.sendRedirect("register");
			return;
		}
		
		if(lname == null) {
			lname = "";
		}
		
		
		// Validate DOB
		if (dob_raw == null) {
			req.getSession().setAttribute("authMessage", "Date of Bith can't be blank");
			res.sendRedirect("register");
			return;
		}
		try {
			dob = ValidationSnipets.parseDate(dob_raw);
		} catch (ParseException e) {
			req.getSession().setAttribute("authMessage", "Invalid Date of Birth Format. Need yyyy-mm-dd");
			res.sendRedirect("register");
			return;
		}
		
		
		// Validate Password
		if (pass1 == null || pass1.length() < 1 || pass2 ==null || pass2.length() < 1) {
			req.getSession().setAttribute("authMessage", "Password can't be blank");
			res.sendRedirect("register");
			return;
		}
		
		if (!ValidationSnipets.passwordFormatValid(pass1)) {
			req.getSession().setAttribute("authMessage", "Password must be at least 8 characters long and contain only a-z,A-z,0-9,!,#,$,^");
			res.sendRedirect("register");
			return;
		}
		
		if (!pass1.equals(pass2)) {
			req.getSession().setAttribute("authMessage", "Passwords don't match");
			res.sendRedirect("register");
			return;
		}
		
		
		// All input is valid. Time to build our new user
		User u = new User(fname,lname, username, email, dob);
		Wall w = new Wall(u);
		
		u.updatePassword(pass1);
		u.setWall(w);
		
		Session sess = HibernateUtility.getSessionFactory().openSession();
		sess.beginTransaction();
		sess.save(u);
		sess.save(w);
		sess.getTransaction().commit();
		
		// Finally
		req.getSession().setAttribute("authMessage", "Registration Successful, go ahead and login!");
		res.sendRedirect("index");
		sess.close();
	}
}
