
<%@page import="java.io.*,org.apache.commons.fileupload.*,java.util.*" %>
<%@page import="javax.servlet.ServletException" %>
<%@page import="com.giga.base.service.GigaServiceLocator" %>
<%@page import="com.giga.dms.IDMSService" %>
<%@page import="com.giga.dms.service.DMSGService" %>
<%@page import="com.giga.dms.service.DMSGServiceImpl" %>
<%@page import="com.giga.dms.util.DMSMetaDataKeys" %>
<%@page import="com.giga.dms.util.GiGaModuleType" %>
<%@page import="com.giga.dms.vo.DMSDocumentVO" %>
<%@page import="com.giga.dms.vo.DMSResultObject" %>
<%@page import="com.giga.util.DateUtil" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Document Preview</title>
</head>
  
 

<%!
	
	private static final String PREVIEW = "PREVIEW";
	private static final String SYSTEM_LINE=com.giga.dms.helper.DMSHelper.SYSTEM_LINE;
	private static final String DMS_DCUMENT_ID=com.giga.dms.helper.DMSHelper.DMS_DCUMENT_ID;
	private static final String DMS_DOCUMENT_LINE=com.giga.dms.helper.DMSHelper.DMS_DOCUMENT_LINE;
	private static final String DMS_DOCUMENT_TYPE=com.giga.dms.helper.DMSHelper.DMS_DOCUMENT_TYPE;
	private static final String DMS_UPLOAD_FORM_DATE=com.giga.dms.helper.DMSHelper.DMS_UPLOAD_FORM_DATE;
	
	private static final String DMS_DOCUMENT_TITLE=com.giga.dms.helper.DMSHelper.DMS_DOCUMENT_TITLE;
	private static final String DMS_BOOKING_CUSTOMER=com.giga.dms.helper.DMSHelper.DMS_BOOKING_CUSTOMER;
	private static final String DMS_LOAD_NO=com.giga.dms.helper.DMSHelper.DMS_LOAD_NO;
	private static final String DMS_DO_NO=com.giga.dms.helper.DMSHelper.DMS_DO_NO;
	private static final String DMS_PO_NO=com.giga.dms.helper.DMSHelper.DMS_PO_NO;
	private static final String DMS_INVOICE_NO=com.giga.dms.helper.DMSHelper.DMS_INVOICE_NO;
	private static final String DMS_QUOTATION_NO=com.giga.dms.helper.DMSHelper.DMS_QUOTATION_NO;
	private static final String DMS_SHIPMENT_NO=com.giga.dms.helper.DMSHelper.DMS_SHIPMENT_NO;
	private static final String DMS_CLAIM_NO=com.giga.dms.helper.DMSHelper.DMS_CLAIM_NO;
	private static final String DMS_SI_NO=com.giga.dms.helper.DMSHelper.DMS_SI_NO;
	private static final String DMS_JOB_NO=com.giga.dms.helper.DMSHelper.DMS_JOB_NO;
	private static final String DMS_BOOKING_NO=com.giga.dms.helper.DMSHelper.DMS_BOOKING_NO;
	
	
	
	DMSGService dmsGService=null;
	public void jspInit() {
	  System.out.println("JSP init()>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	  try{
		dmsGService = (DMSGService) GigaServiceLocator.getService(IDMSService.businessLayer.DMSG_SERVICE);
		System.out.println("JSP Preview DMSGService :: is :"+dmsGService);
		} catch (Exception e) {
			e.printStackTrace();
			 System.out.println("JSP init() Exception >>>>>>"+e.getMessage());
		}
	}

	 public static String getExtenstionMatch(String extKey){
		 String extensionType=null;
		 if(extKey!=null&&!extKey.trim().isEmpty())
		 {	 
			 Map<String,String> extensionMap=new HashMap<String,String>();
			 extensionMap.put("xls","application/excel");
			 extensionMap.put("xl","application/excel");
			 extensionMap.put("doc","application/msword");
			 extensionMap.put("docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document");			 
			 extensionMap.put("pdf","application/pdf");
			 extensionMap.put("jpeg","image/jpeg");
			 extensionMap.put("jpg","image/jpeg");
			 extensionMap.put("png","image/png");
			 extensionMap.put("gif","image/gif");
			 extensionMap.put("tiff","image/tiff");
			 extensionMap.put("tif","image/tif");
			 
			 extensionType=(String)extensionMap.get(extKey.toLowerCase()); 
			 if(extensionType==null){
				 System.out.println("JSP:DMS :Preview: extensionMap not contains the extensionType and it is Null");
				 extensionType=extKey;
			 }
			 
		 }else{
			 extensionType=extKey;
		 }
			System.out.println("JSP::Preview: extensionType : "+extensionType);
		return extensionType;
	};
