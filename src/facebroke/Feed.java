package facebroke;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facebroke.util.ValidationSnipets;

/**
 * Servlet implementation class Feed
 * 
 * Serves as the index page (the "newsfeed" on the mainpage)
 * 
 * In reality, the feed and a user's wall are the same thing, only difference 
 * is that a wall only contains a certain user's content.
 * 
 * This servlet simply sets the user_id to null and forwards it to the WallManager,
 * which interprets this as the newsfeed
 * 
 */
@WebServlet("/index")
public class Feed extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	/**
	 * Call parent constructor
	 */
    public Feed() {
        super();
    }

    
    /**
     * Handle GET requests to the index. Render the wall JSP to the user if they have a valid session
     */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// If not valid session, send user to registration page
		if(!ValidationSnipets.isValidSession(req.getSession())){
			res.sendRedirect("register");
			return;
		}
		
		req.setAttribute("user_id", "");
		req.getRequestDispatcher("wall").forward(req, res);
	}
}
