package com.jtaylor.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 4/29/11
 * Time: 10:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class WildcardPathFileFilter implements FileFilter
{
   private Logger log;
   private static String SEPARATORS="[/\\\\\\\\]";//to replace *
   private static String NON_SEPARATORS="[^/\\\\\\\\]";//to replace *
   private static String ONE_FOLDER_WILD=SEPARATORS+NON_SEPARATORS+"*"+SEPARATORS+"?";//to replace /*/
   private static String N_FOLDER_WILD=".*";//to replace /*/
   private String myWildcardPattern;
   private String myRegexPattern;
   private Pattern myPattern;
   public WildcardPathFileFilter(String wildcardPattern)
   {
      this(wildcardPattern,true);
   }
   public WildcardPathFileFilter(String wildcardPattern,boolean caseSensitive)
   {
      log=Logging.createServerLogger(WildcardPathFileFilter.class);
      log.debug("wildcard pattern: "+wildcardPattern);
      myWildcardPattern=wildcardPattern;
      //first escape all escape characters
      for(int i=0;i<myWildcardPattern.length()&&i<100;i++)
      {
//         System.out.println(myWildcardPattern);
//         System.out.println("i: "+i+";"+myWildcardPattern.charAt(i));
         if(myWildcardPattern.charAt(i)=='\\')
         {
            if(myWildcardPattern.substring(i).startsWith("\\**\\"))
            {
               i+=3;
            }
            else if(myWildcardPattern.substring(i).startsWith("\\*\\"))
            {
               i+=2;
            }
            else if(myWildcardPattern.substring(i).startsWith("\\Q"))
            {
               myWildcardPattern=myWildcardPattern.substring(0,i)+"\\E\\\\Q\\Q"+myWildcardPattern.substring(i+2);
               i+=6;
            }
            else if(myWildcardPattern.substring(i).startsWith("\\E"))
            {
               myWildcardPattern=myWildcardPattern.substring(0,i)+"\\E\\\\E\\Q"+myWildcardPattern.substring(i+2);
               i+=6;
            }
            else
            {
               //do nothing
            }
         }
      }

      myWildcardPattern=myWildcardPattern.replaceAll("/\\*/","\\\\E\\$ONE_FOLD_WILD\\$\\\\Q");
      myWildcardPattern=myWildcardPattern.replaceAll("\\\\\\*\\\\","\\\\E\\$ONE_FOLD_WILD\\$\\\\Q");//this is new
      myWildcardPattern=myWildcardPattern.replaceAll("/\\*\\*/","\\\\E\\$N_FOLD_WILD\\$\\\\Q");
      myWildcardPattern=myWildcardPattern.replaceAll("\\\\\\*\\*\\\\","\\\\E\\$N_FOLD_WILD\\$\\\\Q");//this is new
      log.debug("wildcard pattern after subs: " + myWildcardPattern);
      myRegexPattern=myWildcardPattern.replaceAll("\\*","\\\\E"+NON_SEPARATORS+"*"+"\\\\Q");
      myRegexPattern=myRegexPattern.replaceAll("\\?","\\\\E"+NON_SEPARATORS+"\\\\Q");
      myRegexPattern=myRegexPattern.replaceAll("\\Q$ONE_FOLD_WILD$\\E",ONE_FOLDER_WILD);
      myRegexPattern=myRegexPattern.replaceAll("\\Q$N_FOLD_WILD$\\E",N_FOLDER_WILD);
      myRegexPattern="\\Q"+myRegexPattern+"\\E";
      log.debug("regex pattern final: " + myRegexPattern);
      myPattern= Pattern.compile(myRegexPattern,caseSensitive?0:Pattern.CASE_INSENSITIVE);

   }
   public boolean accept(File pathname)
   {
      return !pathname.isHidden()&&myPattern.matcher(pathname.getAbsolutePath()).matches();
   }
   public static void main(String[] args)
   {
      String myWildcardPattern;
      String myRegexPattern;
      String wildcardPattern="/Users/jtaylor/Desktop/box/findTest/**/afile5.abc";
      myWildcardPattern=wildcardPattern;
      myWildcardPattern=myWildcardPattern.replaceAll("/\\*/","\\\\E\\$ONE_FOLD_WILD\\$\\\\Q");
      myWildcardPattern=myWildcardPattern.replaceAll("/\\*\\*/","\\\\E\\$N_FOLD_WILD\\$\\\\Q");
      System.out.println("wildcard pattern: "+myWildcardPattern);
      myRegexPattern=myWildcardPattern.replaceAll("\\*","\\\\E"+NON_SEPARATORS+"*"+"\\\\Q");
      myRegexPattern=myRegexPattern.replaceAll("\\?","\\\\E"+NON_SEPARATORS+"\\\\Q");
      myRegexPattern=myRegexPattern.replaceAll("\\Q$ONE_FOLD_WILD$\\E",ONE_FOLDER_WILD);
      myRegexPattern=myRegexPattern.replaceAll("\\Q$N_FOLD_WILD$\\E",N_FOLDER_WILD);
      myRegexPattern="\\Q"+myRegexPattern+"\\E";
      System.out.println("regex pattern: "+myRegexPattern);
      Pattern p= Pattern.compile(myRegexPattern);
      List<String> files=new Vector<String>();
      files.add("/Users/jtaylor/Desktop/box/findTest/fold1/afile5.abc");
      files.add("/Users/jtaylor/Desktop/box/findTest/fold1/afile6.abc");
      files.add("/Users/jtaylor/Desktop/box/findTest/fold2/afile6.abc");
      files.add("/Users/jtaylor/Desktop/box/findTest/afile5.abc");
      for (String f:files)
      {
         System.out.println("matches: "+f+": "+p.matcher(f).matches());
      }
   }
}
