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
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.User;
import facebroke.model.Wall;
import facebroke.util.HibernateUtility;
import facebroke.util.ValidationSnipets;


/**
 * Handle /register endpoint. Allows a new user to register via POST
 * 
 */
@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(Register.class);
	
	
	/**
	 * Call parent Servlet constructor
	 */
	public Register() {
		super();
	}

	
	/**
	 * Handle GET requests for the /register page.
	 * Simply should return the registration/login page if user is not logged in.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		// If suer is already logged in, send to homepage
		if(ValidationSnipets.isValidSession(req.getSession())){
			res.sendRedirect("index");
			return;
		}
		
		// Forward the request to the register JSP file
		req.getRequestDispatcher("register.jsp").forward(req, res);
	}


	/**
	 * Handle a new registration via POST request. Simple pass control to the handleRegistration method
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		handleRegistration(req,res);
	}


	/**
	 * Handle registering new User.
	 * 
	 * Accepts the following parameters:
	 *   regUsername -> the username the candidate is requesting (must be unique)
	 *   regEmail -> the email the candidate is requesting (must be unique)
	 *   regFirstName -> candidate's first name (must not be blank)
	 *   regLastName -> candidate's last name (may be blank. i.e. mononyms)
	 *   regDOB -> candidate's Date of Birth
	 *   regPassword -> candidate's Password
	 *   regPasswordConfirm -> must match the first password
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 * @throws ServletException
	 */
	private void handleRegistration(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		if (ValidationSnipets.isValidSession(req.getSession())) {
			res.sendRedirect("index");
			return;
		}
		
		RequestDispatcher reqDis = req.getRequestDispatcher("register.jsp");
		
		
		String username = Encode.forHtml(req.getParameter("regUsername"));
		String email = Encode.forHtml(req.getParameter("regEmail"));
		String fname = Encode.forHtml(req.getParameter("regFirstName"));
		String lname = Encode.forHtml(req.getParameter("regLastName"));
		String dob_raw = Encode.forHtml(req.getParameter("regDOB"));
		String pass1 = Encode.forHtml(req.getParameter("regPassword"));
		String pass2 = Encode.forHtml(req.getParameter("regPasswordConfirm"));
		Calendar dob;
		
		req.setAttribute("regUsername", username);
		req.setAttribute("regEmail", email);
		req.setAttribute("regFirstName", fname);
		req.setAttribute("regLastName", lname);
		req.setAttribute("regDOB", dob_raw);
		
		
		// Validate the user name
		if (username == null) {
			req.setAttribute("authMessage", "Username is required");
			reqDis.forward(req, res);
			return;
		}
		log.info("Received register request with username '{}'",ValidationSnipets.sanitizeCRLF(username));
		log.info("Email: {}",ValidationSnipets.sanitizeCRLF(email));
		log.info("First Name: {}",ValidationSnipets.sanitizeCRLF(fname));
		log.info("Last Name: {}",ValidationSnipets.sanitizeCRLF(lname));
		log.info("DOB: {}",ValidationSnipets.sanitizeCRLF(dob_raw));
		
		// Die if username is not unique
		if(ValidationSnipets.isUsernameTaken(username)) {
			req.setAttribute("authMessage", "Username already taken");
			reqDis.forward(req, res);
			return;
		}
		
		
		// Validate the email
		if(email == null || !ValidationSnipets.isValidEmail(email)) {
			req.setAttribute("authMessage", "Invalid email address");
			reqDis.forward(req, res);
			return;
		}
		
		// Die is email is not unique
		if(ValidationSnipets.isEmailTaken(email)) {
			req.setAttribute("authMessage", "Email already taken");
			reqDis.forward(req, res);
			return;
		}
		
		
		// Validate first and last names
		if (fname == null || fname.length() < 1) {
			req.setAttribute("authMessage", "First name can't be blank. If you have a mononym, leave Last Name blank");
			reqDis.forward(req, res);
			return;
		}
		
		// Catch in case of mononyms
		if(lname == null) {
			lname = "";
		}
		
		
		// Validate DOB
		if (dob_raw == null) {
			req.setAttribute("authMessage", "Date of Bith can't be blank");
			reqDis.forward(req, res);
			return;
		}
		try {
			dob = ValidationSnipets.parseDate(dob_raw);
		} catch (ParseException e) {
			// Die if DOB can't be parsed
			req.setAttribute("authMessage", "Invalid Date of Birth Format. Need yyyy-mm-dd");
			reqDis.forward(req, res);
			return;
		}
		
		
		// Validate Password
		if (pass1 == null || pass1.length() < 1 || pass2 ==null || pass2.length() < 1) {
			req.setAttribute("authMessage", "Password can't be blank");
			reqDis.forward(req, res);
			return;
		}
		
		if (!ValidationSnipets.passwordFormatValid(pass1)) {
			req.setAttribute("authMessage", "Password must be at least 8 characters long and contain only a-z,A-z,0-9,!,#,$,^");
			reqDis.forward(req, res);
			return;
		}
		
		if (!pass1.equals(pass2)) {
			req.setAttribute("authMessage", "Passwords don't match");
			reqDis.forward(req, res);
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
		req.setAttribute("authMessage", "Registration Successful, go ahead and login!");
		req.setAttribute("regUsername", null);
		req.setAttribute("regEmail", null);
		req.setAttribute("regFirstName", null);
		req.setAttribute("regLastName", null);
		req.setAttribute("regDOB", null);
		reqDis.forward(req, res);
		sess.close();
	}
}
