package com.jtaylor.util;

import com.jtaylor.util.datastructures.StructOperations;
import org.apache.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.*;

public class EmailOperations
{
	private static class MailProperties
	{
		public static final String HOST = "mail.host";
		public static final String USER = "mail.user";
		public static final String STORE_PROTOCOL = "mail.store.protocol";
		public static final String TRANSPORT_PROTOCOL = "mail.transport.protocol";
		public static final String PROTOCOL_HOST = "mail.protocol.host";
		public static final String PROTOCOL_USER = "mail.protocol.user";
		public static final String RETURN_ADDRESS = "mail.from";
		public static final String SMTP_AUTHENTICATION = "mail.smtp.auth";
		public static final String SMTP_PORT = "mail.smtp.port";
		public static final String SMTP_STARTTTLS = "mail.smtp.starttls.enable";
		public static final String DEBUG = "mail.debug";
		public static final String SMTP_SOCKET_CLASS = "mail.smtp.socketFactory.class";

		public static final String SSL_SOCKET = "javax.net.ssl.SSLSocketFactory";
	}
	public static EMail createEmail(String from,String to,String cc,String bcc,String subject,String body, List<File> attachments)
	{
		return new EMail(StructOperations.delimetedToList(to,","),
			StructOperations.delimetedToList(cc,","),
			StructOperations.delimetedToList(bcc,","),
			null,	from, subject,body,false,
			attachments);
	}
	public static interface Mailer
	{
		public void send(EMail email) throws Exception;
	}
	public static class SMTP implements Mailer
	{
		public static final String KEY_SERVER="smtp-server";//string
		public static final String KEY_USERNAME="smtp-username";//string
		public static final String KEY_PASSWORD="smtp-password";//string
		public static final String KEY_PORT="smtp-port";//int
		public static final String KEY_SSL="smtp-ssl";//bool
		public static final String KEY_DEF_FROM="smtp-def-from";//bool
		public static final String MODULE_ID_MAIL= "com.moksa.mail";

		public static final boolean DEF_SSL=false;
		public static final int DEF_PORT=25;
		private String server;
		private String username;
		private String password;
		private int port;
		private boolean isSSL;
      private String defaultFrom;

		public SMTP(String serv, String user, String pass, int portNum, boolean ssl)
		{
			server = serv;
			username = user;
			password = pass;
			port = portNum;
			isSSL = ssl;
		}
		public SMTP(Map<String,Object> prefs)
		{
			if (prefs.containsKey(KEY_SERVER))
			{
				Object result = prefs.get(KEY_SERVER);
				if (result instanceof String)
				{
					server = (String) result;
				}
				else
				{
					throw new RuntimeException("email server preference was not a string");
				}
			}
			else
			{
				throw new RuntimeException("missing email server preference");
			}
			if (prefs.containsKey(KEY_USERNAME))
			{
				Object result = prefs.get(KEY_USERNAME);
				if (result instanceof String)
				{
					username = (String) result;
				}
				else
				{
					throw new RuntimeException("username preferece was not a string");
				}
			}
			else
			{
				throw new RuntimeException("missing username preference");
			}
			if (prefs.containsKey(KEY_PASSWORD))
			{
				Object result = prefs.get(KEY_PASSWORD);
				if (result instanceof String)
				{
					password = (String) result;
				}
				else
				{
					throw new RuntimeException("password preference was not a string");
				}
			}
			else
			{
				throw new RuntimeException("missing password preference");
			}
			if (prefs.containsKey(KEY_PORT))
			{
				Object result = prefs.get(KEY_PORT);
				if (result instanceof Integer)
				{
					port = (Integer) result;
				}
				else
				{
					throw new RuntimeException("port preference was not an integer");
				}
			}
			else
			{
				port = DEF_PORT;
			}
			if (prefs.containsKey(KEY_SSL))
			{
				Object result = prefs.get(KEY_SSL);
				if (result instanceof Boolean)
				{
					isSSL = (Boolean) result;
				}
				else
				{
					throw new RuntimeException("ssl preference was not a boolean");
				}
			}
			else
			{
				isSSL = DEF_SSL;
			}
         if(prefs.containsKey(KEY_DEF_FROM))
         {
            Object result = prefs.get(KEY_DEF_FROM);
            if (result instanceof String)
            {
               defaultFrom = (String) result;
            }
            else
            {
               throw new RuntimeException("def from preference was not a string");
            }
         }
		}
		public SMTP(String serv, String user, String pass)
		{
			this(serv, user, pass, DEF_PORT,DEF_SSL);
		}
      public void setDefaultFrom(String defFrom)
      {
         defaultFrom=defFrom;
      }
      public String getDefaultFrom()
      {
         return getDefaultFrom();
      }
		public void setPort(int portNum)
		{
			port = portNum;
		}

