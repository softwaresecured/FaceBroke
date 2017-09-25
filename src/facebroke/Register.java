package facebroke;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.User;
import facebroke.util.HibernateUtility;
import facebroke.util.JspSnippets;

public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		handleRegistration(req,res);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		handleRegistration(req,res);
	}

	private void handleRegistration(HttpServletRequest req, HttpServletResponse res) throws IOException {
		if (JspSnippets.isValidSession(req.getSession())) {
			res.sendRedirect("index.jsp");
			return;
		}
		
		String username = req.getParameter("regUsername");
		String email = req.getParameter("regEmail");
		String fname = req.getParameter("regFirstName");
		String lname = req.getParameter("regLasttName");
		String dob = req.getParameter("regDOB");
		String pass1 = req.getParameter("regPassword");
		String pass2 = req.getParameter("regPasswordConfirm");
		
		
	}
}
