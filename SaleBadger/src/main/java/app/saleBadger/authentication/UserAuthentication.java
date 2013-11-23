package app.saleBadger.authentication;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Base64;

public class UserAuthentication {

	private static final int iterations = 10 * 1024;
	private static final int saltLen = 32;
	private static final int desiredKeyLen = 256;

	public static String getSaltedHashPassword(String password) {
		byte[] salt = null;
		try {
			salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Base64.encodeBase64(salt) + "$" + hash(password, salt);
	}

	public static boolean check(String password, String storedPassword)
			throws Exception {
		String[] saltAndPassword = storedPassword.split("\\$");
		if (saltAndPassword.length != 2) {
			return false;
		}
		String inputHashedPassword = hash(password,
				Base64.decodeBase64(saltAndPassword[0]));
		return inputHashedPassword.equals(saltAndPassword[1]);
	}

	private static String hash(String password, byte[] salt) {
		if (password == null || password.length() == 0) {
			return null;
		}

		SecretKey key = null;
		try {
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("PBKDF2WithHmacSHA1");
			key = keyFactory.generateSecret(new PBEKeySpec(password
					.toCharArray(), salt, iterations, desiredKeyLen));

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Base64.encodeBase64String(key.getEncoded());
	}
}