		public void setSSL(boolean ssl)
		{
			isSSL = ssl;
		}

		public boolean isSSL()
		{
			return isSSL;
		}

		public int getPort()
		{
			return port;
		}

		public String getServer()
		{
			return server;
		}

		public String getUser()
		{
			return username;
		}

		public String getPassword()
		{
			return password;
		}

		public void send(EMail email) throws MessagingException
		{
			send(email,Logger.getLogger(EmailOperations.class));
		}
      public void send(EMail email,Logger log) throws MessagingException
		{
			Properties properties = new Properties();
			properties.put(MailProperties.HOST, server);
         if(username==null||username.equals(""))
         {
//            properties.put(MailProperties)
         }
         else
         {
            properties.put(MailProperties.SMTP_AUTHENTICATION, true);
            properties.put(MailProperties.SMTP_STARTTTLS, true);
         }
			if (isSSL)
			{
				properties.put(MailProperties.SMTP_SOCKET_CLASS, MailProperties.SSL_SOCKET);
			}
			properties.put(MailProperties.SMTP_PORT, port);
			Session mailSession = Session.getInstance(properties, new Authenticator()
			{
				protected PasswordAuthentication getPasswordAuthentication()
				{
					return new PasswordAuthentication(username, password);
				}
			});
//			mailSession.setDebug(true);
//			mailSession.setDebugOut(System.out);
			Message message = new MimeMessage(mailSession);
         if (email.getFromAddress() == null||email.getFromAddress().length()==0)
         {
            if(defaultFrom!=null)
            {
              message.setFrom(new InternetAddress(defaultFrom));
            }
         }
         else
         {
            log.debug("setting from address: "+email.getFromAddress());
            message.setFrom(new InternetAddress(email.getFromAddress()));
         }
         if(!StructOperations.listIsEmpty(email.getToAddresses()))
         {
            log.debug("setting to address: "+email.getToAddresses());
            message.setRecipients(Message.RecipientType.TO, _getInternetAddresses(email.getToAddresses()));
         }
         if (!StructOperations.listIsEmpty(email.getCcAddresses()))
         {
            log.debug("setting cc address: " + email.getCcAddresses());
            message.setRecipients(Message.RecipientType.CC, _getInternetAddresses(email.getCcAddresses()));
         }
         if (!StructOperations.listIsEmpty(email.getBccAddresses()))
         {
            log.debug("setting bcc address: " + email.getCcAddresses());

            message.setRecipients(Message.RecipientType.BCC, _getInternetAddresses(email.getBccAddresses()));
         }
         if (!StructOperations.listIsEmpty(email.getReplyAddresses()))
         {
            log.debug("setting reply address: " + email.getReplyAddresses());
            message.setReplyTo(_getInternetAddresses(email.getReplyAddresses()));
         }
         if (email.getSubject() != null)
         {
            log.debug("setting subject: "+email.getSubject());
            message.setSubject(email.getSubject());
         }
         message.setSentDate(new Date());
         Multipart messageBody = new MimeMultipart();
         if (email.getBody() != null)
         {
            BodyPart textPart = new MimeBodyPart();
            textPart.setContent(email.getBody(), (email.isHTML ? "text/html" : "text/plain"));
            messageBody.addBodyPart(textPart);
         }
         BodyPart attachmentPart;
         if(email.getAttachments()!=null)
         {
            for (File attachment : email.getAttachments())
            {
               attachmentPart = new MimeBodyPart();
               attachmentPart.setDataHandler(new DataHandler(new FileDataSource(attachment)));
               attachmentPart.setFileName(attachment.getName());
               attachmentPart.setHeader("Content-ID", "<" + attachment.getName() + ">");
               //log.debug("sendEmailDebug: just attached: "+attachment+" with id: "+"<"+attachment.getName()+">");
               messageBody.addBodyPart(attachmentPart);
            }
         }
         message.setContent(messageBody);
         Transport.send(message);
		}
	}
	public static class GSMTP extends SMTP
	{
		public GSMTP(String user, String pass)
		{
			super("smtp.gmail.com", user, pass, 465, true);
		}
	}
	
