package com.fxl.frame.util.encrypt;

import java.security.MessageDigest;

public class Sha1Util {

	public static String getSha1(String str){
		String sha1 = null;
		try {
			MessageDigest mDigest = MessageDigest.getInstance("SHA1");
			byte[] result = mDigest.digest(str.getBytes("utf-8"));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < result.length; i++) {
				sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sha1;
	}

}