%>

<%
System.out.println("JSP Preview Reading....Start.... }}}}}}}}}}}}}}}");
boolean previewFlag = true;
String previewResult = "";


String systemLine="";
String documentLine="";
String documentType="";
String documentID="";
String uploadFromDate="";
String uploadToDate="";

String documentTitle="";
String quotation="";
String invoice="";


String bookingNo="";
String poNumber="";
String doNumber="";
String claimNo="";
String voyageNo="";
String vesselName="";
String shipmentNo="";
String jobNo="";


String userRole="AUTHOR";
String typeOfProcess="";

List<DMSDocumentVO> dmsDocumentVOList=new ArrayList<DMSDocumentVO>();
	try {
		typeOfProcess=request.getParameter("TypeOfProcess");
		systemLine=request.getParameter(SYSTEM_LINE);
		documentID=request.getParameter(DMS_DCUMENT_ID);		
		
    System.out.println("JSP Preview  :: typeOfProcess : "+typeOfProcess+"---userRole ::"+userRole+"-----documentID : "+documentID);
	       if(typeOfProcess!=null?true:false)
		   if(typeOfProcess.trim().equals("SEARCH_DMS_DETAILS")){
		      OutputStream outputStream = null;
		       Map<DMSMetaDataKeys,String> metadataObj = new HashMap<DMSMetaDataKeys,String>();
				metadataObj.put(DMSMetaDataKeys.SYSTEM_LINE,systemLine);
				//metadataObj.put(DMSMetaDataKeys.DOCUMENT_LINE, DOCUMENT_LINE_CL);
				//metadataObj.put(DMSMetaDataKeys.DOCUMENT_TYPE, DOCUMENT_TYPE_CL);
				metadataObj.put(DMSMetaDataKeys.DMS_ID, documentID.trim());
				DMSResultObject viewResults = dmsGService.viewDocumentDMS(metadataObj,userRole,GiGaModuleType.DMS);
					if(viewResults.isResultFlag()){
					 System.out.println("Usage :: DMS :: DMSWebPortlet : PREVIEW Content :: SearchFlag :  "+viewResults.isResultFlag()+"--Search Message : "+viewResults.getResultMessage());
					DMSDocumentVO dmsDocumentVoView=(DMSDocumentVO)viewResults.getViewDocumentResult();
					 if(dmsDocumentVoView!=null){
						 Map<DMSMetaDataKeys, String> viewMetadatMapResult=dmsDocumentVoView.getMetaDataValue();
						String mimeTypeV=viewMetadatMapResult.get(DMSMetaDataKeys.MIME_TYPE);
						String fileNameV=viewMetadatMapResult.get(DMSMetaDataKeys.DOCUMENT_TITLE);
						 byte[] byteValV=dmsDocumentVoView.getContentStream();
						 System.out.println("JSP : PREVIEW : MimeType : "+mimeTypeV +"---DocumentTitle  ::"+fileNameV +"-- file byteSize :: "+byteValV);
						response.setContentType(getExtenstionMatch(mimeTypeV));
						response.setHeader("Content-Disposition","inline; filename=" + fileNameV);
						response.getOutputStream().write(byteValV);
						response.getOutputStream().flush();
						response.getOutputStream().close();						return ;
					 }else{
					 previewFlag=false;						
					 }
		          }else{
					previewFlag=false;
				  }
		  }
		  
		} catch(Exception e){
		  e.printStackTrace();
		 System.out.println("Exception in DSM..Preview.........");
		}
		finally{
			System.out.println("JSP.....Finally.......");			
		}
		    if(typeOfProcess.equals(PREVIEW)){
				  if(!previewFlag){
						 System.out.println("No document found for viewDocuments:::::::::::");
						 previewResult = "VIEW_FAILED";
						//response.setContentType("text/plain");
						//response.setCharacterEncoding("UTF-8");
						//response.getWriter().print(previewResult);
				  }
		   }
%>	
<html>