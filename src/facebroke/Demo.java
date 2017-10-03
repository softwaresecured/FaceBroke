package facebroke;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.Post;
import facebroke.model.User;
import facebroke.util.HibernateUtility;
import facebroke.util.ValidationSnipets;


@WebServlet("/demo")
public class Demo extends HttpServlet {

	private static Logger log = LoggerFactory.getLogger(Demo.class);
	private static final long serialVersionUID = 1L;
	private final static int PAGESIZE = 20;


	public Demo() {
		super();
	}

	@Override
	@SuppressWarnings({ "deprecation", "unchecked" })
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		if(!ValidationSnipets.isValidSession(req.getSession())){
			res.sendRedirect("index");
			return;
		}

		Session sess = HibernateUtility.getSessionFactory().openSession();

		String userid = req.getParameter("userid");

		List<User> results = null;

		if (userid != null && userid.length() > 0) {

			if (req.getParameter("injection").equals("allow")) {
				results = sess.createSQLQuery("select * from Users WHERE ID = " + userid).addEntity(User.class).list();
			} else {
				try {
				results = sess.createQuery("FROM User U WHERE U.id = :user_id")
						.setParameter("user_id", Long.parseLong(userid)).list();
				}catch(NumberFormatException e) {
					req.setAttribute("serverMessage","That's not a valid number........");
					req.getRequestDispatcher("error.jsp").forward(req, res);
					sess.close();
					return;
				}
			}
		}

		req.setAttribute("rows", results);

		log.info("Starting JSP");
		req.getRequestDispatcher("user_dump.jsp").forward(req, res);
		log.info("Gave up control");

		sess.close();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if(!ValidationSnipets.isValidSession(req.getSession())){
			res.sendRedirect("index");
			return;
		}

		req.getRequestDispatcher("demo.jsp").forward(req, res);
	}


	public static void main(String[] args) {
		Session sess = HibernateUtility.getSessionFactory().openSession();
		
		int start = 1500;
		
		sess.beginTransaction();
		
		List<Post> results = (List<Post>)sess.createQuery("FROM Post p ORDER BY p.id desc")
								 .setFirstResult(start)
								 .setMaxResults(PAGESIZE)
								 .list();
		
		sess.getTransaction().commit();
		sess.close();
		
		for(Post p : results) {
			System.out.println(p.getId());
		}
		
		System.exit(0);
	}
}
