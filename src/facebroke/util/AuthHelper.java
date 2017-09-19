package facebroke.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AuthHelper {
	
	private final static int SALTLENGTH = 32;
	
	public static void main(String[] args) {
		byte[] salt = generateSalt();
		
		String hp = hashPassword("MyPassword", salt);
		String hp2 = hashPassword("helloworld", salt);
		String hp3 = hashPassword("MyPassword1", salt);
		
		System.out.println("Hashed: " + hp);
		System.out.println("Hashed: " + hp2);
		System.out.println("Hashed: " + hp3);
		System.out.println("Encoded salt: " + encodeBase64(salt));
		
		String b64Salt = encodeBase64(salt);
		String hp4 = hashPassword("MyPassword", decodeBase64(b64Salt));
		System.out.println("Hashed: " + hp4);
		
	}
	
	public static byte[] generateSalt() {
		byte[] salt = new byte[SALTLENGTH];
		
		try {
			SecureRandom r = SecureRandom.getInstance("SHA1PRNG");
			r.nextBytes(salt);
		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return salt;
	}
	
	// Returns a Base64 encoded version of the hash of the salt and password
	public static String hashPassword(String pass, byte[] salt){
		String result = null;
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			md.update(salt);
			md.update(pass.getBytes(StandardCharsets.UTF_8));
			
			result = Base64.getEncoder().encodeToString(md.digest());
			
		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static String encodeBase64(byte[] arr) {
		return Base64.getEncoder().encodeToString(arr);
	}
	
	public static byte[] decodeBase64(String str) {
		return Base64.getDecoder().decode(str);
	}
}
