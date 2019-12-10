package com.tangjianghua.common.util;

import java.security.MessageDigest;

public class MD5Encryptor {
	public static String byteArrayToHexString(byte[] bytes) {
        StringBuffer hs = new StringBuffer();
        for (int n = 0; n < bytes.length; n++) {
            String stmp = (Integer.toHexString(bytes[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0");
            }
            hs.append(stmp);
        }
        return hs.toString();
    }

    public static String MD5Encode(String msg) {
        String resultString = null;
        try {
            resultString = msg;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
        } catch (Exception ex) {
        	ex.printStackTrace();
        	return null;
        }
        return resultString.toUpperCase();
    }
    
}
