package com.jtaylor.util;

import com.jtaylor.util.datastructures.EasyVector;
import com.jtaylor.util.datastructures.Pair;
import com.jtaylor.util.datastructures.StructOperations;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 5/25/11
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class LdapOperations
{
   private Logger log;
   private List<String> myHosts;
   private int myPort;
   private String myUserDN;
   private String myPassword;
   private DirContext myDirContext;
   private String myUserBaseDN;
   private String myRoleBaseDN;
   private static final String LDAP_ATTRIBUTE_ACCOUNT_NAME = "sAMAccountName";
   public static final String LDAP_ATTRIBUTE_MAIL="mail";
   public static final String LDAP_ATTRIBUTE_CN="cn";
   private static final String LDAP_ATTRIBUTE_MEMBER_OF = "memberOf";

   public static final String MODULE_ID_LDAP = "com.moksa.ldap";
   public static final String KEY_SERVER="ldap-server";//string
   public static final String KEY_USERNAME="ldap-username";//string
   public static final String KEY_PASSWORD="ldap-password";//string
   public static final String KEY_PORT="ldap-port";//int
   public static final String KEY_DN_USER="ldap-basedn-user";
   public static final String KEY_DN_ROLE="ldap-basedn-role";
   public static final int DEF_PORT=389;

   public LdapOperations(Map<String,Object> prefs)
   {
      this((String)prefs.get(KEY_SERVER),prefs.containsKey(KEY_PORT)?(Integer)prefs.get(KEY_PORT):DEF_PORT,(String)prefs.get(KEY_USERNAME),(String)prefs.get(KEY_PASSWORD),(String)prefs.get(KEY_DN_USER),prefs.containsKey(KEY_DN_ROLE)?(String)prefs.get(KEY_DN_ROLE):null);
   }
   public LdapOperations(String host,String userDN,String password,String userBaseDN,String roleBaseDN)
   {
      this(host,DEF_PORT,userDN,password,userBaseDN,roleBaseDN);
   }
   public LdapOperations(String host,int port,String userDN,String password,String userBaseDN,String roleBaseDN)
   {
      log=Logging.createServerLogger(LdapOperations.class);
      myHosts=new EasyVector(host.split(","));
      myPort=port;
      myUserDN=userDN;
      myPassword=password;
      myUserBaseDN =userBaseDN;
      myRoleBaseDN =roleBaseDN;
   }
   private void connect() throws Exception
   {
      boolean success=false;
      String host;
      for(int i=0;i<myHosts.size();i++)
      {
         host=myHosts.get(i);
         try
         {
            Hashtable env = new Hashtable();
            String sp = "com.sun.jndi.ldap.LdapCtxFactory";
            env.put(Context.INITIAL_CONTEXT_FACTORY, sp);
            String ldapUrl = "ldap://"+host+":"+myPort+"/";
            env.put(Context.PROVIDER_URL, ldapUrl);
            env.put(Context.SECURITY_AUTHENTICATION,"simple");
            env.put(Context.SECURITY_PRINCIPAL,myUserDN);
            env.put(Context.SECURITY_CREDENTIALS,myPassword);
            myDirContext=new InitialDirContext(env);
         }
         catch(Exception e)
         {
            if(i==myHosts.size()-1)
            {
               log.error("could not connect to last host: "+host,e);
               throw e;
            }
            else
            {
               log.warn("could not connect to host: "+host+", trying others",e);
            }
         }
      }
   }
   private void disconnect() throws Exception
   {
      myDirContext.close();
   }

   //return a list (in order of users) of data list (in order of attributes)
   public List<List<String>> getLdapData(List<String> users,List<String> attributes) throws Exception
   {
      connect();
      String[] attributesArray=new String[attributes.size()];
      for(int i=0;i<attributes.size();i++)
      {
         attributesArray[i]=attributes.get(i);
      }
      List<List<String>> ldapData=new Vector<List<String>>();
      for(String user:users)
      {
         SearchControls sc = new SearchControls();
         sc.setReturningAttributes(attributesArray);
         sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
         String filter="(& (objectClass=person)(sAMAccountName="+user+") )";

         NamingEnumeration results = myDirContext.search(myUserBaseDN, filter, sc);
         if(results.hasMore())
         {
            SearchResult sr = (SearchResult) results.next();
            Attributes attrs = sr.getAttributes();
            List<String> data=new Vector<String>();
            for(String attribute:attributes)
            {
               Attribute attr = attrs.get(attribute);
               if(attr==null)
               {
                  data.add(null);
               }
               else
               {
                  NamingEnumeration enumeration=attr.getAll();
                  while(enumeration.hasMore())
                  {
                     data.add((String)enumeration.next());
                  }
//                  data.add((String) attr.get());
               }
            }
            ldapData.add(data);
         }
         else
         {
            ldapData.add(new Vector<String>());
         }
      }
      disconnect();
      return ldapData;
   }
//   public String getEmailAddressesForNames(List<String> names) throws Exception
//   {
//      List<String> users
//   }
   public String getEmailAddressForName(String name) throws Exception
   {
      String user=getLdapUsersForNames(Arrays.asList(name)).get(0);
      return getEmailAddress(user);
   }

   public List<String> getLdapUsersForNames(List<String> names) throws Exception
   {
      connect();
      String[] attributesArray=new String[]{LDAP_ATTRIBUTE_ACCOUNT_NAME};
      List<String> userNames=new Vector<String>(names.size());
      for(String name:names)
      {
         SearchControls sc = new SearchControls();
         sc.setReturningAttributes(attributesArray);
         sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
         String filter="(& (objectClass=person)(cn="+name+") )";

         NamingEnumeration results = myDirContext.search(myUserBaseDN, filter, sc);
         if(results.hasMore())
         {
            SearchResult sr = (SearchResult) results.next();
            Attributes attrs = sr.getAttributes();
            Attribute attr=attrs.get(LDAP_ATTRIBUTE_ACCOUNT_NAME);
            String userName=null;
            if(attr==null)
            {
               throw new Exception("Object did not have account name: "+attrs);
            }
            else
            {
               NamingEnumeration enumeration=attr.getAll();
               while(enumeration.hasMore())
               {
                  userName=(String)enumeration.next();
               }
            }
            userNames.add(userName);
         }
         else
         {
            throw new Exception("User not found: "+name);
         }
      }
      disconnect();
      return userNames;
   }

   public String getEmailAddress(String user) throws Exception
   {
      return getEmailAddresses(Arrays.asList(user)).get(0);
   }
   public List<String> getEmailAddresses(List<String> users) throws Exception
   {
      List<List<String>> ldapData=getLdapData(users, Arrays.asList(LDAP_ATTRIBUTE_MAIL));
      log.debug("raw data: "+ldapData);
      List<String> emailAddresses=new Vector<String>();
      for(List<String> data:ldapData)
      {
         if(data==null||data.isEmpty())
         {
            emailAddresses.add(null);
         }
         else
         {
            emailAddresses.add(data.get(0));
         }
      }
      return emailAddresses;
   }
//   private List<String> getUserRoles(String user) throws Exception
//   {
//
//   }
   private static Map<String,Pair<Date,List<String>>> userRolesCache=new HashMap<String,Pair<Date,List<String>>>();
   public List<String> getUserRoles(String user,String[] cumulusRoles) throws Exception
   {
      if(!userRolesCache.containsKey(user)||userRolesCache.get(user).getA().getTime()<new Date().getTime()-(1000*60))
      {
         connect();
         List<String> roles=new Vector<String>();
         List<String> groups=getLdapData(Arrays.asList(user), Arrays.asList(LDAP_ATTRIBUTE_MEMBER_OF)).get(0);
         System.out.println("groups: "+groups);
         if(groups==null||groups.isEmpty())
         {
            log.debug("no groups found for user: "+user);
         }
         else
         {
            for(String group:groups)
            {
               String possibleRoleName=parseCN(group);
               if(possibleRoleName!=null&&possibleRoleName.length()>0)
               {
                  if(StructOperations.containsIgnoreCase(cumulusRoles,possibleRoleName))
                  {
                     roles.add(possibleRoleName);
                     System.out.println("adding role: "+possibleRoleName);
                     log.debug("adding role: "+possibleRoleName);
                  }
                  else
                  {
                     System.out.println("ignoring possiblerole because it's not in cumulusroles: "+possibleRoleName);
                     log.debug("ignoring possiblerole because it's not in cumulusroles: "+possibleRoleName);
                  }
               }
               else
               {
                  log.debug("group: "+group+", did not contain a CN");
               }
            }
            log.debug("returning roles: "+roles+" for user: "+user);
         }
         disconnect();
         userRolesCache.put(user,new Pair<Date, List<String>>(new Date(),roles));
      }
      else
      {
         log.debug("using cache");
      }
      return userRolesCache.get(user).getB();
   }
   private static String parseCN(String string)
   {
      int cnIndex=string.toUpperCase().indexOf("CN");
      if(cnIndex>=0)
      {
         string=string.substring(cnIndex+2,string.length());
         int eqIndex=string.indexOf("=");
         if(eqIndex>=0)
         {
            int endIndex=string.contains(",")?string.indexOf(","):string.length();
            return string.substring(eqIndex+1,endIndex).trim();
         }
         else
         {
            return null;
         }
      }
      else
      {
         return null;
      }
   }

   /*//this didn't work at all
   public List<String> getUserRoles(String user,String[] cumulusRoles) throws Exception
   {
      connect();
      List<String> roles=new Vector<String>();
      SearchControls sc = new SearchControls();
      String[] attributesArray=new String[]{LDAP_ATTRIBUTE_CN};
      sc.setReturningAttributes(attributesArray);
      sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
      String filter1="(& (objectClass=group)(member="+user+"))";
      NamingEnumeration results1 = myDirContext.search(myUserBaseDN, filter1, sc);
      System.out.println("anyresults: "+results1.hasMore());
      while(results1.hasMore())
      {
         SearchResult sr1 = (SearchResult) results1.next();
         Attributes attrs1 = sr1.getAttributes();
         Attribute attr1=attrs1.get(LDAP_ATTRIBUTE_CN);
         if(attr1!=null)
         {
            String possibleRoleName=(String)attr1.get();
            if(StructOperations.containsIgnoreCase(cumulusRoles,possibleRoleName))
            {
               roles.add(possibleRoleName);
               System.out.println("adding role: "+possibleRoleName);
               log.debug("adding role: "+possibleRoleName);
            }
            else
            {
               System.out.println("ignoring possiblerole because it's not in cumulusroles: "+possibleRoleName);
               log.debug("ignoring possiblerole because it's not in cumulusroles: "+possibleRoleName);
            }

         }
         else
         {
            System.out.println("cn attribute was null for result: "+attrs1);
            log.debug("cn attribute was null for result: "+attrs1);
         }
      }

      disconnect();
      return roles;
   }
   */
   public List<String> getUsersInRole(String role) throws Exception
   {
      connect();
      List<String> users=new Vector<String>();
      String[] attributesArray=new String[]{};
      SearchControls sc = new SearchControls();
      sc.setReturningAttributes(attributesArray);
      sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
      String filter="(& (cn="+role+")(objectClass=group))";
      NamingEnumeration results = myDirContext.search(myRoleBaseDN, filter, sc);
      if(results.hasMore())
      {
         SearchResult sr = (SearchResult) results.next();
         String dn=sr.getNameInNamespace();
         SearchControls sc1 = new SearchControls();
         String[] attributesArray1=new String[]{LDAP_ATTRIBUTE_ACCOUNT_NAME};
         sc1.setReturningAttributes(attributesArray1);
         sc1.setSearchScope(SearchControls.SUBTREE_SCOPE);
//         String filter1="(& (objectClass=person)(sAMAccountName=lbuaawh))";
         String filter1="(& (objectClass=person)(memberOf="+dn+"))";
         NamingEnumeration results1 = myDirContext.search(myUserBaseDN, filter1, sc1);

         while(results1.hasMore())
         {
            SearchResult sr1 = (SearchResult) results1.next();
            Attributes attrs1 = sr1.getAttributes();
            Attribute attr1=attrs1.get(LDAP_ATTRIBUTE_ACCOUNT_NAME);
            if(attr1!=null)
            {
               users.add((String)attr1.get());
            }
            else
            {
            }
         }
      }
      else
      {
         return new Vector<String>();//it's not a group in ldap
      }
      disconnect();
      return users;
   }
   public static void main(String[] args)
   {
      try
      {
         LdapOperations ldap=new LdapOperations("rpc3whqdc00w2k3.na.rpchome.com","CN=DAM-LDAPREAD-SVC,OU=DAM,OU=MISC,DC=na,DC=rpchome,DC=com","ld@p2011!","OU=Rich Products Corp,DC=na,DC=rpchome,DC=com","OU=Users and Groups,OU=WHQ,OU=Rich Products Corp,DC=na,DC=rpchome,DC=com");
//         System.out.println(ldap.getEmailAddress("lbuakec"));
//         System.out.println(ldap.getLdapData(Arrays.asList("lbuakec"), Arrays.asList(LDAP_ATTRIBUTE_MEMBER_OF)));
//         System.out.println(parseCN("CN=WHQ01DAM_Pkg_Regulatory,OU=Users and Groups,OU=WHQ,OU=Rich Products Corp,DC=na,DC=rpchome,DC=com"));
//         System.out.println(ldap.getUsersInRole("WHQ01DAM_Pkg_Regulatory"));
         System.out.println(ldap.getUserRoles("lbuacyp",new String[]{"CB_DesignFirm",
               "CB_Pre-pressSuppliers",
               "CB_PrinterSupplier",
               "Demo1",
               "Demo2",
               "Demo3",
               "Domain Users",
               "ISB_Agencies",
               "PkGraphics_DesignFirms",
               "PkGraphics_Pre-pressSuppliers",
               "PkGraphics_Printers",
               "VinTestRole",
               "WHQ01DAM_Admin",
               "WHQ01DAM_CB_Cybrarian",
               "WHQ01DAM_CB_MarketingMgrs",
               "WHQ01DAM_CB_PackagingMgr",
               "WHQ01DAM_CB_PowerUsers",
               "WHQ01DAM_CB_Regulatory",
               "WHQ01DAM_FS_Cybrarian",
               "WHQ01DAM_FS_Users",
               "WHQ01DAM_ISB_Cybrarian",
               "WHQ01DAM_ISB_Marketing",
               "WHQ01DAM_ISB_ProductMgrs",
               "WHQ01DAM_ISB_SalesMgrs",
               "WHQ01DAM_Pkg_Cybrarian",
               "WHQ01DAM_Pkg_Legal",
               "WHQ01DAM_Pkg_Marketing",
               "WHQ01DAM_Pkg_PowerUsers",
               "WHQ01DAM_Pkg_Regulatory",
               "WHQ01DAM_Printer"}));
//         System.out.println(ldap.getUserRoles("lbuakec",new String[]{"WHQ01DAM_Pkg_Regulatory"}));
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
//      Hashtable env = new Hashtable();
//
//      String sp = "com.sun.jndi.ldap.LdapCtxFactory";
//      env.put(Context.INITIAL_CONTEXT_FACTORY, sp);
//
//      String ldapUrl = "ldap://rpc3whqdc00w2k3.na.rpchome.com:389/";
//      env.put(Context.PROVIDER_URL, ldapUrl);
//
//      env.put(Context.SECURITY_AUTHENTICATION,"simple");
//
//      String dn="CN=DAM-LDAPREAD-SVC,OU=DAM,OU=MISC,DC=na,DC=rpchome,DC=com";
//      env.put(Context.SECURITY_PRINCIPAL,dn);
//
//      String password="ld@p2011!";
//      env.put(Context.SECURITY_CREDENTIALS,password);
//
////      env.put(Context.REFERRAL,"ignore");//of ignore follow throw
//
//      DirContext dctx = new InitialDirContext(env);
//      String base = "OU=Rich Products Corp,DC=na,DC=rpchome,DC=com";
//      SearchControls sc = new SearchControls();
//      String[] attributeFilter = { "cn", "mail" };
//      sc.setReturningAttributes(attributeFilter);
//      sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
//
////      String filter = "(&(sn=W*)(l=Criteria*))";
//      String filter="(& (objectClass=person)(sAMAccountName=lssidxm) )";
//
//      NamingEnumeration results = dctx.search(base, filter, sc);
//      int i=0;
//      while (results.hasMore()&&i<100) {
//         SearchResult sr = (SearchResult) results.next();
//         Attributes attrs = sr.getAttributes();
//
//         Attribute attr = attrs.get("cn");
//         System.out.print(attr.get() + ": ");
//         attr = attrs.get("mail");
//         System.out.println(attr==null?"null":attr.get());
//         i++;
//      }
//      dctx.close();
   }

}
