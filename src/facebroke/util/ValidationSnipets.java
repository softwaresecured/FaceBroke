package facebroke.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.validator.routines.EmailValidator;
import org.hibernate.Session;

import facebroke.model.User;

public class ValidationSnipets {
	
	/**
	 * Returns true if the current session can be considered "Valid"
	 * @param session - the session object visible in JSP
	 * @return - bool - true if the session is valid for our purposes
	 */
	public static boolean isValidSession(HttpSession session) {
		String validVal = (String)session.getAttribute("valid");
		return !(session.isNew() || validVal == null || validVal.equals("false"));
	}
	
	public static boolean isValidEmail(String email) {
		return EmailValidator.getInstance().isValid(email);
	}
	
	
	public static Calendar parseDate(String date) throws ParseException {
		Calendar result = Calendar.getInstance();
		result.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
		return result;
	}
	
	
	public static boolean passwordFormatValid(String pass) {
		// length or null
		if (pass == null || pass.length() > 32 || pass.length() < 8) {
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
	
	
	@SuppressWarnings("unchecked")
	public static boolean isEmailTaken(String email) {
		Session sess = HibernateUtility.getSessionFactory().openSession();
		List<User> results = null;
		results = sess.createQuery("FROM User U WHERE U.email = :email")
								.setParameter("email", email).list();
		sess.close();
		return results.size() > 0;
	}
	
	
	@SuppressWarnings("unchecked")
	public static boolean isUsernameTaken(String username) {
		Session sess = HibernateUtility.getSessionFactory().openSession();
		List<User> results = null;
		results = sess.createQuery("FROM User U WHERE U.username = :username")
								.setParameter("username", username).list();
		sess.close();
		return results.size() > 0;
	}
	
	
	public static String sanitizeCRLF(String source) {
		if(source == null ) {
			return "";
		}
		return source.replaceAll("[\r\n]","");
	}
}
