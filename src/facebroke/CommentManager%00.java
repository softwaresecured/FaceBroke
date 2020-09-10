package facebroke;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.Comment;
import facebroke.model.Post;
import facebroke.model.User;
import facebroke.util.FacebrokeException;
import facebroke.util.HibernateUtility;
import facebroke.util.ValidationSnipets;



/**
 * This class represents the /comment endpoint.
 * Only supports the POST operation, which is used to create a new Comment on an existing Post
 *
 */
@WebServlet("/comment")
public class CommentManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(CommentManager.class);

	/**
	 * Call base constructor
	 */
    public CommentManager() {
        super();
    }
    
    /**
     * A stub method to look for "delete" in the arguments and pass to delete fucntion for a comemnt
     */
    @Override
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		if(!ValidationSnipets.isValidSession(req.getSession())){
			res.sendRedirect("index");
			return;
		}
		
		String delete = req.getParameter("delete");
		
		if(delete != null && delete.equals("delete")) {
			log.info("Passing to DELETE");
			doDelete(req, res);
			return;
		}
    }


    /**
     * Handle the creation of a new Comment on Post. Requires the following parameters:
     *   creator_id -> the numerical id of the creating user
     *   post_id ->  
     */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if(!ValidationSnipets.isValidSession(req.getSession())){
			res.sendRedirect("index");
			return;
		}
		
		log.info("Loading session factory");
		Session sess = HibernateUtility.getSessionFactory().openSession();
		
		log.info("Fetching request parameters");
		String creator_id_string = req.getParameter("creator_id");
		String post_id_string = req.getParameter("post_id");
		String content = Encode.forHtml(req.getParameter("content"));
		String on_wall = req.getParameter("on_wall");
		
		log.info("Creating comment");
		log.info("Creator ID: {}",ValidationSnipets.sanitizeCRLF(creator_id_string));
		log.info("Post ID: {}",ValidationSnipets.sanitizeCRLF(post_id_string));
		log.info("Content: {}",ValidationSnipets.sanitizeCRLF(content));
		
		User creator;
		Post target;
		long start = 0;
		
		
		try {
			// Validate user
			long creator_id = Long.parseLong(creator_id_string);
			
			// validate start
			start = Long.parseLong(req.getParameter("start"));
			if(start < 0) {
				start = 0;
			}
			
			// Should fix GitHub issue #1
			if(creator_id != (long)req.getSession().getAttribute("user_id")) {
				throw new FacebrokeException("Can't comment as a different user...");
			}
			
			@SuppressWarnings("unchecked")
			List<User> users = sess.createQuery("FROM User u WHERE u.id = :creator_id")
													.setParameter("creator_id", creator_id)
													.list();
			
			if(users.isEmpty()) {
				throw new FacebrokeException("Invalid creator id");
			}
			
			creator = users.get(0);
			log.info("Loaded creator: {}",creator.getUsername());

			
			
			// Validate Post ID
			long post_id = Long.parseLong(post_id_string);
			@SuppressWarnings("unchecked")
			List<Post> posts = sess.createQuery("FROM Post p WHERE p.id = :post_id")
													.setParameter("post_id", post_id)
													.list();
			
			if(posts.isEmpty()) {
				throw new FacebrokeException("Invlaid post id");
			}
			
			target = posts.get(0);
			log.info("Loaded target Post: {}",target.getId());
			
			
			if(content.isEmpty()) {
				throw new FacebrokeException("Comment content can't be empty");
			}
		
			// Only catch these errors, RuntimeErrors propagate
		}catch (FacebrokeException e) {
			req.setAttribute("serverMessage", e.getMessage());
			req.getRequestDispatcher("error.jsp").forward(req, res);
			sess.close();
			return;
		}
		catch (NumberFormatException e) {
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
		
		if(on_wall == null || on_wall.equals("")) {
			res.sendRedirect("index?start="+start+"#"+c.getParent().getId());
		}else {
			res.sendRedirect("wall?user_id="+target.getWall().getId()+"&start="+start+"#"+c.getParent().getId());
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if(!ValidationSnipets.isValidSession(req.getSession())){
			res.sendRedirect("index");
			return;
		}
		
		Session sess = HibernateUtility.getSessionFactory().openSession();
		sess.beginTransaction();
		
		log.info("{}",req.getParameter("start"));
		
		String on_wall = req.getParameter("on_wall");
		String wall_id = req.getParameter("wall_id");
		Comment c;
		
		long start = 0;
		
		try {
			
			long comment_id = Long.parseLong(req.getParameter("comment_id"));
			long user_id = (long) req.getSession().getAttribute("user_id");
			
			// validate start
			start = Long.parseLong(req.getParameter("start"));
			if(start < 0) {
				start = 0;
			}
			
			// Get the current User
			List<User> users = sess.createQuery("FROM User u WHERE u.id = :user_id")
						.setParameter("user_id", user_id)
						.list();

			if (users.isEmpty()) {
				throw new FacebrokeException("Invalid User id");
			}
			
			User u  = users.get(0);
			
			// Get the target Comment
			List<Comment> comments = sess.createQuery("FROM Comment c WHERE c.id = :comment_id")
					.setParameter("comment_id", comment_id)
					.list();

			if (comments.isEmpty()) {
				throw new FacebrokeException("Invalid Comment id");
			}
		
			c  = comments.get(0);
			
			if(c.getCreator().equals(u) || u.getRole().equals(User.UserRole.ADMIN) || c.getParent().getWall().getUser().equals(u)) {
				
				// Need to delete and remove it from the parent collection
				
				List<Post> posts = sess.createQuery("FROM Post p WHERE p.id = :post_id")
						.setParameter("post_id", c.getParent().getId())
						.list();
				
				Post p = posts.get(0);
				p.deleteComment(c);
				sess.save(p);
				sess.delete(c);
				
				log.info("Deleted Comment with ID: {}",c.getId());
			}else {
				throw new FacebrokeException("You don't have permissions to delete this comment");
			}
			
			
		} catch (FacebrokeException e) {
			req.setAttribute("serverMessage", e.getMessage());
			req.getRequestDispatcher("error.jsp").forward(req, res);
			sess.getTransaction().commit();
			sess.close();
			return;
		} catch (NumberFormatException e) {
			req.setAttribute("serverMessage", "Could not parse ID: "+e.getMessage());
			req.getRequestDispatcher("error.jsp").forward(req, res);
			sess.getTransaction().commit();
			sess.close();
			return;
		}
		
		
		if(on_wall == null || on_wall.equals("")) {
			res.sendRedirect("index?start="+start+"#"+c.getParent().getId());
		}else {
			res.sendRedirect("wall?user_id="+wall_id+"&start="+start+"#"+c.getParent().getId());
		}
		
		sess.getTransaction().commit();
		sess.close();
		return;
	}
}
