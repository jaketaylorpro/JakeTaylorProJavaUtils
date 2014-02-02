package com.jtaylor.util.ws;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 1/14/14
 * Time: 12:01 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SecretKeyProvider
{
	public SecretKeyStruct getSecretKey(String identity);

	//public void putSecretKey(String identity,String token);
	public static class SecretKeyStruct
	{
		public String secret_key;
		public String secret_salt;
		public SecretKeyStruct(String key,String salt)
		{
			secret_key=key;
			secret_salt=salt;
		}
	}
}
