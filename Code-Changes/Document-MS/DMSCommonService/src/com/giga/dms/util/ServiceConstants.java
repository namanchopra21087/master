package com.giga.dms.util;




public interface ServiceConstants {
	
	String FILENET_STANZA = "FileNetP8WSI";
	String FILENET_DOMAIN = "FileNetDomain";
	//For Query
	String EMPTYSTRING = " ";
	String DOT=".";
	String UNDERSCORE="_";
	String AND = "AND";
	String EQUALTOOPERATOR = "=";
	String LIKEOPERATOR = "Like";
	String SINGLEQUOTE = "'";
	String WHERECLAUSE = "where";
	String GREATERTHANEQUAL =">=";
	String LESSTHANEQUAL = "<=";
	String OPENBRACES = "(";
	String CLOSEBRACES = ")";
	String BACKSLASH="/";
	String DOCUMENTCLASS = "DocumentClass";
	String ORDERBYCLUASE="order by DateCreated desc";
	String ORDERBYCLUASE_INVOICE="order by InvoiceNo asc";
	String FILE_EXTENSION_PDF=".pdf";
	
	//Date Field Constant
	String CLIENTDATEFORMAT= "MM/dd/yyyy";
	String CLIENT_DISPLAY_DATEFORMAT= "dd/MM/yyyy HH:mm:ss";
	String FILENETDATEFORMAT= "yyyyMMdd'T'HHmmss'Z'";
	String TIME = "00:00:00";
	String FILENET_DATE_RECEIVED="EEE MMM dd HH:mm:ss z yyyy";
	String DOC_TIME_FORMAT ="yyyy-MM-dd'_'HHmmssSSS";
	
	//FileNet property Symbolic names
	String ID="Id";
	String DATECREATED="DateCreated";
	String MIMETYPE="MimeType"; 
	String DOCUMENTTITLE="DocumentTitle";
	String SYSTEMLINE = "SystemLine";
	String DOCUMENTLINE = "DocumentLine";
	String DOCUMENTTYPE= "DocumentType";
	String DELETEBY= "DeleteBy";
	String UPLOADBY="UploadBy";
	
	//FileNet custom property Symbolic names for JSP Reports
	String BOOKINGNO ="BookingNo";
	String JOBNO= "JobNo";
	String SINO = "SINo";
	String CLAIMNO = "ClaimNo";
	String SHIPPMENTNO= "ShipmentNo";
	String INVOICENO= "InvoiceNo";
	String QUOTATIONNO= "QuotationNo";	
	String BOOKINGCUSTOMER= "BookingCustomer";
	String BUSINESSLINE ="BusinessLine";
	String SERVICCETYPE="ServiceType";
	String SERVICELINE="ServiceLine";
	String PORTOFDISCHARGE="PortOfDischarge";
	String CARGOTYPE="CargoType";
	String VESSELNAME="VesselName";
	String VOYAGENO="VoyageNo";
	String COMPANYCODE="CompanyCode";
	String DEBITNOTENO="DebitNoteNo";
	String CREDITNOTENO="CreditNoteNo";
	
	//DOCType for merge
	String LINER_INVOICE="LINER INVOICE";
	String LINER_DEBITNOTE="LINER DEBIT NOTE";
	String LINER_CREDITNOTE="LINER CREDIT NOTE";
		
	//Client property 
	String SEARCHDATEFROM= "FromDate";
	String SEARCHDATETO= "ToDate";
	
	//File Formats 	
	String PDF = "pdf";
	String JPG = "jpg";
	String JPEG = "jpeg";
	String GIF = "gif";
	String PNG = "png";
	String BMP = "bmp";
	String TIF = "tif";
	String TIFF= "tiff";
	String XLS = "xls";
	String XLSX = "xlsx";
	String DOC = "doc";
	String DOCX = "docx";
	 
