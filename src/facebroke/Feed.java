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
		
		req.setAttribute("user_id", null);
		req.getRequestDispatcher("wall").forward(req, res);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
