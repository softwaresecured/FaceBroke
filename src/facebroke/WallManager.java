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

@WebServlet("/wall")
public class WallManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(WallManager.class);
	private static final String INVALID_WALL_ID = "The given Wall does not exist";
	private static final int POSTS_PER_PAGE = 100;

	public WallManager() {
		super();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		if(!ValidationSnipets.isValidSession(req.getSession())){
			res.sendRedirect("index.jsp");
			return;
		}

		long wall_id;
		
		try {
			wall_id = Long.parseLong(req.getParameter("wall_id"));
		}catch(NumberFormatException e) {
			wall_id = (long) req.getSession().getAttribute("user_wall_id");
		}
		
		Session sess = HibernateUtility.getSessionFactory().openSession();
		log.info("Get wall with id = "+wall_id);
		List<User> results = sess.createQuery("FROM User u WHERE u.wall.id = :wall_id")
				.setParameter("wall_id", wall_id).list();

		if (results.size() < 1) {
			req.setAttribute("serverMessage", INVALID_WALL_ID);
			req.getRequestDispatcher("error.jsp").forward(req, res);
			sess.close();
			return;
		}

		User wallOwner = results.get(0);
		req.setAttribute("wall_owner", wallOwner);

		@SuppressWarnings("unchecked")
		List<Post> posts = (List<Post>)sess.createQuery(
				"FROM Post p where p.wall.id = :wall_id ORDER BY p.created desc")
				.setParameter("wall_id", wallOwner.getId())
				.setMaxResults(POSTS_PER_PAGE)
				.list();

		
		
		req.setAttribute("wall_posts", posts);
		

		sess.close();
		req.getRequestDispatcher("wall.jsp").forward(req, res);
		return;
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

	}
}
