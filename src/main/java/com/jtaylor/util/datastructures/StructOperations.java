package com.jtaylor.util.datastructures;

import java.awt.Dimension;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;


public class StructOperations
{
   public static boolean containsIgnoreCase(List<String> array,String string)
   {
      for(String s:array)
      {
         if(s!=null&&s.equalsIgnoreCase(string))
         {
            return true;
         }
      }
      return false;
   }
   public static boolean containsIgnoreCase(String[] array,String string)
   {
      for(String s:array)
      {
         if(s!=null&&s.equalsIgnoreCase(string))
         {
            return true;
         }
      }
      return false;
   }
	public static <E> E getIndexFromIterable(Iterable<E> iterable,int index)
	{
		int i=0;
		for(E e:iterable)
		{
			if(i==index)
			{
				return e;
			}
			i++;
		}
		return null;
	}
	public static <E> Iterable<E> getKeysetSortedByValue(final Map<E, ? extends Comparable> map)
	{
		return new Iterable<E>()
		{
			public Iterator<E> iterator()
			{
				return new Iterator<E>()
				{
					private Map<E, ? extends Comparable> copy = new HashMap<E, Comparable>(map);

					public boolean hasNext()
					{
						return copy.size() > 0;
					}

					public E next()
					{
						Comparable value;
						Comparable max = null;
						E maxKey = null;
						for (E key : copy.keySet())
						{
							value = copy.get(key);
							if (max == null)
							{
								maxKey = key;
								max = value;
							}
							else
							{
								if (max.compareTo(value) < 0)
								{
									maxKey = key;
									max = value;
								}
							}
						}
						copy.remove(maxKey);
						return maxKey;
					}

					public void remove()
					{
						next();
					}

				};
			}
		};

	}

   public static <T> List<T> slice(List<T> list, int start, int finish)
   {
      List<T> slice = new Vector<T>();
      int realFinish = (finish < 0 ? list.size() : finish);
      for (int i = start; i < realFinish && i < list.size(); i++)
      {
         slice.add(list.get(i));
      }
      return slice;
   }

   public static <T> Set<T> slice(Set<T> set, int start, int finish)
   {
      Set<T> slice = new HashSet<T>();
      Iterator<T> iterator=set.iterator();
      //first skip until start
      int i=0;
      while(iterator.hasNext()&&i<start)
      {
         iterator.next();
         i++;
      }
      //next add until finish
      int realFinish = (finish < 0 ? set.size() : finish);
      while(iterator.hasNext()&&i<realFinish)
      {
         slice.add(iterator.next());
         i++;
      }
      return slice;
   }

	public static <T> List<T> setToList(Set<T> set)
	{
		List<T> list = new Vector<T>();
		for (T t : set)
		{
			list.add(t);
		}
		return list;
	}

	public static <T> Set<T> asSet(T... elements)
	{
		Set<T> set = new TreeSet<T>();
		for (T element : elements)
		{
			set.add(element);
		}
		return set;
	}

	public static <T> Collection<T> getComplement(Collection<T> collection1, Collection<T> collection2)
	{
		Collection<T> collection = new Vector<T>();
		for (T t : collection1)
		{
			if (!collection2.contains(t))
			{
				collection.add(t);
			}
		}
		return collection;
	}

	public static boolean arrayContains(Object[] array, Object o)
	{
		return arrayIndexOf(array, o) > -1;
	}

	public static int arrayIndexOf(Object[] array, Object o)
	{
		for (int i = 0; i < array.length; i++)
		{
			Object a = array[i];
			if (o.equals(a))
			{
				return i;
			}
		}
		return -1;
	}

	public static boolean arrayContains(int[] array, int o)
	{
		return arrayIndexOf(array, o) > -1;
	}

	public static int arrayIndexOf(int[] array, int o)
	{
		for (int i = 0; i < array.length; i++)
		{
			int a = array[i];
			if (o == a)
			{
				return i;
			}
		}
		return -1;
	}

	public static boolean arrayIsEmpty(Object[] array)
	{
		if (array == null)
		{
			return true;
		}
		else
		{
			for (int i = 0; i < array.length; i++)
			{
				if (array[i] != null)
				{
					return false;
				}
			}
			return true;
		}
	}

	public static final ToStringer<Object> DEFAULT_TO_STRINGER = new ToStringer<Object>()
	{
		public String toString(Object t)
		{
			if (t == null)
			{
				return "";
			}
			else
			{
				return t.toString();
			}
		}
	};

