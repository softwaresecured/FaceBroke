package facebroke.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


/**
 * A static class to hold useful methods for misc. authentication tasks.
 * 
 * No need to ever instantiate.
 * 
 * 
 */
public class AuthHelper {

	/**
	 * Current default, but would require 
	 */
	public final static int SALTLENGTH = 32;

	public static byte[] decodeBase64(String str) {
		return Base64.getDecoder().decode(str);
	}

	public static String encodeBase64(byte[] arr) {
		return Base64.getEncoder().encodeToString(arr);
	}

	/**
	 * Generate a byte array to be used as a salt
	 * 
	 * @param len
	 *            - num of bytes
	 * @return byte[] of salt
	 */
	public static byte[] generateSalt(int len) {
		byte[] salt = new byte[len];

		try {
			SecureRandom r = SecureRandom.getInstance("SHA1PRNG");
			r.nextBytes(salt);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return salt;
	}

	/**
	 * Returns a Base64 encoded version of the hash of the salt and password
	 * 
	 * @param pass
	 *            UTF-8 plaintext password
	 * @param salt
	 *            byte[] to use as salt
	 * @return Base64-encoded hash of salt+password
	 */
	public static String hashPassword(String pass, byte[] salt) {
		String result = null;

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			md.update(salt);
			md.update(pass.getBytes(StandardCharsets.UTF_8));

			result = Base64.getEncoder().encodeToString(md.digest());

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return result;
	}
}