	public static class SendMail implements Mailer
	{
		public void send(EMail email)
		{
			Logger log=Logger.getLogger(SendMail.class);
			try
			{
//				Process process=Runtime.getRuntime().exec(new String[]{"mail","-s","\""+subject+"\"",toAddress});
				Process process = Runtime.getRuntime().exec(new String[]{"mail", "-s", email.getSubject(), email.getToAddresses().get(0)});
				OutputStream outputStream = process.getOutputStream();
				BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
				//process.waitFor();
				outputWriter.write(email.getBody());
				outputWriter.flush();
				outputWriter.close();

				InputStream errorStream = process.getErrorStream();
				InputStream inputStream = process.getInputStream();

				process.waitFor();
				BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
				while (inputReader.ready())
				{
					log.debug("out: " + inputReader.readLine());
				}
				BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
				while (errorReader.ready())
				{
					log.error("err: " + errorReader.readLine());
				}
			}
			catch (Exception e)
			{
				log.error("there was an error sending an email using sendmail",e);
			}
		}
	}

	public static class EMail
	{
		private String fromAddress;
		private List<String> toAddresses;
		private List<String> ccAddresses;
		private List<String> bccAddresses;
		private List<String> replyAddresses;

		private String subject;
		private String body;
		List<File> attachments;

		private boolean isHTML;

		public EMail(EMail email)
		{
			this.fromAddress=email.fromAddress;//strings are immutable so we dont worry about copying
			if(email.toAddresses!=null)this.toAddresses=new Vector<String>(email.toAddresses);
			if(email.ccAddresses!=null)this.ccAddresses=new Vector<String>(email.ccAddresses);
			if(email.bccAddresses!=null)this.bccAddresses=new Vector<String>(email.bccAddresses);
			if(email.replyAddresses!=null)this.replyAddresses=new Vector<String>(email.replyAddresses);
			this.subject=email.subject;
			this.body=email.body;
			if(email.attachments!=null)this.attachments=new Vector<File>(email.attachments);//the important part of a file (the part that acutaly lives in the javaVM's memory is immutable
		}
		public EMail(String to, String sub, String bod)
		{
			this(Arrays.asList(to), sub, bod);
		}

		public EMail(List<String> toAndCc, String sub, String bod)
		{
			this(StructOperations.slice(toAndCc, 0, 1), StructOperations.slice(toAndCc, 1, -1), new Vector<String>(), new Vector<String>(), null, sub, bod, false, new Vector<File>());
		}

		public EMail(List<String> to, List<String> cc, List<String> bcc, List<String> reply, String from, String sub, String bod, boolean html, List<File> attach)
		{
			toAddresses = to;
			fromAddress = from;
			ccAddresses = cc;
			bccAddresses = bcc;
			replyAddresses = reply;
			subject = sub;
			body = bod;
			isHTML = html;
			attachments = attach;
		}

		public List<String> getToAddresses()
		{
			if (toAddresses == null)
			{
				return new Vector<String>();
			}
			else
			{
				return new Vector<String>(toAddresses);
			}
      }

		public String getSubject()
		{
			return subject;
		}

