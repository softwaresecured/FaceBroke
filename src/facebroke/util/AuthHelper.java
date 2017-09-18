package facebroke.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AuthHelper {
	
	private final static int SALTLENGTH = 32;
	
	public static void main(String[] args) {
		String salt = generateSalt();
		
		for(int i=0; i<SALTLENGTH; i++) {
			System.out.println(i + " - " + (salt.charAt(i) & 0x00FF));
		}
	}
	
	// BROKEN
	public static String generateSalt() {
		SecureRandom r = new SecureRandom();
		byte[] salt = new byte[SALTLENGTH];
		r.nextBytes(salt);
		return new String(salt);
	}
	
	public static String hashPassword(String pass, String salt){
		String result = null;
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
