package com.giga.tms.util;

import java.io.File;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.giga.base.LoggerUtility;
import com.giga.base.UserProfileAccessUtil;
import com.giga.base.service.GigaServiceLocator;
import com.giga.exception.DataAccessException;
import com.giga.exception.ServiceException;
import com.giga.tms.ITMSServices;
import com.giga.tms.service.DeliveryOrderService;
import com.giga.tms.util.mail.TMSEMail;
import com.giga.tms.util.mail.TMSMailStereoTokens;
import com.giga.tms.util.mail.TMSMailType;
import com.giga.tms.vo.DeliveryOrderReportSubDOVO;
import com.giga.tms.vo.DeliveryOrderReportVO;

public class TMSUtil {
	private SimpleDateFormat sdf = null;
	private static SimpleDateFormat ddMMyyyySdf = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat yyyy_mm_dd_Sdf = new SimpleDateFormat("yyyy-MM-dd");

	public boolean isNullOrEmpty(String param) {
		boolean ret = true;
		if (param != null) {
			if (!param.trim().isEmpty()) {
				ret = false;
			}
		}
		return ret;
	}

	public static boolean isNotEmpty(String value) {
		try {
			if (value != null && !value.trim().isEmpty() && !"null".equalsIgnoreCase(value))
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean isEmpty(String value) {
		try {
			if (value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value))
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public boolean isNullOrEmptyMap(Map param) {
		boolean ret = true;
		if (param != null) {
			if (!param.isEmpty()) {
				ret = false;
			}
		}
		return ret;
	}

	public Date toUtilDate(String pattern, String dateStr) throws ParseException {
		Date date = null;
		if (!isNullOrEmpty(dateStr)) {
			sdf = new SimpleDateFormat(pattern);
			date = sdf.parse(dateStr);
		}
		return date;
	}

	public String toDateString(String pattern, Date date) {
		String dateStr = null;
		if (date != null) {
			sdf = new SimpleDateFormat(pattern);
			dateStr = sdf.format(date);
		}
		return dateStr;
	}

	public String toDateConvert(String pattern, String date) {
		SimpleDateFormat formatter1 = null;
		Date d1 = null;
		if (!isNullOrEmpty(date)) {
			try {
				SimpleDateFormat formatter = null;
				if (pattern.equals("yyyy/MM/dd")) {
					formatter = new SimpleDateFormat("yyyy/MM/dd");
					formatter1 = new SimpleDateFormat("dd/MM/yyyy");
				} else {
					formatter = new SimpleDateFormat("yyyy-MM-dd");
					formatter1 = new SimpleDateFormat("dd-MM-yyyy");
				}
				d1 = formatter.parse(date);
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}
		return formatter1.format(d1);
	}

	public String toDateConvertForExcelImport(String pattern, String date) {
		SimpleDateFormat formatter1 = null;
		Date d1 = null;
		if (!isNullOrEmpty(date)) {
			try {
				SimpleDateFormat formatter = null;
				if (pattern.equals("MM/dd/yyyy")) {
					formatter = new SimpleDateFormat("MM/dd/yyyy");
					formatter1 = new SimpleDateFormat("dd/MM/yyyy");
				} else {
					formatter = new SimpleDateFormat("dd-MM-yyyy");
					formatter1 = new SimpleDateFormat("dd/MM/yyyy");
				}
				d1 = formatter.parse(date);
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}
		return formatter1.format(d1);
	}

	public String toDateConvertForExcelImportforBooking(String pattern, String date) {
		SimpleDateFormat formatter1 = null;
		Date d1 = null;
		if (!isNullOrEmpty(date)) {
			try {
				// SimpleDateFormat formatter = null;
				if (pattern.equals("MM/dd/yyyy")) {
					// formatter = new SimpleDateFormat("MM/dd/yyyy");
					formatter1 = new SimpleDateFormat("dd/MM/yyyy");
				} else {
					// formatter = new SimpleDateFormat("dd-MM-yyyy");
					formatter1 = new SimpleDateFormat("dd/MM/yyyy");
				}
				d1 = formatter1.parse(date);
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}
		return formatter1.format(d1);
	}

	public String toDateConvertDisplay(String pattern, String date) {
		SimpleDateFormat formatter1 = null;
		Date d1 = null;
		if (!isNullOrEmpty(date)) {
			try {
				SimpleDateFormat formatter = null;
				if (pattern.equals("yyyy/MM/dd")) {
					formatter = new SimpleDateFormat("yyyy/MM/dd");
					formatter1 = new SimpleDateFormat("dd/MM/yyyy");
				} else {
					formatter = new SimpleDateFormat("yyyy-MM-dd");
					formatter1 = new SimpleDateFormat("dd/MM/yyyy");
				}
				d1 = formatter.parse(date);
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}
		return formatter1.format(d1);
	}

	public String toDateConvertForAtt(String pattern, String date) {
		SimpleDateFormat formatter1 = null;
		Date d1 = null;
		if (!isNullOrEmpty(date)) {
			try {
				SimpleDateFormat formatter = null;
				if (pattern.equals("dd/MM/yyyy")) {
					formatter = new SimpleDateFormat("dd/MM/yyyy");
					formatter1 = new SimpleDateFormat("yyyy/MM/dd");
				} else {
					formatter = new SimpleDateFormat("yyyy-MM-dd");
					formatter1 = new SimpleDateFormat("dd/MM/yyyy");
				}
				d1 = formatter.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return formatter1.format(d1);
	}

	public String getMyDateWithHourMinits(String myDate, String returnFormat, String myFormat) {
		DateFormat dateFormat = new SimpleDateFormat(returnFormat);
		Date date = null;
		String returnValue = "";
		try {
			date = new SimpleDateFormat(myFormat).parse(myDate);
			returnValue = dateFormat.format(date);
		} catch (ParseException e) {
			returnValue = myDate;
			System.out.println("failed");
			e.printStackTrace();
		}

		return returnValue;
	}

	public static double parseDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			return 0.00;
		}
	}

	public static String convertToDdMMYYYY(String date){
		try{
			return ddMMyyyySdf.format(ddMMyyyySdf.parse(date));
		}catch(Exception e){return null;}
	}
	public static Date convertToDdMMYYYYDate(String date){
		try{
			return ddMMyyyySdf.parse(date);
		}catch(Exception e){return null;}
	}
	public static String convertToDdMMYYYYString(Date date){
		try{
			return ddMMyyyySdf.format(date);
		}catch(Exception e){return null;}
	}
	public static String convertTo_YYYY_MM_DD(Date date){
		try{
			return yyyy_mm_dd_Sdf.format(date);
		}catch(Exception e){return null;}
	}
	
	public static void main(String[] args) {
		System.out.println(convertTo_YYYY_MM_DD(new Date()));
	}
	
	public Time toUtilTime(String pattern, String dateStr) throws ParseException {
		Date date = null;
		if (!isNullOrEmpty(dateStr)) {
			sdf = new SimpleDateFormat(pattern);
			date = sdf.parse(dateStr);
		}
		return new java.sql.Time(date.getTime());
	}
	
	/**
	 * Send email notification to customer for creation of delivery order
	 * @param deliveryId
	 * @param deliveryNumber
	 * @param billingCustomerId
	 * @param deliveryZoneMasterIdList
	 * @param currentUserCompanyName
	 * @param currentUserBranchName
	 * @param currentUserName
	 */
public static void emailNotificationToCustomer(String deliveryId,String deliveryNumber,String billingCustomerId,String currentUserName,Map<String, String> headerMap){
		DeliveryOrderService deliveryOrderService = null;
		Logger logger = LoggerUtility.getLogger(TMSUtil.class);
		GenerateTMSReportUtil report = new GenerateTMSReportUtil();
		Map<String,String> doParameterMap = new HashMap<String, String>();
		UserProfileAccessUtil userProfileAccessUtil=null;
		String currentUserCompanyName=null;
		String currentUserBranchName=null;
		String reportName = StringUtils.EMPTY;
		String subReportName = StringUtils.EMPTY;
		String rootPath = "D:";
		 
		 try {
			 deliveryOrderService = (DeliveryOrderService) GigaServiceLocator.getService(ITMSServices.businessLayer.DELIVERY_ORDER_SERVICE);
			 userProfileAccessUtil=UserProfileAccessUtil.getInstance();
			 Map<String,String> userDetails = userProfileAccessUtil.getCurrentUserDetails();
			 if(null!=headerMap){
				 currentUserCompanyName=headerMap.get(DeliveryOrderConstant.COMPANY_CODE);
				 currentUserBranchName=headerMap.get(DeliveryOrderConstant.BRANCH_CODE);
			 }else{
				 currentUserCompanyName=userDetails.get(UserProfileAccessUtil.COMPANYNAME);
				 currentUserBranchName=userDetails.get(UserProfileAccessUtil.BRANCH_CODE);
			 }
			 doParameterMap.put("doId", deliveryId);
			 doParameterMap.put("companyCode", currentUserCompanyName);
			 doParameterMap.put("branchCode", currentUserBranchName);
			 List<DeliveryOrderReportVO> doReportVoList;
		try {
			doReportVoList = deliveryOrderService.loadReportDetails(doParameterMap);
			String currentEmail= TMSReportConstants.GIGA_IT_EMAIL_ID;
	        String directoryName =  rootPath + "/Temp/TMS/DELIVERY_ORDER";
	        File theDir = new File(directoryName);
	  	  	if (!theDir.exists()){
	  	  		theDir.mkdirs();
	  	  	}
			/* arul added for KKCT report */
			logger.debug("currentUserCompanyName report ::::::::::"+currentUserCompanyName);
			if("KK".equalsIgnoreCase(currentUserCompanyName)){
				reportName = "/DeliveryOrder_KKCT";
				subReportName="/DeliveryOrder_SubDO_KKCT";
			}else{
				reportName = "/DeliveryOrder";
				subReportName="/DeliveryOrder_SubDO";
			}
	  	  	String path = directoryName + reportName+deliveryNumber+".pdf";
	  	  	/** Main DO pdf generation*/
	  	  	if(!(null!=headerMap && null!=headerMap.get(DeliveryOrderConstant.IS_DO_PREVIOUSLY_GENERATED) 
	  	  			&& "true".equals(headerMap.get(DeliveryOrderConstant.IS_DO_PREVIOUSLY_GENERATED)))){
	  	  	report.generateDeliveryOrderPDF(reportName, path,deliveryNumber,"0",doReportVoList);
			sendEmail(deliveryNumber, billingCustomerId,currentUserName, deliveryOrderService,doReportVoList.get(0).getDeliveryLocationId(),
					currentEmail, path);
	  	  	}
			/**Logic to send email to additional drop locations*/
			List<DeliveryOrderReportSubDOVO> subDoLists=new ArrayList<DeliveryOrderReportSubDOVO>();
			DeliveryOrderReportVO reportVo=doReportVoList.get(0);
			if(null!=reportVo && null!=reportVo.getSubList()&& reportVo.getSubList().size()>0){
				subDoLists.addAll(reportVo.getSubList());
				reportVo.getSubList().clear();
				
				for(DeliveryOrderReportSubDOVO subDo:subDoLists){
					List<DeliveryOrderReportSubDOVO> subDoListsForPdf=new ArrayList<DeliveryOrderReportSubDOVO>();
					subDoListsForPdf.add(subDo);
					/**Adding remaining fields that are required inside sub-do after splitting of auto-dn*/
					addingDoDataToSubDoForSplitting(reportVo, subDo);
					path = directoryName + subReportName+subDo.getSubDONumber()+".pdf";
					/** Sub DO pdf generation*/
					report.generateDeliveryOrderPDF(subReportName, path,deliveryNumber,subDo.getDeliveryLocationId(),subDoListsForPdf);
					sendEmail(subDo.getSubDONumber(), billingCustomerId,currentUserName, deliveryOrderService,subDo.getDeliveryLocationId(),
							currentEmail, path);
					}
				}
			} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
		}
	}

	/**
	 * Adding remaining fields that are required inside sub-do after splitting of auto-dn
	 * @param reportVo
	 * @param subDo
	 */
	private static void addingDoDataToSubDoForSplitting(DeliveryOrderReportVO reportVo, DeliveryOrderReportSubDOVO subDo) {
		subDo.setPostCode(reportVo.getPostCode());
		subDo.setBookingStateName(reportVo.getBookingStateName());
		subDo.setBookingCountryName(reportVo.getBookingCountryName());
		subDo.setBookingCityName(reportVo.getBookingCityName());
		subDo.setStateName(reportVo.getStateName());
		subDo.setCityName(reportVo.getCityName());
		subDo.setCountryName(reportVo.getCountryName());
		subDo.setPostCode(reportVo.getPostCode());
		subDo.setBookingCustName(reportVo.getBookingCustName());
		subDo.setBookingCustAddr1(reportVo.getBookingCustAddr1());
		subDo.setBookingCustAddr2(reportVo.getBookingCustAddr2());
		subDo.setBookingPostCode(reportVo.getBookingPostCode());
		subDo.setBookingCustFax1(reportVo.getBookingCustFax1());
		subDo.setBookingCustFax2(reportVo.getBookingCustFax2());
		subDo.setBookingCustFax3(reportVo.getBookingCustFax3());
		subDo.setBookingCustTele1(reportVo.getBookingCustTele1());
		subDo.setBookingCustTele2(reportVo.getBookingCustTele2());
		subDo.setBookingCustTele3(reportVo.getBookingCustTele3());
		subDo.setConsigneeAddress1(reportVo.getConsigneeAddress());
		subDo.setTruckRegNo(reportVo.getTruckRegNo());
		subDo.setBranchAddress1(reportVo.getBranchAddress1());
		subDo.setBranchTele1(reportVo.getBranchTele1());
		subDo.setBranchTele2(reportVo.getBranchTele2());
		subDo.setBranchTele3(reportVo.getBranchTele3());
		subDo.setBranchFax1(reportVo.getBranchFax1());
		subDo.setBranchFax2(reportVo.getBranchFax2());
		subDo.setBranchFax3(reportVo.getBranchFax3());
		subDo.setCompanyNo(reportVo.getCompanyNo());
		// Ram added Head Office address in pdf on 24/10/2018
		subDo.setCompanyAddress(reportVo.getCompanyAddress());
		subDo.setCompanyTele1(reportVo.getCompanyTele1());
		subDo.setCompanyTele2(reportVo.getCompanyTele2());
		subDo.setCompanyTele3(reportVo.getCompanyTele3());
		subDo.setCompanyFax1(reportVo.getCompanyFax1());
		subDo.setCompanyFax2(reportVo.getCompanyFax2());
		subDo.setCompanyFax3(reportVo.getCompanyFax3());
		subDo.setCompanyStateName(reportVo.getCompanyStateName());
		subDo.setCompanyCountryName(reportVo.getCompanyCountryName());
		subDo.setCompanyCityName(reportVo.getCompanyCityName());
		subDo.setCompanyPostCode(reportVo.getCompanyPostCode());
	}

	/**
	 * @param deliveryNumber
	 * @param billingCustomerId
	 * @param currentUserName
	 * @param deliveryOrderService
	 * @param doReportVoList
	 * @param currentEmail
	 * @param path
	 * @throws ServiceException
	 * @throws DataAccessException
	 */
	private static void sendEmail(String deliveryNumber,String billingCustomerId, String currentUserName,DeliveryOrderService deliveryOrderService,
			String deliveryLocationId, String currentEmail,String path) throws ServiceException, DataAccessException {
		List<String> toEmail = deliveryOrderService.getOutletEmailId(billingCustomerId,deliveryLocationId);
		List<String> ccEmail = null;
		
		if(null!=toEmail&& toEmail.size()>0){
			ccEmail = new ArrayList<String>();
			ccEmail.add(currentEmail);
		}else{
			toEmail=new ArrayList<String>();
			toEmail.add(currentEmail);
		}
			
		Map<String,String> dataMap = new HashMap<String,String>();
		dataMap.put(TMSMailStereoTokens.deliveryOrder.deliveryOrder, deliveryNumber);
		//Changes made as per Bug-3895
		TMSEMail.sendMail(TMSMailType.DELIVERY_ORDER, toEmail, ccEmail, currentUserName, path, dataMap);
	}
}
