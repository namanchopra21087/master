package com.giga.dms.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.ResourceRequest;

import org.apache.log4j.Logger;

import com.giga.base.LoggerUtility;
import com.giga.dms.util.DMSGConstants;
import com.giga.dms.util.DMSGType;
import com.giga.dms.vo.DMSSearchVO;
import com.giga.dms.vo.KeyValueVo;
import com.google.gson.Gson;
public class DMSHelper { 
	
@SuppressWarnings("static-access")
private static Logger logger = LoggerUtility.getInstance().getLogger(DMSHelper.class); 

 public static final String PROFILE_ACESS_FAILED="PROFILE_ACESS_FAILED"; 
 
 public static final String DMS_SEARCH_DCUMENTS ="DMS_SEARCH_DCUMENTS";//DMS_SEARCH_DOCUMENTS
 public static final String DMS_VIEW_DOCUMENTS="DMS_VIEW_DOCUMENTS";
 public static final String DMS_DOWNLOAD_DOCUMENTS="DMS_DOWNLOAD_DOCUMENTS";
 public static final String DMS_DELETE_DOCUMENTS="DMS_DELETE_DOCUMENTS";
 public static final String MULTIPART_REQUEST_ERROR="MULTIPART_REQUEST_ERROR";
 
 public static final String SYSTEM_LINE="SYSTEM_LINE";
 public static final String	DMS_DOCUMENT_TITLE="DMS_DOCUMENT_TITLE";
 public static final String	DMS_DCUMENT_ID="DMS_DCUMENT_ID";
 public static final String	DMS_DOCUMENT_LINE="DMS_DOCUMENT_LINE";
 public static final String DMS_DOCUMENT_TYPE="DMS_DOCUMENT_TYPE";
 public static final String DMS_BOOKING_NO="DMS_BOOKING_NO";
 public static final String DMS_JOB_NO="DMS_JOB_NO";
 public static final String DMS_SI_NO="DMS_SI_NO";
 public static final String DMS_CLAIM_NO="DMS_CLAIM_NO";
 public static final String DMS_SHIPMENT_NO="DMS_SHIPMENT_NO";
 public static final String DMS_QUOTATION_NO="DMS_QUOTATION_NO";
 public static final String DMS_INVOICE_NO="DMS_INVOICE_NO";
 public static final String DMS_PO_NO="DMS_PO_NO";
 public static final String DMS_DO_NO="DMS_DO_NO";
 public static final String DMS_LOAD_NO="DMS_LOAD_NO";
 public static final String DMS_BOOKING_CUSTOMER="DMS_BOOKING_CUSTOMER";
 public static final String DMS_DN_NO="DMS_DN_NO";
 public static final String DMS_CN_NO="DMS_CN_NO";
 public static final String DMS_UPLOAD_FORM_DATE="DMS_UPLOAD_FORM_DATE";
 public static final String DMS_UPLOAD_TO_DATE="DMS_UPLOAD_TO_DATE";
 
 public static final String SEARCH_DOCUMENT_EXCEPTION="SEARCH_DOCUMENT_EXCEPTION";
 public static final String VIEW_DOCUMENT_EXCEPTION="VIEW_DOCUMENT_EXCEPTION";
 public static final String DOWNLOAD_DOCUMENT_EXCEPTION="DOWNLOAD_DOCUMENT_EXCEPTION";
 public static final String DELETE_DOCUMENT_EXCEPTION="DELETE_DOCUMENT_EXCEPTION";
 public static final String NO_DOCUMENT_FOUND_ERROR="NO_DOCUMENT_FOUND_ERROR";
 
 public static final String LOAD_DOC_TYPE= "LOAD_DOC_TYPE";
 
 public static final String SEARCH_BOOKING="SEARCH_BOOKING";
 public static final String SEARCH_JOB="SEARCH_JOB";
 public static final String SEARCH_SI="SEARCH_SI";
 public static final String SEARCH_CLAIM="SEARCH_CLAIM";
 public static final String SEARCH_SHIPMENT="SEARCH_SHIPMENT";
 public static final String SEARCH_INVOICE="SEARCH_INVOICE";
 public static final String SEARCH_QUOTATION="SEARCH_QUOTATION";
 public static final String SEARCH_PO="SEARCH_PO";
 public static final String SEARCH_DO="SEARCH_DO";
 public static final String SEARCH_LOAD="SEARCH_LOAD";
 public static final String SEARCH_BOOKING_CUSTOMER="SEARCH_BOOKING_CUSTOMER";
 public static final String SEARCH_CREDITNOTE="SEARCH_CREDITNOTE";
 public static final String SEARCH_DEBITNOTE="SEARCH_DEBITNOTE";
 public static final String SEARCH_DELIVERYNOTE="SEARCH_DELIVERYNOTE";
 public static final String SEARCH_PULLINGORDER="SEARCH_PULLINGORDER";
 
