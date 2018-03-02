package facebroke;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Handle /logout endpoint. Simply clears the session attributes and kills session
 * 
 */
@WebServlet("/logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(Login.class);
	
	
	/**
	 * Call parent Servlet constructor
	 */
	public Logout() {
		super();
	}
	
	
	/**
	 * Catch GET requests and pass to handleLogout
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		handleLogout(req, res);
	}

	
	/**
	 * Catch POST requests and pass to handleLogout
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		handleLogout(req, res);
	}
	
	
	/**
	 * Handle the cleanup of a user session when they request logout, from either POST or GET
	 * 
	 * @param req - HttpServletRequest from either GET or POST
	 * @param res - HttpServletResponse to pass info back to user
	 * @throws ServletException - Hiccups propagate up
	 * @throws IOException - Hiccups propagate up
	 */
	protected void handleLogout(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		RequestDispatcher reqDis = req.getRequestDispatcher("index");
		HttpSession sess = req.getSession();
		long user_id = (long) req.getSession().getAttribute("user_id");
		sess.setAttribute("valid", "false");
		sess.setAttribute("user_id", "");
		sess.setAttribute("user_username", "");
		sess.setAttribute("user_fname", "");
		sess.setAttribute("user_lname", "");
		sess.invalidate();
		
		log.info("User {} logged out",user_id);
		reqDis.forward(req, res);
	}
}
