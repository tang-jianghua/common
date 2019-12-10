package com.tangjianghua.common.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class RSAUtil {
	private static final Logger logger = LoggerFactory.getLogger(RSAUtil.class);
	private static String RSAKeyStore = "rsa.keypair.dat";
	private static final String transformation = "RSA/ECB/PKCS1Padding";
	private static final String Algorithm = "RSA";
	static{
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null){
            logger.info("security provider BC not found");
            Security.addProvider(new BouncyCastleProvider());
        }
    }
	
	/** 
	 * * 生成密钥对 * 
	 *  
	 * @return KeyPair * 
	 * @throws EncryptException 
	 */  
	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(Algorithm, BouncyCastleProvider.PROVIDER_NAME);
			final int KEY_SIZE = 1024;// 没什么好说的了，这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低  
			keyPairGen.initialize(KEY_SIZE, new SecureRandom());
			KeyPair keyPair = keyPairGen.generateKeyPair();
			
			saveKeyPair(keyPair);
			return keyPair;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void saveKeyPair(KeyPair kp) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(RSAKeyStore);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			// 生成密钥 
			oos.writeObject(kp);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static KeyPair getKeyPair() {
		try {
			ObjectInputStream oos = new ObjectInputStream(RSAUtil.class.getClassLoader().getResourceAsStream("rsa.keypair.dat"));
			KeyPair kp = (KeyPair) oos.readObject();
			oos.close();
			return kp;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 
	 * * 生成公钥 * 
	 *  
	 * @param modulus * 
	 * @param publicExponent * 
	 * @return RSAPublicKey * 
	 * @throws Exception 
	 */  
	public static RSAPublicKey generateRSAPublicKey(byte[] modulus,
			byte[] publicExponent) {
		KeyFactory keyFac = null;
		try {
			keyFac = KeyFactory.getInstance(Algorithm, BouncyCastleProvider.PROVIDER_NAME);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
  
		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(
				new BigInteger(modulus), new BigInteger(publicExponent));
		try {
			return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
		} catch (InvalidKeySpecException ex) {
			logger.error("生成公钥异常",ex);
			return null;
		}
	}

	/** 
	 * * 生成私钥 * 
	 *  
	 * @param modulus * 
	 * @param privateExponent * 
	 * @return RSAPrivateKey * 
	 * @throws Exception 
	 */
	public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) {
		KeyFactory keyFac = null;  
		try {
			keyFac = KeyFactory.getInstance(Algorithm, BouncyCastleProvider.PROVIDER_NAME);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(
				new BigInteger(modulus), new BigInteger(privateExponent));
		try {
			return (RSAPrivateKey)keyFac.generatePrivate(priKeySpec);
		} catch (InvalidKeySpecException ex) {
			logger.error("生成私钥异常",ex);
			return null;
		}
	}
	/** 
	 * * 加密 * 
	 *  
	 * @param key  加密的密钥 * 
	 * @param data 待加密的明文数据 * 
	 * @return 加密后的数据 * 
	 * @throws Exception 
	 */  
	public static byte[] encrypt(PublicKey pk, byte[] data) throws Exception {  
		try {  
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
	
	/** 
	 * * 解密 *
	 *  
	 * @param key 解密的密钥 * 
	 * @param raw 已经加密的数据 * 
	 * @return 解密后的明文 * 
	 * @throws Exception 
	 */
	public static byte[] decrypt(PrivateKey pk, byte[] raw) {  
		try {  
			Cipher cipher = Cipher.getInstance(transformation, BouncyCastleProvider.PROVIDER_NAME);
			cipher.init(Cipher.DECRYPT_MODE, pk);  
			int blockSize = cipher.getBlockSize();  
			ByteArrayOutputStream bout = new ByteArrayOutputStream(64);  
			int j = 0;  

			while (raw.length - j * blockSize > 0) {
				bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
				j++;
			}
			return bout.toByteArray();  
		} catch (Exception e) {  
			logger.error("解密异常",e);
			return null;
		}
	}
	
	/** 
	 * * 加密 * 
	 *  
	 * @param key  加密的密钥 * 
	 * @param data 待加密的明文数据 * 
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
	
	/** 
	 * * 解密 *
	 *  
	 * @param key 解密的密钥 * 
	 * @param raw 已经加密的数据 * 
	 * @return 解密后的明文 * 
	 * @throws Exception 
	 */
	public static byte[] decrypt(String priKey, byte[] raw) {  
		try {  
			PrivateKey privateKey = getPrivateKey(priKey);
			Cipher cipher = Cipher.getInstance(transformation, BouncyCastleProvider.PROVIDER_NAME);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);  
			int blockSize = cipher.getBlockSize();  
			ByteArrayOutputStream bout = new ByteArrayOutputStream(64);  
			int j = 0;  

			while (raw.length - j * blockSize > 0) {
				bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
				j++;
			}
			return bout.toByteArray();  
		} catch (Exception e) {  
			logger.error("解密异常",e);
			return null;
		}
	}
	
	public static PublicKey getPubKey(String pubKey) {
		  PublicKey publicKey = null;
		  try {
		      java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(
		     new BASE64Decoder().decodeBuffer(pubKey));
		   // RSA对称加密算法
		   KeyFactory keyFactory;
		   keyFactory = KeyFactory.getInstance(Algorithm);
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
		  * 实例化私钥
		  *
		  * @return
		  */
	public static  PrivateKey getPrivateKey(String priKey) {
		  PrivateKey privateKey = null;
		 
		  PKCS8EncodedKeySpec priPKCS8;
		  try {
		   priPKCS8 = new PKCS8EncodedKeySpec(
		     new BASE64Decoder().decodeBuffer(priKey));
		   KeyFactory keyf = KeyFactory.getInstance(Algorithm);
		   privateKey = keyf.generatePrivate(priPKCS8);
		  } catch (IOException e) {
		   e.printStackTrace();
		  } catch (NoSuchAlgorithmException e) {
		   e.printStackTrace();
		  } catch (InvalidKeySpecException e) {
		   e.printStackTrace();
		  }
		  return privateKey;
		 }


}