 public static final String UPLOAD_SUCCESS = "1001";
 public static final String UPLOAD_FAILURE_NOT_ALL_SET = "1002";
 public static final String UPLOAD_FAILURE_BY_ERROR = "1003";
 
 	public static String LOGGED_USER="";
 

	public static final String LOAD_BOOKING_PODS= "LOAD_BOOKING_PODS";
	public static final String LOAD_BOOKING_SERVICETYPES= "LOAD_BOOKING_SERVICETYPES";
	public static final String LOAD_DMS_BOOKING_SERVICELINES= "LOAD_DMS_BOOKING_SERVICELINES";
	public static final String LOAD_BOOKING_CARGOTYPES= "LOAD_BOOKING_CARGOTYPES";
	public static final String LOAD_BOOKING_BUSINESSLINE= "LOAD_BOOKING_BUSINESSLINE";
	public static final String LOAD_BOOKINGCUS_AND_BUSINESSLINE= "LOAD_BOOKINGCUS_AND_BUSINESSLINE";
	
	private static final String NO_RESULT_FOUND="NO_RESULT_FOUND";
	
	private static final String SERVICE_EXCEPTION="SERVICE_EXCEPTION";
	private static final String PROCESS_EXCEPTION="PROCESS_EXCEPTION";
	private static final String VIEW_SUCCESS="VIEW_SUCCESS";
	private static final String DOWNLOAD_SUCCESS="DOWNLOAD_SUCCESS";
	private static final String SEARCH_SUCCESS="DOWNLOAD_SUCCESS";
	private static final String DELETE_SUCCESS="DOWNLOAD_SUCCESS";
 
	public DMSHelper() { 
		super();
		
	}
	
	public static DMSHelper getInstance(){
		return new DMSHelper();
	}

	/**
	 * This method is used to convert Object into JSon String value
	 * @param obj
	 * @return
	 */
	public String convertToJson(Object obj){
		String toJsonString="";
		Gson gson=null;
		 if(obj!=null){
			 gson=new Gson();
			 toJsonString=gson.toJson(obj);
		 }
		 logger.debug("DMSHelper :: convertToJson(): toJsonString"+toJsonString) ;
		 return toJsonString;
	}

