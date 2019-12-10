package com.tangjianghua.common.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Base64Utils;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

public class EncryptDemo {

	private static String transformation = "RSA/ECB/PKCS1Padding";
	private static final String ALGORITHM = "RSA";

	private static final String PUBKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCFctqs7rYe/qGZaH0h3YQ4FKNNsPu8P03K8s/+J+emVY5fc8uxKnNQRLDiCpvAL6kY7vGH1lZ3a35/acDjNT0wNh/ZPR0jXkIr13c94SVXPTQoHZy+8e2lGUh5xOd4B4Xd7fTQ2IsK+4ieFkM1l0niT9Z9LH0g65zGqYNzMsKCwIDAQAB";
	static {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}

	public static void main(String[] args) throws Exception {

		// 报文格式：A|B|C 三个参数由“|”分割拼接成字符串
		String A = null;
		String B = "QR930000101296|1022|15457147118141861594|898604061918c0659661";
		String C = null;

		// 构建A参数-----------------------------------------------------1
		// 随机生成两个24位的字符串作为3des秘钥 pk加密敏感字段 wk用来签名
		String pk = keyCreate(24);
		String wk = keyCreate(24);

		// 将两个字符串拼接
		String key = pk + "|" + wk;
		// rsa公钥加密
		byte[] encrypt = encrypt(PUBKEY, key.getBytes());
		// base64编码
		A = Base64Utils.encodeToString(encrypt);

		// 构建C参数-----------------------------------------------------2
		// 用pinkey对响应内容进行3des加密
		byte[] encryptMode = encryptMode(pk, B.getBytes());
		// base64加密
		B = Base64Utils.encodeToString(encryptMode);
		// 对加密的响应密文进行md5加密
		String md5Res = MD5Encode(B);
		// 用workey对md5的响应密文进行加密
		byte[] signbtye = encryptMode(wk, md5Res.getBytes());
		// 对签名进行base64加密
		C = Base64Utils.encodeToString(signbtye);

		// 拼接参数-----------------------------------------------3
		String param = A + "|" + B + "|" + C;
		System.out.println(param);
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
	 * * 加密 *
	 * 
	 * @param key
	 *            加密的密钥 *
	 * @param data
	 *            待加密的明文数据 *
	 * @return 加密后的数据 *
	 * @throws Exception
	 */
	public static byte[] encrypt(String pubKey, byte[] data) throws Exception {
		try {
			PublicKey pk = getPubKey(pubKey);
			Cipher cipher = Cipher.getInstance(transformation, BouncyCastleProvider.PROVIDER_NAME);
			cipher.init(Cipher.ENCRYPT_MODE, pk);
			int blockSize = cipher.getBlockSize();
			// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
			// 加密块大小为127
			// byte,加密后为128个byte;因此共有2个加密块，第一个127
			// byte第二个为1个byte
			int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
			int leavedSize = data.length % blockSize;
			int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
			byte[] raw = new byte[outputSize * blocksSize];
			int i = 0;
			while (data.length - i * blockSize > 0) {
				if (data.length - i * blockSize > blockSize)
					cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
				else
					cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
				// 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到
				// ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了
				// OutputSize所以只好用dofinal方法。

				i++;
			}
			return raw;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	// 实例化公钥
	public static PublicKey getPubKey(String pubKey) {
		PublicKey publicKey = null;
		try {
			java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(
					new BASE64Decoder().decodeBuffer(pubKey));
			// RSA对称加密算法
			java.security.KeyFactory keyFactory;
			keyFactory = java.security.KeyFactory.getInstance(ALGORITHM);
			// 取公钥匙对象
			publicKey = keyFactory.generatePublic(bobPubKeySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return publicKey;
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
			SecretKey deskey = new SecretKeySpec(key.getBytes(), "DESede/ECB/PKCS7Padding");
			// 加密
			Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS7Padding");
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

	public static String byteArrayToHexString(byte[] bytes) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < bytes.length; n++) {
			stmp = (Integer.toHexString(bytes[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs;
	}
}
