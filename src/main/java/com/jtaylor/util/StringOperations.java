package com.jtaylor.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 12/14/13
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringOperations
{
	public static String extract(String from,String pattern)
	{
		Matcher m=Pattern.compile(pattern).matcher(from);
		if(m.matches())
		{
			return m.group(1);
		}
		else
		{
			return null;
		}
	}
	public static void main(String[] args)
	{
		String s ="testabcdefghq";
		System.out.println(s+": "+extract(s,".*a(.*)g.*"));
		/*
		Pattern p1=Pattern.compile(".*a(.*)q.*");
		Matcher m=p1.matcher(s);
		m.matches();
		System.out.println(m.groupCount());
		System.out.println(m.group(0));
		System.out.println(m.group(1));
		*/
	}
}
