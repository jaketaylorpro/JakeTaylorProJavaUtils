package com.jtaylor.util.ws;

import java.security.InvalidParameterException;
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
	public String _securitySalt;

	public SecurityToken(long expirationTime,String securitySalt,String securityIdent)
	{
		_expirationTime=expirationTime;
		_securitySalt=securitySalt;
		_securityIdent=securityIdent;
	}
	public SecurityToken(String serialized)
	{
		String time="";
		String salt="";
		String ident="";
		char[] chars=serialized.toCharArray();
		if (chars.length!=108)//3*36
		{
			throw new InvalidParameterException("SecurityToken length was: "+chars.length+"; it needs to be 108");
		}
		int i=0;
		while(i<chars.length)
		{
			time=chars[i++]+time;
			salt=chars[i++]+salt;
			ident=chars[i++]+ident;
		}
		_expirationTime =Long.parseLong(time);
		_securityIdent =ident.trim();
		_securitySalt=salt.trim();
	}

	public Date getExpirationTime()
	{
		return new Date(_expirationTime);
	}
	public String getSecurityIdent()
	{
		return _securityIdent;
	}
	public String getSecuritySalt()
	{
		return _securitySalt;
	}
	@Override
	public String toString()
	{
		char[] chars=new char[108];
		char[] timeChars=(_expirationTime+"").toCharArray();
		char[] saltChars=_securitySalt.toCharArray();
		char[] identChars=_securityIdent.toCharArray();
		int i=0;
		int j=0;
		while(i<chars.length)//interweave the three fields to create the most distributed entropy
		{
			if(j<timeChars.length)
			{
				chars[i++]=timeChars[timeChars.length-1-j];//put in reverse order so entropy is concentrated at the beginning and buffer at the end
			}
			else
			{
				chars[i++]='0';//buffer the long integer with 0s which will disappear upon parsing
			}
			if(j<saltChars.length)
			{
				chars[i++]=saltChars[saltChars.length-1-j];
			}
			else
			{
				chars[i++]=' ';//buffer the string with spaces which will disappear upon trimming
			}
			if(j<identChars.length)
			{
				chars[i++]=identChars[identChars.length-1-j];
			}
			else
			{
				chars[i++]=' ';//buffer the string with spaces which will disappear upon trimming
			}
			j++;
		}
		return new String(chars);
	}
	public static void main(String[] args)
	{
		long time=1;
		String salt= "111111111111112222222222222222222";
		String ident="333333333333333333333333333333333333";
		SecurityToken st=new SecurityToken(time,salt,ident);
		String serialized=st.toString();
//		System.out.println(serialized);
		st=new SecurityToken(st.toString());
//		System.out.println(st.getExpirationTime());
//		System.out.println(st.getExpirationTime().getTime());
//		System.out.println("'"+st.getSecuritySalt()+"'");
//		System.out.println("'"+st.getSecurityIdent()+"'");
		System.out.println(time==st.getExpirationTime().getTime());
		System.out.println(salt.equals(st.getSecuritySalt()));
		System.out.println(ident.equals(st.getSecurityIdent()));
		System.out.println(serialized.equals(st.toString()));
	}
}
