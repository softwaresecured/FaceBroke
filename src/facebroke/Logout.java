package facebroke;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.User;
import facebroke.util.HibernateUtility;

public class Logout extends HttpServlet {
	
	private static Logger log = LoggerFactory.getLogger(Login.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		handleLogout(req, res);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		handleLogout(req, res);
	}
	
	/**
	 * Handle the cleanup of a user session when they request logout, from either POST or GET
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void handleLogout(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		RequestDispatcher reqDis = req.getRequestDispatcher("index.jsp");
		HttpSession sess = req.getSession();
		sess.setAttribute("valid", "false");
		sess.setAttribute("user_id", "");
		sess.setAttribute("user_username", "");
		sess.setAttribute("user_fname", "");
		sess.setAttribute("user_lname", "");
		sess.invalidate();
		
		reqDis.forward(req, res);
	}
}
