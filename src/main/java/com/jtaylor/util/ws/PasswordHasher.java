package com.jtaylor.util.ws;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 2/1/14
 * Time: 9:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class PasswordHasher
{
	public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
	public static final Charset utf8=Charset.forName("UTF-8");
	//public static SecretKeyFactory PBKDF2_SECRET_KEY_FACTORY;
	public static final int HASH_LENGTH=36;
	public static final int HASH_PASSES=1000;
	public static String hashPassword(String password,String secret_salt)
	{
		try
		{
			SecretKeyFactory spf=SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
			PBEKeySpec ks=new PBEKeySpec(password.toCharArray(),secret_salt.toString().getBytes(utf8),HASH_PASSES,HASH_LENGTH*8);
			return new String(spf.generateSecret(ks).getEncoded(),utf8);
		}
		catch (NoSuchAlgorithmException e)
		{
			return null;
		}
		catch (InvalidKeySpecException e)
		{
			return null;
		}
	}
}
