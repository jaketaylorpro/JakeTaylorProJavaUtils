package com.jtaylor.util;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 9/23/11
 * Time: 1:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Base64Operations
{
   public static String fixLineBreaks(String base64String)
   {
      return base64String.replaceAll("\\Q&#xA;\\E","\n");
   }
   public static byte[] decode(String base64String)
   {
      return Base64.decodeBase64(fixLineBreaks(base64String));
   }
   public static String encode(byte[] bytes)
   {
      return new String(Base64.encodeBase64(bytes));
   }
   public static String encodeURL(byte[] bytes)
   {
      return new String(Base64.encodeBase64(bytes,false,true));
   }
}
