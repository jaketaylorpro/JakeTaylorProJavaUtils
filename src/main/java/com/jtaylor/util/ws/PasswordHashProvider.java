package com.jtaylor.util.ws;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 12/17/13
 * Time: 8:32 AM
 * To change this template use File | Settings | File Templates.
 */
public interface PasswordHashProvider
{
	public String getPasswordHash(String identity);
}
