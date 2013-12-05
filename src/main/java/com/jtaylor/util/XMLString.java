package com.jtaylor.util;

import org.apache.log4j.Logger;


public class XMLString
{
	public static void main(String[] args)
	{
		String test="<address><type>0</type><street1>1100 East Hector Street</street1><street2>Suite 215</street2><city>Conshohocken</city><state>PA</state><zip>19428</zip><country></country><preferred>false</preferred></address>";
		XMLString testXML=new XMLString(test);
		XMLString innerXML=new XMLString(testXML.getXMLTag("address", 0));
		System.out.println(innerXML);
		System.out.println(innerXML.getXMLTag("type",0));
		System.out.println(innerXML.getXMLTag("street1",0));
		/*
		String test="<address><type>Work</type><street1>a</street1><street2>b</street2><city>c</city><state>d</state><zip>e</zip><country>f</country></address><address><type>Home</type><street1>g</street1><street2>h</street2><city>i</city><state>j</state><zip>k</zip><country>l</country></address>";
		XMLString testXML=new XMLString(test);
		System.out.println(testXML.countXMLTag("address"));
		System.out.println(testXML.getXMLTag("address", 0));
		System.out.println(testXML.getXMLTag("address", 1));
		System.out.println(testXML.getXMLTag("address", 2));
		*/
	}
	String myString;
	private Logger log;
	public XMLString(String xml)
	{
		myString=xml;
		log=Logging.createServerLogger(XMLString.class);
	}
	public String getXMLTag(String tag,int index)
	{
		String remainder=new String(myString);
		int count=0;
		while(remainder.contains(tag))
		{
			if(count==index)
			{
				return remainder.substring(remainder.indexOf("<"+tag+">")+tag.length()+2,remainder.indexOf("</"+tag+">")); 
			}
			count++;
			remainder=remainder.substring(remainder.indexOf("</"+tag+">")+tag.length()+2);
		}
		return "";
	}
	public String getXMLTag(String tag)
	{
		return getXMLTag(tag,0);
	}
	public int countXMLTag(String tag)
	{
		try
		{
			String remainder=new String(myString);
			int count=0;
			while(remainder.contains("<"+tag+">"))
			{
				count++;
				remainder=remainder.substring(remainder.indexOf("<"+tag+">")+tag.length()+2);
			}
			return count;
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	public String toString()
	{
		return myString;
	}
}
