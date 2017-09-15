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
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.util.HibernateUtility;

public class Dummy extends HttpServlet {
	
	private static Logger log = LoggerFactory.getLogger(Dummy.class);
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		
		RequestDispatcher reqDis;
		reqDis = req.getRequestDispatcher("/colour.jsp");
		
		
		log.info("Forwarding request {} to {}",req.toString(),res.toString());
		reqDis.forward(req, res);
		
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		
		Session s = HibernateUtility.getSessionFactory().openSession();
		String userid = req.getParameter("userid");
		
		List results = new ArrayList<String>();
		results.add("No results");
		
		if(userid != null && userid.length() > 0) {
			results = s.createSQLQuery("select * from Users WHERE ID = "+userid).list();
		}

		req.setAttribute("rows", results);

		log.info("Starting JSP");
		req.getRequestDispatcher("/user.jsp").forward(req, res);
		log.info("Gave up control");
		
	}
}
