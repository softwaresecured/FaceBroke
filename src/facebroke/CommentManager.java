package facebroke;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.Comment;
import facebroke.model.Post;
import facebroke.model.User;
import facebroke.util.FacebrokeException;
import facebroke.util.HibernateUtility;
import facebroke.util.ValidationSnipets;


@WebServlet("/comment")
public class CommentManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(CommentManager.class);

    public CommentManager() {
        super();
    }


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if(!ValidationSnipets.isValidSession(req.getSession())){
			res.sendRedirect("index");
			return;
		}
		
		Session sess = HibernateUtility.getSessionFactory().openSession();
		
		RequestDispatcher reqDis = req.getRequestDispatcher("index");
		
		String creator_id_string = req.getParameter("creator_id");
		String post_id_string = req.getParameter("post_id");
		String content = req.getParameter("content");
		
		log.info("Creating comment");
		log.info("Creator ID: "+creator_id_string);
		log.info("Post ID: "+post_id_string);
		log.info("Content: "+content);
		log.info(req.getPathInfo());
		log.info(req.getRequestURI());
		log.info(req.getRequestURL().toString());
		log.info(req.getContextPath());
		
		User creator;
		Post target;
		
		// Validate user
		try {
			long creator_id = Long.parseLong(creator_id_string);
			List<User> results = (List<User>) sess.createQuery("FROM User u WHERE u.id = :creator_id")
													.setParameter("creator_id", creator_id)
													.list();
			
			if(results.isEmpty()) {
				throw new FacebrokeException("Invalid creator id");
			}
			
			creator = results.get(0);
		}catch (Exception e) {
			req.setAttribute("serverMessage", e.getMessage());
			req.getRequestDispatcher("error.jsp").forward(req, res);
			sess.close();
			return;
		}
		
		
		// Validate Post ID
		try {
			long post_id = Long.parseLong(post_id_string);
			List<Post> results = (List<Post>) sess.createQuery("FROM Post p WHERE p.id = :post_id")
													.setParameter("post_id", post_id)
													.list();
			
			if(results.isEmpty()) {
				throw new FacebrokeException("Invlaid post id");
			}
			
			target = results.get(0);
			
		}catch(Exception e) {
			req.setAttribute("serverMessage", e.getMessage());
			req.getRequestDispatcher("error.jsp").forward(req, res);
			sess.close();
			return;
		}
		
		
		// Create the comment
		Comment c = new Comment(creator,target,content);
		sess.beginTransaction();
		sess.save(c);
		sess.getTransaction().commit();
		sess.close();
		
		log.info("Created a new comment");
		
		res.sendRedirect("index");
	}
}
