package facebroke;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.User;
import facebroke.util.FacebrokeException;
import facebroke.util.HibernateUtility;
import facebroke.util.ValidationSnipets;


@WebServlet("/settings")
public class Settings extends HttpServlet {
	private static Logger log = LoggerFactory.getLogger(Settings.class);
	private static final long serialVersionUID = 1L;

    public Settings() {
        super();
    }


	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		log.info("Got "+req.getParameterMap().size()+" paramters to GET");
		
		if (!ValidationSnipets.isValidSession(req.getSession())) {
			res.sendRedirect("index.jsp");
			log.info("Failed to validate session with \"valid\"=" + req.getSession().getAttribute("valid"));
			return;
		}
		
		String target_id_string = req.getParameter("id");
		
		log.info("Received GET param \"id\"="+target_id_string);
		
		// Validate permissions
		try {
			Session sess = HibernateUtility.getSessionFactory().openSession();
			long target_id = Long.parseLong(target_id_string);
			@SuppressWarnings("unchecked")
			List<User> target_list = (List<User>) sess.createQuery("FROM User u where u.id = :user_id")
									.setParameter("user_id", target_id)
									.list();
			@SuppressWarnings("unchecked")
			List<User> current_user_list = (List<User>) sess.createQuery("FROM User u where u.id = :user_id")
											.setParameter("user_id", (long)req.getSession().getAttribute("user_id"))
											.list();
			
			if(target_list == null || target_list.isEmpty() || current_user_list == null || current_user_list.isEmpty()) {
				throw new FacebrokeException("User with id = \""+target_id+"\" is not currently accessible");
			}
			User target = target_list.get(0);
			User current_user = current_user_list.get(0);
			
			if(target == null) {
				sess.close();
				throw new FacebrokeException("Invalid user id  for Settings page");
			}
			
			if(target.getId() != current_user.getId() && !current_user.getRole().equals(User.UserRole.ADMIN)) {
				sess.close();
				throw new FacebrokeException("User with id = \""+current_user.getId()+"\" has insufficient privileges to lookup other users' settings");
			}
			
			
			// If we made it here, the current user is either an admin or owner of the account specified
			req.getSession().setAttribute("target", target);
			req.setAttribute("target_user_id", target.getId());
			req.getRequestDispatcher("settings.jsp").forward(req, res);
			sess.close();
			
		}catch(FacebrokeException e) {
			req.setAttribute("serverMessage", e.getMessage());
			req.getRequestDispatcher("error.jsp").forward(req, res);
			return;
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		log.info("Got "+req.getParameterMap().size()+" paramters to POST");
		if (!ValidationSnipets.isValidSession(req.getSession())) {
			res.sendRedirect("index.jsp");
			log.info("Failed to validate session with \"valid\"=" + req.getSession().getAttribute("valid"));
			return;
		}
		
		log.info(req.getRequestURI());
		log.info(req.getRequestURL().toString());
		log.info(req.getQueryString());
		log.info(req.getContextPath());
		
		
		
		String target_user_id_string = req.getParameter("target_id");
		String username = req.getParameter("regUsername");
		String email = req.getParameter("regEmail");
		String fname = req.getParameter("regFirstName");
		String lname = req.getParameter("regLastName");
		String pass1 = req.getParameter("regPassword");
		String pass2 = req.getParameter("regPasswordConfirm");
		
		// immediately add the id back as a parameter
		// better way would be session var, but I want provide an attack surface
		req.setAttribute("target_user_id",target_user_id_string);
		
		String forwardPath = "settings?id="+target_user_id_string;
		//RequestDispatcher reqDis = req.getRequestDispatcher(forwardPath);
		log.info("Forward path is "+forwardPath);
		
		// Get a session to fetch the target user to be updated
		long target_id = Long.parseLong(target_user_id_string);
		Session sess = HibernateUtility.getSessionFactory().openSession();
		@SuppressWarnings("unchecked")
		List<User> target_list = (List<User>) sess.createQuery("FROM User u where u.id = :user_id")
								.setParameter("user_id", target_id)
								.list();
		
		try {
			if(target_list == null || target_list.isEmpty()) {
				throw new FacebrokeException("User with id = \""+target_id+"\" is not currently accessible");
			}
			
			User target = target_list.get(0);
			List<String> errors = new ArrayList<>();
			List<String> changes = new ArrayList<>();
			
			
			
			// Change the user name if needed
			if (username != null && !target.getUsername().equals(username)) {
				if(!ValidationSnipets.isUsernameTaken(username)) {
					target.setUsername(username);
					changes.add("Username updated");
					log.info("Username updated");
				}else {
					errors.add("Username is unavaialble");
				}
			}
			
			
			// Change the email if needed
			if(email != null && !target.getEmail().equals(email)) {
				if(!ValidationSnipets.isEmailTaken(email)) {
					target.setEmail(email);
					changes.add("Email is updated");
					log.info("Email is updated");
				}else {
					errors.add("Email is unavaialble");
				}
			}
			

			// Change first and last names if needed
			if (fname != null && fname.length() > 0 && !target.getFname().equals(fname)) {
				target.setFname(fname);
				changes.add("First name updated");
				log.info("First name updated");
			}
			
			if(lname != null && lname.length() > 0 && !target.getLname().equals(lname)) {
				target.setLname(lname);
				changes.add("Last name updated");
				log.info("Last name updated");
			}
			
			
			// Validate Password
			if(pass1 == null || pass1.isEmpty()) {
				// DO Nothing since user doesn't want password to change
			}else if (!ValidationSnipets.passwordFormatValid(pass1)) {
				errors.add("Password must be at least 8 characters long and contain only a-z,A-z,0-9,!,#,$,^");
			}else if(target.isPasswordValid(pass1)) {
				// Password unchanged, so do nothing
			}else if(!pass1.equals(pass2)) {
				errors.add("Passwords do not match");
			}else {
				//Got here, so in fact the new passwords match
				// Updated stored password
				target.updatePassword(pass1);
				changes.add("Password updated");
			}
			
			
			if(!errors.isEmpty()) {
				req.getSession().setAttribute("settingsErrors", errors);
			}
			
			// All done processing the input
			// Save the updated user if changed
			// and echo messages out to user
			if(!changes.isEmpty()) {
				sess.beginTransaction();
				sess.update(target);
				sess.getTransaction().commit();
				req.getSession().setAttribute("settingsUpdated", changes);
			}

			
		}catch(FacebrokeException e) {
			req.setAttribute("serverMessage", e.getMessage());
			req.getRequestDispatcher("error.jsp").forward(req, res);
			sess.close();
			log.error(e.getMessage());
			return;
		}
		
		
		// All done, just go back to the settings page
		sess.close();
		res.sendRedirect(forwardPath);
	}

}