	public DMSSearchVO createSearchVO(ResourceRequest request,DMSGType searchType,String systemLine,String branchCode,String companyCode) {
		
		 logger.debug("DMSHelper :: createSearchVO(): Start ::: \n ::[resourceRequest :"+request+"] ::[ DMSType :searchType: "+searchType+"] :: [systemLine : "+systemLine+"]") ;
		DMSSearchVO dmsSearchVO=null;
		try{
			if(systemLine!=null?systemLine.trim().isEmpty()?true:false:true){return null;}
			dmsSearchVO=new DMSSearchVO();
			dmsSearchVO.setSystemLine(systemLine.trim());
			dmsSearchVO.setBranchCode(nullCheckerString(branchCode));
			dmsSearchVO.setCompanyCode(nullCheckerString(companyCode));
			
			switch(searchType){
			case SEARCH_BOOKING:
				dmsSearchVO.setBookingCustomer(nullCheckerString(request.getParameter(DMSGConstants.P_BK_CUSTOMER)));
				dmsSearchVO.setBookingDateFrom(nullCheckerString(request.getParameter(DMSGConstants.P_BK_DATE_FROM)));
				dmsSearchVO.setBookingDateTo(nullCheckerString(request.getParameter(DMSGConstants.P_BK_DATE_TO)));
				dmsSearchVO.setBusinessLine(nullCheckerString(request.getParameter(DMSGConstants.P_BUSINESSLINE)));
				dmsSearchVO.setVesselName(nullCheckerString(request.getParameter(DMSGConstants.P_VESSELNAME)));
				dmsSearchVO.setEtaDate(nullCheckerString(request.getParameter(DMSGConstants.P_ETA_DATE)));
				dmsSearchVO.setVoyageNo(nullCheckerString(request.getParameter(DMSGConstants.P_VOYAGENO)));
				break;
			case SEARCH_JOB:
				dmsSearchVO.setBookingCustomer(nullCheckerString(request.getParameter(DMSGConstants.P_BK_CUSTOMER)));
				dmsSearchVO.setBookingNo(nullCheckerString(request.getParameter(DMSGConstants.P_BK_NUMBER)));
				dmsSearchVO.setPortOfDischarge(nullCheckerString(request.getParameter(DMSGConstants.P_POD)));
				dmsSearchVO.setJobDateFrom(nullCheckerString(request.getParameter(DMSGConstants.P_JOB_DATE_FROM)));
				dmsSearchVO.setJobDateTo(nullCheckerString(request.getParameter(DMSGConstants.P_JOB_DATE_TO)));
				break;
			case SEARCH_SI:
				dmsSearchVO.setBookingCustomer(nullCheckerString(request.getParameter(DMSGConstants.P_BK_CUSTOMER)));
				dmsSearchVO.setBookingNo(nullCheckerString(request.getParameter(DMSGConstants.P_BK_NUMBER)));
				dmsSearchVO.setPortOfDischarge(nullCheckerString(request.getParameter(DMSGConstants.P_POD)));
				dmsSearchVO.setSiDateFrom(nullCheckerString(request.getParameter(DMSGConstants.P_SI_DATE_FROM)));
				dmsSearchVO.setSiDateTo(nullCheckerString(request.getParameter(DMSGConstants.P_SI_DATE_TO)));
				break;
			case SEARCH_CLAIM:
				dmsSearchVO.setClaimDateFrom(nullCheckerString(request.getParameter(DMSGConstants.P_CLAIM_DATE_FROM)));
				dmsSearchVO.setClaimDateTo(nullCheckerString(request.getParameter(DMSGConstants.P_CLAIM_DATE_TO)));
				dmsSearchVO.setBookingNo(nullCheckerString(request.getParameter(DMSGConstants.P_BK_NUMBER)));
				dmsSearchVO.setBookingCustomer(nullCheckerString(request.getParameter(DMSGConstants.P_BK_CUSTOMER)));
				dmsSearchVO.setIncidentNo(nullCheckerString(request.getParameter(DMSGConstants.P_INCIDENT_NO)));
				break;
				
			case SEARCH_SHIPMENT:
				dmsSearchVO.setShipmentDateFrom(nullCheckerString(request.getParameter(DMSGConstants.P_SHIPMNT_DATE_FROM)));
				dmsSearchVO.setShipmentDateTo(nullCheckerString(request.getParameter(DMSGConstants.P_SHIPMNT_DATE_TO)));
				dmsSearchVO.setVesselName(nullCheckerString(request.getParameter(DMSGConstants.P_VESSELNAME)));
				dmsSearchVO.setVoyageNo(nullCheckerString(request.getParameter(DMSGConstants.P_VOYAGENO)));
				dmsSearchVO.setEtaDate(nullCheckerString(request.getParameter(DMSGConstants.P_ETA_DATE))); 
				break;
				
			case SEARCH_INVOICE:
				dmsSearchVO.setInvoiceDateFrom(nullCheckerString(request.getParameter(DMSGConstants.P_INVOICE_DATE_FROM)));
				dmsSearchVO.setInvoiceDateTo(nullCheckerString(request.getParameter(DMSGConstants.P_INVOICE_DATE_TO)));
				dmsSearchVO.setBookingCustomer(nullCheckerString(request.getParameter(DMSGConstants.P_BK_CUSTOMER)));
				break;
				
			case SEARCH_QUOTATION:
				dmsSearchVO.setQuotationDateFrom(nullCheckerString(request.getParameter(DMSGConstants.P_QUOT_DATE_FROM)));
				dmsSearchVO.setQuotationDateTo(nullCheckerString(request.getParameter(DMSGConstants.P_QUOT_DATE_TO)));
				dmsSearchVO.setCustomerName(nullCheckerString(request.getParameter(DMSGConstants.P_CUSTOMER_NAME)));
				break;
				
			case SEARCH_PO:
				dmsSearchVO.setPoDateFrom(nullCheckerString(request.getParameter(DMSGConstants.P_PO_DATE_FROM)));
				dmsSearchVO.setPoDateTo(nullCheckerString(request.getParameter(DMSGConstants.P_PO_DATE_TO)));
				dmsSearchVO.setVendorName(nullCheckerString(request.getParameter(DMSGConstants.P_VENDOR_NAME))); 
				break;
			case SEARCH_DO:
				//P_BILL_CUSTOMER,P_BK_NUMBER,P_LOAD_NO,P_DELIVERY_LOC,P_PICKUPLOCATION,P_PICKUP_DATE,P_DELIVERY_DATE,P_DO_DATE_FROM,P_DO_DATE_TO
				dmsSearchVO.setBillingCustomer(nullCheckerString(request.getParameter(DMSGConstants.P_BILL_CUSTOMER)));
				dmsSearchVO.setPickupLocation(nullCheckerString(request.getParameter(DMSGConstants.P_PICKUPLOCATION)));
				dmsSearchVO.setDeliveryLocation(nullCheckerString(request.getParameter(DMSGConstants.P_DELIVERY_LOC)));
				dmsSearchVO.setBookingNo(nullCheckerString(request.getParameter(DMSGConstants.P_BK_NUMBER)));
				dmsSearchVO.setLoadNo(nullCheckerString(request.getParameter(DMSGConstants.P_LOAD_NO)));
				dmsSearchVO.setPickupDate(nullCheckerString(request.getParameter(DMSGConstants.P_PICKUP_DATE)));
				dmsSearchVO.setDeliveryDate(nullCheckerString(request.getParameter(DMSGConstants.P_DELIVERY_DATE)));
				dmsSearchVO.setDoDateFrom(nullCheckerString(request.getParameter(DMSGConstants.P_DO_DATE_FROM)));
				dmsSearchVO.setDoDateTo(nullCheckerString(request.getParameter(DMSGConstants.P_DO_DATE_TO)));
				break;
			case SEARCH_LOAD:
				dmsSearchVO.setBookingCustomer(nullCheckerString(request.getParameter(DMSGConstants.P_BK_CUSTOMER)));
				dmsSearchVO.setBookingDateFrom(nullCheckerString(request.getParameter(DMSGConstants.P_BK_DATE_FROM)));
				dmsSearchVO.setBookingDateTo(nullCheckerString(request.getParameter(DMSGConstants.P_BK_DATE_TO)));
				dmsSearchVO.setPickupLocation(nullCheckerString(request.getParameter(DMSGConstants.P_PICKUPLOCATION)));
				dmsSearchVO.setDeliveryLocation(nullCheckerString(request.getParameter(DMSGConstants.P_DELIVERY_LOC)));
				dmsSearchVO.setTruckType(nullCheckerString(request.getParameter(DMSGConstants.P_TRUCK_TYPE)));
				break;
			case SEARCH_BOOKING_CUSTOMER:
				dmsSearchVO.setCustomerName(nullCheckerString(request.getParameter(DMSGConstants.P_CUSTOMER_NAME)));
				dmsSearchVO.setCustomerCode(nullCheckerString(request.getParameter(DMSGConstants.P_CUSTOMER_CODE)));
				break;
			case SEARCH_CREDITNOTE:
				dmsSearchVO.setCreditNoteNo(nullCheckerString(request.getParameter(DMSGConstants.P_CN_NO)));
				if(systemLine.equals("OFS")){
					dmsSearchVO.setShipmentNo(nullCheckerString(request.getParameter(DMSGConstants.P_SHIPMENT_NO)));
				}else
					if(systemLine.equals("TMS")){
						dmsSearchVO.setInvoiceNo(nullCheckerString(request.getParameter(DMSGConstants.P_INVOICE_NO)));
					}
				dmsSearchVO.setBillingCustomer(nullCheckerString(request.getParameter(DMSGConstants.P_BILLING_CUSTOMER)));
				dmsSearchVO.setCnDateForm(nullCheckerString(request.getParameter(DMSGConstants.P_CN_FROM_DATE)));
				dmsSearchVO.setCnDateTo(nullCheckerString(request.getParameter(DMSGConstants.P_CN_TO_DATE)));
				break;
			case SEARCH_DEBITNOTE:
				dmsSearchVO.setDebitNoteNo(nullCheckerString(request.getParameter(DMSGConstants.P_DN_NO)));
				if(systemLine.equals("OFS")){
					dmsSearchVO.setShipmentNo(nullCheckerString(request.getParameter(DMSGConstants.P_SHIPMENT_NO)));
				}else
					if(systemLine.equals("TMS")){
						dmsSearchVO.setInvoiceNo(nullCheckerString(request.getParameter(DMSGConstants.P_INVOICE_NO)));
					}
				dmsSearchVO.setBillingCustomer(nullCheckerString(request.getParameter(DMSGConstants.P_BILLING_CUSTOMER)));
				dmsSearchVO.setDnDateFrom(nullCheckerString(request.getParameter(DMSGConstants.P_DN_FROM_DATE)));
				dmsSearchVO.setDnDateTo(nullCheckerString(request.getParameter(DMSGConstants.P_DN_TO_DATE)));
				break;	
				
			case SEARCH_DELIVERYNOTE:
				dmsSearchVO.setDeliveryNoteNo(nullCheckerString(request.getParameter(DMSGConstants.P_DELIVERYNOTE_NO)));
				dmsSearchVO.setShipmentNo(nullCheckerString(request.getParameter(DMSGConstants.P_SHIPMENT_NO)));
				dmsSearchVO.setDeliveryNoteType(nullCheckerString(request.getParameter(DMSGConstants.P_DELIVERYNOTE_TYPE)));
				dmsSearchVO.setDeliveryNoteDateFrom(nullCheckerString(request.getParameter(DMSGConstants.P_DELIVERYNOTE_FROM_DATE)));
				dmsSearchVO.setDeliveryNoteDateTo(nullCheckerString(request.getParameter(DMSGConstants.P_DELIVERYNOTE_TO_DATE)));
				break;	
				
			case SEARCH_PULLINGORDER:
				dmsSearchVO.setPullingOrderNo(nullCheckerString(request.getParameter(DMSGConstants.P_PLO_NO)));
				dmsSearchVO.setCustomerName(nullCheckerString(request.getParameter(DMSGConstants.P_CUSTOMER_NAME)));
				dmsSearchVO.setBookingNo(nullCheckerString(request.getParameter(DMSGConstants.P_BK_NUMBER)));
				dmsSearchVO.setPloDateFrom(nullCheckerString(request.getParameter(DMSGConstants.P_PLO_FROM_DATE)));
				dmsSearchVO.setPloDateTo(nullCheckerString(request.getParameter(DMSGConstants.P_PLO_TO_DATE)));
				break;	
				
			default:
				break;
			}
			
			
	 }catch(Exception e){
		 logger.error("DMSHelper :: createSearchVO : Exception ",e);
		 dmsSearchVO=null;
	 }
		 logger.debug("DMSHelper :: createSearchVO :END :: dmsSearchVO "+dmsSearchVO);
		
		return dmsSearchVO;
	}
	
	