	//FileNet File Formats
	String MIME_TYPE_PDF = "application/pdf";	
	String MIME_TYPE_JPG_OR_JPEG = "image/jpeg";
	String MIME_TYPE_GIF = "image/gif";
	String MIME_TYPE_PNG = "image/png";
	String MIME_TYPE_BMP = "image/bmp";
	String MIME_TYPE_TIFF_OR_TIF = "image/tiff";
	String MIME_TYPE_XLS = "application/vnd.ms-excel";
	String MIME_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	String MIME_TYPE_DOC = "application/msword";
	String MIME_TYPE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	
	//Success Message
	//String UPLOAD_SUCCESS_MESSAGE="Documents uploaded successfully to DMS";
	String DELETE_SUCCESS_MESSAGE="Document deleted successfully from DMS";
	
	//Error Constant
	String TECHNICAL_ERROR = "TECHNICAL_ERROR";
	String TECHNICAL_ERROR_MSG = "Technical error occured while performing operations. Please contact DMS support team.";
	String DOCUMENT_PARSING_ERROR="DOCUMENT_PARSING_ERROR";
	String DOCUMENT_PARSING_ERROR_MSG="Error Occurred while parsing document content";
	String E_ACCESS_DENIED="E_ACCESS_DENIED";
	String E_ACCESS_DENIED_ERROR_MSG="User Not Authorized to delete the document";
	String E_BAD_CLASSID="E_BAD_CLASSID";
	String E_NULL_OR_INVALID_PARAM_VALUE="E_NULL_OR_INVALID_PARAM_VALUE";
	String E_BAD_CLASSID_ERROR_MSG="Parameter objectIdentity has an invalid value.";
	String E_OBJECT_NOT_FOUND="E_OBJECT_NOT_FOUND";
	String E_OBJECT_NOT_FOUND_ERROR_MSG="The Requested item was not found in DMS";
	String PROPERTY_FILE_ERROR="Error While Loading Propertty File";
	String HEADER_ERROR = "HEADER_ERROR";
	String HEADER_ERROR_MSG= "Credentials Error";
	
	String FILE_FORAMT_ERROR="FILE_FORMAT_NOT_SUPPORTED";
	String FILE_FORAMT_ERROR_MSG= "File Format not supported. Upload only pdf,images(tiff,bmp,jpg),excel,word format files";
	String FILE_FORAMT_EXTENSION_MSG= "Check the upload file name has proper extension";
	
	String DOCUMENT_MERGR_MAP_EMPTY_ERROR="DOCUMENT MERGR MAP EMPTY";
	String DOCUMENT_MERGR_MAP_EMPTY_ERROR_MSG="Document merger error. check the query and meta data values";
	
	String MANDATORY_FIELD_MISSING_ERROR = "MANDATORY FIELDS MISSING" ;
	String MANDATORY_FIELD_MISSING_ERROR_MSG = "Validate all the mandatory fields are set" ;
	
	String DATE_ERROR="DATE_FORMAT_EXCEPTION";
	String DATE_ERROR_MSG="Date format exception. Pass the date format as MM/dd/yyyy";
	
	String FROM_DATE_ERROR = "FROM_AND_TO_DATE_ERROR";
	String FROM_DATE_ERROR_MSG = "From Date should be smaller than To Date";
	
	//Encryption key and algorothm
	String ENCRYPTION_ALGORITHM = "AES";
	String ENCRYPTION_KEY_STRING = "GmGDmSSeCrEtSiGn";
	

	//Mandatory field Check 
	//String SYSTEM_LINE_NOT_FOUND = "SYSTEM_LINE_NOT_FOUND";
	//String SYSTEM_LINE_NOT_FOUND_ERROR_MSG = "The mandatory fields System Line Not found";
	
	//String HEADER_USERNAME_KEY = "Username";
	//String HEADER_PASSWORD_KEY = "Password";
	
	//Document Line Value for Mandatory fields Validation
	/*String DOC_LINE_CUSTOMSCLERANCE = "Customs Clearance"; 
	String DOC_LINE_STATUTORY = "Statutory"; 
	String DOC_LINE_SYSTEMOUTPUT = "System Output"; 
	String DOC_LINE_OTHERS = "Others"; */
	
		
	//FileNet custom property Symbolic names for TMS
	/*String PONO= "PONo";
	String DONO= "DONo";
	String LOADNO= "LoadNo";*/
	
}
