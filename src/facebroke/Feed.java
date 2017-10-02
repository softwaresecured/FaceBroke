package facebroke;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import facebroke.model.Post;
import facebroke.model.User;
import facebroke.util.HibernateUtility;
import facebroke.util.ValidationSnipets;

/**
 * Servlet implementation class Feed
 */
@WebServlet("/index")
public class Feed extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int POSTS_PER_PAGE = 20;

    public Feed() {
        super();
    }

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if(!ValidationSnipets.isValidSession(req.getSession())){
			res.sendRedirect("register");
			return;
		}

		Session sess = HibernateUtility.getSessionFactory().openSession();
		
		long user_id = (long)req.getSession().getAttribute("user_id");
		
		@SuppressWarnings("unchecked")
		List<Post> posts = (List<Post>)sess.createQuery(
				"FROM Post p ORDER BY p.created desc")
				.setMaxResults(POSTS_PER_PAGE)
				.list();
		
		req.setAttribute("posts", posts);
		

		sess.close();
		req.getRequestDispatcher("feed.jsp").forward(req, res);
		return;
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
