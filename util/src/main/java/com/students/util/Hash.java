package com.students.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
	private static MessageDigest alg;
	
	static {
		try {
			alg = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static byte[] hash(byte[] password) {
		return alg.digest(password);
	}
	
	public static byte[] hash(String password, String salt, int position) {
		var saltBytes = salt.getBytes();
		var simpleHash = hash(password.getBytes());
		var saltHash = new byte[simpleHash.length + saltBytes.length];
		
		System.arraycopy(simpleHash, 0, saltHash, 0, position);
		System.arraycopy(saltBytes, 0, saltHash, 0, saltBytes.length);
		System.arraycopy(simpleHash, position, saltHash, position + saltBytes.length, simpleHash.length - position);
		
		
		return hash(saltHash);
	}
}