		public String getBody()
		{
			return body;
		}

		public String getFromAddress()
		{
			return fromAddress;
		}

		public List<String> getCcAddresses()
		{
			if (ccAddresses == null)
			{
				return new Vector<String>();
			}
			else
			{
				return new Vector<String>(ccAddresses);
			}		}

		public List<String> getBccAddresses()
		{
			if (bccAddresses == null)
			{
				return new Vector<String>();
			}
			else
			{
				return new Vector<String>(bccAddresses);
			}
		}

		public List<String> getReplyAddresses()
		{
			if(replyAddresses==null)
			{
				return new Vector<String>();
			}
			else
			{
				return new Vector<String>(replyAddresses);
			}
		}

		public List<File> getAttachments()
		{
			if(attachments==null)
			{
				return new Vector<File>();
			}
			else
			{
				return new Vector<File>(attachments);
			}
		}

		public boolean isHTML()
		{
			return isHTML;
		}

		public void setToAddresses(List<String> toAddr)
		{
			toAddresses = toAddr;
		}

		public void setFromAddress(String fromAddr)
		{
			fromAddress = fromAddr;
		}

		public void setCcAddresses(List<String> ccAddrs)
		{
			ccAddresses = ccAddrs;
		}

		public void setBccAddresses(List<String> bccAddrs)
		{
			bccAddresses = bccAddrs;
		}

		public void setReplyAddresses(List<String> replyAddrs)
		{
			replyAddresses = replyAddrs;
		}

		public void setAttachments(List<File> attachs)
		{
			attachments = attachs;
		}

		public void setHTML(boolean html)
		{
			isHTML = html;
		}
		public void setSubject(String subj)
		{
			subject=subj;
		}
		public void setBody(String bod)
		{
			body=bod;
		}
	}

	public static void main(String[] args)
	{
		System.out.println("sending");
		SMTP smtp=new GSMTP("webhelpdesk1.moksa@gmail.com","3$a$en33");
      try
      {
         smtp.send(new EMail(Arrays.asList("jtaylor.moksa@gmail.com"),new Vector<String>(),new Vector<String>(),new Vector<String>(),null,"test","testbody",false,null));
      }
      catch (MessagingException e)
      {
         e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }
      //sendGmail("wildmanjake@gmail.com", "gmail test", "this is a test of the new emailoperations class", "jtaylor.moksa@gmail.com", "dizzydevil");
		//sendmailEmail("wildmanjake@gmail.com", "tes t2", "hello. hello. hellooooooo.");
		System.out.println("done");
	}

	public static void sendEmail(String toAddress, String subject, String body, String server, String username, String password)
	{
      try
      {
		   new SMTP(server, username, password).send(new EMail(toAddress, subject, body));
      }
      catch (Exception e)
      {
         Logger.getLogger(EmailOperations.class).error("There was an error sending the email",e);
      }
   }

	public static void sendGmail(String toAddress, String subject, String body, String username, String password)
	{
      try
      {
		   new GSMTP(username, password).send(new EMail(toAddress, subject, body));

      }
      catch (Exception e)
      {
         Logger.getLogger(EmailOperations.class).error("There was an error sending the email",e);
      }
	}

	public static void sendmailEmail(String toAddress, String subject, String body)
	{
		Logger log=Logger.getLogger(EmailOperations.class);
		try
		{
//			Process process=Runtime.getRuntime().exec(new String[]{"mail","-s","\""+subject+"\"",toAddress});
			Process process = Runtime.getRuntime().exec(new String[]{"mail", "-s", subject, toAddress});
			OutputStream outputStream = process.getOutputStream();
			BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
			//process.waitFor();
			outputWriter.write(body);
			outputWriter.flush();
			outputWriter.close();

			InputStream errorStream = process.getErrorStream();
			InputStream inputStream = process.getInputStream();

			process.waitFor();
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
			while (inputReader.ready())
			{
				log.debug("out: " + inputReader.readLine());
			}
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
			while (errorReader.ready())
			{
				log.error("err: " + errorReader.readLine());
			}
		}
		catch (Exception e)
		{
			log.error("there was an error sending an email using sendmail",e);
		}
	}


