package com.giga.tms.util.mail;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import com.giga.base.LoggerUtility;


public class TMSMailTemplateGenerator { 

	public static final String MAILTEMPLATE_XML = "/resources/tms-mail-templates.xml";
	private static Logger logger=LoggerUtility.getLogger(TMSMailTemplateGenerator.class);
	private static Configuration mailConfig;
	static {
		try {
			mailConfig = new XMLConfiguration(MAILTEMPLATE_XML);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	public Map<String,String> getMailContent(TMSMailType mailType, Map<String,String> dataMap) {
		Map<String,String> map;
		String to = null;
		String cc = null;
		String subject = null;
		String content = null;
		if(mailConfig==null)
		{
		try {
			URL fileInput = this.getClass().getClassLoader()
					.getResource(MAILTEMPLATE_XML);
			mailConfig = new XMLConfiguration(fileInput);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		}
		switch (mailType) {
		//WORK REQUEST START HERE
		case WORKREQUEST_SENTFORCONFIRMATION:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("workRequest_sentForConfirmation.to");
			cc = mailConfig.getString("workRequest_sentForConfirmation.cc");
			subject = mailConfig.getString("workRequest_sentForConfirmation.subject");
			subject = subject.replace(TMSMailStereoTokens.workRequestSentForConfirmation.WORKREQUEST_NO, dataMap.get(TMSMailStereoTokens.workRequestSentForConfirmation.WORKREQUEST_NO));
			
			content = mailConfig.getString("workRequest_sentForConfirmation.content");
			content = content.replace(TMSMailStereoTokens.workRequestSentForConfirmation.WORKREQUEST_NO, dataMap.get(TMSMailStereoTokens.workRequestSentForConfirmation.WORKREQUEST_NO));
			content = content.replace(TMSMailStereoTokens.workRequestSentForConfirmation.REMARKS, dataMap.get(TMSMailStereoTokens.workRequestSentForConfirmation.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			return map;
		}
		case WORKREQUEST_REFERBACK:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("workRequest_referBack.to");
			cc = mailConfig.getString("workRequest_referBack.cc");
			subject = mailConfig.getString("workRequest_referBack.subject");
			subject = subject.replace(TMSMailStereoTokens.workRequestReferBack.WORKREQUEST_NO, dataMap.get(TMSMailStereoTokens.workRequestReferBack.WORKREQUEST_NO));
			content = mailConfig.getString("workRequest_referBack.content");
			content = content.replace(TMSMailStereoTokens.workRequestReferBack.WORKREQUEST_NO, dataMap.get(TMSMailStereoTokens.workRequestReferBack.WORKREQUEST_NO));
			content = content.replace(TMSMailStereoTokens.workRequestReferBack.REMARKS, dataMap.get(TMSMailStereoTokens.workRequestReferBack.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			return map;
		}
		case WORKREQUEST_CONFIRMED:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("workRequest_confirmed.to");
			cc = mailConfig.getString("workRequest_confirmed.cc");
			subject = mailConfig.getString("workRequest_confirmed.subject");
			subject = subject.replace(TMSMailStereoTokens.workRequestConfirmed.WORKREQUEST_NO, dataMap.get(TMSMailStereoTokens.workRequestConfirmed.WORKREQUEST_NO));
			content = mailConfig.getString("workRequest_confirmed.content");
			content = content.replace(TMSMailStereoTokens.workRequestConfirmed.WORKREQUEST_NO, dataMap.get(TMSMailStereoTokens.workRequestConfirmed.WORKREQUEST_NO));
			content = content.replace(TMSMailStereoTokens.workRequestConfirmed.REMARKS, dataMap.get(TMSMailStereoTokens.workRequestConfirmed.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			return map;
		}
		case WORKREQUEST_CANCELED:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("workRequest_cancelled.to");
			cc = mailConfig.getString("workRequest_cancelled.cc");
			subject = mailConfig.getString("workRequest_cancelled.subject");
			subject = subject.replace(TMSMailStereoTokens.workRequestCanceled.WORKREQUEST_NO, dataMap.get(TMSMailStereoTokens.workRequestCanceled.WORKREQUEST_NO));
			content = mailConfig.getString("workRequest_cancelled.content");
			content = content.replace(TMSMailStereoTokens.workRequestCanceled.WORKREQUEST_NO, dataMap.get(TMSMailStereoTokens.workRequestCanceled.WORKREQUEST_NO));
			content = content.replace(TMSMailStereoTokens.workRequestCanceled.REMARKS, dataMap.get(TMSMailStereoTokens.workRequestCanceled.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			return map;
		}
		case WORKREQUEST_COMPLETEWO:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("workRequest_completeWO.to");
			cc = mailConfig.getString("workRequest_completeWO.cc");
			subject = mailConfig.getString("workRequest_completeWO.subject");
			subject = subject.replace(TMSMailStereoTokens.workRequestCompleted.WORKREQUEST_NO, dataMap.get(TMSMailStereoTokens.workRequestCompleted.WORKREQUEST_NO));
			content = mailConfig.getString("workRequest_completeWO.content");
			content = content.replace(TMSMailStereoTokens.workRequestCompleted.WORKREQUEST_NO, dataMap.get(TMSMailStereoTokens.workRequestCompleted.WORKREQUEST_NO));
			content = content.replace(TMSMailStereoTokens.workRequestCompleted.REMARKS, dataMap.get(TMSMailStereoTokens.workRequestCompleted.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		case WORKREQUEST_REJECT:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("workRequest_reject.to");
			cc = mailConfig.getString("workRequest_reject.cc");
			subject = mailConfig.getString("workRequest_reject.subject");
			subject = subject.replace(TMSMailStereoTokens.workRequestRejected.WORKREQUEST_NO, dataMap.get(TMSMailStereoTokens.workRequestRejected.WORKREQUEST_NO));
			content = mailConfig.getString("workRequest_reject.content");
			content = content.replace(TMSMailStereoTokens.workRequestRejected.WORKREQUEST_NO, dataMap.get(TMSMailStereoTokens.workRequestRejected.WORKREQUEST_NO));
			content = content.replace(TMSMailStereoTokens.workRequestRejected.REMARKS, dataMap.get(TMSMailStereoTokens.workRequestRejected.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		
	/********************************** QUOTATION**********************/
		
		
		case QUOTATION_SENTFORCONFIRMATION:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("quotation_sentForConfirmation.to");
			cc = mailConfig.getString("quotation_sentForConfirmation.cc");
			subject = mailConfig.getString("quotation_sentForConfirmation.subject");
			subject = subject.replace(TMSMailStereoTokens.quotationSentForConfirmation.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.quotationSentForConfirmation.CUSTOME_NAME));
			content = mailConfig.getString("quotation_sentForConfirmation.content");
			content = content.replace(TMSMailStereoTokens.quotationSentForConfirmation.QUOTATION_NO, dataMap.get(TMSMailStereoTokens.quotationSentForConfirmation.QUOTATION_NO));
			content = content.replace(TMSMailStereoTokens.quotationSentForConfirmation.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.quotationSentForConfirmation.CUSTOME_NAME));
			//content = content.replace(MailStereoTokens.quotationSentForConfirmation.BUSINESS_LINE, dataMap.get(MailStereoTokens.quotationSentForConfirmation.BUSINESS_LINE));
			//content = content.replace(MailStereoTokens.quotationSentForConfirmation.REMARKS, dataMap.get(MailStereoTokens.quotationSentForConfirmation.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			System.out.println("QUOTATION_SENTFORCONFIRMATION map"+map);
			return map;
		}
		
		case QUOTATION_REFER_BACK_EVENT : {
			
			map = new HashMap<String,String>();
			to = mailConfig.getString("quotation_referback.to");
			cc = mailConfig.getString("quotation_referback.cc");
			subject = mailConfig.getString("quotation_referback.subject");			
			content = mailConfig.getString("quotation_referback.content");
			
			subject = subject.replace(TMSMailStereoTokens.quotationReferBack.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.quotationReferBack.CUSTOME_NAME));
			content = content.replace(TMSMailStereoTokens.quotationReferBack.QUOTATION_NO, dataMap.get(TMSMailStereoTokens.quotationReferBack.QUOTATION_NO));
			content = content.replace(TMSMailStereoTokens.quotationReferBack.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.quotationReferBack.CUSTOME_NAME));
			//content = content.replace(MailStereoTokens.quotationReferBack.BUSINESS_LINE, dataMap.get(MailStereoTokens.quotationReferBack.BUSINESS_LINE));
			
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			System.out.println("QUOTATION_EXPIRYDATE_ENDS MailTemplateGenerator map"+map);
			return map;
			
		}
		case QUOTATION_CONFIRM_EVENT : {
			
			map = new HashMap<String,String>();
			to = mailConfig.getString("quotation_confirm.to");
			cc = mailConfig.getString("quotation_confirm.cc");
			subject = mailConfig.getString("quotation_confirm.subject");			
			content = mailConfig.getString("quotation_confirm.content");
			
			subject = subject.replace(TMSMailStereoTokens.quotationReferBack.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.quotationReferBack.CUSTOME_NAME));
			content = content.replace(TMSMailStereoTokens.quotationReferBack.QUOTATION_NO, dataMap.get(TMSMailStereoTokens.quotationReferBack.QUOTATION_NO));
			content = content.replace(TMSMailStereoTokens.quotationReferBack.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.quotationReferBack.CUSTOME_NAME));
			//content = content.replace(MailStereoTokens.quotationReferBack.BUSINESS_LINE, dataMap.get(MailStereoTokens.quotationReferBack.BUSINESS_LINE));
			
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			System.out.println("QUOTATION_EXPIRYDATE_ENDS MailTemplateGenerator map"+map);
			return map;
			
		}
		case QUOTATION_CANCEL_EVENT : {
			
			map = new HashMap<String,String>();
			to = mailConfig.getString("quotation_cancel.to");
			cc = mailConfig.getString("quotation_cancel.cc");
			subject = mailConfig.getString("quotation_cancel.subject");			
			content = mailConfig.getString("quotation_cancel.content");
			
			subject = subject.replace(TMSMailStereoTokens.quotationReferBack.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.quotationReferBack.CUSTOME_NAME));
			content = content.replace(TMSMailStereoTokens.quotationReferBack.QUOTATION_NO, dataMap.get(TMSMailStereoTokens.quotationReferBack.QUOTATION_NO));
			content = content.replace(TMSMailStereoTokens.quotationReferBack.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.quotationReferBack.CUSTOME_NAME));
			//content = content.replace(MailStereoTokens.quotationReferBack.BUSINESS_LINE, dataMap.get(MailStereoTokens.quotationReferBack.BUSINESS_LINE));
			
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			System.out.println("QUOTATION_EXPIRYDATE_ENDS MailTemplateGenerator map"+map);
			return map;
			
		}
		case QUOTATION_APPROVE_EVENT : {
			
			map = new HashMap<String,String>();
			to = mailConfig.getString("quotation_approved.to");
			cc = mailConfig.getString("quotation_approved.cc");
			subject = mailConfig.getString("quotation_approved.subject");			
			content = mailConfig.getString("quotation_approved.content");
			
			subject = subject.replace(TMSMailStereoTokens.quotationApprove.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.quotationApprove.CUSTOME_NAME));
			content = content.replace(TMSMailStereoTokens.quotationApprove.QUOTATION_NO, dataMap.get(TMSMailStereoTokens.quotationApprove.QUOTATION_NO));
			content = content.replace(TMSMailStereoTokens.quotationApprove.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.quotationApprove.CUSTOME_NAME));
			//content = content.replace(MailStereoTokens.quotationReferBack.BUSINESS_LINE, dataMap.get(MailStereoTokens.quotationReferBack.BUSINESS_LINE));
			
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			System.out.println("QUOTATION_EXPIRYDATE_ENDS MailTemplateGenerator map"+map);
			return map;
			
		}
		case QUOTATION_CUSTOMER_CANCEL : {
			
			map = new HashMap<String,String>();
			to = mailConfig.getString("quotation_customer_cancel.to");
			cc = mailConfig.getString("quotation_customer_cancel.cc");
			subject = mailConfig.getString("quotation_customer_cancel.subject");			
			content = mailConfig.getString("quotation_customer_cancel.content");
			
			subject = subject.replace(TMSMailStereoTokens.quotationCustomerCancel.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.quotationCustomerCancel.CUSTOME_NAME));
			content = content.replace(TMSMailStereoTokens.quotationCustomerCancel.QUOTATION_NO, dataMap.get(TMSMailStereoTokens.quotationCustomerCancel.QUOTATION_NO));
			content = content.replace(TMSMailStereoTokens.quotationCustomerCancel.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.quotationCustomerCancel.CUSTOME_NAME));
			//content = content.replace(MailStereoTokens.quotationReferBack.BUSINESS_LINE, dataMap.get(MailStereoTokens.quotationReferBack.BUSINESS_LINE));
			
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			System.out.println("QUOTATION_CUSTOMER_CANCEL MailTemplateGenerator map"+map);
			return map;
			
		}
		case QUOTATION_VALIDITY_ENDS : {
			
			map = new HashMap<String,String>();
			to = mailConfig.getString("quotation_validityEnds.to");
			cc = mailConfig.getString("quotation_validityEnds.cc");
			subject = mailConfig.getString("quotation_validityEnds.subject");
			subject = subject.replace(TMSMailStereoTokens.quotationValidatyEnds.QUOTATION_NO, dataMap.get(TMSMailStereoTokens.quotationValidatyEnds.QUOTATION_NO));
			content = mailConfig.getString("quotation_sentForConfirmation.content");			
			content = content.replace(TMSMailStereoTokens.quotationValidatyEnds.QUOATION_DATE, dataMap.get(TMSMailStereoTokens.quotationValidatyEnds.QUOATION_DATE));
			content = content.replace(TMSMailStereoTokens.quotationValidatyEnds.QUOTATION_NO, dataMap.get(TMSMailStereoTokens.quotationValidatyEnds.QUOTATION_NO));
			content = content.replace(TMSMailStereoTokens.quotationValidatyEnds.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.quotationValidatyEnds.CUSTOME_NAME));
			content = content.replace(TMSMailStereoTokens.quotationValidatyEnds.QUOATION_VALIDTODATE, dataMap.get(TMSMailStereoTokens.quotationValidatyEnds.QUOATION_VALIDTODATE));
			
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			System.out.println("QUOTATION_VALIDITY_ENDS MailTemplateGenerator map"+map);
			return map;
			
		}
		case QUOTATION_EXPIRYDATE_ENDS : {
			
			map = new HashMap<String,String>();
			to = mailConfig.getString("quotation_expiryDateEnds.to");
			cc = mailConfig.getString("quotation_expiryDateEnds.cc");
			subject = mailConfig.getString("quotation_expiryDateEnds.subject");			
			content = mailConfig.getString("quotation_expiryDateEnds.content");
			
			subject = subject.replace(TMSMailStereoTokens.quotationExpiryEnds.QUOTATION_NO, dataMap.get(TMSMailStereoTokens.quotationExpiryEnds.QUOTATION_NO));
			content = content.replace(TMSMailStereoTokens.quotationExpiryEnds.QUOTATION_NO, dataMap.get(TMSMailStereoTokens.quotationExpiryEnds.QUOTATION_NO));
			content = content.replace(TMSMailStereoTokens.quotationExpiryEnds.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.quotationExpiryEnds.CUSTOME_NAME));
			content = content.replace(TMSMailStereoTokens.quotationExpiryEnds.QUOATION_EXPDATE, dataMap.get(TMSMailStereoTokens.quotationExpiryEnds.QUOATION_EXPDATE));
			
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			System.out.println("QUOTATION_EXPIRYDATE_ENDS MailTemplateGenerator map"+map);
			return map;
			
		}
		
		
		/*********************************** TMS_CLAIM_START *************************************************/
		case SEND_TMSCLAIM_CONFIRMATION:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("send_tmsclaim_confirmation.to");
			cc = mailConfig.getString("send_tmsclaim_confirmation.cc");
			subject = mailConfig.getString("send_tmsclaim_confirmation.subject");
			subject = subject.replace(TMSMailStereoTokens.sendClaimConfirmation.CLAIM_NO, dataMap.get(TMSMailStereoTokens.sendClaimConfirmation.CLAIM_NO));
			subject = subject.replace(TMSMailStereoTokens.sendClaimConfirmation.REMARKS, dataMap.get(TMSMailStereoTokens.sendClaimConfirmation.REMARKS));
			content = mailConfig.getString("send_tmsclaim_confirmation.content");
			content = content.replace(TMSMailStereoTokens.sendClaimConfirmation.CLAIM_NO, dataMap.get(TMSMailStereoTokens.sendClaimConfirmation.CLAIM_NO));
			content = content.replace(TMSMailStereoTokens.sendClaimConfirmation.CLAIMANT, dataMap.get(TMSMailStereoTokens.sendClaimConfirmation.CLAIMANT));
			content = content.replace(TMSMailStereoTokens.sendClaimConfirmation.REMARKS, dataMap.get(TMSMailStereoTokens.sendClaimConfirmation.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		case CONFIRM_TMSCLAIM:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("confirm_tmsclaim.to");
			cc = mailConfig.getString("confirm_tmsclaim.cc");
			subject = mailConfig.getString("confirm_tmsclaim.subject");
			subject = subject.replace(TMSMailStereoTokens.confirmClaim.CLAIM_NO, dataMap.get(TMSMailStereoTokens.confirmClaim.CLAIM_NO));
			subject = subject.replace(TMSMailStereoTokens.confirmClaim.REMARKS, dataMap.get(TMSMailStereoTokens.confirmClaim.REMARKS));
			content = mailConfig.getString("confirm_tmsclaim.content");
			content = content.replace(TMSMailStereoTokens.confirmClaim.CLAIM_NO, dataMap.get(TMSMailStereoTokens.confirmClaim.CLAIM_NO));
			content = content.replace(TMSMailStereoTokens.confirmClaim.CLAIMANT, dataMap.get(TMSMailStereoTokens.confirmClaim.CLAIMANT));
			content = content.replace(TMSMailStereoTokens.confirmClaim.REMARKS, dataMap.get(TMSMailStereoTokens.confirmClaim.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		case TMSCLAIM_REFERBACK:{
			logger.debug("MailTemplate : dataMap :: "+dataMap );
			map = new HashMap<String,String>();
			to = mailConfig.getString("referBack_tmsclaim.to");
			cc = mailConfig.getString("referBack_tmsclaim.cc");
			subject = mailConfig.getString("referBack_tmsclaim.subject");
			subject = subject.replace(TMSMailStereoTokens.referBackClaim.CLAIM_NO, dataMap.get(TMSMailStereoTokens.referBackClaim.CLAIM_NO));
			subject = subject.replace(TMSMailStereoTokens.referBackClaim.REMARKS, dataMap.get(TMSMailStereoTokens.referBackClaim.REMARKS));
			content = mailConfig.getString("referBack_tmsclaim.content");
			content = content.replace(TMSMailStereoTokens.referBackClaim.CLAIM_NO, dataMap.get(TMSMailStereoTokens.referBackClaim.CLAIM_NO));
			content = content.replace(TMSMailStereoTokens.referBackClaim.CLAIMANT, dataMap.get(TMSMailStereoTokens.referBackClaim.CLAIMANT));
			content = content.replace(TMSMailStereoTokens.referBackClaim.REMARKS, dataMap.get(TMSMailStereoTokens.referBackClaim.REMARKS));
			logger.debug("MailTemplate : content :: "+content+"\n----- subject :"+subject );
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		/*********************************** TMS_CLAIM_END *************************************************/
		/*********************************** TMS_INCIDENT_START*************************************************/
		case INCIDENT_SENTFORCONFIRMATION:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("incident_sentForConfirmation.to");
			cc = mailConfig.getString("incident_sentForConfirmation.cc");
			subject = mailConfig.getString("incident_sentForConfirmation.subject");
			subject = subject.replace(TMSMailStereoTokens.incidentSentForConfirmation.INCIDENT_NO, dataMap.get(TMSMailStereoTokens.incidentSentForConfirmation.INCIDENT_NO));
			
			content = mailConfig.getString("incident_sentForConfirmation.content");
			content = content.replace(TMSMailStereoTokens.incidentSentForConfirmation.INCIDENT_NO, dataMap.get(TMSMailStereoTokens.incidentSentForConfirmation.INCIDENT_NO));
			content = content.replace(TMSMailStereoTokens.incidentSentForConfirmation.REMARKS, dataMap.get(TMSMailStereoTokens.incidentSentForConfirmation.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			return map;
		}
		case INCIDENT_REFERBACK:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("incident_referBack.to");
			cc = mailConfig.getString("incident_referBack.cc");
			subject = mailConfig.getString("incident_referBack.subject");
			subject = subject.replace(TMSMailStereoTokens.incidentReferBack.INCIDENT_NO, dataMap.get(TMSMailStereoTokens.incidentReferBack.INCIDENT_NO));
			content = mailConfig.getString("incident_referBack.content");
			content = content.replace(TMSMailStereoTokens.incidentReferBack.INCIDENT_NO, dataMap.get(TMSMailStereoTokens.incidentReferBack.INCIDENT_NO));
			content = content.replace(TMSMailStereoTokens.incidentReferBack.REMARKS, dataMap.get(TMSMailStereoTokens.incidentReferBack.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			return map;
		}
		case INCIDENT_CONFIRMED:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("incident_confirmed.to");
			cc = mailConfig.getString("incident_confirmed.cc");
			subject = mailConfig.getString("incident_confirmed.subject");
			subject = subject.replace(TMSMailStereoTokens.incidentConfirmed.INCIDENT_NO, dataMap.get(TMSMailStereoTokens.incidentConfirmed.INCIDENT_NO));
			content = mailConfig.getString("incident_confirmed.content");
			content = content.replace(TMSMailStereoTokens.incidentConfirmed.INCIDENT_NO, dataMap.get(TMSMailStereoTokens.incidentConfirmed.INCIDENT_NO));
			content = content.replace(TMSMailStereoTokens.incidentConfirmed.REMARKS, dataMap.get(TMSMailStereoTokens.incidentConfirmed.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			return map;
		}
		case INCIDENT_CANCELLED:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("incident_cancelled.to");
			cc = mailConfig.getString("incident_cancelled.cc");
			subject = mailConfig.getString("incident_cancelled.subject");
			subject = subject.replace(TMSMailStereoTokens.incidentCancelled.INCIDENT_NO, dataMap.get(TMSMailStereoTokens.incidentCancelled.INCIDENT_NO));
			content = mailConfig.getString("incident_cancelled.content");
			content = content.replace(TMSMailStereoTokens.incidentCancelled.INCIDENT_NO, dataMap.get(TMSMailStereoTokens.incidentCancelled.INCIDENT_NO));
			content = content.replace(TMSMailStereoTokens.incidentCancelled.REMARKS, dataMap.get(TMSMailStereoTokens.incidentCancelled.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			return map;
		}
		case INCIDENT_CLOSED:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("incident_closed.to");
			cc = mailConfig.getString("incident_closed.cc");
			subject = mailConfig.getString("incident_closed.subject");
			subject = subject.replace(TMSMailStereoTokens.incidentClosed.INCIDENT_NO, dataMap.get(TMSMailStereoTokens.incidentClosed.INCIDENT_NO));
			content = mailConfig.getString("incident_closed.content");
			content = content.replace(TMSMailStereoTokens.incidentClosed.INCIDENT_NO, dataMap.get(TMSMailStereoTokens.incidentClosed.INCIDENT_NO));
			content = content.replace(TMSMailStereoTokens.incidentClosed.REMARKS, dataMap.get(TMSMailStereoTokens.incidentClosed.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		case INCIDENT_CLAIM_CREATED:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("incident_claim_created.to");
			cc = mailConfig.getString("incident_claim_created.cc");
			subject = mailConfig.getString("incident_claim_created.subject");
			subject = subject.replace(TMSMailStereoTokens.incidentClaimCreated.INCIDENT_NO, dataMap.get(TMSMailStereoTokens.incidentClaimCreated.INCIDENT_NO));
			content = mailConfig.getString("incident_claim_created.content");
			content = content.replace(TMSMailStereoTokens.incidentClaimCreated.INCIDENT_NO, dataMap.get(TMSMailStereoTokens.incidentClaimCreated.INCIDENT_NO));
			content = content.replace(TMSMailStereoTokens.incidentClaimCreated.REMARKS, dataMap.get(TMSMailStereoTokens.incidentClaimCreated.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		case INCIDENT_DRIVER_RECOVERY:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("incident_driverRecovery.to");
			cc = mailConfig.getString("incident_driverRecovery.cc");
			subject = mailConfig.getString("incident_driverRecovery.subject");
			subject = subject.replace(TMSMailStereoTokens.incidentDriverRecovery.INCIDENT_NO, dataMap.get(TMSMailStereoTokens.incidentDriverRecovery.INCIDENT_NO));
			content = mailConfig.getString("incident_driverRecovery.content");
			content = content.replace(TMSMailStereoTokens.incidentDriverRecovery.INCIDENT_NO, dataMap.get(TMSMailStereoTokens.incidentDriverRecovery.INCIDENT_NO));
			if(dataMap.get(TMSMailStereoTokens.incidentDriverRecovery.DONUMBER)!=null && !dataMap.get(TMSMailStereoTokens.incidentDriverRecovery.DONUMBER).isEmpty())
			{
			content = content.replace(TMSMailStereoTokens.incidentDriverRecovery.DONUMBER, dataMap.get(TMSMailStereoTokens.incidentDriverRecovery.DONUMBER));
			}
			content = content.replace(TMSMailStereoTokens.incidentDriverRecovery.REMARKS, dataMap.get(TMSMailStereoTokens.incidentDriverRecovery.REMARKS));
			System.out.println("into latest :::::::::::::::::::::::incident_driverRecovery"+content);
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		
		/*********************************** TMS_INCIDENT_END*************************************************/
		
		/**********************************TRAILER STARTS**********************/
			
		case TRAILER_NXTINSPECTION_ENDS : {
			map = new HashMap<String,String>();
			to = mailConfig.getString("trailerNxtInspecEnds.to");
			cc = mailConfig.getString("trailerNxtInspecEnds.cc");
			subject = mailConfig.getString("trailerNxtInspecEnds.subject");			
			content = mailConfig.getString("trailerNxtInspecEnds.content");
			subject = subject.replace(TMSMailStereoTokens.trailerNxtInspecEnds.OBJECT_TYPE, dataMap.get(TMSMailStereoTokens.trailerNxtInspecEnds.OBJECT_TYPE));

			subject = subject.replace(TMSMailStereoTokens.trailerNxtInspecEnds.REGISTRATION_NO, dataMap.get(TMSMailStereoTokens.trailerNxtInspecEnds.REGISTRATION_NO));
			content = content.replace(TMSMailStereoTokens.trailerNxtInspecEnds.REGISTRATION_NO, dataMap.get(TMSMailStereoTokens.trailerNxtInspecEnds.REGISTRATION_NO));
			content = content.replace(TMSMailStereoTokens.trailerNxtInspecEnds.NEXT_INCEPTION_DUE_DATE, dataMap.get(TMSMailStereoTokens.trailerNxtInspecEnds.NEXT_INCEPTION_DUE_DATE));
			content = content.replace(TMSMailStereoTokens.trailerNxtInspecEnds.OBJECT_TYPE, dataMap.get(TMSMailStereoTokens.trailerNxtInspecEnds.OBJECT_TYPE));
			content = content.replace(TMSMailStereoTokens.trailerNxtInspecEnds.PERIOD, dataMap.get(TMSMailStereoTokens.trailerNxtInspecEnds.PERIOD));
		
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			return map;
			
			}
			
		case TRAILER_ROADTAX_EXP_ENDS : {
				map = new HashMap<String,String>();
				to = mailConfig.getString("trailerRoadTaxExpiryEnds.to");
				cc = mailConfig.getString("trailerRoadTaxExpiryEnds.cc");
				subject = mailConfig.getString("trailerRoadTaxExpiryEnds.subject");			
				content = mailConfig.getString("trailerRoadTaxExpiryEnds.content");
				
				subject = subject.replace(TMSMailStereoTokens.trailerRoadTaxExpiryEnds.REGISTRATION_NO, dataMap.get(TMSMailStereoTokens.trailerRoadTaxExpiryEnds.REGISTRATION_NO));
				subject = subject.replace(TMSMailStereoTokens.trailerRoadTaxExpiryEnds.OBJECT_TYPE, dataMap.get(TMSMailStereoTokens.trailerRoadTaxExpiryEnds.OBJECT_TYPE));

				content = content.replace(TMSMailStereoTokens.trailerRoadTaxExpiryEnds.REGISTRATION_NO, dataMap.get(TMSMailStereoTokens.trailerRoadTaxExpiryEnds.REGISTRATION_NO));
				content = content.replace(TMSMailStereoTokens.trailerRoadTaxExpiryEnds.ROAD_TAX_DUE_DATE, dataMap.get(TMSMailStereoTokens.trailerRoadTaxExpiryEnds.ROAD_TAX_DUE_DATE));
				content = content.replace(TMSMailStereoTokens.trailerRoadTaxExpiryEnds.OBJECT_TYPE, dataMap.get(TMSMailStereoTokens.trailerRoadTaxExpiryEnds.OBJECT_TYPE));
				content = content.replace(TMSMailStereoTokens.trailerRoadTaxExpiryEnds.PERIOD, dataMap.get(TMSMailStereoTokens.trailerRoadTaxExpiryEnds.PERIOD));
				map.put("TO", to);
				map.put("CC", cc);
				map.put("SUBJECT", subject);
				map.put("CONTENT", content);
				
				return map;
				
			}
		case TRAILER_INSURANCE_EXPIRY_ENDS : {
				map = new HashMap<String,String>();
				to = mailConfig.getString("trailerInsuranceExpiryEnds.to");
				cc = mailConfig.getString("trailerInsuranceExpiryEnds.cc");
				subject = mailConfig.getString("trailerInsuranceExpiryEnds.subject");			
				content = mailConfig.getString("trailerInsuranceExpiryEnds.content");
				
				subject = subject.replace(TMSMailStereoTokens.trailerInsuranceExpiryEnds.REGISTRATION_NO, dataMap.get(TMSMailStereoTokens.trailerInsuranceExpiryEnds.REGISTRATION_NO));
				subject = subject.replace(TMSMailStereoTokens.trailerInsuranceExpiryEnds.OBJECT_TYPE, dataMap.get(TMSMailStereoTokens.trailerInsuranceExpiryEnds.OBJECT_TYPE));

				content = content.replace(TMSMailStereoTokens.trailerInsuranceExpiryEnds.REGISTRATION_NO, dataMap.get(TMSMailStereoTokens.trailerInsuranceExpiryEnds.REGISTRATION_NO));
				content = content.replace(TMSMailStereoTokens.trailerInsuranceExpiryEnds.OBJECT_TYPE, dataMap.get(TMSMailStereoTokens.trailerInsuranceExpiryEnds.OBJECT_TYPE));
				content = content.replace(TMSMailStereoTokens.trailerInsuranceExpiryEnds.PERIOD, dataMap.get(TMSMailStereoTokens.trailerInsuranceExpiryEnds.PERIOD));
				content = content.replace(TMSMailStereoTokens.trailerInsuranceExpiryEnds.INSURANCE_EXPIRY_DATE, dataMap.get(TMSMailStereoTokens.trailerInsuranceExpiryEnds.INSURANCE_EXPIRY_DATE));
				
				map.put("TO", to);
				map.put("CC", cc);
				map.put("SUBJECT", subject);
				map.put("CONTENT", content);
				
				return map;
				
				}
			/**********************************TRAILER ENDS**********************/
		
		/********************************** PURCHASE ORDER START**********************/
		case PO_SENTFORCONFIRMATION:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("po_sentForConfirmation.to");
			cc = mailConfig.getString("po_sentForConfirmation.cc");
			subject = mailConfig.getString("po_sentForConfirmation.subject");
			subject = subject.replace(TMSMailStereoTokens.poSentForConfirmation.PO_NO, dataMap.get(TMSMailStereoTokens.poSentForConfirmation.PO_NO));
			subject = subject.replace(TMSMailStereoTokens.poSentForConfirmation.VENDOR_NAME, dataMap.get(TMSMailStereoTokens.poSentForConfirmation.VENDOR_NAME));
			content = mailConfig.getString("po_sentForConfirmation.content");
			content = content.replace(TMSMailStereoTokens.poSentForConfirmation.PO_NO, dataMap.get(TMSMailStereoTokens.poSentForConfirmation.PO_NO));
			content = content.replace(TMSMailStereoTokens.poSentForConfirmation.VENDOR_NAME, dataMap.get(TMSMailStereoTokens.poSentForConfirmation.VENDOR_NAME));
			content = content.replace(TMSMailStereoTokens.poSentForConfirmation.BUSINESS_LINE, dataMap.get(TMSMailStereoTokens.poSentForConfirmation.BUSINESS_LINE));
			content = content.replace(TMSMailStereoTokens.poSentForConfirmation.REMARKS, dataMap.get(TMSMailStereoTokens.poSentForConfirmation.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}case PO_REFERBACK:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("po_referBack.to");
			cc = mailConfig.getString("po_referBack.cc");
			subject = mailConfig.getString("po_referBack.subject");
			subject = subject.replace(TMSMailStereoTokens.poReferBack.PO_NO, dataMap.get(TMSMailStereoTokens.poReferBack.PO_NO));
			subject = subject.replace(TMSMailStereoTokens.poReferBack.VENDOR_NAME, dataMap.get(TMSMailStereoTokens.poReferBack.VENDOR_NAME));
			content = mailConfig.getString("po_referBack.content");
			content = content.replace(TMSMailStereoTokens.poReferBack.PO_NO, dataMap.get(TMSMailStereoTokens.poReferBack.PO_NO));
			content = content.replace(TMSMailStereoTokens.poReferBack.VENDOR_NAME, dataMap.get(TMSMailStereoTokens.poReferBack.VENDOR_NAME));
			content = content.replace(TMSMailStereoTokens.poReferBack.BUSINESS_LINE, dataMap.get(TMSMailStereoTokens.poReferBack.BUSINESS_LINE));
			content = content.replace(TMSMailStereoTokens.poReferBack.REMARKS, dataMap.get(TMSMailStereoTokens.poReferBack.REMARKS));
			content = content.replace(TMSMailStereoTokens.poReferBack.REASON, dataMap.get(TMSMailStereoTokens.poReferBack.REASON));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}case PO_CONFIRMED:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("po_confirmed.to");
			cc = mailConfig.getString("po_confirmed.cc");
			subject = mailConfig.getString("po_confirmed.subject");
			subject = subject.replace(TMSMailStereoTokens.poConfirmed.PO_NO, dataMap.get(TMSMailStereoTokens.poConfirmed.PO_NO));
			subject = subject.replace(TMSMailStereoTokens.poConfirmed.VENDOR_NAME, dataMap.get(TMSMailStereoTokens.poConfirmed.VENDOR_NAME));
			content = mailConfig.getString("po_confirmed.content");
			content = content.replace(TMSMailStereoTokens.poConfirmed.PO_NO, dataMap.get(TMSMailStereoTokens.poConfirmed.PO_NO));
			content = content.replace(TMSMailStereoTokens.poConfirmed.VENDOR_NAME, dataMap.get(TMSMailStereoTokens.poConfirmed.VENDOR_NAME));
			content = content.replace(TMSMailStereoTokens.poConfirmed.BUSINESS_LINE, dataMap.get(TMSMailStereoTokens.poConfirmed.BUSINESS_LINE));
			content = content.replace(TMSMailStereoTokens.poConfirmed.REMARKS, dataMap.get(TMSMailStereoTokens.poConfirmed.REMARKS));
			
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}case PO_CANCELED:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("po_canceled.to");
			cc = mailConfig.getString("po_canceled.cc");
			subject = mailConfig.getString("po_canceled.subject");
			subject = subject.replace(TMSMailStereoTokens.poCanceled.PO_NO, dataMap.get(TMSMailStereoTokens.poCanceled.PO_NO));
			subject = subject.replace(TMSMailStereoTokens.poCanceled.VENDOR_NAME, dataMap.get(TMSMailStereoTokens.poCanceled.VENDOR_NAME));
			subject = subject.replace(TMSMailStereoTokens.poCanceled.TMSORNEXUS, dataMap.get(TMSMailStereoTokens.poCanceled.TMSORNEXUS));
			content = mailConfig.getString("po_canceled.content");
			content = content.replace(TMSMailStereoTokens.poCanceled.PO_NO, dataMap.get(TMSMailStereoTokens.poCanceled.PO_NO));
			content = content.replace(TMSMailStereoTokens.poCanceled.VENDOR_NAME, dataMap.get(TMSMailStereoTokens.poCanceled.VENDOR_NAME));
			content = content.replace(TMSMailStereoTokens.poCanceled.BUSINESS_LINE, dataMap.get(TMSMailStereoTokens.poCanceled.BUSINESS_LINE));
			content = content.replace(TMSMailStereoTokens.poCanceled.REMARKS, dataMap.get(TMSMailStereoTokens.poCanceled.REMARKS));
			content = content.replace(TMSMailStereoTokens.poCanceled.REASON, dataMap.get(TMSMailStereoTokens.poCanceled.REASON));
			
			
			
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		/********************************** PURCHASE ORDER END**********************/
		
		
		/**************************CREDITNOTE/DEBITNOTE START*****************************/
		
		case CREDITNOTE_SENTFORCONFIRMATION:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("creditNote_sentForConfirmation.to");
			cc = mailConfig.getString("creditNote_sentForConfirmation.cc");
			subject = mailConfig.getString("creditNote_sentForConfirmation.subject");
			subject = subject.replace(TMSMailStereoTokens.creditNoteSentForConfirmation.CREDITNOTE_NO, dataMap.get(TMSMailStereoTokens.creditNoteSentForConfirmation.CREDITNOTE_NO));
			
			content = mailConfig.getString("creditNote_sentForConfirmation.content");
			content = content.replace(TMSMailStereoTokens.creditNoteSentForConfirmation.CREDITNOTE_NO, dataMap.get(TMSMailStereoTokens.creditNoteSentForConfirmation.CREDITNOTE_NO));
			content = content.replace(TMSMailStereoTokens.creditNoteSentForConfirmation.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.creditNoteSentForConfirmation.CUSTOME_NAME));
			content = content.replace(TMSMailStereoTokens.creditNoteSentForConfirmation.BUSINESS_LINE, dataMap.get(TMSMailStereoTokens.creditNoteSentForConfirmation.BUSINESS_LINE));
			content = content.replace(TMSMailStereoTokens.creditNoteSentForConfirmation.REMARKS, dataMap.get(TMSMailStereoTokens.creditNoteSentForConfirmation.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			return map;
		}
		
		case CREDITNOTE_REFERBACK:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("creditNote_referBack.to");
			cc = mailConfig.getString("creditNote_referBack.cc");
			subject = mailConfig.getString("creditNote_referBack.subject");
			subject = subject.replace(TMSMailStereoTokens.creditNoteReferBack.CREDITNOTE_NO, dataMap.get(TMSMailStereoTokens.creditNoteReferBack.CREDITNOTE_NO));
			content = mailConfig.getString("creditNote_referBack.content");
			content = content.replace(TMSMailStereoTokens.creditNoteReferBack.CREDITNOTE_NO, dataMap.get(TMSMailStereoTokens.creditNoteReferBack.CREDITNOTE_NO));
			content = content.replace(TMSMailStereoTokens.creditNoteReferBack.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.creditNoteReferBack.CUSTOME_NAME));
			content = content.replace(TMSMailStereoTokens.creditNoteReferBack.BUSINESS_LINE, dataMap.get(TMSMailStereoTokens.creditNoteReferBack.BUSINESS_LINE));
			content = content.replace(TMSMailStereoTokens.creditNoteReferBack.REMARKS, dataMap.get(TMSMailStereoTokens.creditNoteReferBack.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			return map;
		}
		
		case CREDITNOTE_CONFIRMED:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("creditNote_confirmed.to");
			cc = mailConfig.getString("creditNote_confirmed.cc");
			subject = mailConfig.getString("creditNote_confirmed.subject");
			subject = subject.replace(TMSMailStereoTokens.creditNoteConfirmed.CREDITNOTE_NO, dataMap.get(TMSMailStereoTokens.creditNoteConfirmed.CREDITNOTE_NO));
			content = mailConfig.getString("creditNote_confirmed.content");
			content = content.replace(TMSMailStereoTokens.creditNoteConfirmed.CREDITNOTE_NO, dataMap.get(TMSMailStereoTokens.creditNoteConfirmed.CREDITNOTE_NO));
			content = content.replace(TMSMailStereoTokens.creditNoteConfirmed.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.creditNoteConfirmed.CUSTOME_NAME));
			content = content.replace(TMSMailStereoTokens.creditNoteConfirmed.BUSINESS_LINE, dataMap.get(TMSMailStereoTokens.creditNoteConfirmed.BUSINESS_LINE));
			content = content.replace(TMSMailStereoTokens.creditNoteConfirmed.REMARKS, dataMap.get(TMSMailStereoTokens.creditNoteConfirmed.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			return map;
		}
		
		case CREDITNOTE_CANCELED:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("creditNote_canceled.to");
			cc = mailConfig.getString("creditNote_canceled.cc");
			subject = mailConfig.getString("creditNote_canceled.subject");
			subject = subject.replace(TMSMailStereoTokens.creditNoteCanceled.CREDITNOTE_NO, dataMap.get(TMSMailStereoTokens.creditNoteCanceled.CREDITNOTE_NO));
			content = mailConfig.getString("creditNote_canceled.content");
			content = content.replace(TMSMailStereoTokens.creditNoteCanceled.CREDITNOTE_NO, dataMap.get(TMSMailStereoTokens.creditNoteCanceled.CREDITNOTE_NO));
			content = content.replace(TMSMailStereoTokens.creditNoteCanceled.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.creditNoteCanceled.CUSTOME_NAME));
			content = content.replace(TMSMailStereoTokens.creditNoteCanceled.BUSINESS_LINE, dataMap.get(TMSMailStereoTokens.creditNoteCanceled.BUSINESS_LINE));
			content = content.replace(TMSMailStereoTokens.creditNoteCanceled.REMARKS, dataMap.get(TMSMailStereoTokens.creditNoteCanceled.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
	
		
		case DEBITNOTE_SENTFORCONFIRMATION:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("debitNote_sentForConfirmation.to");
			cc = mailConfig.getString("debitNote_sentForConfirmation.cc");
			subject = mailConfig.getString("debitNote_sentForConfirmation.subject");
			subject = subject.replace(TMSMailStereoTokens.debitNoteSentForConfirmation.DEBITNOTE_NO, dataMap.get(TMSMailStereoTokens.debitNoteSentForConfirmation.DEBITNOTE_NO));
			
			content = mailConfig.getString("debitNote_sentForConfirmation.content");
			
			content = content.replace(TMSMailStereoTokens.debitNoteSentForConfirmation.DEBITNOTE_NO, dataMap.get(TMSMailStereoTokens.debitNoteSentForConfirmation.DEBITNOTE_NO));
			content = content.replace(TMSMailStereoTokens.debitNoteSentForConfirmation.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.debitNoteSentForConfirmation.CUSTOME_NAME));
			content = content.replace(TMSMailStereoTokens.debitNoteSentForConfirmation.BUSINESS_LINE, dataMap.get(TMSMailStereoTokens.debitNoteSentForConfirmation.BUSINESS_LINE));
			content = content.replace(TMSMailStereoTokens.debitNoteSentForConfirmation.REMARKS, dataMap.get(TMSMailStereoTokens.debitNoteSentForConfirmation.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			return map;
		}		
		case DEBITNOTE_REFERBACK:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("debitNote_referBack.to");
			cc = mailConfig.getString("debitNote_referBack.cc");
			subject = mailConfig.getString("debitNote_referBack.subject");
			subject = subject.replace(TMSMailStereoTokens.debitNoteReferBack.DEBITNOTE_NO, dataMap.get(TMSMailStereoTokens.debitNoteReferBack.DEBITNOTE_NO));
			content = mailConfig.getString("debitNote_referBack.content");
			content = content.replace(TMSMailStereoTokens.debitNoteReferBack.DEBITNOTE_NO, dataMap.get(TMSMailStereoTokens.debitNoteReferBack.DEBITNOTE_NO));
			content = content.replace(TMSMailStereoTokens.debitNoteReferBack.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.debitNoteReferBack.CUSTOME_NAME));
			content = content.replace(TMSMailStereoTokens.debitNoteReferBack.BUSINESS_LINE, dataMap.get(TMSMailStereoTokens.debitNoteReferBack.BUSINESS_LINE));
			content = content.replace(TMSMailStereoTokens.debitNoteReferBack.REMARKS, dataMap.get(TMSMailStereoTokens.debitNoteReferBack.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			return map;
		}		
		case DEBITNOTE_CONFIRMED:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("debitNote_confirmed.to");
			cc = mailConfig.getString("debitNote_confirmed.cc");
			subject = mailConfig.getString("debitNote_confirmed.subject");
			subject = subject.replace(TMSMailStereoTokens.debitNoteConfirmed.DEBITNOTE_NO, dataMap.get(TMSMailStereoTokens.debitNoteConfirmed.DEBITNOTE_NO));
			content = mailConfig.getString("debitNote_confirmed.content");
			content = content.replace(TMSMailStereoTokens.debitNoteConfirmed.DEBITNOTE_NO, dataMap.get(TMSMailStereoTokens.debitNoteConfirmed.DEBITNOTE_NO));
			content = content.replace(TMSMailStereoTokens.debitNoteConfirmed.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.debitNoteConfirmed.CUSTOME_NAME));
			content = content.replace(TMSMailStereoTokens.debitNoteConfirmed.BUSINESS_LINE, dataMap.get(TMSMailStereoTokens.debitNoteConfirmed.BUSINESS_LINE));
			content = content.replace(TMSMailStereoTokens.debitNoteConfirmed.REMARKS, dataMap.get(TMSMailStereoTokens.debitNoteConfirmed.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			
			return map;
		}		
		case DEBITNOTE_CANCELED:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("debitNote_canceled.to");
			cc = mailConfig.getString("debitNote_canceled.cc");
			subject = mailConfig.getString("debitNote_canceled.subject");
			subject = subject.replace(TMSMailStereoTokens.debitNoteCanceled.DEBITNOTE_NO, dataMap.get(TMSMailStereoTokens.debitNoteCanceled.DEBITNOTE_NO));
			content = mailConfig.getString("debitNote_canceled.content");
			content = content.replace(TMSMailStereoTokens.debitNoteCanceled.DEBITNOTE_NO, dataMap.get(TMSMailStereoTokens.debitNoteCanceled.DEBITNOTE_NO));
			content = content.replace(TMSMailStereoTokens.debitNoteCanceled.CUSTOME_NAME, dataMap.get(TMSMailStereoTokens.debitNoteCanceled.CUSTOME_NAME));
			content = content.replace(TMSMailStereoTokens.debitNoteCanceled.BUSINESS_LINE, dataMap.get(TMSMailStereoTokens.debitNoteCanceled.BUSINESS_LINE));
			content = content.replace(TMSMailStereoTokens.debitNoteCanceled.REMARKS, dataMap.get(TMSMailStereoTokens.debitNoteCanceled.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		
		/**********************CREDITNOTE/DEBITNOTE END*****************************/
		
		case INVOICE_SEND_CONFIRMATION: {
			map = new HashMap<String,String>();
			to = mailConfig.getString("invoice_send_confirmation.to");
			cc = mailConfig.getString("invoice_send_confirmation.cc");
			subject = mailConfig.getString("invoice_send_confirmation.subject");
			subject = subject.replace(TMSMailStereoTokens.InvoiceSendForConfirmation.INVOICE_NUMBER, dataMap.get(TMSMailStereoTokens.InvoiceSendForConfirmation.INVOICE_NUMBER));
			content = mailConfig.getString("invoice_send_confirmation.content");
			content = content.replace(TMSMailStereoTokens.InvoiceSendForConfirmation.INVOICE_NUMBER, dataMap.get(TMSMailStereoTokens.InvoiceSendForConfirmation.INVOICE_NUMBER));
			content = content.replace(TMSMailStereoTokens.InvoiceSendForConfirmation.CUSTOMER_NAME, dataMap.get(TMSMailStereoTokens.InvoiceSendForConfirmation.CUSTOMER_NAME));
			content = content.replace(TMSMailStereoTokens.InvoiceSendForConfirmation.BUZ_LINE, dataMap.get(TMSMailStereoTokens.InvoiceSendForConfirmation.BUZ_LINE));
			content = content.replace(TMSMailStereoTokens.InvoiceSendForConfirmation.REMARKS, dataMap.get(TMSMailStereoTokens.InvoiceSendForConfirmation.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		case INVOICE_CANCEL:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("invoice_cancel.to");
			cc = mailConfig.getString("invoice_cancel.cc");
			subject = mailConfig.getString("invoice_cancel.subject");
			subject = subject.replace(TMSMailStereoTokens.InvoiceCancel.INVOICE_NUMBER, dataMap.get(TMSMailStereoTokens.InvoiceCancel.INVOICE_NUMBER));
			content = mailConfig.getString("invoice_cancel.content");
			content = content.replace(TMSMailStereoTokens.InvoiceCancel.INVOICE_NUMBER, dataMap.get(TMSMailStereoTokens.InvoiceCancel.INVOICE_NUMBER));
			content = content.replace(TMSMailStereoTokens.InvoiceCancel.CUSTOMER_NAME, dataMap.get(TMSMailStereoTokens.InvoiceCancel.CUSTOMER_NAME));
			content = content.replace(TMSMailStereoTokens.InvoiceCancel.BUZ_LINE, dataMap.get(TMSMailStereoTokens.InvoiceCancel.BUZ_LINE));
			content = content.replace(TMSMailStereoTokens.InvoiceCancel.REMARKS, dataMap.get(TMSMailStereoTokens.InvoiceCancel.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		case INVOICE_REFER_BACK:{
			map = new HashMap<String,String>();
			to = mailConfig.getString("invoice_refer_back.to");
			cc = mailConfig.getString("invoice_refer_back.cc");
			subject = mailConfig.getString("invoice_refer_back.subject");
			subject = subject.replace(TMSMailStereoTokens.InvoiceReferback.INVOICE_NUMBER, dataMap.get(TMSMailStereoTokens.InvoiceReferback.INVOICE_NUMBER));
			content = mailConfig.getString("invoice_refer_back.content");
			content = content.replace(TMSMailStereoTokens.InvoiceReferback.INVOICE_NUMBER, dataMap.get(TMSMailStereoTokens.InvoiceReferback.INVOICE_NUMBER));
			content = content.replace(TMSMailStereoTokens.InvoiceReferback.CUSTOMER_NAME, dataMap.get(TMSMailStereoTokens.InvoiceReferback.CUSTOMER_NAME));
			content = content.replace(TMSMailStereoTokens.InvoiceReferback.BUZ_LINE, dataMap.get(TMSMailStereoTokens.InvoiceReferback.BUZ_LINE));
			content = content.replace(TMSMailStereoTokens.InvoiceReferback.REMARKS, dataMap.get(TMSMailStereoTokens.InvoiceReferback.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		case INVOICE_CONFIRM: {
			map = new HashMap<String,String>();
			to = mailConfig.getString("invoice_confirm.to");
			cc = mailConfig.getString("invoice_confirm.cc");
			subject = mailConfig.getString("invoice_confirm.subject");
			subject = subject.replace(TMSMailStereoTokens.InvoiceConfirm.INVOICE_NUMBER, dataMap.get(TMSMailStereoTokens.InvoiceConfirm.INVOICE_NUMBER));
			content = mailConfig.getString("invoice_confirm.content");
			content = content.replace(TMSMailStereoTokens.InvoiceConfirm.INVOICE_NUMBER, dataMap.get(TMSMailStereoTokens.InvoiceConfirm.INVOICE_NUMBER));
			content = content.replace(TMSMailStereoTokens.InvoiceConfirm.CUSTOMER_NAME, dataMap.get(TMSMailStereoTokens.InvoiceConfirm.CUSTOMER_NAME));
			content = content.replace(TMSMailStereoTokens.InvoiceConfirm.BUZ_LINE, dataMap.get(TMSMailStereoTokens.InvoiceConfirm.BUZ_LINE));
			content = content.replace(TMSMailStereoTokens.InvoiceConfirm.REMARKS, dataMap.get(TMSMailStereoTokens.InvoiceConfirm.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		
		/**********************TMS BOOKING STARTS*****************************/
		case TMSBOOKING_SEND_FOR_CONFIRMATION: {
			
			
			//Subject - BOOKINGNO
			//Content - APPROVALCUTTOFFDATE, BOOKINGNO, CUSTOMERNAME
			map = new HashMap<String,String>();
			to = mailConfig.getString("tmsbooking_send_for_confirmation.to");
			cc = mailConfig.getString("tmsbooking_send_for_confirmation.cc");
			subject = mailConfig.getString("tmsbooking_send_for_confirmation.subject");
			subject = subject.replace(TMSMailStereoTokens.tmsBookingSendForConfirmation.TMS_BOOKING_NUMBER, dataMap.get(TMSMailStereoTokens.tmsBookingSendForConfirmation.TMS_BOOKING_NUMBER));
			content = mailConfig.getString("tmsbooking_send_for_confirmation.content");
			content = content.replace(TMSMailStereoTokens.tmsBookingSendForConfirmation.TMS_BOOKING_NUMBER, dataMap.get(TMSMailStereoTokens.tmsBookingSendForConfirmation.TMS_BOOKING_NUMBER));
			content = content.replace(TMSMailStereoTokens.tmsBookingSendForConfirmation.TMS_SHIPMENT_NUMBER, dataMap.get(TMSMailStereoTokens.tmsBookingSendForConfirmation.TMS_SHIPMENT_NUMBER));
			content = content.replace(TMSMailStereoTokens.tmsBookingSendForConfirmation.CUSTOMER_NAME, dataMap.get(TMSMailStereoTokens.tmsBookingSendForConfirmation.CUSTOMER_NAME));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		case TMSBOOKING_REFER_BACK: {
			//Subject - BOOKINGNO, REMARKS
			//Content - BOOKINGNO, REMARKS
			map = new HashMap<String,String>();
			to = mailConfig.getString("tmsbooking_refer_back.to");
			cc = mailConfig.getString("tmsbooking_refer_back.cc");
			subject = mailConfig.getString("tmsbooking_refer_back.subject");
			subject = subject.replace(TMSMailStereoTokens.tmsBookingReferback.TMS_BOOKING_NUMBER, dataMap.get(TMSMailStereoTokens.tmsBookingReferback.TMS_BOOKING_NUMBER));
			content = mailConfig.getString("tmsbooking_refer_back.content");
			content = content.replace(TMSMailStereoTokens.tmsBookingReferback.TMS_BOOKING_NUMBER, dataMap.get(TMSMailStereoTokens.tmsBookingReferback.TMS_BOOKING_NUMBER));
			content = content.replace(TMSMailStereoTokens.tmsBookingReferback.TMS_SHIPMENT_NUMBER, dataMap.get(TMSMailStereoTokens.tmsBookingReferback.TMS_SHIPMENT_NUMBER));
			content = content.replace(TMSMailStereoTokens.tmsBookingReferback.CUSTOMER_NAME, dataMap.get(TMSMailStereoTokens.tmsBookingReferback.CUSTOMER_NAME));
			content = content.replace(TMSMailStereoTokens.tmsBookingReferback.REMARKS, dataMap.get(TMSMailStereoTokens.tmsBookingReferback.REMARKS));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		case TMSBOOKING_CONFIRM: {
			//Subject - BOOKINGNO
			//Content - BOOKINGNO
			map = new HashMap<String,String>();
			to = mailConfig.getString("tmsbooking_confirm.to");
			cc = mailConfig.getString("tmsbooking_confirm.cc");
			subject = mailConfig.getString("tmsbooking_confirm.subject");
			subject = subject.replace(TMSMailStereoTokens.tmsBookingConfirm.TMS_BOOKING_NUMBER, dataMap.get(TMSMailStereoTokens.tmsBookingConfirm.TMS_BOOKING_NUMBER));
			content = mailConfig.getString("tmsbooking_confirm.content");
			content = content.replace(TMSMailStereoTokens.tmsBookingConfirm.TMS_BOOKING_NUMBER, dataMap.get(TMSMailStereoTokens.tmsBookingConfirm.TMS_BOOKING_NUMBER));
			content = content.replace(TMSMailStereoTokens.tmsBookingConfirm.TMS_SHIPMENT_NUMBER, dataMap.get(TMSMailStereoTokens.tmsBookingConfirm.TMS_SHIPMENT_NUMBER));
			content = content.replace(TMSMailStereoTokens.tmsBookingConfirm.CUSTOMER_NAME, dataMap.get(TMSMailStereoTokens.tmsBookingConfirm.CUSTOMER_NAME));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		case TMSBOOKING_CANCEL: {
			map = new HashMap<String,String>();
			//Subject - BOOKINGNO
			//Content - BOOKINGNO
			map = new HashMap<String,String>();
			to = mailConfig.getString("tmsbooking_cancel.to");
			cc = mailConfig.getString("tmsbooking_cancel.cc");
			subject = mailConfig.getString("tmsbooking_cancel.subject");
			subject = subject.replace(TMSMailStereoTokens.tmsBookingCancel.TMS_BOOKING_NUMBER, dataMap.get(TMSMailStereoTokens.tmsBookingCancel.TMS_BOOKING_NUMBER));
			content = mailConfig.getString("tmsbooking_cancel.content");
			content = content.replace(TMSMailStereoTokens.tmsBookingCancel.TMS_BOOKING_NUMBER, dataMap.get(TMSMailStereoTokens.tmsBookingCancel.TMS_BOOKING_NUMBER));
			content = content.replace(TMSMailStereoTokens.tmsBookingCancel.TMS_SHIPMENT_NUMBER, dataMap.get(TMSMailStereoTokens.tmsBookingCancel.TMS_SHIPMENT_NUMBER));
			content = content.replace(TMSMailStereoTokens.tmsBookingCancel.CUSTOMER_NAME, dataMap.get(TMSMailStereoTokens.tmsBookingCancel.CUSTOMER_NAME));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		/**********************TMS BOOKING ENDS*****************************/
		
		case TMSDRIVER_EXPIRY: {
			map = new HashMap<String,String>();
			//Subject - BOOKINGNO
			//Content - BOOKINGNO
			map = new HashMap<String,String>();
			to = mailConfig.getString("tmsdriver_expiry.to");
			cc = mailConfig.getString("tmsdriver_expiry");
			subject = mailConfig.getString("tmsdriver_expiry.subject");
			content = mailConfig.getString("tmsdriver_expiry.content");
			content = content.replace(TMSMailStereoTokens.tmsDriverExpiry.EmailData, dataMap.get(TMSMailStereoTokens.tmsDriverExpiry.EmailData));
			
			map.put("TO", to);
			map.put("CC", cc);
			map.put("SUBJECT", subject);
			map.put("CONTENT", content);
			return map;
		}
		
		/**********************TMS DELIVERYORDER START*****************************/
		
		case DELIVERY_ORDER: {
			map = new HashMap<String,String>();
			//Subject - BOOKINGNO
			//Content - BOOKINGNO
			map = new HashMap<String,String>();
			to = mailConfig.getString("delivery_order.to");
			cc = mailConfig.getString("delivery_order.cc");
			subject = mailConfig.getString("delivery_order.subject");
			content = mailConfig.getString("delivery_order.content");
			content = content.replace(TMSMailStereoTokens.deliveryOrder.deliveryOrder, dataMap.get(TMSMailStereoTokens.deliveryOrder.deliveryOrder));
			
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

	/*private void printMap(Map<String,String> map){
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " : "
				+ entry.getValue());
		}
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
	}
	public static void main(final String[] args) throws Exception {
		System.out.println("\n\n");
		TMSEMail eM = new TMSEMail();
		
		TMSMailTemplateGenerator mg = new TMSMailTemplateGenerator();
		Map<String,String> map = new HashMap<String,String>();
		
		
		//ADD CHASSIS
		map.put(TMSMailStereoTokens.addChassis.CHASSIS, "809808080809809878967096709869");
		map.put(TMSMailStereoTokens.addChassis.SI_NO, "SLNO9o87987jhkjh979");
		//mg.printMap(mg.getMailContent(MailType.ADD_CHASSIS,map));
		Map <String,String> tempMap = mg.getMailContent(TMSMailType.SHUTOUT_CHASSIS,map);
		String subject = tempMap.get("SUBJECT");
		String content = tempMap.get("CONTENT");
		map.clear();
		tempMap.clear();
		//eM.sendMail("ganeshkumar.t@hcl.com","satishkmr@hcl.com",subject, content);
		
		// DELETE CHASSIS
		map.put(TMSMailStereoTokens.deleteChassis.CHASSIS, "565467q287");
		map.put(TMSMailStereoTokens.deleteChassis.SI_NO, "SLNO56346536-7987-7987");
		//mg.printMap(mg.getMailContent(MailType.DELETE_CHASSIS,map));
		tempMap = mg.getMailContent(TMSMailType.DELETE_CHASSIS,map);
		subject = tempMap.get("SUBJECT");
		content = tempMap.get("CONTENT");
		map.clear();
		tempMap.clear();
		//eM.sendMail("ganeshkumar.t@hcl.com","satishkmr@hcl.com",subject, content);
		
		//MODIFY CHASSIS
		map.put(TMSMailStereoTokens.modifyChassis.CHASSIS, "354258-870-AGJHGJH");
		map.put(TMSMailStereoTokens.modifyChassis.SI_NO, "SLNO56346536-7987-7987");
		//mg.printMap(mg.getMailContent(MailType.MODIFY_CHASSIS,map));
		tempMap = mg.getMailContent(TMSMailType.MODIFY_CHASSIS,map);
		subject = tempMap.get("SUBJECT");
		content = tempMap.get("CONTENT");
		map.clear();
		tempMap.clear();
		//eM.sendMail("ganeshkumar.t@hcl.com","satishkmr@hcl.com",subject, content);
		
		//MODIFY SI
		map.put(TMSMailStereoTokens.modifySI.SI_NO, "670970kjhklkh-79070-hkj");
		map.put(TMSMailStereoTokens.modifySI.JOB_NO, "JBNO-79-56346536-ABH");
		//mg.printMap(mg.getMailContent(MailType.MODIFY_SI,map));
		tempMap = mg.getMailContent(TMSMailType.MODIFY_SI,map);
		subject = tempMap.get("SUBJECT");
		content = tempMap.get("CONTENT");
		map.clear();
		tempMap.clear();
		//eM.sendMail("ganeshkumar.t@hcl.com","satishkmr@hcl.com",subject, content);
		
		map.put(TMSMailStereoTokens.rejectJobs.COMMENTS, "Your SI is rejected, due to irrelevant doc submission");
		map.put(TMSMailStereoTokens.rejectJobs.JOB_NO, "JBNO-7564367-689XML");
		//mg.printMap(mg.getMailContent(MailType.REJECT_JOB,map));
		tempMap = mg.getMailContent(TMSMailType.REJECT_JOB,map);
		subject = tempMap.get("SUBJECT");
		content = tempMap.get("CONTENT");
		map.clear();
		tempMap.clear();
		//eM.sendMail("ganeshkumar.t@hcl.com","satishkmr@hcl.com",subject, content);
		
		map.put(TMSMailStereoTokens.shutoutChassis.CHASSIS, "70540984-345-123-456-678-45435");
		map.put(TMSMailStereoTokens.shutoutChassis.SI_NO, "SLNO-57687-798");
		//mg.printMap(mg.getMailContent(MailType.SHUTOUT_CHASSIS,map));
		tempMap = mg.getMailContent(TMSMailType.SHUTOUT_CHASSIS,map);
		subject = tempMap.get("SUBJECT");
		content = tempMap.get("CONTENT");
		map.clear();
		tempMap.clear();
		
		//eM.sendMail("ganeshkumar.t@hcl.com","satishkmr@hcl.com",subject, content);
	}*/
		

}
