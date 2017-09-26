package facebroke.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
	
	
	public static Date parseDate(String date) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd").parse(date);
	}
	
	
	public static boolean passwordFormatValid(String pass) {
		
		// length
		if (pass.length() > 32 || pass.length() < 8) {
			return false;
		}
		
		List<Character> validSymbols = Arrays.asList('!','#','$','^');
		
		for (Character c : pass.toCharArray()) {
			if (!Character.isDigit(c) && !Character.isLetter(c) && !validSymbols.contains(c)) {
				return false;
			}
		}
		
		
		return true;
	}
}
