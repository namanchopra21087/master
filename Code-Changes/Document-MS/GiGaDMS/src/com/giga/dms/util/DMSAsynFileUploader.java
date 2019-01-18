package com.giga.dms.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.giga.dms.IDMSService;
import com.giga.dms.dao.DMSGDaoImpl;
import com.giga.dms.util.mail.DMSMailTemplateGenerator;
import com.giga.dms.util.mail.DMSMailType;
import com.giga.dms.vo.DMSDocumentAuditHistoryVO;
import com.giga.dms.vo.DMSDocumentVO;
import com.giga.dms.vo.DMSResultObject;
import com.giga.dms.vo.DMSSearchConentVO;
import com.giga.exception.ServiceException;
import com.ibm.websphere.asynchbeans.Work;

public class DMSAsynFileUploader implements Work {
	private static Logger log = LoggerUtility.getLogger(DMSAsynFileUploader.class);
	
	private String fromAddress;
	private List<String> ccAddress;
	private List<String>  toMailId;
	private Session session;
	private String fileNames;
	private String userName;
	private DMSMailType mailType;
	private Map<String,String> mailContentMap;
	List<DMSDocumentVO> documentListVO;
	private String userRole;
	private DMSAysnProcessType processType;
	
	public DMSAsynFileUploader(Session session,DMSMailType mailType,String fromAddress,List<String> toAddress,List<String> ccAddress, String userName,String fileNames,Map<String,String> mailContentMap,List<DMSDocumentVO> documentListVO,String userRole,DMSAysnProcessType processType){
		this.fromAddress=fromAddress;
		this.ccAddress=ccAddress;
		this.toMailId = toAddress;
		this.mailType=mailType;
		this.session = session;
		this.fileNames=fileNames;
		this.userName=userName;
		this.mailContentMap=mailContentMap;
		this.documentListVO=documentListVO;
		this.userRole=userRole;
		this.processType=processType;
	}
	
