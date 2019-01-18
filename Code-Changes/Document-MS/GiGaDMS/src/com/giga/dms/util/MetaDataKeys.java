package com.giga.dms.util;

public class MetaDataKeys {
	public static final String ID="Id";
	public static final String SYS_LINE="SystemLine";
	public static final String DOC_LINE="DocumentLine";
	public static final String DOC_TYPE="DocumentType";
	public static final String DOC_TITLE = "DocumentTitle";
	
	public static final String BUS_LINE = "BusinessLine";
	public static final String SERV_TYPE = "ServiceType";
	public static final String SERV_LINE = "ServiceLine";
	
	
	
	public static final String BOOKING_NO = "BookingNo";
	public static final String JOB_NO = "JobNo";
	public static final String SI_NO = "SINo";
	public static final String INCIDENT_NO = "IncidentNo";
	public static final String CLAIM_NO = "ClaimNo";
	public static final String SHIPMENT_NO = "ShipmentNo";
	public static final String INVOICE_NO = "InvoiceNo";
	public static final String QUOT_NO = "QuotationNo";
	public static final String PO_NO = "PONo";
	public static final String DO_NO = "DONo";
	public static final String CREDIT_NOTE_NO = "CreditNoteNo";
	public static final String DEBIT_NOTE_NO = "DebitNoteNo";
	public static final String LOAD_NO = "LoadNo";
	public static final String WORKORDER_NO= "WorkOrderNo";
	
	public static final String CARGO_TYPE = "CargoType";
	public static final String VESSEL_NAME = "VesselName";
	public static final String VOYAGE_NO = "VoyageNo";
	public static final String BOOK_CUST = "BookingCustomer";
	public static final String POD = "PortOfDischarge";
	public static final String POL = "PortOfLoading";
	
	public static final String MIME_TYPE = "MimeType";
	public static final String UPLOAD_BY = "UploadBy";
	public static final String DELETE_BY = "DeleteBy";
	public static final String FROM_DATE = "FromDate";
	public static final String TO_DATE = "ToDate";
	public static final String UPLOADED_DATE = "DateCreated";
	public static final String QUOTATION_DATE = "QuotationDate";
	public static final String CREDITNOTE_DATE = "CreditNoteDate";
	public static final String DEBITNOTE_DATE = "DebitNoteDate";
	public static final String INVOICE_DATE = "InvoiceDate";
	public static final String PODATE = "PODate";
	public static final String COMPANY_CODE = "CompanyCode";	
	//YMS -- Changes
	public static final String INCIDENT_DETAILS = "IncidentDetails";
	public static final String CONSIGNEE = "Consignee";
	public static final String CHASSIS_NO = "ChassisNo";
	public static final String INSURANCE_AGENT = "InsuranceAgent";
	public static final String RESPONSIBLE_PARTY = "ResponsiblityParty";
	public static final String IS_CANCELLED = "IsCancelled";
	public static final String DELIVERYNOTE_NO = "DeliveryNoteNo";
	public static final String PULLINGORDER_NO = "PullingOrderNo";
	public static final String FORWARDINGAGENT_NAME = "ForwardAgentName";
	
	public static final String CLAIM_TYPE = "ClaimType";
	public static final String CLAIM_DATE = "ClaimDate";
	public static final String INCIDENT_DATE = "IncidentDate";
	public static final String REMARKS = "Remarks";
	
}
/**
 * 
	 SystemLine (mandatory)  This value is from base system Setup Master. Ex: OFS/TMS/YMS
	DocumentLine (mandatory)  This value is from base system Setup Master. Ex: CUSTOMS CLEARANCE/SYSTEM OUTPUTS/STATUTORY
	DocumentType 
	DocumentTitle 
	BookingNo
	JobNo
	SINo
	ClaimNo
	ShipmentNo
	InvoiceNo
	QuotationNo
	BookingCustomer
	BusinessLine
	ServiceType
	ServiceLine
	PortOfDischarge
	CargoType
	VesselName
	VoyageNo
	LoadNo
	DONo
	PONo
	WorkOrderNo
	ClaimNo
	InvoiceNo
	BookingCustomer
	PortOfLoading
	CargoType
	UploadBy
	DeleteBy
	CreditNoteNo
	DebitNoteNo

	UploadBy
	FromDate (mandatory)  This is date to be passed in string Ex: 01/01/2015 (mm/dd/yyyy)
	ToDate (mandatory)  This is date to be passed in string Ex: 01/01/2015 (mm/dd/yyyy)
* 
*/
