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
	public String getSecretKey(String identity);

	public void putSecretKey(String identity,String token);
}