	/**
	   * This method Used to Sorted the Dopdown values As Map<String,String> format 
	   * 
	   * @category Method  		sortMapByValue()
	   * @param  type_id        holds set of map instance         				  
	   * @return Map<K, V> 	    List of Map object as Ascending Order
	   */
	public static<K,V> Map<K, V> sortMapByValue(Map<K, V> map) {
	  List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
	          map.entrySet());
	  Collections.sort(list,
	          new Comparator<Map.Entry<K, V>>() {
	              public int compare(Map.Entry<K, V> o1,
	                      Map.Entry<K, V> o2) {
	                  return (((String) o1.getValue()).compareTo((String)o2.getValue()));
	              }
	          });
	
	  Map<K, V> result = new LinkedHashMap<K, V>();
	  for (Iterator<Map.Entry<K, V>> it = list.iterator(); it.hasNext();) {
	      Map.Entry<K, V> entry = it.next();
	      result.put(entry.getKey(), entry.getValue());
	  }
  return result;
}	
	private String nullCheckerString(String str){
		return str!=null?!str.trim().isEmpty()?str.trim():"":"";
	}

	public List<com.giga.dms.vo.KeyValueVo> getDocTypeListVO(
			Map<String, String> listOfDocTypeMap) {
		
		List<KeyValueVo> alKVvo=new ArrayList<KeyValueVo>();
		  Set<String> mapKey=listOfDocTypeMap.keySet();
		   for(String key:mapKey){
			   KeyValueVo kvVO=new KeyValueVo();
			   String val=(String)listOfDocTypeMap.get(key);
			   kvVO.setKey(key);
			   kvVO.setValue(val);
			   alKVvo.add(kvVO);
		   }
		return alKVvo;
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
			 extensionType=(String)extensionMap.get(extKey.toLowerCase()); 
			 if(extensionType==null){
				 System.out.println("JSP:DMS :Preview: extensionMap not contains the extensionType and it is Null  ");
				 extensionType=extKey;
			 }
			 
		 }else{
			 extensionType=extKey;
		 }
			System.out.println("JSP::Preview: extensionType : "+extensionType);
		return extensionType;
	};
	
	public static String toFormat(String strDate){
		  String newDateFormat="";
		      if(strDate!=null && !strDate.isEmpty()){
		    	  newDateFormat=""+strDate.trim().split("/")[1]+"/"+strDate.trim().split("/")[0]+"/"+strDate.trim().split("/")[2];
		      }
		System.out.println("New Date Format :: "+newDateFormat);
		return newDateFormat;
	}
	
	
  /*  public  static void main(String[] args) {
  	     toFormat("20/05/2015");
	}*/
}
