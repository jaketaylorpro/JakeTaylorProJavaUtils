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

	public EncryptionServiceFactory getEncryptionServiceFactory()
	{
		return _encryptionServiceFactory;
	}

	@Override
	public Class<TokenSecureLoginRequest> getInType()
	{
		return TokenSecureLoginRequest.class;
	}

	/**
	 *
	 * @param r request's login credentials. {identity, password}
	 * @param op post
	 * @return hashes the request's password, if it matches the record of the identities hash, then return a login token
	 * @throws ServletException
	 */
	@Override
	public TokenSecureAjaxObject doHttpOperation(TokenSecureLoginRequest r,
	                                             HttpOperation op) throws ServletException
	{
		//load the user's password hash from the database
		String passwordHash=getPasswordHashProvider().getPasswordHash(r.$security_ident);
		if(passwordHash == null)
		{
			throw new SecurityException("No such identity: '"+r.$security_ident+"'");
		}
		else
		{
			Charset utf8=Charset.forName("UTF-8");
			//get the users secret key and salt from the database
			SecretKeyProvider.SecretKeyStruct secretSecretKeyStruct =getSecretKeyProvider().getSecretKey(r.$security_ident);
			//hash the requested password with secret key and salt
			String requestedPasswordHash=PasswordHasher.hashPassword(r.$security_password,secretSecretKeyStruct.secret_salt);
			if(requestedPasswordHash.equals(passwordHash))
			{
				String serializedSecurityToken=new SecurityToken(new Date().getTime()+getTokenLifespan(),r.$security_ident,
																				 secretSecretKeyStruct.secret_salt).toString();
				EncryptionService encryptionService=getEncryptionServiceFactory().getEncryptionService(secretSecretKeyStruct.secret_key.getBytes(
						utf8));
				String encryptedSecurityToken=new String(encryptionService.encrypt(serializedSecurityToken.getBytes(utf8)));
				return new TokenSecureAjaxObject(r.$security_ident,encryptedSecurityToken);
			}
			else
			{
				throw new SecurityException("Incorrect password");
			}
		}
	}

}
