package com.giga.tms.util.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import com.giga.base.LoggerUtility;
//import com.giga.tms.util.TMSPrintConstants;
import com.ibm.websphere.asynchbeans.Work;
import com.ibm.websphere.asynchbeans.WorkException;
import com.ibm.websphere.asynchbeans.WorkItem;
import com.ibm.websphere.asynchbeans.WorkManager;



public class TMSEMail{
	private static Logger log = LoggerUtility.getLogger(TMSEMail.class);
	//Email Properties Configuration Type  
	//public static final boolean CONFIGURATION_FLAG =true;// true = jndi , false = properties 
	//public static final String EMAIL_PROPERTIES_CONFIGURATION_TYPE ="PROPERTIES";
	
	static Configuration configInstance;
	public static final String EMAIL_PROPERTIES = "resources/email.properties";
	public static  String EMAIL_JNDI_NAME;
	public static  String WORKMANAGER_JNDI_NAME;
	private static  String FROM_ADDRESS; 
	private static javax.mail.Session session = null;
	static javax.naming.InitialContext ctx;

	static {
		try {
			try {
				configInstance = new PropertiesConfiguration(EMAIL_PROPERTIES);
			} catch (ConfigurationException e) {
				
				e.printStackTrace();
			}

			FROM_ADDRESS= configInstance.getString("FROM_ADDRESS");
			WORKMANAGER_JNDI_NAME = configInstance.getString("WORKMANAGER_JNDI_NAME");
			EMAIL_JNDI_NAME =  configInstance.getString("EMAIL_JNDI_NAME");
			log.debug("Usage : EMail : Static Block : Properties file based Email Configuration : Mail Session creation Success :: ");
			//props.put("mail.smtp.auth", true);
	
			ctx = new javax.naming.InitialContext();
			session = (javax.mail.Session) ctx.lookup(EMAIL_JNDI_NAME);
			log.debug("Usage : EMail : Static Block : JNDI based Email Configuration : Mail Session creation Success :: ");

			} catch (NamingException e) {
				
				log.error("Usage : EMail : Static Block : JNDI based Email Configuration : Mail Session creation failed :: ",e);
			}
		
	}
	


