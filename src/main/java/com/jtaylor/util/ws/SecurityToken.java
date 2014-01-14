package com.jtaylor.util.ws;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 1/14/14
 * Time: 12:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class SecurityToken
{
	public long _expirationTime;
	public String _securityIdent;

	public SecurityToken(long expirationTime,String securityIdent)
	{
		_expirationTime=expirationTime;
		_securityIdent=securityIdent;
	}
	public SecurityToken(String serialized)
	{
		_expirationTime =Long.parseLong(serialized.substring(0,serialized.indexOf(':')));
		_securityIdent =serialized.substring(serialized.indexOf(':') + 1);
	}

	public Date getExpirationTime()
	{
		return new Date(_expirationTime);
	}
	public String getSecurityIdent()
	{
		return _securityIdent;
	}
	@Override
	public String toString()
	{
		return _expirationTime +":"+ _securityIdent;
	}

}
