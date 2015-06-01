package test.pool;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Triple DES(3DES) 加密
 */
public class TripleDES {
	/** Triple DES 要求key固定为24个字节长度 */
	public final static int KEY_LEN = 24;

	/**
	 * @param args在java中调用sun公司提供的3DES加密解密算法时
	 *            ，需要使 用到$JAVA_HOME/jre/lib/目录下如下的4个jar包： jce.jar
	 *            security/US_export_policy.jar security/local_policy.jar
	 *            ext/sunjce_provider.jar
	 */
	private static final String Algorithm = "DESede"; // 定义加密算法,可用
														// DES,DESede,Blowfish

	static class SingletonHolder {
		static TripleDES singleton = new TripleDES();
	}

	public static TripleDES getInstance() {
		return SingletonHolder.singleton;
	}

	private Cipher cipher;

	private TripleDES() {
		try {
			// 添加新安全算法,如果用JCE就要把它添加进去
			Security.addProvider(new com.sun.crypto.provider.SunJCE());
			// 初始化cipher
			cipher = Cipher.getInstance(Algorithm);
		} catch (Throwable t) {
			throw new RuntimeException("Initialize TripleDES cipher fail.", t);
		}
	}
	
	public Cipher getCipher() {
		return cipher;
	}

	/**
	 * 加密字节流
	 * 
	 * @param src
	 *            明文
	 * @param keybyte
	 *            24字节长度的密钥
	 * @return 加密后的密文（二进制）
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] src, byte[] keybyte) throws Exception {
		// 生成密钥
		SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
		// 加密
		cipher.init(Cipher.ENCRYPT_MODE, deskey);
		return cipher.doFinal(src);// 在单一方面的加密或解密
	}

	/**
	 * 加密字符串
	 * 
	 * @param src
	 *            明文字符串
	 * @param salt
	 *            密钥
	 * @return 加密后的密文
	 * @throws Exception
	 */
	public byte[] encrypt(String src, String salt) throws Exception {
		return encrypt(src.getBytes("UTF-8"), prepareKey(salt));
	}
	
	public byte[] encrypt(String src, byte[] key) throws Exception {
		return encrypt(src.getBytes("UTF-8"), key);
	}

	/**
	 * 解密字节流
	 * 
	 * @param src
	 *            要解密的密文
	 * @param keybyte
	 *            密钥
	 * @return 解密后的明文
	 * @throws Exception
	 */
	public byte[] decrypt(byte[] src, byte[] keybyte) throws Exception {
		// 生成密钥
		SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
		// 解密
		cipher.init(Cipher.DECRYPT_MODE, deskey);
		return cipher.doFinal(src);
	}

	public byte[] decrypt(byte[] src, String salt) throws Exception {
		return decrypt(src, prepareKey(salt));
	}

	// 转换成十六进制字符串
	public static String byte2Hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
			if (n < b.length - 1)
				hs = hs + ":";
		}
		return hs.toUpperCase();
	}

	public byte[] prepareKey(String key) throws Exception {
		byte[] raw = key.getBytes("UTF-8");
		if (raw.length == KEY_LEN) {
			return raw;
		} else {
			byte[] right = new byte[KEY_LEN];
			Arrays.fill(right, (byte) 0);
			System.arraycopy(raw, 0, right, 0, raw.length > KEY_LEN ? KEY_LEN
					: raw.length);
			return right;
		}
	}

	public static void main(String[] args) throws Exception {
		TripleDES cipher = TripleDES.getInstance();

		String data = "世界，你好！Hello world!";
		byte[] key = new byte[24];
		for (int i = 0; i < key.length; ++i) {
			key[i] = (byte) i;
		}

		long beginMillis = System.currentTimeMillis();
		for (int i = 0; i < 1; ++i) {
			System.out.println("加密前的字符串:" + data);
			byte[] encoded = cipher.encrypt(data, key);

			System.out.println("加密后, len=" + encoded.length);
			for (byte b : encoded) {
//				System.out.print(Integer.toHexString(b & 0xFF) + ' ');
				System.out.print(String.format("%02X ", b));
			}
			System.out.println();

//			byte[] srcBytes = cipher.decrypt(encoded, key);
//			System.out.println("解密后的字符串:" + (new String(srcBytes)));
			Cipher c = cipher.getCipher();
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(key, Algorithm);
			// 解密
			c.init(Cipher.DECRYPT_MODE, deskey);
			ByteArrayInputStream bais = new ByteArrayInputStream(encoded);
			CipherInputStream cis = new CipherInputStream(bais, c);
			InputStreamReader isr = new InputStreamReader(cis);
			BufferedReader reader = new BufferedReader(isr);
			System.out.println("!!!解密后的字符串:" + reader.readLine());
			reader.close();
		}

		System.out
				.println("time=" + (System.currentTimeMillis() - beginMillis));
	}
}
