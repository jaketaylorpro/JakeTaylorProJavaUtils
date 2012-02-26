package com.jtaylor.util;

import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

public class ColorOperations
{
   public static final Color brick=  ColorOperations.hexStringToColor("#990000");
   public static final Color manilla=  ColorOperations.hexStringToColor("#e6dd93");
	public static final Color midnight= ColorOperations.hexStringToColor("#000080");
	public static final Color forest=   ColorOperations.hexStringToColor("#008000");
	public static final Color paleBlue= ColorOperations.hexStringToColor("#ccffff");
	public static final Color grey1= ColorOperations.hexStringToColor("#111111");
	public static final Color grey2= ColorOperations.hexStringToColor("#222222");
	public static final Color grey3= ColorOperations.hexStringToColor("#333333");
	public static final Color grey4= ColorOperations.hexStringToColor("#444444");
	public static final Color grey5= ColorOperations.hexStringToColor("#555555");
	public static final Color grey6= ColorOperations.hexStringToColor("#666666");
	public static final Color grey7= ColorOperations.hexStringToColor("#777777");
	public static final Color grey8= ColorOperations.hexStringToColor("#888888");
	public static final Color grey9= ColorOperations.hexStringToColor("#999999");
	public static final Color grey10= ColorOperations.hexStringToColor("#AAAAAA");
	public static final Color grey11= ColorOperations.hexStringToColor("#BBBBBB");
	public static final Color grey12= ColorOperations.hexStringToColor("#CCCCCC");
	public static final Color grey13= ColorOperations.hexStringToColor("#DDDDDD");
	public static final Color grey14= ColorOperations.hexStringToColor("#EEEEEE");
	public static Color hexStringToColor(String s)
	{
		try
		{
			if(s.startsWith("#"))
			{
				return hexStringToColor(s.substring(1));
			}
			else
			{
				return new Color(Integer.parseInt(s.substring(0, 2),16),Integer.parseInt(s.substring(2, 4),16),Integer.parseInt(s.substring(4, 6),16));
			}
		}
		catch(Exception e)
		{
			System.err.println("hexStringToColorException:\n\t"+e.getMessage()+"\n\t"+e.getCause());
			return null;
		}
	}
	public static String colorToHexString(Color color)
	{
		String r=Integer.toHexString(color.getRed());
		String g=Integer.toHexString(color.getGreen());
		String b=Integer.toHexString(color.getBlue());
		return "#"+(r.length()==2?r:"0"+r)+
			(g.length()==2?g:"0"+g)+
			(b.length()==2?b:"0"+b);
	}
   public static Map<String,String> CUMULUS_COLOR_NAMES =new TreeMap<String,String>(new CaseAgnosticAlphabeticStringComparator());
   public static Map<String,String> CUMULUS_COLOR_NAMES_HEX =new TreeMap<String,String>(new CaseAgnosticAlphabeticStringComparator());
   static
   {
      CUMULUS_COLOR_NAMES.put("Red Label","#FF796B");
      CUMULUS_COLOR_NAMES.put("Orange Label","#FCBA58");
      CUMULUS_COLOR_NAMES.put("Yellow Label","#F0E45F");
      CUMULUS_COLOR_NAMES.put("Green Label","#B7E05F");
      CUMULUS_COLOR_NAMES.put("Blue Label","#164ABFE");
      CUMULUS_COLOR_NAMES.put("Violet Label","#D09ADD");
      CUMULUS_COLOR_NAMES.put("Grey Label","#B5B5B5");
      CUMULUS_COLOR_NAMES.put("White Label","#FFFFFF");
      for(String key:CUMULUS_COLOR_NAMES.keySet())
      {
         CUMULUS_COLOR_NAMES_HEX.put(CUMULUS_COLOR_NAMES.get(key),key);
      }
   }
   public static Map<String,String> CSS_COLOR_NAMES =new TreeMap<String,String>(new CaseAgnosticAlphabeticStringComparator());
   public static Map<String,String> CSS_COLOR_NAMES_HEX =new TreeMap<String,String>(new CaseAgnosticAlphabeticStringComparator());
   static
   {
      CSS_COLOR_NAMES.put("AliceBlue","#F0F8FF");
      CSS_COLOR_NAMES.put("AntiqueWhite","#FAEBD7");
      CSS_COLOR_NAMES.put("Aqua","#00FFFF");
      CSS_COLOR_NAMES.put("Aquamarine","#7FFFD4");
      CSS_COLOR_NAMES.put("Azure","#F0FFFF");
      CSS_COLOR_NAMES.put("Beige","#F5F5DC");
      CSS_COLOR_NAMES.put("Bisque","#FFE4C4");
      CSS_COLOR_NAMES.put("Black","#000000");
      CSS_COLOR_NAMES.put("BlanchedAlmond","#FFEBCD");
      CSS_COLOR_NAMES.put("Blue","#0000FF");
      CSS_COLOR_NAMES.put("BlueViolet","#8A2BE2");
      CSS_COLOR_NAMES.put("Brown","#A52A2A");
      CSS_COLOR_NAMES.put("BurlyWood","#DEB887");
      CSS_COLOR_NAMES.put("CadetBlue","#5F9EA0");
      CSS_COLOR_NAMES.put("Chartreuse","#7FFF00");
      CSS_COLOR_NAMES.put("Chocolate","#D2691E");
      CSS_COLOR_NAMES.put("Coral","#FF7F50");
      CSS_COLOR_NAMES.put("CornflowerBlue","#6495ED");
      CSS_COLOR_NAMES.put("Cornsilk","#FFF8DC");
      CSS_COLOR_NAMES.put("Crimson","#DC143C");
      CSS_COLOR_NAMES.put("Cyan","#00FFFF");
      CSS_COLOR_NAMES.put("DarkBlue","#00008B");
      CSS_COLOR_NAMES.put("DarkCyan","#008B8B");
      CSS_COLOR_NAMES.put("DarkGoldenRod","#B8860B");
      CSS_COLOR_NAMES.put("DarkGray","#A9A9A9");
      CSS_COLOR_NAMES.put("DarkGrey","#A9A9A9");
      CSS_COLOR_NAMES.put("DarkGreen","#006400");
      CSS_COLOR_NAMES.put("DarkKhaki","#BDB76B");
      CSS_COLOR_NAMES.put("DarkMagenta","#8B008B");
      CSS_COLOR_NAMES.put("DarkOliveGreen","#556B2F");
      CSS_COLOR_NAMES.put("Darkorange","#FF8C00");
      CSS_COLOR_NAMES.put("DarkOrchid","#9932CC");
      CSS_COLOR_NAMES.put("DarkRed","#8B0000");
      CSS_COLOR_NAMES.put("DarkSalmon","#E9967A");
      CSS_COLOR_NAMES.put("DarkSeaGreen","#8FBC8F");
      CSS_COLOR_NAMES.put("DarkSlateBlue","#483D8B");
      CSS_COLOR_NAMES.put("DarkSlateGray","#2F4F4F");
      CSS_COLOR_NAMES.put("DarkSlateGrey","#2F4F4F");
      CSS_COLOR_NAMES.put("DarkTurquoise","#00CED1");
      CSS_COLOR_NAMES.put("DarkViolet","#9400D3");
      CSS_COLOR_NAMES.put("DeepPink","#FF1493");
      CSS_COLOR_NAMES.put("DeepSkyBlue","#00BFFF");
      CSS_COLOR_NAMES.put("DimGray","#696969");
      CSS_COLOR_NAMES.put("DimGrey","#696969");
      CSS_COLOR_NAMES.put("DodgerBlue","#1E90FF");
      CSS_COLOR_NAMES.put("FireBrick","#B22222");
      CSS_COLOR_NAMES.put("FloralWhite","#FFFAF0");
      CSS_COLOR_NAMES.put("ForestGreen","#228B22");
      CSS_COLOR_NAMES.put("Fuchsia","#FF00FF");
      CSS_COLOR_NAMES.put("Gainsboro","#DCDCDC");
      CSS_COLOR_NAMES.put("GhostWhite","#F8F8FF");
      CSS_COLOR_NAMES.put("Gold","#FFD700");
      CSS_COLOR_NAMES.put("GoldenRod","#DAA520");
      CSS_COLOR_NAMES.put("Gray","#808080");
      CSS_COLOR_NAMES.put("Grey","#808080");
      CSS_COLOR_NAMES.put("Green","#008000");
      CSS_COLOR_NAMES.put("GreenYellow","#ADFF2F");
      CSS_COLOR_NAMES.put("HoneyDew","#F0FFF0");
      CSS_COLOR_NAMES.put("HotPink","#FF69B4");
      CSS_COLOR_NAMES.put("IndianRed ","#CD5C5C");
      CSS_COLOR_NAMES.put("Indigo ","#4B0082");
      CSS_COLOR_NAMES.put("Ivory","#FFFFF0");
      CSS_COLOR_NAMES.put("Khaki","#F0E68C");
      CSS_COLOR_NAMES.put("Lavender","#E6E6FA");
      CSS_COLOR_NAMES.put("LavenderBlush","#FFF0F5");
      CSS_COLOR_NAMES.put("LawnGreen","#7CFC00");
      CSS_COLOR_NAMES.put("LemonChiffon","#FFFACD");
      CSS_COLOR_NAMES.put("LightBlue","#ADD8E6");
      CSS_COLOR_NAMES.put("LightCoral","#F08080");
      CSS_COLOR_NAMES.put("LightCyan","#E0FFFF");
      CSS_COLOR_NAMES.put("LightGoldenRodYellow","#FAFAD2");
      CSS_COLOR_NAMES.put("LightGray","#D3D3D3");
      CSS_COLOR_NAMES.put("LightGrey","#D3D3D3");
      CSS_COLOR_NAMES.put("LightGreen","#90EE90");
      CSS_COLOR_NAMES.put("LightPink","#FFB6C1");
      CSS_COLOR_NAMES.put("LightSalmon","#FFA07A");
      CSS_COLOR_NAMES.put("LightSeaGreen","#20B2AA");
      CSS_COLOR_NAMES.put("LightSkyBlue","#87CEFA");
      CSS_COLOR_NAMES.put("LightSlateGray","#778899");
      CSS_COLOR_NAMES.put("LightSlateGrey","#778899");
      CSS_COLOR_NAMES.put("LightSteelBlue","#B0C4DE");
      CSS_COLOR_NAMES.put("LightYellow","#FFFFE0");
      CSS_COLOR_NAMES.put("Lime","#00FF00");
      CSS_COLOR_NAMES.put("LimeGreen","#32CD32");
      CSS_COLOR_NAMES.put("Linen","#FAF0E6");
      CSS_COLOR_NAMES.put("Magenta","#FF00FF");
      CSS_COLOR_NAMES.put("Maroon","#800000");
      CSS_COLOR_NAMES.put("MediumAquaMarine","#66CDAA");
      CSS_COLOR_NAMES.put("MediumBlue","#0000CD");
      CSS_COLOR_NAMES.put("MediumOrchid","#BA55D3");
      CSS_COLOR_NAMES.put("MediumPurple","#9370D8");
      CSS_COLOR_NAMES.put("MediumSeaGreen","#3CB371");
      CSS_COLOR_NAMES.put("MediumSlateBlue","#7B68EE");
      CSS_COLOR_NAMES.put("MediumSpringGreen","#00FA9A");
      CSS_COLOR_NAMES.put("MediumTurquoise","#48D1CC");
      CSS_COLOR_NAMES.put("MediumVioletRed","#C71585");
      CSS_COLOR_NAMES.put("MidnightBlue","#191970");
      CSS_COLOR_NAMES.put("MintCream","#F5FFFA");
      CSS_COLOR_NAMES.put("MistyRose","#FFE4E1");
      CSS_COLOR_NAMES.put("Moccasin","#FFE4B5");
      CSS_COLOR_NAMES.put("NavajoWhite","#FFDEAD");
      CSS_COLOR_NAMES.put("Navy","#000080");
      CSS_COLOR_NAMES.put("OldLace","#FDF5E6");
      CSS_COLOR_NAMES.put("Olive","#808000");
      CSS_COLOR_NAMES.put("OliveDrab","#6B8E23");
      CSS_COLOR_NAMES.put("Orange","#FFA500");
      CSS_COLOR_NAMES.put("OrangeRed","#FF4500");
      CSS_COLOR_NAMES.put("Orchid","#DA70D6");
      CSS_COLOR_NAMES.put("PaleGoldenRod","#EEE8AA");
      CSS_COLOR_NAMES.put("PaleGreen","#98FB98");
      CSS_COLOR_NAMES.put("PaleTurquoise","#AFEEEE");
      CSS_COLOR_NAMES.put("PaleVioletRed","#D87093");
      CSS_COLOR_NAMES.put("PapayaWhip","#FFEFD5");
      CSS_COLOR_NAMES.put("PeachPuff","#FFDAB9");
      CSS_COLOR_NAMES.put("Peru","#CD853F");
      CSS_COLOR_NAMES.put("Pink","#FFC0CB");
      CSS_COLOR_NAMES.put("Plum","#DDA0DD");
      CSS_COLOR_NAMES.put("PowderBlue","#B0E0E6");
      CSS_COLOR_NAMES.put("Purple","#800080");
      CSS_COLOR_NAMES.put("Red","#FF0000");
      CSS_COLOR_NAMES.put("RosyBrown","#BC8F8F");
      CSS_COLOR_NAMES.put("RoyalBlue","#4169E1");
      CSS_COLOR_NAMES.put("SaddleBrown","#8B4513");
      CSS_COLOR_NAMES.put("Salmon","#FA8072");
      CSS_COLOR_NAMES.put("SandyBrown","#F4A460");
      CSS_COLOR_NAMES.put("SeaGreen","#2E8B57");
      CSS_COLOR_NAMES.put("SeaShell","#FFF5EE");
      CSS_COLOR_NAMES.put("Sienna","#A0522D");
      CSS_COLOR_NAMES.put("Silver","#C0C0C0");
      CSS_COLOR_NAMES.put("SkyBlue","#87CEEB");
      CSS_COLOR_NAMES.put("SlateBlue","#6A5ACD");
      CSS_COLOR_NAMES.put("SlateGray","#708090");
      CSS_COLOR_NAMES.put("SlateGrey","#708090");
      CSS_COLOR_NAMES.put("Snow","#FFFAFA");
      CSS_COLOR_NAMES.put("SpringGreen","#00FF7F");
      CSS_COLOR_NAMES.put("SteelBlue","#4682B4");
      CSS_COLOR_NAMES.put("Tan","#D2B48C");
      CSS_COLOR_NAMES.put("Teal","#008080");
      CSS_COLOR_NAMES.put("Thistle","#D8BFD8");
      CSS_COLOR_NAMES.put("Tomato","#FF6347");
      CSS_COLOR_NAMES.put("Turquoise","#40E0D0");
      CSS_COLOR_NAMES.put("Violet","#EE82EE");
      CSS_COLOR_NAMES.put("Wheat","#F5DEB3");
      CSS_COLOR_NAMES.put("White","#FFFFFF");
      CSS_COLOR_NAMES.put("WhiteSmoke","#F5F5F5");
      CSS_COLOR_NAMES.put("Yellow","#FFFF00");
      CSS_COLOR_NAMES.put("YellowGreen","#9ACD32");
      for(String key:CSS_COLOR_NAMES.keySet())
      {
         CSS_COLOR_NAMES_HEX.put(CSS_COLOR_NAMES.get(key),key);
      }
   }
}
