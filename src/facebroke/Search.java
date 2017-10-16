package facebroke;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet to handle the /search endpoint.
 * 
 * Basically, this will handle search requests and render a results
 * page out to the requesting user
 * 
 * @author matt @ Software Secured
 */
@WebServlet("/search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	/**
	 * Call parent servlet
	 */
    public Search() {
        super();
    }


    /**
     * Simple shim to pass GET requests to handleSearch
     */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		handleSearch(req, res);
	}


	/**
     * Simple shim to pass POST requests to handleSearch
     */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		handleSearch(req, res);
	}

	
	private void handleSearch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
	}
}