	public static <T> String matrixToString(T[][] matrix)
	{
		return matrixToString(matrix, DEFAULT_TO_STRINGER, ',', ',');
	}

	public static <T> String matrixToString(T[][] matrix, ToStringer<T> stringer, char cellDelim, char rowDelim)
	{
		if (matrix == null)
		{
			return null;
		}
		else
		{
			String string = "{";
			try
			{
				for (T[] array : matrix)
				{
					string += arrayToString(array, stringer, cellDelim);
					string += rowDelim;
				}
				return string.substring(0, string.length() - 1) + "}";//remove final comma and add closing bracket
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return string + "err}";
			}
		}
	}

	public static <K,V> String mapToString(Map<K,V> map)
	{
		String string="";
		for(K k:map.keySet())
		{
			string+=k+"\n\t"+map.get(k)+"\n";
		}
		return string;
	}

	public static <T> String arrayToString(T[] array)
	{
		return new EasyVector<T>(array).toString();
	}

//	public static <T> String arrayToString(T[] array)
//	{
//		return arrayToString(array, DEFAULT_TO_STRINGER, ',');
//	}

	public static <T> String arrayToString(T[] array, ToStringer<T> stringer, char cellDelim)
	{
		if (array == null)
		{
			return null;
		}
		else if (array.length == 0)
		{
			return "{}";
		}
		else
		{
			String string = "{";
			try
			{
				for (T cell : array)
				{
					string += stringer.toString(cell) + cellDelim;
				}
				return string.substring(0, string.length() - 1) + "}";//remove final comma and add closing bracket
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return string + "err}";
			}
		}
	}

	public static <K, V> Map<K, V> pruneNullValues(Map<K, V> map)
	{
		Map<K, V> prunedMap = new HashMap<K, V>();
		V value;
		for (K key : map.keySet())
		{
			value = map.get(key);
			if (value != null)
			{
				prunedMap.put(key, value);
			}
		}
		return prunedMap;
	}

	public static boolean stringEndsWithIgnoreCase(String string, String suffix)
	{
		Pattern p = Pattern.compile(".*\\Q" + suffix + "\\E$");
		return p.matcher(string).matches();
	}

	public static void pruneEmptyStrings(Object[][] matrix)
	{
		for(int i=0;i<matrix.length;i++)
		{
			for(int j=0;j<matrix[0].length;j++)
			{
				if(matrix[i][j] instanceof String&&((String)matrix[i][j]).trim().length()==0)
				{
					matrix[i][j]=0;
				}
			}
		}
	}

	public static <A,B> boolean mapContainsKey(Map<A,B> myFieldMap, Object key)
	{
		for(A a:myFieldMap.keySet())
		{
			if(key.equals(a))
			{
				return true;
			}
		}
		return false;
	}

	public static <A,B> Object[][] listOfPairsToMatrix(List<Pair<A,B>> list)
	{
		Object[][] matrix=new Object[list.size()][2];
		for(int i=0;i<matrix.length;i++)
		{
			matrix[i][0]=list.get(i).getA();
			matrix[i][1]=list.get(i).getB();
		}
		return matrix;
	}

	public static List<String> delimetedToList(String str, String delim)
	{
		List<String> list=new Vector<String>();
		for(String l:str.split(delim))
		{
			list.add(l.trim());
		}
		return list;
	}