	@Override
	public void run() { 
		
		DMSResultObject uploadResult=new DMSResultObject();
		DMSServicesUtil dmsSUtil=new DMSServicesUtil();
		System.out.println("DMSAsynFileUploader : run() : Runner............processType : "+processType);
		 switch(processType){
		 	
		 case SINGEL_UPLOAD :	
			 						uploadResult=dmsSUtil.asynUploadByFilePath(fileNames, documentListVO, userRole);
			 						break;
		 case MULTIPLE_UPLOAD :
			 						uploadResult=dmsSUtil.asynMultipleUploadByFilePath(documentListVO, userRole);
			 						break;
		default:
			break;
			 					
		 }
		 if(!uploadResult.isResultFlag()){
			 System.out.println("Document failed to "+processType+" into  DMS...  Mail is Triggering to user....");
			 DMSResultObject emailResultMsg=sendMail();
			 uploadResult.setResultMessage(uploadResult.getResultMessage()+"-->>Email :"+emailResultMsg.getResultMessage());
			 System.out.println("Document failed to "+processType+" into  DMS...  Mail is Sended to user....");
		 }else{
			 System.out.println("Successfully "+processType+" document into DMS... NO Mail is Trigger to DMS Approver....\n"+uploadResult.getResultMessage());
		 }
		 try {
				System.out.println("DMSAsynFileUploader :: DB process start");
				DMSGDaoImpl  dmsGDao= (DMSGDaoImpl)GigaServiceLocator.getService(IDMSService.daoLayer.DMSG_DAO);
				List<DMSDocumentAuditHistoryVO> auditHistoryVOList=new ArrayList<DMSDocumentAuditHistoryVO>();
				for (DMSSearchConentVO dmsDocVO: uploadResult.getSearchDocmentResult()) {
					
					String dmsId=dmsDocVO.getDmsId();
					String loggedInUser=dmsDocVO.getUploadedBy();
					String sourceSystem =dmsDocVO.getSystemLine();
					String fileName =dmsDocVO.getDocumentTitle(); 
					String remarks = fileName+", " +uploadResult.getResultMessage();
					System.out.println("dmsId : "+dmsId+"loggedInUser :: "+loggedInUser+"--sourceSystem :"+sourceSystem+"--fileName :"+fileName+"--remarks : "+remarks);
					
					
					DMSDocumentAuditHistoryVO auditHistoryVO = new DMSDocumentAuditHistoryVO();
					auditHistoryVO.setDocumentNumber(dmsId);
					auditHistoryVO.setActionBy(loggedInUser);					
					auditHistoryVO.setSourceSystem(sourceSystem);
					auditHistoryVO.setRemarks(remarks);
					auditHistoryVOList.add(auditHistoryVO);
					}
					DMSResultObject resultObj = dmsGDao.logToDMSDocumnetHistory(auditHistoryVOList);
					System.out.println("DMSAsynFileUploader :: DB process end  : resultObj :: "+resultObj.getResultMessage());
				
			} catch (ServiceException e) {
				e.printStackTrace();
			}
	}
	
	
	public DMSResultObject sendMail()
	{
		System.out.println("Mail sending.......");
		
		MimeMessage message = new MimeMessage(session);
		// Create the email addresses involved
		String subject="";
		String failureReason="";
		DMSResultObject dmsRObject=new DMSResultObject();
		try {
			
			dmsRObject.setResultFlag(false);
			
			message.setFrom(new InternetAddress(fromAddress));
			Map<String,String> emailDetails = new DMSMailTemplateGenerator().getMailContent(mailType, mailContentMap);
			
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
					Multipart multipart = new MimeMultipart("alternative");
					// Create your text message part
					BodyPart messageBodyPart = new MimeBodyPart();
					messageBodyPart.setText("some text to send");
					// Add the text part to the multipart
					multipart.addBodyPart(messageBodyPart);
					// Create the html part
					messageBodyPart = new MimeBodyPart();
					messageBodyPart.setContent(emailDetails.get("CONTENT"), "text/html");
					// Add html part to multi part
					multipart.addBodyPart(messageBodyPart);
					
				 // Associate multi-part with message
					message.setContent(multipart);
					// Send message
					Transport transport = session.getTransport("smtp");
					transport.connect();//"smtp.gmail.com", "username", "password");
					transport.sendMessage(message, message.getAllRecipients());
					dmsRObject.setResultFlag(true);
					dmsRObject.setResultMessage("Mail sent successfully...");
					log.info("Mail sent successfully......");
					}  catch(AuthenticationFailedException e){
					log.error("Authentication Error. Looking Up the context again..");
					try {
						javax.naming.InitialContext ctx = new javax.naming.InitialContext();
						session = (javax.mail.Session) ctx.lookup(DMSAsynServicesUtil.EMAIL_JNDI_NAME_DMS);
						Transport transport = session.getTransport("smtp");
						transport.connect();//"smtp.gmail.com", "username", "password");
						System.out.println("Transport: " + transport.toString());
						transport.sendMessage(message, message.getAllRecipients());
						
						dmsRObject.setResultFlag(true);
						dmsRObject.setResultMessage("Mail sent successfully...");
						log.info("Mail sent successfully......");
					} catch (NamingException ne) {
						
						log.error("Exception while Looking up mail contextin asynEmail()",ne);
						failureReason=ne.getMessage();
						dmsRObject.setResultMessage("Mail sent failed :: "+failureReason);
					}
				}
				catch (AddressException e) {
					
					log.error("Exception in asynEmail()",e);
					failureReason=e.getMessage();
					dmsRObject.setResultMessage("Mail sent failed :: "+failureReason);
				} catch (MessagingException e) {
					
					log.error("Exception in asynEmail()",e);
					failureReason=e.getMessage();
					dmsRObject.setResultMessage("Mail sent failed :: "+failureReason);
				}
			
			//CommonTMSDAO  commondao= (CommonTMSDAO)GigaServiceLocator.getService(ITMSServices.daoLayer.COMMON_TMS_DAO);	
			//commondao.saveEMailLog(toMailId, ccAddress, mailType.toString(),subject,userName , isSuccess, isWithAttachment, failureReason);
							
		}catch(Exception e){
			e.printStackTrace();
			log.error("Exception in asynEmail",e);
			dmsRObject.setResultMessage("Mail sent failed :: "+e.getMessage());
		}
		return dmsRObject;
	}
	
	@Override
	public void release() {
		
		
	}
	

}
