package facebroke.util;

import javax.servlet.http.HttpSession;

public class JspSnippets {
	
	/**
	 * Returns true if the current session can be considered "Valid"
	 * @param session - the session object visible in JSP
	 * @return - bool - true if the session is valid for our purposes
	 */
	public static boolean isValidSession(HttpSession session) {
		String validVal = (String)session.getAttribute("valid");
		return !(session.isNew() || validVal == null || validVal == "false");
	}
}
