package com.tangjianghua.common.util;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttPayload {
	private static final Logger LOGGER = LoggerFactory.getLogger(MqttPayload.class);
	/**
	 * 
	 * @param paylaoad 打印内容
	 * @param cnt 打印联数
	 * @return
	 */
	public static byte[] fixPayload(String paylaoad, Integer cnt) {
		if(cnt == null || (1>cnt || cnt>3)) return paylaoad.getBytes();
		LOGGER.debug("要转换的payload:{},  CNT:{}",paylaoad,cnt);
		byte[] a = toBytes("1E10");
		String content = paylaoad;
		byte[] d = content.getBytes();
		Integer length = d.length;
		LOGGER.debug("要转换的payload.length:{}",length);
		byte b = unsignedShortToByte2(cnt)[1];
		byte[] c = unsignedShortToByte2(length);

		byte[] total = new byte[length + a.length + 1 + 2];
		System.arraycopy(a, 0, total, 0, a.length);
		total[a.length] = b;
		System.arraycopy(c, 0, total, a.length + 1, c.length);
		System.arraycopy(d, 0, total, a.length + 1 + c.length, d.length);
		LOGGER.debug("要转换的payload.Hex:{}",bytesToHexString(total));
		return total;
	}

	public static byte[] toBytes(String str) {
		if (str == null || str.trim().equals("")) {
			return new byte[0];
		}

		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			String subStr = str.substring(i * 2, i * 2 + 2);
			bytes[i] = (byte) Integer.parseInt(subStr, 16);
		}

		return bytes;
	}

	public static byte[] unsignedShortToByte2(int s) {
		byte[] targets = new byte[2];
		targets[0] = (byte) (s >> 8 & 0xFF);
		targets[1] = (byte) (s & 0xFF);
		return targets;
	}
	
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
}
