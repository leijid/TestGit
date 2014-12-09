/**
 * leijid
 *
 * Test.java
 *
 * 2014��9��19��
 */
package com.self.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author leijid
 * 
 */
public class Test {

	private static final String AES = "AES";

	private static final String ENCODING = "UTF-8";

	private static final String AES_ECB_PADDING = "AES/ECB/NoPadding";

	private static final int AES_KEY_LENGTH = 192;

	private static final String AES_KEY_FILE = "c:\\aes.key";

	private static final char PADDING_CHARACTER = '*';

	public static void main(String[] args) {
		byte[] encryptResult = encryptAES(addPadding("����AES192�����㷨1", 16),
		        AES_KEY_LENGTH);
		System.out.println(parseByte2HexStr(encryptResult));
		byte[] decryptResult = decryptAES(encryptResult, AES_KEY_LENGTH);
		String resultStr = byte2String(decryptResult);
		System.out.println(noPadding(resultStr, PADDING_CHARACTER));
	}

	public static byte[] encryptAES(String content, int keySize) {
		try {
			SecretKeySpec spec = getAESKeySpec(keySize);
			Cipher cipher = Cipher.getInstance(AES_ECB_PADDING);
			cipher.init(Cipher.ENCRYPT_MODE, spec);
			byte[] byteContent = string2Byte(content);
			byte[] result = cipher.doFinal(byteContent);
			return result;
		} catch (Exception e) {
			System.out.println("AES����ʧ��1" + e);
		}
		return null;
	}

	public static byte[] decryptAES(byte[] content, int keySize) {
		try {
			SecretKeySpec spec = getAESKeySpec(keySize);
			Cipher cipher = Cipher.getInstance(AES_ECB_PADDING);
			cipher.init(Cipher.DECRYPT_MODE, spec);
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			System.out.println("AES����ʧ��" + e);
		}
		return null;
	}

	public static String parseByte2HexStr(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			sb.append(hex);
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * ����ַ�
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	private static String addPadding(String str, int length) {
		return padding(str, PADDING_CHARACTER, length, 0);
	}

	private static String padding(String str, char ch, int length, int i) {
		if (str == null || str.length() == 0) {
			return null;
		}
		int strLen = string2Byte(str).length;
		StringBuffer sb = new StringBuffer();
		if (strLen % length == 0) {
			String paddingLen = String.valueOf(i);
			str = str.substring(0, str.length() - paddingLen.length());
			sb.append(str).append(i);
			return sb.toString();
		} else {
			String paddingStr = sb.append(str).append(ch).toString();
			return padding(paddingStr, ch, length, i + 1);
		}
	}

	/**
	 * �Ƴ�����ַ�
	 * 
	 * @param str
	 * @param ch
	 * @return
	 */
	private static String noPadding(String str, char ch) {
		if (str == null || str.length() == 0) {
			return null;
		}
		int len = str.length();
		String subStr = str.substring(str.lastIndexOf(ch) + 1, len);
		String resultStr = str.substring(0, len - Integer.parseInt(subStr));
		return resultStr;
	}

	/**
	 * 
	 * @param keySize
	 *            ��Կ����
	 * @return
	 * @throws Exception
	 */
	private static SecretKeySpec getAESKeySpec(int keySize) throws Exception {
		if ((keySize != 128) && (keySize != 192) && (keySize != 256)) {
			throw new Exception("���ܵ�keySize����!");
		}
		byte[] bytes = new byte[keySize / 8];
		File f = new File(AES_KEY_FILE);
		if (f.exists()) {
			readAESKeyFile(bytes, f);
		} else {
			bytes = wirteAESKeyFile(keySize, bytes, f);
		}
		SecretKeySpec spec = new SecretKeySpec(bytes, AES);
		return spec;
	}

	/**
	 * ��ȡ��Կ�ļ�
	 * 
	 * @param bytes
	 * @param f
	 */
	private static void readAESKeyFile(byte[] bytes, File f) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(f);
			in.read(bytes);
		} catch (FileNotFoundException e) {
			System.out.println("δ�ҵ�AES�ӽ��������KEY" + e);
		} catch (IOException e) {
			System.out.println("��ȡAES�ӽ��������KEYʧ��" + e);
		} finally {
			closeStream(in, null);
		}
	}

	/**
	 * ������Կ�ļ�
	 * 
	 * @param keySize
	 * @param bytes
	 * @param f
	 * @return
	 */
	private static byte[] wirteAESKeyFile(int keySize, byte[] bytes, File f) {
		SecretKey key;
		KeyGenerator kgen;
		FileOutputStream out = null;
		try {
			kgen = KeyGenerator.getInstance(AES);
			kgen.init(keySize);
			key = kgen.generateKey();
			bytes = key.getEncoded();
			out = new FileOutputStream(f);
			out.write(bytes);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("û�иüӽ����㷨" + e);
		} catch (FileNotFoundException e) {
			System.out.println("δ�ҵ�AES�ӽ��������KEY" + e);
		} catch (IOException e) {
			System.out.println("����AES�ӽ��������KEYʧ��" + e);
		} finally {
			closeStream(null, out);
		}
		return bytes;
	}

	private static byte[] string2Byte(String str) {
		byte[] b = null;
		try {
			b = str.getBytes(ENCODING);
		} catch (UnsupportedEncodingException e) {
			System.out.println("��֧�ָ��ֱ��뷽ʽ" + e);
		}
		return b;
	}

	public static String byte2String(byte[] bytes) {
		String str = null;
		try {
			str = new String(bytes, ENCODING);
		} catch (UnsupportedEncodingException e) {
			System.out.println("��֧�ָ��ֱ��뷽ʽ" + e);
		}
		return str;
	}

	private static void closeStream(InputStream in, OutputStream out) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				System.out.println("�������ر��쳣" + e);
			}
		}
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				System.out.println("������ر��쳣" + e);
			}
		}
	}
}
