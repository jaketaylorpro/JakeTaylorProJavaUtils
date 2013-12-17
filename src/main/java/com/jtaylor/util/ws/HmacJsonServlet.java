package com.jtaylor.util.ws;

import com.jtaylor.util.enums.HttpOperation;

import javax.servlet.ServletException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 12/17/13
 * Time: 8:26 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class HmacJsonServlet <I extends ISecureAjaxObject,O> extends JsonHttpServlet <I,O>
{
	
	@Override
	public O doHttpOperation(I i, HttpOperation op) throws ServletException
	{
		try
		{
			Charset utf8=Charset.forName("UTF-8");
			MessageDigest hmac=MessageDigest.getInstance("SHA256");
			hmac.update(getServletConfig().getInitParameter("salt").getBytes(utf8));
			
			hmac.update(getServletConfig().getInitParameter("pepper").getBytes(utf8));
			doSecureHttpOperation(i,op);
		}
		catch(NoSuchAlgorithmException e)
		{
			throw new ServletException("cannot initialize hmac hasher",e);
		}
	}
	public abstract O doSecureHttpOperation(I i, HttpOperation op);
}
