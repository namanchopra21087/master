package com.giga.dms.util.mail;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import com.giga.base.LoggerUtility;


public class DMSMailTemplateGenerator { 

	public static final String MAILTEMPLATE_XML_DMS = "/resources/dms-mail-templates.xml";
	private static Logger logger=LoggerUtility.getLogger(DMSMailTemplateGenerator.class);
	private static Configuration mailConfig;
	static {
		try {
			logger.debug("DMS :DMSMailTemplateGenerator : ... static block ");
			mailConfig = new XMLConfiguration(MAILTEMPLATE_XML_DMS);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	public Map<String,String> getMailContent(DMSMailType mailType, Map<String,String> dataMap) {
		Map<String,String> map;
		String to = null;
		String cc = null;
		String subject = null;
		String content = null;
		if(mailConfig==null)
		{
		try {
			URL fileInput = this.getClass().getClassLoader().getResource(MAILTEMPLATE_XML_DMS);
			mailConfig = new XMLConfiguration(fileInput);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		}
		switch (mailType) {
		/*********************************** TMS_CLAIM_START *************************************************/
		case SEND_UPLOAD_DMS_MAIL:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("send_upload_dms_mail.to");
			cc = mailConfig.getString("send_upload_dms_mail.cc");
			subject = mailConfig.getString("send_upload_dms_mail.subject");
			subject = subject.replace(DMSMailStereoTokens.sendUploadDMSMail.DOCUMENT_NAME, dataMap.get(DMSMailStereoTokens.sendUploadDMSMail.DOCUMENT_NAME));
			subject = subject.replace(DMSMailStereoTokens.sendUploadDMSMail.REMARKS, dataMap.get(DMSMailStereoTokens.sendUploadDMSMail.REMARKS));
			content = mailConfig.getString("send_upload_dms_mail.content");
			content = content.replace(DMSMailStereoTokens.sendUploadDMSMail.DOCUMENT_NAME, dataMap.get(DMSMailStereoTokens.sendUploadDMSMail.DOCUMENT_NAME));
			content = content.replace(DMSMailStereoTokens.sendUploadDMSMail.DOCUMENT_TYPE, dataMap.get(DMSMailStereoTokens.sendUploadDMSMail.DOCUMENT_TYPE));
			content = content.replace(DMSMailStereoTokens.sendUploadDMSMail.DOCUMENTTYPE_NO, dataMap.get(DMSMailStereoTokens.sendUploadDMSMail.DOCUMENTTYPE_NO));
			content = content.replace(DMSMailStereoTokens.sendUploadDMSMail.REMARKS, dataMap.get(DMSMailStereoTokens.sendUploadDMSMail.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		default:
			return null;
		}
	}
}