	public static boolean listIsEmpty(List list)
	{
		if(list==null)
		{
			return true;
		}
		int nulls=0;
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i)==null)
			{
				nulls++;
			}
		}
		if(nulls>=list.size())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static boolean endsWithIgnoreCase(String string, String end)
	{
		if(string.length()<end.length())
		{
			return false;
		}
		String stringEnd=string.substring(string.length()-end.length());
//		System.out.println("stringend: "+end);
		return stringEnd.equalsIgnoreCase(end);
	}

	public static HashMap<String, Object> readPreferenceMap(byte[] data) throws IOException, ClassNotFoundException
   {
      ObjectInputStream oin=new ObjectInputStream(new ByteArrayInputStream(data));
      return (HashMap<String,Object>)oin.readObject();
	}

	public static byte[] writePreferenceMap(HashMap<String,Object> prefs) throws IOException
   {
		ByteArrayOutputStream bout=new ByteArrayOutputStream();
      ObjectOutputStream oout=new ObjectOutputStream(bout);
      oout.writeObject(prefs);
      return bout.toByteArray();
	}
	public static boolean allGreaterThanOrEqualTo(Collection<Double> doubles, double d)
	{
		for(double doub:doubles)
		{
			if(Double.compare(doub,d)<1)
			{
				return false;
			}
		}
		return true;
	}

	public static List<UUID> uidsToUUIDs(List<String> uids)
	{
		List<UUID> guids=new Vector<UUID>();
		for(String uid:uids)
		{
			guids.add(UUID.fromString(uid));
		}
		return guids;
	}

	public static <T> Vector<Vector<T>> mapToVectorOfVectors(Map<T, T> map)
	{
		Vector<Vector<T>> vector=new Vector<Vector<T>>();
		for(T t:map.keySet())
		{
			vector.add(new Vector<T>(Arrays.asList(t,map.get(t))));
		}
		return vector;
	}

   public static <T> T getMax(List<T> list, Comparator<T> comparator)
   {
      T max=list.get(0);
      for(int i=1;i<list.size();i++)
      {
         if(comparator.compare(list.get(i),max)>0)
         {
            max=list.get(i);
         }
      }
      return max;
   }

   public static boolean startsWithAny(String prefix, Collection<String> list)
   {
      for(String s:list)
      {
         if(prefix.startsWith(s))
         {
            return true;
         }
      }
      return false;
   }

   public static boolean anyEndsWith(String suffix, Collection<String> list)
   {
      for(String s:list)
      {
         if(s.endsWith(suffix))
         {
            return true;
         }
      }
      return false;
   }

   public static boolean allEquals(Collection c, Object s)
   {
      for(Object o:c)
      {
         if(!o.equals(s))
         {
            return false;
         }
      }
      return true;
   }

   public static int nIndexOf(String source, String pattern, int index)
   {
      int count = 0;
      for(int i=0;i<source.length();i++)
      {
         if(source.substring(i).startsWith(pattern))
         {
            if(count ==index)
            {
               return i;
            }
            else
            {
               count++;
            }
         }
      }
      return -1;
   }

   public static interface ToStringer<T>
	{
		public String toString(T t);
	}

	public static <T> Vector<Vector<T>> matrixToListOfLists(T[][] matrix)
	{
		Vector<Vector<T>> lists = new Vector<Vector<T>>();
		Vector<T> list;
		for (int i = 0; i < matrix.length; i++)
		{
			list = new Vector<T>();
			for (int j = 0; j < matrix[0].length; j++)
			{
				list.add(matrix[i][j]);
			}
			lists.add(list);
		}
		return lists;
	}

	public static Object[][] listOfListsToMatrix(List<List<Object>> matrix2)
	{
		Object[][] matrix = new Object[matrix2.size()][getLargestSubVectorSize(matrix2)];
		for (int i = 0; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix[0].length; j++)
			{
				try
				{
					matrix[i][j] = matrix2.get(i).get(j);
				}
				catch (Exception e)
				{
				}
			}
		}
		return matrix;
	}

	private static int getLargestSubVectorSize(List<List<Object>> matrix2)
	{
		int max = 0;
		for (List<Object> list : matrix2)
		{
			if (list.size() > max)
			{
				max = list.size();
			}
		}
		return max;
	}

	public static <T, Y> Map<T, Y> concatinateMaps(
		Map<T, Y> closenessMetadata,
		Map<T, Y> fontMetadata)
	{
		Map<T, Y> combinedMap = new HashMap<T, Y>();
		for (T key : fontMetadata.keySet())
		{
			combinedMap.put(key, fontMetadata.get(key));
		}
		for (T key : closenessMetadata.keySet())
		{
			combinedMap.put(key, closenessMetadata.get(key));
		}
		return combinedMap;
	}

	public static Object[][] concatinateMatriciesVertically(Object[][] matrix1, Object[][] matrix2)
	{
		Object[][] matrix3 = new Object[matrix1.length + matrix2.length][matrix1[0].length + matrix2[0].length];
		for (int i = 0; i < matrix3.length; i++)
		{
			if (i < matrix1.length)
			{
				matrix3[i] = matrix1[i];
			}
			else
			{
				matrix3[i] = matrix2[i - matrix1.length];
			}
		}
		return matrix3;
	}

	public static <T> List<T> concatinateLists(List<T> list1, List<T> list2)
	{
		List<T> combinedList = new Vector<T>(list1.size()+list2.size());
		for (T t : list1)
		{
			combinedList.add(t);
		}
		for (T t : list2)
		{
			combinedList.add(t);
		}
		return combinedList;
	}

	public static int countOccurances(String source, String pattern)
	{
		int count = 0;
		String temp = new String(source);
		while (temp.indexOf(pattern) > -1)
		{
			count++;
			temp = temp.substring(temp.indexOf(pattern) + 1);
		}
		return count;
	}

	public static Object[][] convertListOfRowsToMatrix(List<List<?>> lists)
	{
		Object[][] matrix = new Object[_getMaxSize(lists)][lists.size()];
		List<?> list;
		for (int x = 0; x < lists.size(); x++)
		{
			list = lists.get(x);
			for (int y = 0; y < list.size(); y++)
			{
				matrix[x][y] = list.get(y);
			}
		}
		return matrix;
	}

	private static int _getMaxSize(List<List<?>> lists)
	{
		int max = 0;
		for (List<?> l : lists)
		{
			if (l.size() > max)
			{
				max = l.size();
			}
		}
		return max;
	}

	public static Object[][] convertListOfColumnsToMatrix(List<List<?>> lists)
	{
		Object[][] matrix = new Object[_getMaxSize(lists)][lists.size()];
		List<?> list;
		for (int y = 0; y < lists.size(); y++)
		{
			list = lists.get(y);
			for (int x = 0; x < list.size(); x++)
			{
				matrix[x][y] = list.get(x);
			}
		}
		return matrix;
	}

	public static boolean listStartsWith(List<String> strings, String prefix)
	{
		for (String string : strings)
		{
			if (string.startsWith(prefix))
			{
				return true;
			}
		}
		return false;
	}

	public static <T> boolean listContainsAny(Collection<T> collection1, Collection<T> collection2)
	{
		for (T t : collection1)
		{
			if (collection2.contains(t))
			{
				return true;
			}
		}
		return false;
	}

	public static Dimension max(Dimension... dimensions)
	{
		Dimension max = dimensions[0].getSize();//its essentailly a copy constructor
		for (Dimension d : dimensions)
		{
			if (d.getHeight() > max.getHeight())
			{
				max.setSize(max.getWidth(), d.getHeight());
			}
			if (d.getWidth() > max.getWidth())
			{
				max.setSize(d.getWidth(), max.getHeight());
			}
		}
		return max;
	}

	public static <T> boolean listIsFullOfEquals(List<T> tabs)
	{
		if (tabs.size() < 2)
		{
			return true;
		}
		else
		{
			for (int i = 1; i < tabs.size(); i++)
			{
				if (tabs.get(i) == null)
				{
					if (tabs.get(i - 1) != null)
					{
						return false;
					}
				}
				else if (!tabs.get(i).equals(tabs.get(i - 1)))
				{
					return false;
				}
			}
		}
		return true;

	}

	public static <T> List<T> arrayToList(T[] array)
	{
		List<T> list = new Vector<T>();
		for (T t : array)
		{
			list.add(t);
		}
		return list;
	}

	public static List<Integer> arrayToList(int[] array)
	{
		List<Integer> list = new Vector<Integer>();
		for (int i : array)
		{
			list.add(i);
		}
		return list;
	}

	public static <E extends Enum<E>> EnumSet<E> stringListToEnumSet(List<String> stringList, Class<E> enumClass)
	{
		return stringListToEnumSet(stringList, enumClass, false);
	}

	public static <E extends Enum<E>> EnumSet<E> stringListToEnumSet(List<String> stringList, Class<E> enumClass, boolean reformat)
	{
		EnumSet<E> set = EnumSet.noneOf(enumClass);
		for (String string : stringList)
		{
			for (E e : enumClass.getEnumConstants())
			{
				if (string.equals((reformat ? reformatStringToEnum(e.toString()) : e.toString())))
				{
					set.add(e);
					break;
				}
			}
		}
		return set;
	}
