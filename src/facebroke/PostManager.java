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
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.Post;
import facebroke.model.Post.PostType;
import facebroke.model.User;
import facebroke.model.Wall;
import facebroke.util.FacebrokeException;
import facebroke.util.HibernateUtility;
import facebroke.util.ValidationSnipets;


/**
 * Handle /wall endpoint.
 * 
 *   GET -> Handles pagination and returning the correct ranges of posts to users for a particular wall
 *   POST -> Allows a user to create a new Post on a given Wall
 * 
 * @author matt @ Software Secured
 */
@WebServlet("/wall")
public class PostManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(PostManager.class);
	private static final String INVALID_WALL_ID = "The given Wall does not exist";
	private static final int POSTS_PER_PAGE = 20;

	
	/**
	 * Call parent Servlet constructor
	 */
	public PostManager() {
		super();
	}

	
	/**
	 * Handle GET requests. Means returning a Wall JSP with the correct request parameters to render a page
	 * Accepts the following parameters:
	 *   user_id -> the user_id corresponding to the wall being requested. -1 implies get the general newsfeed (all posts)
	 *   start -> the id of the first post to be on the requested page
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		if(!ValidationSnipets.isValidSession(req.getSession())){
			res.sendRedirect("index");
			return;
		}
		
		RequestDispatcher reqDis;
		
		
		int pageStart, postsPerPage;
		long user_id;
		
		try {
			user_id = Long.parseLong(req.getParameter("user_id"));
			reqDis = req.getRequestDispatcher("wall.jsp");
		}catch(NumberFormatException e) {
			user_id = -1;
			reqDis = req.getRequestDispatcher("feed.jsp");
		}
		log.info("User id: {}",user_id);
		
		try {
			pageStart = Integer.parseInt(req.getParameter("start"));
		}catch(NumberFormatException e) {
			pageStart = 0;
			
		}
		
		req.setAttribute("start",pageStart);
		
		try {
			postsPerPage = (int)req.getSession().getAttribute("postsPerPage");
		}catch (Exception e) {
			postsPerPage = POSTS_PER_PAGE;
			req.getSession().setAttribute("postsPerPage",postsPerPage);
		}
		
		Session sess = HibernateUtility.getSessionFactory().openSession();
		
		List<Post> posts;
		
		// We want to get the main newsfeed
		if(user_id == -1) {
			posts = sess.createQuery(
					"FROM Post p ORDER BY p.created desc")
					.setFirstResult(pageStart)
					.setMaxResults(postsPerPage)
					.list();
		}else {
			log.info("Get wall with user id = "+user_id);
			List<User> results = sess.createQuery("FROM User u WHERE u.id = :user_id")
					.setParameter("user_id", user_id).list();

			if (results.size() < 1) {
				req.setAttribute("serverMessage", INVALID_WALL_ID);
				req.getRequestDispatcher("error.jsp").forward(req, res);
				sess.close();
				return;
			}

			User wallOwner = results.get(0);
			req.setAttribute("wall_owner", wallOwner);

			posts = sess.createQuery(
						"FROM Post p where p.wall.id = :wall_id ORDER BY p.created desc")
						.setParameter("wall_id", wallOwner.getId())
						.setFirstResult(pageStart)
						.setMaxResults(postsPerPage)
						.list();
			
		}

		req.setAttribute("posts", posts);
		
		sess.close();
		reqDis.forward(req, res);
		return;
	}


	/**
	 * Handle POST request. A User sends a request to create a new Post on a Wall
	 * Accepts the following parameters:
	 *   user_id -> the ID of the target user, the owner of the wall this is being psoted to
	 *   creator_id -> the ID of the user creating the post
	 *   type -> the type of the content string (only really handling TEXT right now)
	 *   content -> the content string
	 *   on_wall -> if not null or empty, then we're sending the request on a wall
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if(!ValidationSnipets.isValidSession(req.getSession())){
			res.sendRedirect("index");
			return;
		}
		
		Session sess = HibernateUtility.getSessionFactory().openSession();
		
		
		String wall_id_string = req.getParameter("user_id");
		String creator_id_string = req.getParameter("creator_id");
		String type_string = req.getParameter("type");
		String content = Encode.forHtml(req.getParameter("content"));
		String on_wall = req.getParameter("on_wall");
		
		log.info("Received POST for a new post");
		log.info("Wall ID: {}",ValidationSnipets.sanitizeCRLF(wall_id_string));
		log.info("Creator ID: {}",ValidationSnipets.sanitizeCRLF(creator_id_string));
		log.info("Content Type: {}",ValidationSnipets.sanitizeCRLF(type_string));
		
		Wall target;
		User creator;
		PostType type;
		
		
		try {
			// Validate Wall ID
			long wall_id = Long.parseLong(wall_id_string);
			
			List<Wall> walls = sess.createQuery("FROM Wall w WHERE w.id = :wall_id")
							 .setParameter("wall_id", wall_id)
							 .list();
			
			if(walls.isEmpty()) {
				throw new FacebrokeException("Invalid Wall id");
			}
			
			target = walls.get(0);
			
			
			
			// Validate User ID
			long user_id = Long.parseLong(creator_id_string);
			
			List<User> users = sess.createQuery("FROM User u WHERE u.id = :user_id")
					 						.setParameter("user_id", user_id)
					 						.list();
			
			if (users.isEmpty()) {
				throw new FacebrokeException("Invalid User id");
			}
			
			creator = users.get(0);
			
			
			
			// Validate Content Type
			if (type_string.equals("TEXT")) {
				type = PostType.TEXT;
				
			}else if(type_string.equals("IMAGE")) {
				type = PostType.IMAGE;
				throw new FacebrokeException("Unimplemented Content Type: "+type_string);
				
			} else if(type_string.equals("LINK")) {
				type = PostType.LINK;
				throw new FacebrokeException("Unimplemented Content Type: "+type_string);
				
			} else {
				throw new FacebrokeException("Invalid Content Type: "+type_string);
			}
			
			
			
			// BAD IDEA but temporarily treat all content as valid
			if(content.isEmpty()) {
				throw new FacebrokeException("Post content can't be empty");
			}
			
		}catch(FacebrokeException e) {
			req.setAttribute("serverMessage", e.getMessage());
			req.getRequestDispatcher("error.jsp").forward(req, res);
			sess.close();
			return;
		}catch(NumberFormatException e) {
			req.setAttribute("serverMessage", "Could not parse ID: "+e.getMessage());
			req.getRequestDispatcher("error.jsp").forward(req, res);
			sess.close();
			return;
		}

		
		Post p = new Post(target, creator, type, content);
		sess.beginTransaction();
		sess.save(p);
		sess.getTransaction().commit();
		sess.close();
		
		log.info("Created a new post");
		
		if(on_wall == null || on_wall.equals("")) {
			// Must be on Newsfeed so go back there
			res.sendRedirect("index");
		}else {
			// Must be on a User's wall so return there
			res.sendRedirect("wall?user_id="+target.getId());
		}
		
	}
}
