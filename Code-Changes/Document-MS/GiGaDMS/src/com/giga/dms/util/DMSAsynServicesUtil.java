package com.giga.dms.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import com.giga.base.LoggerUtility;
import com.giga.dms.util.mail.DMSMailStereoTokens;
import com.giga.dms.util.mail.DMSMailType;
import com.giga.dms.vo.DMSDocumentVO;
import com.giga.dms.vo.DMSMailContentVO;
import com.giga.dms.vo.DMSResultObject;
import com.ibm.websphere.asynchbeans.WorkException;
import com.ibm.websphere.asynchbeans.WorkItem;
import com.ibm.websphere.asynchbeans.WorkManager;



public class DMSAsynServicesUtil{
	private static Logger log = LoggerUtility.getLogger(DMSAsynServicesUtil.class);
	private static Configuration configInstance;
	public static final String EMAIL_PROPERTIES = "resources/DmsEmail.properties";
	public static  String EMAIL_JNDI_NAME_DMS;
	public static  String WORKMANAGER_JNDI_NAME_DMS;
	private static  String FROM_ADDRESS; 
	private static javax.mail.Session session = null;
	static javax.naming.InitialContext ctx;
    static {
    	mailSessionCreation();
    }
	private static void mailSessionCreation(){
		try {
			System.out.println("mailSessionCreation ::>>>>>>>>>>>>>***************");
			try {
				configInstance = new PropertiesConfiguration(EMAIL_PROPERTIES);
			} catch (ConfigurationException e) {
				
				e.printStackTrace();
			}
			System.out.println("configInstance >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+configInstance);
		FROM_ADDRESS= configInstance.getString("FROM_ADDRESS");
		WORKMANAGER_JNDI_NAME_DMS = configInstance.getString("WORKMANAGER_JNDI_NAME_DMS");
		EMAIL_JNDI_NAME_DMS =  configInstance.getString("EMAIL_JNDI_NAME_DMS");
		log.debug("Usage : EMail : Static Block : Properties file based Email Configuration : Mail Session creation Success :: ");
		ctx = new javax.naming.InitialContext();
		session = (javax.mail.Session) ctx.lookup(EMAIL_JNDI_NAME_DMS);
		log.debug("Usage : DMSEMail : Static Block : JNDI based Email Configuration : Mail Session creation Success :: ");
		System.out.println("Usage : DMSEMail : Static Block : JNDI based Email Configuration : Mail Session creation Success ::********* ");

		} catch (NamingException e) {
			
			log.error("Usage : DMSEMail : Static Block : JNDI based Email Configuration :NamingException Mail Session creation failed :: ",e);
			System.out.println("Usage : DMSEMail : Static Block : JNDI based Email Configuration :===NamingException== Mail Session creation failed>>>>>>>>>>"+e);
		}catch(Exception e){
			System.out.println("Usage : DMSEMail : Static Block : JNDI based Email Configuration :====Exception==  Mail Session creation failed>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+e);
		}
	}
	
	
	public static DMSResultObject asynUploadByFilePath(String completePathFileName,Map<DMSMetaDataKeys, String> mapObj, String userRole,DMSMailContentVO dmsMailContentVO){
		System.out.println("DMSAsynServiceUtil : asynUploadByFilePath : process Start ::");
		
		System.out.println("DMSAsynServiceUtil : asynUploadByFilePath : metaDataMap  ::"+mapObj+"\n completePathFileName : "+completePathFileName+"\n userRole : "+userRole+"\n dmsMailContentVO :"+dmsMailContentVO);
		DMSResultObject dmsResultObject=new DMSResultObject();
		List<String> toMailId=new ArrayList<String>();
		List<String> ccAddress=new ArrayList<String>();
		String userName="";
		String fileName="";
		String moduleName="";
		String moduleNo="";
		String remarks="";
		   try{
			if(dmsMailContentVO!=null){
				if(dmsMailContentVO.getToMailId()!=null){
					toMailId=dmsMailContentVO.getToMailId();
				};
				if(dmsMailContentVO.getCcAddress()!=null){
					ccAddress=dmsMailContentVO.getCcAddress();
				};
				 Map<DMSEmailContentKey, String> mailData=dmsMailContentVO.getMailData();
				 if(mailData!=null){
				 userName=mailData.get(DMSEmailContentKey.USER_NAME);
				 fileName=mapObj.get(DMSMetaDataKeys.DOCUMENT_TITLE);//mailData.get(DMSEmailContentKey.FILE_NAME);
				 moduleName=mailData.get(DMSEmailContentKey.MODULE_NAME);
				 moduleNo=mailData.get(DMSEmailContentKey.MODULE_NO);
				}
			}
		 	Map<String,String> mailContentMap = new HashMap<String,String>();
		 	mailContentMap.put(DMSMailStereoTokens.sendUploadDMSMail.DOCUMENT_NAME,fileName);
		 	mailContentMap.put(DMSMailStereoTokens.sendUploadDMSMail.DOCUMENT_TYPE,moduleName);
		 	mailContentMap.put(DMSMailStereoTokens.sendUploadDMSMail.DOCUMENTTYPE_NO,moduleNo);
		 	mailContentMap.put(DMSMailStereoTokens.sendUploadDMSMail.REMARKS, remarks);
		 	
		 	
		 	List<DMSDocumentVO> documentListVO=new ArrayList<DMSDocumentVO>();
		 	DMSDocumentVO dmsDoc=new DMSDocumentVO();
		 	dmsDoc.setMetaDataValue(mapObj);
		 	documentListVO.add(dmsDoc);
		 	System.out.println("DMSAsynServiceUtil : asynUploadByFilePath : userName : "+userName+"--fileName : "+fileName+"--moduleName : "+moduleName+"--moduleNo : "+moduleNo+"--remarks :"+remarks);
		 	dmsResultObject=ascyUploadToDMS(toMailId,DMSMailType.SEND_UPLOAD_DMS_MAIL,ccAddress,userName,completePathFileName,mailContentMap,documentListVO,userRole,DMSAysnProcessType.SINGEL_UPLOAD);
		   }catch(Exception e){
			   dmsResultObject.setResultFlag(false);
			   dmsResultObject.setResultMessage("PROCESSING_EXCEPTION : DMSAsynServiceUtil : asynUploadByFilePath : Exception in process the asynchronous method..");
		   }
		   System.out.println("DMSAsynServiceUtil : asynUploadByFilePath : process end ::");
		return dmsResultObject;
	}
	
	
	

	public static DMSResultObject asynMultipleUpload(List<DMSDocumentVO> documentListVO, String userRole,DMSMailContentVO dmsMailContentVO){
		System.out.println("asynMultipleUploadByFilePath : process Start ::");
		System.out.println("asynMultipleUploadByFilePath : metaDataMap  :documentListVO Size :"+documentListVO.size()+"\n userRole : "+userRole+"\n dmsMailContentVO :"+dmsMailContentVO);
		DMSResultObject dmsResultObject=new DMSResultObject();
		List<String> toMailId=new ArrayList<String>();
		List<String> ccAddress=new ArrayList<String>();
		String userName="";
		String fileName="";
		String moduleName="";
		String moduleNo="";
		String remarks="";
		String completePathFileName="";
		try{
			if(dmsMailContentVO!=null){
				if(dmsMailContentVO.getToMailId()!=null){
					toMailId=dmsMailContentVO.getToMailId();
				};
				if(dmsMailContentVO.getCcAddress()!=null){
					ccAddress=dmsMailContentVO.getCcAddress();
				};
				 Map<DMSEmailContentKey, String> mailData=dmsMailContentVO.getMailData();
				 if(mailData!=null){
				 userName=mailData.get(DMSEmailContentKey.USER_NAME);
				 fileName=mailData.get(DMSEmailContentKey.FILE_NAME);
				 moduleName=mailData.get(DMSEmailContentKey.MODULE_NAME);
				 moduleNo=mailData.get(DMSEmailContentKey.MODULE_NO);
				}
			}
		 	Map<String,String> mailContentMap = new HashMap<String,String>();
		 	mailContentMap.put(DMSMailStereoTokens.sendUploadDMSMail.DOCUMENT_NAME,fileName);
		 	mailContentMap.put(DMSMailStereoTokens.sendUploadDMSMail.DOCUMENT_TYPE,moduleName);
		 	mailContentMap.put(DMSMailStereoTokens.sendUploadDMSMail.DOCUMENTTYPE_NO,moduleNo);
		 	mailContentMap.put(DMSMailStereoTokens.sendUploadDMSMail.REMARKS, remarks);
		 
		 	System.out.println("asynMultipleUploadByFilePath : userName : "+userName+"--fileName : "+fileName+"--moduleName : "+moduleName+"--moduleNo : "+moduleNo+"--remarks :"+remarks);
		 	//mailSessionCreation();//============================== create the mail session...
		 	dmsResultObject=ascyUploadToDMS(toMailId,DMSMailType.SEND_UPLOAD_DMS_MAIL,ccAddress,userName,completePathFileName,mailContentMap,documentListVO,userRole,DMSAysnProcessType.MULTIPLE_UPLOAD);
	 }catch(Exception e){
		   dmsResultObject.setResultFlag(false);
		   dmsResultObject.setResultMessage("PROCESSING_EXCEPTION : DMSAsynServiceUtil : asynMultipleUploadByFilePath : Exception in process the asynchronous method..");
	   }
	   System.out.println("DMSAsynServiceUtil : asynMultipleUploadByFilePath : process end ::");
	return dmsResultObject;
	}
	
	
	
	public static DMSResultObject ascyUploadToDMS(List<String> toMailId,DMSMailType dmsMailType,List<String> ccAddress,String userName,String fileName,Map<String,String> mailContentMap,List<DMSDocumentVO> documentListVO,String userRole,DMSAysnProcessType processType) {
		log.debug("sendEmail()  <--");
		DMSResultObject resultObject=new DMSResultObject();
		try{
			System.out.println("toMailId::::"+toMailId+"---ccAddress::::"+ccAddress+"--userName::::"+userName+"--fileName::::"+fileName);
			if(documentListVO!=null &&  documentListVO.size()>0){
				System.out.println("documentListVO is NOT NULL and NOT Empty:::: for the GetCompanyCode()>>>>>>>>>>>>>>>>");
				for(DMSDocumentVO dmsVo:documentListVO){
					Map<DMSMetaDataKeys,String> dmsMetaDataVal=dmsVo.getMetaDataValue();
					dmsMetaDataVal.put(DMSMetaDataKeys.COMPANY_CODE, DMSServicesUtil.getCompanyCode());
					dmsVo.setMetaDataValue(dmsMetaDataVal);
					System.out.println("after SetCompanyCode  : dmsMetaDataVal :"+dmsMetaDataVal);
				}
					
			}
			
			System.out.println("dataMap::::"+mailContentMap.entrySet());
		   InitialContext ic = new InitialContext();
		   System.out.println("WORKMANAGER_JNDI_NAME::::"+WORKMANAGER_JNDI_NAME_DMS);
		   WorkManager wm = (WorkManager)ic.lookup(WORKMANAGER_JNDI_NAME_DMS);
		   List<String> fileNames = new ArrayList<String>();
		   fileNames.add(fileName);
		   WorkItem item = wm.startWork(new DMSAsynFileUploader(session,dmsMailType,FROM_ADDRESS,toMailId,ccAddress,userName,fileName,mailContentMap,documentListVO,userRole,processType));
		   System.out.println("startWork :::"+item.getStatus()); 
		  log.info("Mail Asyn process status"+item.getStatus());
		  resultObject.setResultFlag(true);
		  resultObject.setResultMessage("UPLOAD_SUCCESS : "+item.getStatus());
		} catch (NamingException e) {
			e.printStackTrace();
	  		log.error("Exception in sendEmail()",e);
	  		resultObject.setResultFlag(false);
			resultObject.setResultMessage("PROCESSING_EXCEPTION : "+"Naming Exception in Async file upload");
	  	} catch (WorkException e) {
	  		e.printStackTrace();
	  		log.error("Exception in sendEmail()",e);
	  		resultObject.setResultFlag(false);
			resultObject.setResultMessage("PROCESSING_EXCEPTION : "+"WorkException Exception in Async file upload");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			log.error("Exception in sendEmail()",e);
			resultObject.setResultFlag(false);
			resultObject.setResultMessage("PROCESSING_EXCEPTION : "+"IllegalArgumentException Exception in Async file upload");
		}
		return resultObject;
	}
	
	
	
	
	
}
