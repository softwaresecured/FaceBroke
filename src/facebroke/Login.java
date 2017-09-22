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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.User;
import facebroke.util.HibernateUtility;

public class Login extends HttpServlet {

	private static Logger log = LoggerFactory.getLogger(Login.class);
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		RequestDispatcher reqDis;
		reqDis = req.getRequestDispatcher("index.jsp");

		log.info("Forwarding request {} to {}", req.toString(), res.toString());
		
		reqDis.forward(req, res);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		RequestDispatcher reqDis = req.getRequestDispatcher("index.jsp");
		
		String user_cred = (String)req.getParameter("user_cred");
		String pass = (String)req.getParameter("password");
		
		if(user_cred == null || pass == null) {
			req.setAttribute("errorMessage", "Invalid Login Credentials");
			reqDis.forward(req, res);
			return;
		}
		
		
		// Creds are not null, so need to try and validate against the db
		
		Session sess = HibernateUtility.getSessionFactory().openSession();
		
		List<User> results = null;
		
		if(validEmail(user_cred)) {
			results = sess.createQuery("FROM User U WHERE U.id = :user_id")
					.setParameter("user_id", Long.parseLong(userid)).list();
		}else {
			results = sess.createQuery("FROM User U WHERE U.id = :user_id")
					.setParameter("user_id", Long.parseLong(userid)).list();
		}
		
		
	}
	
	private static boolean validEmail(String email) {
		boolean result = false;
		try {
			InternetAddress addr = new InternetAddress(email);
			addr.validate();
			result = true;
		} catch (Exception e) {
			// Don't need to do anything, the simple 'false' return will tell enough
		}
		return result;
	}
}
