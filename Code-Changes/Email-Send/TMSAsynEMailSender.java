package com.giga.tms.util.mail;

import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.giga.base.LoggerUtility;
import com.giga.base.service.GigaServiceLocator;
import com.giga.tms.ITMSServices;
import com.giga.tms.dao.CommonTMSDAO;
//import com.giga.tms.dao.domain.EMailLog;
import com.ibm.websphere.asynchbeans.Work;

public class TMSAsynEMailSender implements Work {
	private static Logger log = LoggerUtility.getLogger(TMSAsynEMailSender.class);
	
	private String fromAddress;
	private List<String> ccAddress;
	private List<String>  toMailId;
	private TMSMailType mailType;
	private Session session;
	private List<String> fileNames;
	private String userName;
	private Map<String,String> dataMap;
	
	public TMSAsynEMailSender(Session session,TMSMailType mailType,String fromAddress,List<String> toAddress,List<String> ccAddress, String userName,List<String> fileNames,Map<String,String> dataMap){
		this.fromAddress=fromAddress;
		this.ccAddress=ccAddress;
		this.toMailId = toAddress;
		this.mailType=mailType;
		this.session = session;
		this.fileNames=fileNames;
		this.userName=userName;
		this.dataMap=dataMap;
	}
	@Override
	public void run() {
		MimeMessage message = new MimeMessage(session);
	
		// Create the email addresses involved
		String subject="";
		Boolean isSuccess= false;
		Boolean isWithAttachment=false;
		String failureReason="";
		try {
			message.setFrom(new InternetAddress(fromAddress));
			Map<String,String> emailDetails = new TMSMailTemplateGenerator().getMailContent(mailType, dataMap);
			
			if(emailDetails.get("TO")!=null && emailDetails.get("TO").length()>0){
				log.info("Appending TO address from Template:"+emailDetails.get("TO"));
				toMailId.add(emailDetails.get("TO"));
			}
			if(emailDetails.get("CC")!=null && emailDetails.get("CC").length()>0){
				log.info("Appending CC address from Template:"+emailDetails.get("CC"));
				ccAddress.add(emailDetails.get("CC"));
			}
			//InternetAddress from = new InternetAddress(fromMailId);
			if(emailDetails.get("SUBJECT")!=null && emailDetails.get("SUBJECT").length()>0){
				log.info("Subject:"+emailDetails.get("SUBJECT"));
				subject=emailDetails.get("SUBJECT");
			}
		
			try{					
					message.setSubject(subject);
					//message.setFrom(from);
					InternetAddress address[] = new InternetAddress[toMailId.size()];
			         for(int i=0;i<toMailId.size();i++){
			        	 address[i] = new InternetAddress(toMailId.get(i));
			         }
			         message.addRecipients(javax.mail.Message.RecipientType.TO,address);
			         if(ccAddress!=null&& ccAddress.size()>0){
			        	 InternetAddress ccaddress[] = new InternetAddress[ccAddress.size()];
				         for(int i=0;i<ccAddress.size();i++){
				        	 ccaddress[i] = new InternetAddress(ccAddress.get(i));
				         }
				         message.addRecipients(javax.mail.Message.RecipientType.CC,ccaddress);
			         }
			         
			         // Create a multi-part to combine the parts
					Multipart multipart = new MimeMultipart();
					// Create your text message part
					BodyPart messageBodyPart = new MimeBodyPart();
					
					messageBodyPart.setContent(emailDetails.get("CONTENT"), "text/html; charset=UTF-8");
					// Add html part to multi part
					multipart.addBodyPart(messageBodyPart);
					
					// Attachment
					if(fileNames!=null && !fileNames.isEmpty()){
						for(String filename : fileNames){
							if(filename!=null && filename.length()>0){
								 messageBodyPart = new MimeBodyPart();
							     DataSource source = new FileDataSource(filename);
							         messageBodyPart.setDataHandler(new DataHandler(source));
							         messageBodyPart.setFileName(source.getName());
							    multipart.addBodyPart(messageBodyPart);
							    
							    isWithAttachment=true;
							}
						}
					}
					
				 // Associate multi-part with message
					message.setContent(multipart);
					// Send message
					Transport transport = session.getTransport("smtp");
					transport.connect();//"smtp.gmail.com", "username", "password");
					transport.sendMessage(message, message.getAllRecipients());
					isSuccess=true;
					log.info("Mail sent successfully......");
					}  catch(AuthenticationFailedException e){
					log.error("Authentication Error. Looking Up the context again..");
					try {
						javax.naming.InitialContext ctx = new javax.naming.InitialContext();
						session = (javax.mail.Session) ctx.lookup(TMSEMail.EMAIL_JNDI_NAME);
						Transport transport = session.getTransport("smtp");
						transport.connect();//"smtp.gmail.com", "username", "password");
						System.out.println("Transport: " + transport.toString());
						transport.sendMessage(message, message.getAllRecipients());
						isSuccess=true;
						log.info("Mail sent successfully......");
					} catch (NamingException ne) {
						
						log.error("Exception while Looking up mail contextin asynEmail()",ne);
						failureReason=ne.getMessage();
					}
				}
				catch (AddressException e) {
					
					log.error("Exception in asynEmail()",e);
					failureReason=e.getMessage();
				} catch (MessagingException e) {
					
					log.error("Exception in asynEmail()",e);
					failureReason=e.getMessage();
				}
			
			CommonTMSDAO  commondao= (CommonTMSDAO)GigaServiceLocator.getService(ITMSServices.daoLayer.COMMON_TMS_DAO);
			//toMailId = truncateStr(toMailId, 255);
			//ccAddress = truncateStr(ccAddress, 255);
			String mailTypeStr = truncateStr(mailType.toString(), 50);
			subject = truncateStr(subject, 255);
			failureReason = truncateStr(failureReason, 500);
			
			if(userName==null){ // arul added on 16Mar2017
				userName = "TMS_SYSTEM_GENERATED";
			}
			
			userName = truncateStr(userName, 60);
			commondao.saveEMailLog(toMailId, ccAddress, mailTypeStr,subject,userName , isSuccess, isWithAttachment, failureReason);
							
		}catch(Exception e){
			e.printStackTrace();
			log.error("Exception in asynEmail",e);
		}
					
	}
	@Override
	public void release() {
		
		
	}
	
	public static String truncateStr(String value, int length) {
	   if(value.length() > length) {
			 return value.substring(0, length);
	   }else {
			 return value;
	   }
	}

}
