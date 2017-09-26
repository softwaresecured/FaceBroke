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
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.User;
import facebroke.util.HibernateUtility;
import facebroke.util.ValidationSnipets;

public class Login extends HttpServlet {

	private static Logger log = LoggerFactory.getLogger(Login.class);
	private static final long serialVersionUID = 1L;

	private static final String INVALID_LOGIN_CREDS = "Invalid Login Credentials";

	public Login() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		handleLogin(req, res);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		handleLogin(req, res);
	}

	protected void handleLogin(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		RequestDispatcher reqDis = req.getRequestDispatcher("index.jsp");

		String user_cred = (String)req.getParameter("user_cred");
		String pass = (String)req.getParameter("password");

		if(user_cred == null || pass == null) {
			req.setAttribute("serverMessage", INVALID_LOGIN_CREDS);
			reqDis.forward(req, res);
			return;
		}

		if(user_cred == "" || pass == "") {
			req.setAttribute("serverMessage", "Credentials can't be blank");
			reqDis.forward(req, res);
			return;
		}


		// Creds are not null, so need to try and validate against the db

		Session sess = HibernateUtility.getSessionFactory().openSession();

		List<User> results = null;

		Query<User> query;

		if(ValidationSnipets.isValidEmail(user_cred)) {
			query = sess.createQuery("FROM User U WHERE U.email = :user_cred");
		}else {
			query = sess.createQuery("FROM User U WHERE U.username = :user_cred");
		}

		results = query.setParameter("user_cred", user_cred).list();

		log.info("Size of result list: " + results.size());

		if(results.size() < 1) {
			req.setAttribute("serverMessage", INVALID_LOGIN_CREDS);
			reqDis.forward(req, res);
			sess.close();
			return;
		}

		User candidate = results.get(0);

		if(candidate.isPasswordValid(pass)) {
			req.getSession().setAttribute("valid", "true");
			req.getSession().setAttribute("user_id", candidate.getId());
			req.getSession().setAttribute("user_username", candidate.getUsername());
			req.getSession().setAttribute("user_fname", candidate.getFname());
			req.getSession().setAttribute("user_lname", candidate.getLname());
			res.sendRedirect("index.jsp");
		}else {
			log.info("Invalid password entered");
			req.setAttribute("serverMessage", INVALID_LOGIN_CREDS);
			reqDis.forward(req, res);
		}

		sess.close();
	}
}