//	public static <E extends Enum<E>> List<String> enumSetToStringList(EnumSet<E> enumSet)
//	{
//		return enumSetToStringList(enumSet, false);
//	}
//	public static <E extends Enum<E>> List<String> enumSetToStringList(EnumSet<E> enumSet,boolean reformat)
//	{

	//		List<String> list=new Vector<String>();
//		for(E e:enumSet)
//		{
//			list.add((reformat?reformatEnumToString(e.toString()):e.toString()));
//		}
//		return list;
//	}
	public static <E extends Enum<E>> List<String> enumArrayToStringList(E[] enumArray)
	{
		return enumArrayToStringList(enumArray, false);
	}

	public static <E extends Enum<E>> List<String> enumArrayToStringList(E[] enumArray, boolean reformat)
	{
		List<String> list = new Vector<String>();
		for (E e : enumArray)
		{
			list.add((reformat ? reformatEnumToString(e.toString()) : e.toString()));
		}
		return list;
	}

	public static <E extends Enum<E>> int getEnumIndex(E e)
	{
		Enum[] enumConstants = e.getClass().getEnumConstants();
		for (int i = 0; i < enumConstants.length; i++)
		{
			if (enumConstants[i] == e)
			{
				return i;
			}
		}
		return -1;
	}

	public static String reformatStringToEnum(String string)
	{
		return string.toUpperCase().trim().replaceAll("[\\s]+", "_");
	}

	public static String reformatEnumToString(String string)
	{
		System.out.println("debug: " + string);
		string = string.toLowerCase().replaceAll("_", " ").trim();
		System.out.println("debug: " + string);
		if (string.length() > 0)
		{
			string = Character.toUpperCase(string.charAt(0)) + string.substring(1);
			for (int i = 1; i < string.length(); i++)
			{
				if (string.charAt(i) == ' ' && i + 1 < string.length())
				{
					string = string.substring(0, i + 1) + Character.toUpperCase(string.charAt(i + 1)) + (i + 2 < string.length() ? string.substring(i + 2) : "");
					System.out.println("debug: " + string);
				}
			}
		}
		return string;
	}

	public static <T> List<List<T>> splitList(List<T> list, int subListSize)
	{
		List<List<T>> superList = new Vector<List<T>>();
		int j = 0;
		List<T> subList = new Vector<T>();
		for (int i = 0; i < list.size(); i++)
		{
			if (j >= subListSize)
			{
				superList.add(subList);
				j = 0;
				subList = new Vector<T>();
			}
			subList.add(list.get(i));
			j++;
		}
		if (j != 0)
		{
			superList.add(subList);
		}
		return superList;
	}

	public static <A, B> Map<A, B> listOfPairsToMap(List<Pair<A, B>> list)
	{
		Map<A, B> map = new HashMap<A, B>();
		for (Pair<A, B> pair : list)
		{
			map.put(pair.getA(), pair.getB());
		}
		return map;
	}

	public static enum tester
	{
		one, two, THREE_A
	}

	public static <E extends Enum<E>> List<String> dummy(Class<E> e)
	{
		return enumArrayToStringList(e.getEnumConstants());
	}

	public static String enumToString(Enum e)
	{
		String string = e.toString();
		string = string.toLowerCase();
		int index = string.indexOf('_');
		while (index >= 0 && index + 1 < string.length())
		{
			string = string.substring(0, index) + " " + string.substring(index + 1, index + 2).toUpperCase() + string.substring(index + 2);
			index = string.indexOf('_');
		}
		string = string.substring(0, 1).toUpperCase() + string.substring(1);
		if (string.charAt(string.length() - 1) == '_')
		{
			string = string.substring(0, string.length() - 1);
		}
		return string;
	}

	enum Enum1
	{
		HELLO_WORLD, B_, C
	}

	;

	public static void main(String[] args)
	{
      List<Integer> list=Arrays.asList(1,2,3,4,99,4,5,100,9);
      System.out.println(getMax(list,new ComparableComparator<Integer>()));
//		String email="Dear Chrystal,\n" +
//						 " \n" +
//						 "I hope this e-mail finds you well.  WWE Studios has developed a new online service to make the approval of images related to our movies more user-friendly.  In this e-mail, you find a link to review and approve images from the DVD commentary from WWE Studios film, The Chaperone, starring Ariel Winter.  \n" +
//						 " \n" +
//						 "We hope that you can take a few minutes to review and approve the images in this easy to use online system.  Once you click on the link below, you can view all of the images and make your selections.  Once you are done with your selections, click the SUBMIT button and your choices will be automatically registered into our photo system.  You will not have to send back any contact sheets or even alert us when you are done.  Our photo system will register your approvals or rejections once you click SUBMIT.    \n" +
//						 " \n" +
//						 "%link%\n" +
//						 " \n" +
//						 "Please review the images by Friday, December 10.  You can call me at 203-352-8657 with any questions.  \n" +
//						 " \n" +
//						 "Thank you very much for your help with this.  \n" +
//						 " \n" +
//						 "Regards,\n" +
//						 " \n" +
//						 " \n" +
//						 "Kevin Hennessy \n" +
//						 "WWE Studios\n" +
//						 "World Wrestling Entertainment Inc.  \n" +
//						 "1241 East Main Street\n" +
//						 "Stamford, CT 06902";
//		String url= "http://photoapprovals.wwecorp.com/ApprovalTask/Application.html?eid=yw0ew";
//		System.out.println(email.replaceAll("%link%",url));
//		System.out.println(encode(123,456,789));
//		System.out.println(decode(encode(123,456,789)));
//		System.out.println(endsWithIgnoreCase("hello.dol","DOL"));
//		System.out.println(Enum1.HELLO_WORLD.toString());
//		System.out.println(enumToString(Enum1.HELLO_WORLD));
//		System.out.println(enumToString(Enum1.B_));
//		Map<String,Object> map=new HashMap<String,Object>();
//		map.put("a",null);
//		System.out.println(map);
		//System.out.println(containsAny("DoubleTrouble",new String[]{"doub"},true));
//		System.out.println(dummy(tester.class));
//		System.out.println(enumArrayToStringList(tester.class.getEnumConstants(),true));
//		System.out.println("reformatted: "+reformatEnumToString("HELLO_DUDE"));
//		List<Integer> l=new Vector<Integer>();
//		for(int i=0;i<300;i++)
//		{
//			l.add(i);
//		}
//		System.out.println("complete list: "+l);
//		System.out.println("reorganized list: "+splitList(l, 52));
	}

	public static <T> List<T> enumerationToList(Enumeration<T> enumeration)
	{
		List<T> list = new Vector<T>();
		while (enumeration.hasMoreElements())
		{
			list.add(enumeration.nextElement());
		}
		return list;
	}

	public static <T> List<T> castList(List<?> list, Class<T> t)
	{
		List<T> newList = new Vector<T>();
		for (Object item : list)
		{
			if (t.isInstance(item))
			{
				newList.add((T) item);
			}
		}
		return newList;
	}

	public static boolean containsAny(String string, String[] words, boolean ignoreCase)
	{
		for (String word : words)
		{
			Pattern p = Pattern.compile(".*" + Pattern.quote(word) + ".*", Pattern.CASE_INSENSITIVE);
			if (p.matcher(string).matches())
			{
				return true;
			}
		}
		return false;
	}
   public static boolean containsAny(String string, char[] letters)
   {
      for(char c:string.toCharArray())
      {
         for(char l:letters)
         {
            if(c==l)
            {
               return true;
            }
         }
      }
      return false;
   }

	private static String encode(int... nums)
	{
		String encodedString = "";
		String stringNum;
		String encodedNum;
		boolean first = true;
		for (int num : nums)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				encodedString += "0";
			}
			stringNum = Integer.toString(num,6);
			encodedNum = "";
			for (char digit : stringNum.toCharArray())
			{
				if (digit == '0') encodedNum += 'q';
				else if (digit == '1') encodedNum += 'w';
				else if (digit == '2') encodedNum += 'e';
				else if (digit == '3') encodedNum += 'r';
				else if (digit == '4') encodedNum += 't';
				else if (digit == '5') encodedNum += 'y';
			}
			encodedString += encodedNum;
		}
		return encodedString;
	}

	private static List<Integer> decode(String encodedString)
	{
		List<Integer> nums = new Vector<Integer>();
		String decodedNum;
		for (String encodedNum : encodedString.split("0"))
		{
			decodedNum = "";
			for (char digit : encodedNum.toCharArray())
			{
				if (digit == 'q') decodedNum += '0';
				else if (digit == 'w') decodedNum += '1';
				else if (digit == 'e') decodedNum += '2';
				else if (digit == 'r') decodedNum += '3';
				else if (digit == 't') decodedNum += '4';
				else if (digit == 'y') decodedNum += '5';
			}
			nums.add(Integer.parseInt(decodedNum,6));
		}
		return nums;
	}


}
