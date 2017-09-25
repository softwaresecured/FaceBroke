package facebroke.util;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpSession;

public class ValidationSnipets {
	
	/**
	 * Returns true if the current session can be considered "Valid"
	 * @param session - the session object visible in JSP
	 * @return - bool - true if the session is valid for our purposes
	 */
	public static boolean isValidSession(HttpSession session) {
		String validVal = (String)session.getAttribute("valid");
		return !(session.isNew() || validVal == null || validVal == "false");
	}
	
	public static boolean isValidEmail(String email) {
		boolean result = false;
		try {
			InternetAddress addr = new InternetAddress(email);
			addr.validate();
			result = true;
		} catch (Exception e) {
			// Don't need to do anything, the simple 'false' return will tell enough
		}
		return result;
	}
}