	/*public void sendEMail(Map<String, String> map) {
		System.out.println("inside Send Email");
		log.debug("Entering EMail- sendEMail()");
		Map<String, String> localMap = new HashMap<String, String>();
		localMap.putAll(map);

		String mail_id = localMap.get(TMSPrintConstants.MAIL_ID);
		String type = localMap.get(TMSPrintConstants.TYPE);
		String url = localMap.get(TMSPrintConstants.URL);
		String encrpytedFilePath = localMap
				.get(TMSPrintConstants.ENCRYPTED_FILE_PATH);

		System.out.println("MAIL ID : " + mail_id);
		System.out.println("TYPE : " + type);
		System.out.println("URL : " + url);
		
		System.out.println("Content to be Sent : \n");
		StringBuilder sb = new StringBuilder();
		sb.append("Dear User,\n");
		sb.append("Kindly download "+type+"  PDF from the link below \n");
		sb.append(url+"?ref="+encrpytedFilePath);
		
		System.out.println("------------"+ configInstance);
		String smtpHostServer = configInstance
				.getString(TMSPrintConstants.SMTP_HOST_SERVER);
		System.out.println("SMTPSERVER :" + smtpHostServer);
		System.out.println("Exiting EMail- sendEMail()");
		sendMail("ganeshkumar.t@hcl.com",mail_id,"Batch Mail", sb.toString());
	}*/

	
	public static void sendMail(TMSMailType type, List<String> toMailId,List<String> ccAddress,  String userName,String fileName,Map<String,String> dataMap) {
		log.debug("sendEmail()  <--");
		try{
			System.out.println("type::::"+type);
			System.out.println("toMailId::::"+toMailId);
			System.out.println("ccAddress::::"+ccAddress);
			System.out.println("userName::::"+userName);
			System.out.println("fileName::::"+fileName);
			System.out.println("dataMap::::"+dataMap.entrySet());
		   InitialContext ic = new InitialContext();
		   System.out.println("WORKMANAGER_JNDI_NAME::::"+WORKMANAGER_JNDI_NAME);
		   WorkManager wm = (WorkManager)ic.lookup(WORKMANAGER_JNDI_NAME);
		   
		   List<String> fileNames = new ArrayList<String>();
		   fileNames.add(fileName);
		   WorkItem item = wm.startWork(new TMSAsynEMailSender(session,type,FROM_ADDRESS,toMailId,ccAddress,userName,fileNames,dataMap));

		   System.out.println("startWork :::"+item.getStatus());
		  log.info("Mail Asyn process status"+item.getStatus());
		} catch (NamingException e) {
			e.printStackTrace();
	  		log.error("Exception in sendEmail()",e);
	  	} catch (WorkException e) {
	  		e.printStackTrace();
	  		log.error("Exception in sendEmail()",e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			log.error("Exception in sendEmail()",e);
		}
	}

	public static void sendMail(TMSMailType type, List<String> toMailId,List<String> ccAddress,String userName,Map<String,String> dataMap,List<String> fileNames) {
		log.debug("sendEmail()  <--");
		try{
			System.out.println("type::::"+type);
			System.out.println("toMailId::::"+toMailId);
			System.out.println("ccAddress::::"+ccAddress);
			System.out.println("userName::::"+userName);
			System.out.println("fileName::::"+fileNames);
			System.out.println("dataMap::::"+dataMap);
		   InitialContext ic = new InitialContext();
		   System.out.println("WORKMANAGER_JNDI_NAME::::"+WORKMANAGER_JNDI_NAME);
		   WorkManager wm = (WorkManager)ic.lookup(WORKMANAGER_JNDI_NAME);

		   WorkItem item = wm.startWork(new TMSAsynEMailSender(session,type,FROM_ADDRESS,toMailId,ccAddress,userName,fileNames,dataMap));

		   System.out.println("startWork END::::"+item.getStatus());
		  log.info("Mail Asyn process status"+item.getStatus());
		} catch (NamingException e) {
	  		log.error("Exception in sendEmail()",e);
	  	} catch (WorkException e) {
	  		log.error("Exception in sendEmail()",e);
		} catch (IllegalArgumentException e) {
			log.error("Exception in sendEmail()",e);
		}
		
	}
	
	/*public static void sendMail(String fromMailId, String toMailId,  String subject, String htmlMessage)
	{
		
	}*/
	
/*public static void sendMail(String fromMailId, String toMailId,  String subject, String htmlMessage, final String password) throws AddressException, MessagingException {
		
		//Session session = Session.getInstance(props, null);
		
		 Session session = Session.getDefaultInstance(props,   new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(password, "");
	            }
	        });
		 
		 MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(fromMailId));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO,
                                  new InternetAddress(toMailId));

         // Set Subject: header field
         message.setSubject("This is the Subject Line!");

         // Now set the actual message
         message.setText("This is actual message");

         // Send message
         Transport transport = session.getTransport("smtp");
         transport.send(message);
         System.out.println("Sent message successfully....");
	}*/
	
/*	public static void main(String[] args) throws AddressException, MessagingException {

		System.out.println("SimpleEmail Start");
		StringBuilder sb = new StringBuilder();
		sb.append("Dear User,\n");
		sb.append("Kindly download PDF from the link below \n");
		sb.append(PrintConstants.URLPATH+"?ref="+"L%2F0PXtVi61N6Cttd6qQXdm12s4Rdu8zsjhwrZXPe1piu0EWp");
		String toEmailID = "jothi.k@hcl.com";
		sendMail("ganeshkumar.t@hcl.com",toEmailID,"Batch Mail - Testing", sb.toString(),null);
		System.out.println();
		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpHostServer);
		Session session = Session.getInstance(props, null);
		EmailUtil.sendEmail(session, toEmailID, "SimpleEmail Testing Subject",
				"SimpleEmail Testing Body");
		
		
	}
	*/
	/*
	private static void meth1(String a){
		System.out.println("String ");
	}
	
	private static void meth1(Integer a){
		System.out.println("Integre ");
	}
	
	public static void main(String[] args) {
		meth1((Integer)null);
	}
*/
}
