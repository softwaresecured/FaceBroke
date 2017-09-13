import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Dummy extends HttpServlet {
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		String colour = req.getParameter("colour");
		PrintWriter out = res.getWriter();
	
		out.println (
			      "<html> \n" +
			        "<head> \n" +
			          "<title> My first jsp  </title> \n" +
			        "</head> \n" +
			        "<body> \n" +
			          "<font size=\"12px\">" +
			            colour +
			          "</font> \n" +
			        "</body> \n" +
			      "</html>" 
			    );  
	}
}
