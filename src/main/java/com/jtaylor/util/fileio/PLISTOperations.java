package com.jtaylor.util.fileio;

import com.jtaylor.util.SystemOperations;
import com.jtaylor.util.DateOperations;
import com.jtaylor.util.datastructures.Pair;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class PLISTOperations
{
   public static File getPreferenceFile(String id)
   {
      return new File(SystemOperations.SYSTEM_LIBRARY_FOLDER,id+".plist");
   }
   public static Object getPreference(String id,String key) throws ReadPlistException
   {
      return getPreference(id,key,null);
   }
   public static boolean hasPreference(String id,String key) throws ReadPlistException
   {
      File preferenceFile=getPreferenceFile(id);
      if(preferenceFile.exists())
      {
         Map<String,Object> plist=readPlist(preferenceFile);
         return plist.containsKey(key);
      }
      return false;
   }
   public static Object safelyGetPreference(String id,String key,Object fallback)
   {
      try
      {
         return getPreference(id,key,fallback);
      }
      catch (Exception e)
      {
         return fallback;
      }
   }
   public static Object getPreference(String id,String key,Object fallback) throws ReadPlistException
   {
      File preferenceFile=getPreferenceFile(id);
      if(preferenceFile.exists())
      {
         Map<String,Object> plist=readPlist(preferenceFile);
         if(plist.containsKey(key))
         {
            return plist.get(key);
         }
         else
         {
            return fallback;
         }
      }
      else
      {
         return fallback;
      }
   }
   public static Object setPreference(String id,String key,Object value) throws WritePlistException
   {
      File preferenceFile=getPreferenceFile(id);
      Map<String,Object> preferences=new HashMap<String, Object>();
      if(preferenceFile.exists())
      {
         try
         {
            preferences.putAll(readPlist(preferenceFile));
         }
         catch (Exception e)
         {}
      }
      Object oldValue=preferences.put(key,value);
      writePlist(preferences,preferenceFile);
      return oldValue;
   }
	public static void main(String[] args)
	{
		System.out.println("writing");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("one", 1);
		File file= new File("/Users/jtaylor/Desktop/test.plist");
      try
      {
         writePlist(map,file);
      }
      catch (WritePlistException e)
      {
         e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }
      System.out.println("done");
		System.out.println("reading");
		try
		{
//			System.out.println(FileOperations.cat(new DoctypeFilterInputStream(new FileInputStream(file))));
			System.out.println(readPlist(file));
			System.out.println("done");
		}
		catch (Exception e)
		{
			System.err.println("error");
			e.printStackTrace();
		}

	}

	public static final String PLIST_DOCTYPE_PUBLIC = "-//Apple//DTD PLIST 1.0//EN";

	public static final String PLIST_DOCTYPE_SYSTEM = "http://www.apple.com/DTDs/PropertyList-1.0.dtd";
	//public static final String PLIST_DOCTYPE_SYSTEM="/System/Library/DTDs/PropertyList.dtd";

	//allows us to specify a dtd handler of type org.xml.sax.ext.DeclHandler
//	public static final String ID_DTD_VALIDATION = "http://xml.org/sax/features/validation";
//	public static final String ID_DECLARATION_HANDLER="http://xml.org/sax/properties/declaration-handler";
	//our implementation of the org.xml.sax.ext.DeclHandler
//	public static final String DECLARATION_HANDLER="com.jtaylor.util.fileio.PLISTOperations.DTDHandler";
	/*public static final DeclHandler DECLARATION_HANDLER=new DeclHandler()
	{
		public void elementDecl(String name, String model) throws SAXException
		{
		}
		public void attributeDecl(String eName, String aName, String type, String mode, String value) throws SAXException
		{
		}
		public void internalEntityDecl(String name, String value) throws SAXException
		{
		}
		public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException
		{
		}
	};*/

	public static byte[] serializePlist(Map<String,Object> plistData) throws WritePlistException
   {
		ByteArrayOutputStream bout=new ByteArrayOutputStream();
		writePlist(plistData,bout);
		return Base64.encode(bout.toByteArray()).getBytes();
	}
	public static Map<String, Object> deserializePlist(byte[] bytes) throws IOException, ParserConfigurationException, SAXException, ReadPlistException
	{
		byte[] decodedBytes=Base64.decode(new String(bytes));
		ByteArrayInputStream bin = new ByteArrayInputStream(decodedBytes);
		return readPlist(bin);

	}
	public static void writePlist(Map<String, Object> plistData, File file) throws WritePlistException
   {
		try
		{
			writePlist(plistData, new FileOutputStream(file));
		}
		catch (FileNotFoundException e)
		{
         throw new WritePlistException("File not found: "+file);
		}
	}

	public static void writePlist(Map<String, Object> plistData, OutputStream outputStream) throws WritePlistException
   {
		try
		{
			DocumentBuilderFactory documentBuilderFactory = new DocumentBuilderFactoryImpl();
//			documentBuilderFactory.setFeature(ID_DTD_VALIDATION, false); //this doesnt work
			//documentBuilderFactory.setAttribute(ID_DECLARATION_HANDLER,DECLARATION_HANDLER); //this doesnt work
			Document document = documentBuilderFactory.newDocumentBuilder().newDocument();
			//document.appendChild(doctype);
			Element plist = document.createElement("plist");
			plist.setAttribute("version", "1.0");
			document.appendChild(plist);
			Element dict = document.createElement("dict");
			plist.appendChild(dict);
			_writeDict(plistData, dict, document);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();//new TransformerFactoryImpl(); we'll try this next
			Transformer transformer;
			try
			{
				transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				//transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,"plist");//"-//Apple//DTD PLIST 1.0//EN");//"http://www.apple.com/DTDs/PropertyList-1.0.dtd");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, PLIST_DOCTYPE_PUBLIC);
				transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, PLIST_DOCTYPE_SYSTEM);

				try
				{
					transformer.transform(new DOMSource(document), new StreamResult(outputStream));
					outputStream.flush();
				}
				catch (TransformerException e)
				{
               throw new WritePlistException("Error streaming the results");
				}

			}
			catch (TransformerConfigurationException e)
			{
            throw new WritePlistException("Error Building the transformer",e);
			}

		}
		catch (Exception e)
		{
			throw new WritePlistException("There was an Error Building the document",e);
		}
	}

	private static void _writeDict(Map<String, Object> plistData, Node dictNode, Document document)
	{
		Element keyNode;
		Element objectNode;
		for (String key : plistData.keySet())
		{
			keyNode = document.createElement("key");
			keyNode.appendChild(document.createTextNode(key));
			dictNode.appendChild(keyNode);
			objectNode = _writeNode(plistData.get(key), document);
			dictNode.appendChild(objectNode);
		}
	}

	private static Element _writeNode(Object object, Document document)
	{
		Node objectNode = null;
		if (object instanceof String)
		{
			objectNode = document.createElement("string");
			objectNode.appendChild(document.createTextNode(object.toString()));
		}
		else if (object instanceof Double)
		{
			objectNode = document.createElement("real");
			objectNode.appendChild(document.createTextNode(object.toString()));
		}
		else if (object instanceof Integer)
		{
			objectNode = document.createElement("integer");
			objectNode.appendChild(document.createTextNode(object.toString()));

		}
		else if (object instanceof Boolean)
		{
			objectNode = document.createElement(object.toString());
		}
		else if (object instanceof Date)
		{
			objectNode = document.createElement("date");
			objectNode.appendChild(document.createTextNode(DateOperations.dateFormatISO8601.format(object)));
		}
		else if (object instanceof byte[])
		{
			objectNode = document.createElement("data");
			objectNode.appendChild(document.createTextNode(new String((byte[]) object)));
		}
		else if (object instanceof List)
		{
			objectNode = document.createElement("array");
			_writeArray((List) object, objectNode, document);
		}
		else if (object instanceof Map)
		{
			objectNode = document.createElement("dict");
			_writeDict((Map<String, Object>) object, objectNode, document);
		}
		return (Element) objectNode;
	}

	private static void _writeArray(List object, Node arrayNode,
											  Document document)
	{
		Element objectNode;
		for (Object o : object)
		{
			objectNode = _writeNode(o, document);
			arrayNode.appendChild(objectNode);
		}
	}

	public static void createDefaultPrefs(List<Pair<String, Class>> mandatoryKeys, File file) throws WritePlistException
	{
		Map<String,Object> prefs=new HashMap<String,Object>();
		Class clazz;
		for(Pair<String,Class> pair:mandatoryKeys)
		{
			clazz=pair.getB();
			if (clazz.isAssignableFrom(String.class))
			{
				prefs.put(pair.getA(), "stringValue");
			}
			else if (clazz.isAssignableFrom(Integer.class))
			{
				prefs.put(pair.getA(), 0);
			}
			else if (clazz.isAssignableFrom(Double.class))
			{
				prefs.put(pair.getA(), 0d);
			}
			else if (clazz.isAssignableFrom(Boolean.class))
			{
				prefs.put(pair.getA(), false);
			}
			else if (clazz.isAssignableFrom(Date.class))
			{
				prefs.put(pair.getA(), new Date());
			}
			else if (clazz.isAssignableFrom(Map.class))
			{
				prefs.put(pair.getA(), new HashMap());
			}
			else if (clazz.isAssignableFrom(Map.class))
			{
				prefs.put(pair.getA(), new HashMap());
			}
			else if (clazz.isAssignableFrom(List.class))
			{
				prefs.put(pair.getA(), new Vector());
			}
			else
			{
				throw new RuntimeException("unknown class: "+pair.getB());
			}
		}
		writePlist(prefs,file);
	}

   public static class ReadPlistException extends Exception
   {
      public ReadPlistException(String message)
      {
         super(message);
      }

      public ReadPlistException(String message, Throwable cause)
      {
         super(message,cause);
      }
   }
   public static class WritePlistException extends Exception
   {
      public WritePlistException(String message)
      {
         super(message);
      }

      public WritePlistException(String message, Throwable cause)
      {
         super(message,cause);
      }
   }

	public static Map<String, Object> readPlist(File plistFile) throws ReadPlistException
	{
		return readPlist(plistFile,null);
	}
	public static Map<String, Object> readPlist(File plistFile,List<Pair<String,Class>> mandatoryKeys) throws ReadPlistException
	{
		FileInputStream plistStream;
      try
      {
         plistStream = new FileInputStream(plistFile);
      }
      catch (FileNotFoundException e)
      {
         throw new ReadPlistException("File not found: "+plistFile);
      }
      return readPlist(plistStream,mandatoryKeys);
	}

	public static Map<String, Object> readPlist(InputStream plistStream) throws ReadPlistException
	{
		return readPlist(plistStream,null);
	}
	public static Map<String, Object> readPlist(InputStream plistStream, List<Pair<String, Class>> mandatoryKeys) throws  ReadPlistException
	{
		DocumentBuilderFactory documentBuilderFactory = new DocumentBuilderFactoryImpl();
		documentBuilderFactory.setValidating(false);
		documentBuilderFactory.setIgnoringElementContentWhitespace(true);
		documentBuilderFactory.setIgnoringComments(true);
      DocumentBuilder documentBuilder = null;
      try
      {
         documentBuilder = documentBuilderFactory.newDocumentBuilder();
         try
         {
            Document document = documentBuilder.parse(new DoctypeFilterInputStream(plistStream));
            Element element = (Element) document.getElementsByTagName("plist").item(0);
            removeWhitespaceNodes(element);
            NodeList nodeList = element.getChildNodes();
            Node node;
            for (int i = 0; i < nodeList.getLength(); i++)
            {
               node = nodeList.item(i);
               if (node.getNodeName().equalsIgnoreCase("dict"))
               {
                  if(mandatoryKeys==null||mandatoryKeys.size()==0)
                  {
                     return _readDict(node);
                  }
                  else
                  {
                     return checkMandatoryKeys(_readDict(node),mandatoryKeys);
                  }
               }
            }
            throw new ReadPlistException("dict not found");
         }
         catch (SAXException e)
         {
            throw new ReadPlistException("There was a sax exception while parsing the document",e);
         }
         catch (IOException e)
         {
            throw new ReadPlistException("There was an io exception while parsing the document",e);
         }
      }
      catch (ParserConfigurationException e)
      {
         throw new ReadPlistException("There was an error constructing the document",e);
      }
	}

	public static Map<String, Object> checkMandatoryKeys(Map<String,Object> plist, List<Pair<String, Class>> keys) throws ReadPlistException
	{
		for(Pair<String,Class> key:keys)
		{
			if(!plist.containsKey(key.getA())||plist.get(key.getA())==null)
			{
				throw new ReadPlistException("PList missing mandatory value: "+key.getA());
			}
			if(!key.getB().isAssignableFrom(plist.get(key.getA()).getClass()))
			{
				throw new ReadPlistException("PList value: "+key.getA()+" is not of mandatory type: "+key.getB().getName()+" ("+plist.get(key.getA())+":"+plist.get(key.getA()).getClass()+")");
			}
		}
		return plist;
	}

	private static Map<String, Object> _readDict(Node dictNode) throws ReadPlistException
	{
		Map<String, Object> dictMap = new HashMap<String, Object>();
		NodeList nodeList = dictNode.getChildNodes();
		Node node;
		String nodeName;
		String nodeText;
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			node = nodeList.item(i);
			nodeName = node.getNodeName();
			nodeText = node.getTextContent();
			if (nodeName.equalsIgnoreCase("key"))
			{
				dictMap.put(nodeText, _readNode(nodeList.item(i + 1)));
			}
		}
		return dictMap;
	}

	private static Object _readNode(Node node) throws ReadPlistException
	{
		String nodeName = node.getNodeName();
		String nodeText = node.getTextContent();
//		System.out.println("Node name: " + nodeName);
//		System.out.println("Reading value: " + nodeText);
		if (nodeName.equalsIgnoreCase("string"))
		{
			return nodeText;
		}
		else if (nodeName.equalsIgnoreCase("integer"))
		{
			try
			{
				return Integer.parseInt(nodeText);
			}
			catch (NumberFormatException e)
			{
				throw new ReadPlistException("numberFormatException(" + nodeName + "): " + nodeText);
			}
		}
		else if (nodeName.equalsIgnoreCase("real"))
		{
			try
			{
				return Double.parseDouble(nodeText);
			}
			catch (NumberFormatException e)
			{
				throw new ReadPlistException("numberFormatException(" + nodeName + "): " + nodeText);
			}
		}
		else if (nodeName.equalsIgnoreCase("true"))
		{
			return new Boolean(true);
		}
		else if (nodeName.equalsIgnoreCase("false"))
		{
			return new Boolean(false);
		}
		else if (nodeName.equalsIgnoreCase("data"))
		{
			return nodeText.replaceAll("[\t\n]", "").getBytes();//we leave it in base64
		}
		else if (nodeName.equalsIgnoreCase("date"))
		{
			try
			{
				return DateOperations.dateFormatISO8601.parse(nodeText);
			}
			catch (ParseException e)
			{
				throw new ReadPlistException("dateFormatException(" + nodeName + "): " + nodeText);
			}
		}
		else if (nodeName.equalsIgnoreCase("dict"))
		{
			return _readDict(node);
		}
		else if (nodeName.equalsIgnoreCase("array"))
		{
			return _readArray(node);
		}
		else
		{
			throw new ReadPlistException("unknown node type: " + nodeName + " " + nodeText);
		}
	}

	private static List<Object> _readArray(Node arrayNode) throws ReadPlistException
	{
		List<Object> arrayList = new Vector<Object>();
		NodeList nodeList = arrayNode.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			arrayList.add(_readNode(nodeList.item(i)));
		}
		return arrayList;
	}

	public static void removeWhitespaceNodes(Element e)
	{
		NodeList children = e.getChildNodes();
		for (int i = children.getLength() - 1; i >= 0; i--)
		{
			Node child = children.item(i);
			if (child instanceof Text && ((Text) child).getData().trim().length() == 0)
			{
				e.removeChild(child);
			}
			else if (child instanceof Element)
			{
				removeWhitespaceNodes((Element) child);
			}
		}
	}

	public static class DoctypeFilterInputStream extends BufferedInputStream
	{
		/**
		 * constructor
		 *
		 * @param in the underlying input stream to change the doctype of
		 */
		protected DoctypeFilterInputStream(InputStream in)
		{
			super(in);
		}
/**
	 * convienience fron end to read(byte[],int,int)
	 */
	public int read(byte b[]) throws IOException
	{
		return read(b, 0, b.length);
	}
	/**
	 * this is just a fancy front end to the read() function
	 */
	public int read(byte b[],int off,int len) throws IOException//TODO optimise this code
	{
		if (b == null)
		{
		    throw new NullPointerException();
		}
		else if ((off < 0) || (off > b.length) || (len < 0) ||
			   ((off + len) > b.length) || ((off + len) < 0))
		{
		    throw new IndexOutOfBoundsException();
		}
		else if (len == 0)
		{
		    return 0;
		}

		int c = read();
		if (c == -1)
		{
		    return -1;
		}
		b[off] = (byte)c;

		int i = 1;
		try
		{
		    for (; i < len ; i++)
		    {
				c = read();
				if (c == -1)
				{
				    break;
				}
				if (b != null)
				{
				    b[off + i] = (byte)c;
				}
		    }
		}
		catch (IOException ee) {}
		return i;
	}
		@Override
		public int read() throws IOException
		{
			mark(10);
			byte[] buf=new byte[9];
			super.read(buf,0,9);
			reset();
			String bufString=new String(buf);
			if(bufString.equals("<!DOCTYPE"))
			{
				char readChar;
				do
				{
					readChar= (char) super.read();
				}while(readChar!='>'&&readChar!=-1);
				return super.read();
			}
			else
			{
				reset();
				return super.read();
			}
		}
	}
}
