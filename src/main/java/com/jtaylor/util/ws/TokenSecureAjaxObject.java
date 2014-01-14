package com.jtaylor.util.ws;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 12/16/13
 * Time: 11:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class TokenSecureAjaxObject
{
	public String $security_ident;
	public String $security_token;

	public TokenSecureAjaxObject(String securityIdent, String securityToken)
	{
		$security_ident=securityIdent;
		$security_token=securityToken;
	}
}
