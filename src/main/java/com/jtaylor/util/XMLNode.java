package com.jtaylor.util;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class XMLNode
{
	public static void main(String[] args)
	{
//		String test="<address person=\"jake\"><type>0</type><country></country><preferred>false</preferred></address>";
		String test="<address><type>0</type><street1>1100 East Hector Street</street1><street2>Suite 215</street2><city>Conshohocken</city><state>PA</state><zip>19428</zip><country></country><preferred>false</preferred></address>";
		try
        {
	        XMLNode node=new XMLNode(test);
	        System.out.println("name: "+node.getName());
	        System.out.println("attributes: "+node.getAttributes());
	        System.out.println("childNodes: "+node.getChildNodes());
	        String tree=node.getName()+"\n";
	        for(XMLNode x:node.getChildNodes())
	        {
	        	tree+=x.getName()+x.getAttributes()+":"+x.getChildNodes().size()+"="+x.getTextContents()+"\n";
	        	for(XMLNode y:x.getChildNodes())
	        	{
	        		tree+="\t"+y.getName()+y.getAttributes()+":"+y.getChildNodes().size()+"="+y.getTextContents()+"\n";
	        	}
	        }
	        System.out.println("childNodeTree: "+tree);
        }
        catch (BadXMLException e)
        {
	        e.printStackTrace();
        }
	}
	private String name;
	private Map<String,String> attributes;
	private boolean leaf;
	private List<XMLNode> childNodes;
	private String textContents;
	private Logger log;
	private static class BadXMLException extends Exception{public BadXMLException(Throwable t){super(t);}}
	public XMLNode(String xml) throws BadXMLException
	{
		this("XMLNode",new HashMap<String,String>(),xml);
	}
	private XMLNode(String myName,Map<String,String> myAttributes,String myTextContents) throws BadXMLException
	{
		log=Logging.createServerLogger(XMLNode.class);
		try
		{
			name=myName;
			attributes=myAttributes;
			childNodes=new Vector<XMLNode>();
			textContents=new String(myTextContents);
			String xmlt=myTextContents.trim();
			int tagStart=0;
			if(xmlt.length()<=1||xmlt.charAt(tagStart)!='<')
			{
				leaf=true;
			}
			else
			{
				leaf=false;
				while(xmlt.trim().length()>0)
				{
					int nameStart=tagStart+1;
					int nameLength=minPositive(xmlt.substring(nameStart).indexOf(' '),xmlt.substring(nameStart).indexOf('>'));
					String nodeName=xmlt.substring(nameStart,nameStart+nameLength);
					xmlt=xmlt.substring(nameStart+nameLength).trim();
					Map<String,String> nodeAttributes=new HashMap<String, String>();
					
					while(!xmlt.startsWith(">")&&!xmlt.startsWith("/>"))
					{
						int aEnd=xmlt.indexOf('=');
						String att=xmlt.substring(0, aEnd);
						xmlt=xmlt.substring(aEnd+2).trim();//+2 for '="'
						int vEnd=xmlt.indexOf('"');
						String value=xmlt.substring(0,vEnd);
						xmlt=xmlt.substring(vEnd+1).trim();//+1 for '"'
						nodeAttributes.put(att,value);
					}
					xmlt=xmlt.substring(1);//get rid of '>'
					String nodeTextContents="";
					if(!xmlt.startsWith("/>"))
					{
						int depth=0;
						int nextO;//nextOpener tag
						int nextC;//nextCloser tag
						int nextCL;//nextClosing pointy bracket
						
						boolean done=false;
						while(!done)
						{
							if(xmlt.startsWith("</"+nodeName+">"))
							{
								done=true;
								xmlt=xmlt.substring(("</"+nodeName+">").length());
							}
							else
							{
								nextC=xmlt.indexOf("</");
								nextO=xmlt.indexOf('<');
								nextCL=xmlt.indexOf('>');
								if(nextC==-1||nextO==-1)
								{
									throw new BadXMLException(new Exception("depth mismatch"));
								}
								
								if(nextO==0||nextC==0)
								{
									if(nextC==0)
									{
										depth--;
									}
									else
									{
										depth++;
									}
									nodeTextContents+=xmlt.substring(0,nextCL+1);
									xmlt=xmlt.substring(nextCL+1);
								}
								else if(nextO<nextC)
								{
									nodeTextContents+=xmlt.substring(0,nextO);
									xmlt=xmlt.substring(nextO);
								}
								else
								{
									nodeTextContents+=xmlt.substring(0,nextC);
									xmlt=xmlt.substring(nextC);
								}
							}
						}
					}
					childNodes.add(new XMLNode(nodeName, nodeAttributes, nodeTextContents));
				}
			}
		}
		catch(Exception e)
		{
			throw new BadXMLException(e);
		}
	}
	private int minPositive(int i1, int i2)
    {
		if(i1>=0)
		{
			if(i2>=0)
			{
				return Math.min(i1, i2);
			}
			else
			{
				return i1;
			}
		}
		else
		{
			return i2;
		}
    }
	public String getName()
	{
		return name;
	}
	public Map<String,String> getAttributes()
	{
		return attributes;
	}
	public List<XMLNode> getChildNodes()
	{
		return childNodes;
	}
	public boolean hasChildNodes()
	{
		return !leaf&&getChildNodes().size()>0;
	}
	public String getTextContents()
	{
		return textContents;
	}
	public String toString()
	{
		String aString="";
		
		for(String attribute:attributes.keySet())
		{
			
			aString+=" "+attribute+"=\""+attributes.get(attribute)+"\"";
		}
		return "<"+name+aString+">"+textContents+"</"+name+">";
	}
}