	private static Address[] _getInternetAddresses(List<String> recipients)
	{
		Logger log=Logger.getLogger(EmailOperations.class);
		InternetAddress[] addresses = new InternetAddress[recipients.size()];
		int i = 0;
		for (String recipient : recipients)
		{
			try
			{
				if(recipient!=null)
				{
					addresses[i] = new InternetAddress(recipient);
				}
			}
			catch (AddressException e)
			{
				log.error("getInternetAddressesException: " + recipient,e);
			}
			finally
			{
				i++;
			}
		}
		return addresses;
	}

//	public static class EMailAppender extends AppenderSkeleton
//	{
//		private PatternLayout mySubjectLayout;
//		private PatternLayout myBodyLayout;
//		private EMail myEmail;
//		private Mailer myMailer;
//		private Level myMinLevel;
//		public EMailAppender(Mailer mailer, EMail email)
//		{
//			this(mailer,email,Level.ALL);
//		}
//		public EMailAppender(Mailer mailer, EMail email,Level minLevel)
//		{
//			super();
//			myMailer = mailer;
//			myEmail = email;
//			mySubjectLayout = new PatternLayout(myEmail.getSubject());
//			myBodyLayout = new PatternLayout(myEmail.getBody());
//			myMinLevel=minLevel;
//		}
//
//		@Override
//		protected void append(LoggingEvent loggingEvent)
//		{
//			if(loggingEvent.getLevel().isGreaterOrEqual(myMinLevel))
//			{
//				EMail mail=new EMail(myEmail);
//				mail.setBody(myBodyLayout.format(loggingEvent));
//				mail.setSubject(mySubjectLayout.format(loggingEvent));
//				myMailer.send(mail);
//			}
//		}
//
//		@Override
//		public boolean requiresLayout()
//		{
//			return false;
//		}
//
//		@Override
//		public void close(){}
//	}
//	public static void sendLocalEmail(String address,String subject,String message)
//	{
//		sendLocalEmail(Arrays.asList(address),null,null,subject,message);
//	}
//	public static void sendLocalEmail(List<String> toAddresses,List<String> ccAddresses,List<String> bccAddresses, String subject,String message)
//	{
//		String addressList="";
//		for(String address:toAddresses)
//		{
//			addressList+=address+",";
//		}
//		if(addressList.length()>0)
//		{
//			addressList=addressList.substring(0, addressList.length()-1);
//		}
//		String ccList="";
//		if(ccAddresses!=null)
//		{
//			for(String address:ccAddresses)
//			{
//				ccList+=address+",";
//			}
//			if(ccList.length()>0)
//			{
//				ccList=ccList.substring(0, ccList.length()-1);
//			}
//		}
//		String bccList="";
//		if(bccAddresses!=null)
//		{
//			for(String address:bccAddresses)
//			{
//				bccList+=address+",";
//			}
//			if(bccList.length()>0)
//			{
//				bccList=bccList.substring(0, bccList.length()-1);
//			}
//		}
//		try
//        {
//			List<String> args=new Vector<String>();
//			args.add("mail");
//			args.add("-s");
//			args.add("subject");
//			if(ccAddresses!=null)
//			{
//				args.add("-c");
//				args.add(ccList);
//			}
//			if(bccAddresses!=null)
//			{
//				args.add("-b");
//				args.add(bccList);
//			}
//			args.add(addressList);
//			String[] args1=new String[args.size()];
//			args.toArray(args1);
//			Process process= Runtime.getRuntime().exec(args1);
//			OutputStream outputStream=process.getOutputStream();
//	        outputStream.write(message.getBytes());
//	        outputStream.flush();
//	        outputStream.close();
//        }
//        catch (Exception e)
//        {
//        	log.debug("sendEmailExcepiton:\n\t"+e.getMessage()+"\n\t"+e.getCause());
//        }
//	}
}
