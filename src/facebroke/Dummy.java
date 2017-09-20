package facebroke;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class Dummy extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(Dummy.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		
		RequestDispatcher reqDis;
		reqDis = req.getRequestDispatcher("/colour.jsp");
		
		
		log.info("Forwarding request {} to {}",req.toString(),res.toString());
		reqDis.forward(req, res);
		
	}
	
	@Override
	@SuppressWarnings({ "deprecation", "unchecked" })
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		
		Session sess = HibernateUtility.getSessionFactory().openSession();
		
		String userid = req.getParameter("userid");
		
		List results = new ArrayList<>();
		results.add("No results");
		
		if(userid != null && userid.length() > 0) {
			
			if(req.getParameter("injection").equals("allow")) {
				results = sess.createSQLQuery("select * from Users WHERE ID = "+userid).addEntity(User.class).list();
			}else {
				results = sess.createQuery("FROM User U WHERE U.id = :user_id")
								.setParameter("user_id", Long.parseLong(userid))
								.list();
			}
		}

		req.setAttribute("rows", results);

		log.info("Starting JSP");
		req.getRequestDispatcher("/user.jsp").forward(req, res);
		log.info("Gave up control");
		
	}
}
