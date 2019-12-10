package com.tangjianghua.common.util;

import org.springframework.util.Base64Utils;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Des3Util {
	//private static final String Algorithm = "DESede"; // 定义 加密算法,可用
														// DES,DESede,Blowfish
	private static final String Algorithm = "DESede/ECB/PKCS7Padding";
	private static final String hexString = "0123456789ABCDEF";
	private static final String ENCODING = "UTF-8";

	public static String generateKey() throws NoSuchAlgorithmException {
		KeyGenerator kg = KeyGenerator.getInstance("DESede");
		kg.init(112);// must be equal to 112 or 168
		SecretKey generateKey = kg.generateKey();
		byte[] encoded = generateKey.getEncoded();
		return new BASE64Encoder().encode(encoded);
	}

	/**
	 * 生成指定位数的随机秘钥
	 * 
	 * @param KeyLength
	 * @return Keysb
	 */
	public static String keyCreate(int KeyLength) {

		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer Keysb = new StringBuffer();
		for (int i = 0; i < KeyLength; i++) // 生成指定位数的随机秘钥字符串
		{
			int number = random.nextInt(base.length());
			Keysb.append(base.charAt(number));
		}
		return Keysb.toString();
	}

	/**
	 * 
	 * @param keybyte
	 *            加密密钥，长度为24字节
	 * @param src
	 *            字节数组(根据给定的字节数组构造一个密钥。 )
	 * @return
	 */
	public static byte[] encryptMode(byte[] keybyte, byte[] src) {

		try {
			// 根据给定的字节数组和算法构造一个密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;

	}

	/**
	 *
	 * @param key
	 *            秘钥
	 * @param src
	 *            字节数组(根据给定的字节数组构造一个密钥。 )
	 * @return
	 */
	public static byte[] encryptMode(String key, byte[] src) {

		try {
			// 根据给定的字节数组和算法构造一个密钥
			SecretKey deskey = new SecretKeySpec(key.getBytes(), Algorithm);
			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;

	}

	/**
	 *
	 * @param keybyte
	 *            密钥
	 * @param src
	 *            需要解密的数据
	 * @return
	 */
	public static byte[] decryptMode(byte[] keybyte, byte[] src) {

		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;

	}

	/**
	 *
	 * @param key
	 *            密钥
	 * @param src
	 *            需要解密的数据
	 * @return
	 */
	public static byte[] decryptMode(String key, byte[] src) {

		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(key.getBytes(), Algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;

	}

	/**
	 *
	 * @param key
	 *            base64编码的秘钥
	 * @param src
	 *            字节数组(根据给定的字节数组构造一个密钥。 )
	 * @return
	 */
	public static String encryptStr(String key, String str) {

		try {

			// 根据给定的字节数组和算法构造一个密钥
			SecretKey deskey = new SecretKeySpec(key.getBytes(ENCODING), Algorithm);
			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			byte[] doFinal = c1.doFinal(str.getBytes(ENCODING));
			return Base64Utils.encodeToString(doFinal);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;

	}

	/**
	 *
	 * @param key
	 *            base64编码的秘钥
	 * @param src
	 *            字节数组(根据给定的字节数组构造一个密钥。 )
	 * @return
	 */
	public static String decryptStr(String key, String str) {

		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(key.getBytes(ENCODING), Algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			byte[] doFinal = c1.doFinal(Base64Utils.decodeFromString(str));
			return new String(doFinal);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;

	}
	/**
	 *
	 * 字符串转为16进制
	 *
	 * @param str
	 * @return
	 */
	public static String encode(byte[] bytes) {

		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	/**
	 *
	 * @param bytes
	 * @return 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 */

	public static byte[] decode(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < bytes.length(); i += 2)
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
		return baos.toByteArray();
	}

	// 转换成十六进制字符串
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";
		}
		return hs.toUpperCase();
	}

	// byte数组(用来生成密钥的)
	final static byte[] keyBytes = { 0x11, 0x22, 0x4F, 0x58, (byte) 0x88, 0x10, 0x40, 0x38, 0x28, 0x25, 0x79, 0x51,
			(byte) 0xCB, (byte) 0xDD, 0x55, 0x66, 0x77, 0x29, 0x74, (byte) 0x98, 0x30, 0x40, 0x36, (byte) 0xE2 };

	/**
	 * DES3加密
	 * 
	 * @param str
	 * @return String
	 */
	public static String encodeDes3(String str) {
		byte[] encoded = encryptMode(keyBytes, str.getBytes());
		return encode(encoded);
	}

	/**
	 * DES3解密
	 * 
	 * @param str
	 * @return String
	 */
	public static String decodeDes3(String str) {
		byte[] encoded = decryptMode(keyBytes, decode(str));
		return new String(encoded);
	}


}
