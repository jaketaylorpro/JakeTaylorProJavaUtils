package com.jtaylor.util.ws;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 1/14/14
 * Time: 12:54 AM
 * To change this template use File | Settings | File Templates.
 */
public interface EncryptionServiceFactory
{
	public EncryptionService getEncryptionService(byte[] bytes);
}
