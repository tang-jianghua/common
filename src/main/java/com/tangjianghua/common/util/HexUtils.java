package com.tangjianghua.common.util;

public class HexUtils {

	public static String bytesToHex(byte[] bytes) {
		StringBuilder buf = new StringBuilder(bytes.length * 2);
		for(byte b : bytes) {
			buf.append(String.format("%02x", new Integer(b & 0xff)));
			buf.append(" ");
		}
		return buf.toString();
	}
	public static String byteToHex(byte b) {
		StringBuilder buf = new StringBuilder(2);
			buf.append(String.format("%02x", new Integer(b & 0xff)));
		return buf.toString();
	}

	public static byte[] hexStrToBytes(String hexStr) {
		if (hexStr == null || hexStr.trim().equals("")) {
			return new byte[0];
		}
		byte[] bytes = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			String subStr = hexStr.substring(i * 2, i * 2 + 2);
			bytes[i] = (byte) Integer.parseInt(subStr, 16);
		}
		return bytes;

	}

}
