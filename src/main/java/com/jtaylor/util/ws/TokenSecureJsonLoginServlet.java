package com.jtaylor.util.ws;

import com.jtaylor.util.enums.HttpOperation;

import javax.servlet.ServletException;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 1/14/14
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class TokenSecureJsonLoginServlet extends JsonHttpServlet <TokenSecureLoginRequest,TokenSecureAjaxObject>
{
	private PasswordHashProvider _passwordHashProvider;
	private SecretKeyProvider _secretKeyProvider;
	private EncryptionServiceFactory _encryptionServiceFactory;
	private int _tokenLifespan;
	public TokenSecureJsonLoginServlet(PasswordHashProvider passwordHashProvider,SecretKeyProvider secretKeyProvider,EncryptionServiceFactory encryptionServiceFactory,int tokenLifespanMilliseconds)
	{
		_passwordHashProvider=passwordHashProvider;
		_secretKeyProvider=secretKeyProvider;
		_encryptionServiceFactory=encryptionServiceFactory;
		_tokenLifespan=tokenLifespanMilliseconds;
	}

	public PasswordHashProvider getPasswordHashProvider()
	{
		return _passwordHashProvider;
	}

	public SecretKeyProvider getSecretKeyProvider()
	{
		return _secretKeyProvider;
	}

	public int getTokenLifespan()
	{
		return _tokenLifespan;
	}

	@Override
	public Class<TokenSecureLoginRequest> getInType()
	{
		return TokenSecureLoginRequest.class;
	}

	@Override
	public TokenSecureAjaxObject doHttpOperation(TokenSecureLoginRequest r,
	                                             HttpOperation op) throws ServletException
	{
		String passwordHash=_passwordHashProvider.getPasswordHash(r.$security_ident);
		if(passwordHash == null)
		{
			throw new SecurityException("No such user: '"+r.$security_ident+"'");
		}
		else
		{
			Charset utf8=Charset.forName("UTF-8");
			String serializedSecurityToken=new SecurityToken(new Date().getTime()+getTokenLifespan(),r.$security_ident).toString();
			String secretKey=_secretKeyProvider.getSecretKey(r.$security_ident);
			EncryptionService encryptionService=_encryptionServiceFactory.getEncryptionService(secretKey.getBytes(utf8));
			String encryptedSecurityToken=new String(encryptionService.encrypt(serializedSecurityToken.getBytes(utf8)));
			_secretKeyProvider.putSecretKey(r.$security_ident,encryptedSecurityToken);
			return new TokenSecureAjaxObject(r.$security_ident,encryptedSecurityToken);
		}
	}

}
