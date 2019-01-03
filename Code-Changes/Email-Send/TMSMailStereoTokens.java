package com.giga.tms.util.mail;

public interface TMSMailStereoTokens {
	String PREFIX = "$_$_";
	String SUFFIX = "_$_$";

	
	/*********************WORK REQUEST NOTE START*****************************/
	interface workRequestSentForConfirmation {
		String WORKREQUEST_NO = PREFIX + "WORKREQUEST" + SUFFIX;
		//String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		//String BUSINESS_LINE  = PREFIX + "BUSINESSLINE" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	
	interface workRequestReferBack {
		String WORKREQUEST_NO = PREFIX + "WORKREQUEST" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	
	interface workRequestConfirmed {
		String WORKREQUEST_NO = PREFIX + "WORKREQUEST" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	interface workRequestCanceled {
		String WORKREQUEST_NO = PREFIX + "WORKREQUEST" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	interface workRequestCompleted {
		String WORKREQUEST_NO = PREFIX + "WORKREQUEST" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	interface workRequestRejected{
		String WORKREQUEST_NO = PREFIX + "WORKREQUEST" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	/*********************WORK REQUEST NOTE END*****************************/
	
	/*********************QUOTATION START*****************************/
	
	interface quotationSentForConfirmation {
		String QUOTATION_NO = PREFIX + "QUOTATIONNO" + SUFFIX;
		String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		
	}
	
	interface quotationReferBack {
		
		
		String QUOTATION_NO = PREFIX + "QUOTATIONNO" + SUFFIX;
		String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
			
	}
	interface quotationConfirm {
		
		
		String QUOTATION_NO = PREFIX + "QUOTATIONNO" + SUFFIX;
		String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
			
	}
	interface quotationCancel {
	
	
	String QUOTATION_NO = PREFIX + "QUOTATIONNO" + SUFFIX;
	String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
	
	}
	interface quotationApprove {
		String QUOTATION_NO = PREFIX + "QUOTATIONNO" + SUFFIX;
		String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
			
		}
	interface quotationCustomerCancel {
		String QUOTATION_NO = PREFIX + "QUOTATIONNO" + SUFFIX;
		String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
			
		}
	interface quotationValidatyEnds {
		
		String QUOATION_DATE = PREFIX + "QUOTATIONDATE" + SUFFIX;
		String QUOTATION_NO = PREFIX + "QUOTATIONNO" + SUFFIX;
		String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		String QUOATION_VALIDTODATE = PREFIX + "QUOTATIONVALIDTODATE" + SUFFIX;
		
			
	}
	
interface quotationExpiryEnds {
		
		String QUOATION_DATE = PREFIX + "QUOTATIONDATE" + SUFFIX;
		String QUOTATION_NO = PREFIX + "QUOTATIONNO" + SUFFIX;
		String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		String QUOATION_EXPDATE = PREFIX + "QUOTATIONEXPDATE" + SUFFIX;
			
	}
	
	/*********************QUOTATION END*****************************/
	
/**************************CLAIM START*****************************/
interface sendClaimConfirmation {
	String CLAIM_NO = PREFIX + "CLAIMNO" + SUFFIX;
	String CLAIMANT  = PREFIX + "CLAIMANT" + SUFFIX;
	String REMARKS = PREFIX + "REMARKS" + SUFFIX;
}
interface confirmClaim {
	String CLAIM_NO = PREFIX + "CLAIMNO" + SUFFIX;
	String CLAIMANT = PREFIX + "CLAIMANT" + SUFFIX;
	String REMARKS = PREFIX + "REMARKS" + SUFFIX;
}
interface referBackClaim {
	String CLAIM_NO = PREFIX + "CLAIMNO" + SUFFIX;
	String CLAIMANT = PREFIX + "CLAIMANT" + SUFFIX;
	String REMARKS = PREFIX + "REMARKS" + SUFFIX;
}
	

/**************************CLAIM END*****************************/
/*********************INCIDENT LOG STARTS*****************************/
interface incidentSentForConfirmation {
	String INCIDENT_NO = PREFIX + "INCIDENT" + SUFFIX;
	String REMARKS = PREFIX + "REMARKS" + SUFFIX;
}

interface incidentReferBack {
	String INCIDENT_NO = PREFIX + "INCIDENT" + SUFFIX;
	String REMARKS = PREFIX + "REMARKS" + SUFFIX;
}
interface incidentConfirmed {
	String INCIDENT_NO = PREFIX + "INCIDENT" + SUFFIX;
	String REMARKS = PREFIX + "REMARKS" + SUFFIX;
}
interface incidentCancelled {
	String INCIDENT_NO = PREFIX + "INCIDENT" + SUFFIX;
	String REMARKS = PREFIX + "REMARKS" + SUFFIX;
}
interface incidentClosed {
	String INCIDENT_NO = PREFIX + "INCIDENT" + SUFFIX;
	String REMARKS = PREFIX + "REMARKS" + SUFFIX;
}
interface incidentClaimCreated{
	String INCIDENT_NO = PREFIX + "INCIDENT" + SUFFIX;
	String REMARKS = PREFIX + "REMARKS" + SUFFIX;
}
interface incidentDriverRecovery{
	String INCIDENT_NO = PREFIX + "INCIDENT" + SUFFIX;
	String DONUMBER = PREFIX + "DONUMBER" + SUFFIX;
	String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	
}

/*********************INCIDENT LOG END*****************************/
	
	
	
	/********************TRAILER STARTS*****************************/

	interface trailerInsuranceExpiryEnds {
		String INSURANCE_EXPIRY_DATE = PREFIX + "INSURANCEEXPIRYDATE" + SUFFIX;
		String REGISTRATION_NO = PREFIX + "TRAILERREGNO" + SUFFIX;
		String PERIOD= PREFIX + "PERIOD" + SUFFIX;
		String OBJECT_TYPE=PREFIX + "OBJECTTYPE" + SUFFIX;
	}
	
	interface trailerNxtInspecEnds {
		String NEXT_INCEPTION_DUE_DATE = PREFIX + "NEXTINCEPTIONDUEDATE" + SUFFIX;
		String REGISTRATION_NO = PREFIX + "TRAILERREGNO" + SUFFIX;
		String PERIOD= PREFIX + "PERIOD" + SUFFIX;
		String OBJECT_TYPE=PREFIX + "OBJECTTYPE" + SUFFIX;
	}
	
	interface trailerRoadTaxExpiryEnds {
		String ROAD_TAX_DUE_DATE = PREFIX + "ROADTAXDUEDATE" + SUFFIX;
		String REGISTRATION_NO = PREFIX + "TRAILERREGNO" + SUFFIX;
		String PERIOD= PREFIX + "PERIOD" + SUFFIX;
		String OBJECT_TYPE=PREFIX + "OBJECTTYPE" + SUFFIX;
	}

	/********************TRAILER ENDS*****************************/
	
	/*********************PURCHASE ORDER START*****************************/
	interface poSentForConfirmation {
		String PO_NO = PREFIX + "PONO" + SUFFIX;
		String VENDOR_NAME = PREFIX + "VENDORNAME" + SUFFIX;
		String BUSINESS_LINE  = PREFIX + "BUSINESSLINE" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	
	interface poReferBack {
		String PO_NO = PREFIX + "PONO" + SUFFIX;
		String VENDOR_NAME = PREFIX + "VENDORNAME" + SUFFIX;
		String BUSINESS_LINE  = PREFIX + "BUSINESSLINE" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
		String REASON = PREFIX + "REASON" + SUFFIX;
	}
	
	interface poConfirmed {
		String PO_NO = PREFIX + "PONO" + SUFFIX;
		String VENDOR_NAME = PREFIX + "VENDORNAME" + SUFFIX;
		String BUSINESS_LINE  = PREFIX + "BUSINESSLINE" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	interface poCanceled {
		String PO_NO = PREFIX + "PONO" + SUFFIX;
		String VENDOR_NAME = PREFIX + "VENDORNAME" + SUFFIX;
		String BUSINESS_LINE  = PREFIX + "BUSINESSLINE" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
		String REASON = PREFIX + "REASON" + SUFFIX;
		String TMSORNEXUS = PREFIX + "TMSORNEXUS" + SUFFIX;
	}
	/*********************PURCHASE ORDER END*****************************/
	
	
	
	
	/*********************CREDIT NOTE START*****************************/
	interface creditNoteSentForConfirmation {
		String CREDITNOTE_NO = PREFIX + "CREDITNOTENO" + SUFFIX;
		String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		String BUSINESS_LINE  = PREFIX + "BUSINESSLINE" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	
	interface creditNoteReferBack {
		String CREDITNOTE_NO = PREFIX + "CREDITNOTENO" + SUFFIX;
		String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		String BUSINESS_LINE  = PREFIX + "BUSINESSLINE" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	
	interface creditNoteConfirmed {
		String CREDITNOTE_NO = PREFIX + "CREDITNOTENO" + SUFFIX;
		String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		String BUSINESS_LINE  = PREFIX + "BUSINESSLINE" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	interface creditNoteCanceled {
		String CREDITNOTE_NO = PREFIX + "CREDITNOTENO" + SUFFIX;
		String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		String BUSINESS_LINE  = PREFIX + "BUSINESSLINE" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	/*********************CREDIT NOTE END*****************************/
	
	interface debitNoteSentForConfirmation {
	
		String DEBITNOTE_NO = PREFIX + "DEBITNOTENO" + SUFFIX;
		String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		String BUSINESS_LINE  = PREFIX + "BUSINESSLINE" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	
	interface debitNoteReferBack {
		String DEBITNOTE_NO = PREFIX + "DEBITNOTENO" + SUFFIX;
		String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		String BUSINESS_LINE  = PREFIX + "BUSINESSLINE" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	
	interface debitNoteConfirmed {
		String DEBITNOTE_NO = PREFIX + "DEBITNOTENO" + SUFFIX;
		String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		String BUSINESS_LINE  = PREFIX + "BUSINESSLINE" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	interface debitNoteCanceled {
		String DEBITNOTE_NO = PREFIX + "DEBITNOTENO" + SUFFIX;
		String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		String BUSINESS_LINE  = PREFIX + "BUSINESSLINE" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	
	/* Invoice Mail Configuration */
	
	interface InvoiceSendForConfirmation {
		String INVOICE_NUMBER = PREFIX + "INVOICENO" + SUFFIX;
		String BUZ_LINE = PREFIX + "BUSINESSLINE" + SUFFIX;
		String CUSTOMER_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	
	interface InvoiceCancel {
		String INVOICE_NUMBER = PREFIX + "INVOICENO" + SUFFIX;
		String BUZ_LINE = PREFIX + "BUSINESSLINE" + SUFFIX;
		String CUSTOMER_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	
	interface InvoiceReferback {
		String INVOICE_NUMBER = PREFIX + "INVOICENO" + SUFFIX;
		String BUZ_LINE = PREFIX + "BUSINESSLINE" + SUFFIX;
		String CUSTOMER_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	
	interface InvoiceConfirm {
		String INVOICE_NUMBER = PREFIX + "INVOICENO" + SUFFIX;
		String BUZ_LINE = PREFIX + "BUSINESSLINE" + SUFFIX;
		String CUSTOMER_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	 /* Invoice Mail Configuration */
	
	
	/*** TMS BOOKING ENTRIES STARTS***/
	
	interface tmsBookingSendForConfirmation {
		String TMS_BOOKING_NUMBER = PREFIX + "BOOKINGNO" + SUFFIX;
		String TMS_SHIPMENT_NUMBER = PREFIX + "SHIPMENTNO" + SUFFIX;
		String CUSTOMER_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		String APPROVALCUTTOFFDATE = PREFIX + "APPROVALCUTTOFFDATE" + SUFFIX;
	}
	
	interface tmsBookingCancel {
		String TMS_BOOKING_NUMBER = PREFIX + "BOOKINGNO" + SUFFIX;
		String TMS_SHIPMENT_NUMBER = PREFIX + "SHIPMENTNO" + SUFFIX;
		String CUSTOMER_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
	}
	
	interface tmsBookingReferback {
		String TMS_BOOKING_NUMBER = PREFIX + "BOOKINGNO" + SUFFIX;
		String TMS_SHIPMENT_NUMBER = PREFIX + "SHIPMENTNO" + SUFFIX;
		String CUSTOMER_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		String REMARKS = PREFIX + "REMARKS" + SUFFIX;
	}
	
	interface tmsBookingConfirm {
		String TMS_BOOKING_NUMBER = PREFIX + "BOOKINGNO" + SUFFIX;
		String TMS_SHIPMENT_NUMBER = PREFIX + "SHIPMENTNO" + SUFFIX;
		String CUSTOMER_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
	}
	/*** TMS BOOKING ENTRIES ENDS***/

	interface tmsDriverExpiry {
		
		String EmailData = PREFIX + "DATA" + SUFFIX;
		//String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		//String BUSINESS_LINE  = PREFIX + "BUSINESSLINE" + SUFFIX;
		
	}
	
	interface deliveryOrder {
		
		String EmailData = PREFIX + "DATA" + SUFFIX;
		String deliveryOrder = PREFIX + "DELIVERYORDERNO" + SUFFIX;
		//String CUSTOME_NAME = PREFIX + "CUSTOMERNAME" + SUFFIX;
		//String BUSINESS_LINE  = PREFIX + "BUSINESSLINE" + SUFFIX;
		
	}
}
