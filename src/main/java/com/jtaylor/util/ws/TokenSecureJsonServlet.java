package com.jtaylor.util.ws;

import com.jtaylor.util.enums.HttpOperation;

import javax.servlet.ServletException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 12/17/13
 * Time: 8:26 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TokenSecureJsonServlet<I extends TokenSecureAjaxObject,O> extends JsonHttpServlet <I,O>
{
	//TODO initialization is incomplete
	protected EncryptionServiceFactory encryptionServiceFactory;
	protected SecretKeyProvider secretKeyProvider;
	@Override
	public O doHttpOperation(I i, HttpOperation op) throws ServletException
	{
		try
		{
			SecretKeyProvider.SecretKeyStruct secretSecretKeyStruct =secretKeyProvider.getSecretKey(i.$security_ident);
			String secretKey= secretSecretKeyStruct.secret_key;
			if(secretKey==null)
			{
				throw new SecurityException("No such user: '"+i.$security_ident+"'");
			}
			else
			{
				Charset utf8=Charset.forName("UTF-8");
				EncryptionService encryptionService = encryptionServiceFactory.getEncryptionService(secretKey.getBytes(utf8));
				SecurityToken decryptedToken=new SecurityToken(new String(encryptionService.decrypt(i.$security_token.getBytes(utf8)),utf8));
				if(i.$security_ident.equalsIgnoreCase(decryptedToken.getSecurityIdent())&&decryptedToken.getExpirationTime().after(new Date()))
				{
					return doSecureHttpOperation(i,op);
				}
				else
				{
					throw new SecurityException("Invalid token: '"+i.$security_token+"'");
				}
			}
		}
		catch (UnsupportedCharsetException e)
		{
			throw new ServletException("unsupported charset: 'UTF-8'",e);
		}
		catch (SecurityException e)
		{
			throw new ServletException(e);
		}
		catch (Exception e)
		{
			throw new ServletException("uncaught exception",e);
		}
	}
	public abstract O doSecureHttpOperation(I i, HttpOperation op);
}
