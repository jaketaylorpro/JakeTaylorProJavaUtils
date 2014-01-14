package com.jtaylor.util.ws;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 1/14/14
 * Time: 12:06 AM
 * To change this template use File | Settings | File Templates.
 */
public interface EncryptionService
{
	public byte[] encrypt(byte[] clear);
	public byte[] decrypt(byte[] crypt);
}
