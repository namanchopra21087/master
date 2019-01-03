package com.giga.tms.dosummary.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.log4j.Logger;

import com.giga.base.BaseVO;
import com.giga.base.LoggerUtility;
import com.giga.base.UserProfileAccessUtil;
import com.giga.base.service.GigaServiceLocator;
import com.giga.exception.DataAccessException;
import com.giga.exception.ServiceException;
import com.giga.master.util.MasterConstants;
import com.giga.master.vo.CustomerMasterExcelReportVO;
import com.giga.tms.ITMSServices;
import com.giga.tms.service.DOSummaryService;
import com.giga.tms.util.GenerateTMSReportUtil;
import com.giga.tms.vo.DOSummaryVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * A sample portlet based on GenericPortlet
 */
public class DOSummaryPortlet extends Basedosummary {
	
	DOSummaryService doSummaryService=null;
	String jsonString = null;
	List<BaseVO> dosummarylist = null;
	 DOSummaryVo doSummaryVo=null;
	private Logger logger=null;
	private String currentUserName = null;
	private String loginUserRole = null;
	private String currentUserCompanyName = null;	
	private String currentUserBranchName = null;	
	UserProfileAccessUtil userProfileAccessUtil=null;
	
	public void initialize() {

		try {
			logger=LoggerUtility.getInstance().getLogger(DOSummaryPortlet.class);
			doSummaryService = (DOSummaryService) GigaServiceLocator
					.getService(ITMSServices.businessLayer.DOSUMMARY_SERVICE);
			logger = LoggerUtility.getLogger(DOSummaryPortlet.class);
			logger.debug("Logger:::::-TMS Delivery Order Summary Module");
				userProfileAccessUtil=UserProfileAccessUtil.getInstance();
			
		} catch (Exception  e) {
		   logger.error("Usage : TMS :: DOSummary : DOSummaryPortle Class : initialize() Method :: "  ,e);                     
           
			e.printStackTrace();
		}
	}
	/**
	 * Serve up the <code>view</code> mode.
	 * 
	 * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		// Set the MIME type for the render response
		response.setContentType(request.getResponseContentType());
			logger.debug("Usage : TMS :: DOSummary : DOSummaryPortle Class : doView():Processing.... " );   
			//To get Login User Details
			getLoginUserDetails(request);
	        request.setAttribute("loginUserRole",loginUserRole);

		// Check if portlet session exists
		DOSummaryPortletSessionBean sessionBean = getSessionBean(request);
		if( sessionBean==null ) {
			response.getWriter().println("<b>NO PORTLET SESSION YET</b>");
			return;
		}
		PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(getJspFilePath(request, VIEW_JSP));
		rd.include(request,response);
	}
	
	@Override
	public void serveResource(ResourceRequest request,
			ResourceResponse response) throws PortletException,
			IOException {
		logger.debug("Usage : TMS :: DOSummary : DOSummaryPortle Class : serveResource():Processing.... " );
		
		//Getting current logged-in user info from SESSION
		PortletSession session = request.getPortletSession();
		loginUserRole = (String) session.getAttribute("loginUserRole");
		currentUserName = (String) session.getAttribute("currentUserName");
		currentUserCompanyName = (String) session.getAttribute("currentUserCompanyName");
		currentUserBranchName = (String) session.getAttribute("currentUserBranchName");
		if ("EVNT_EXPORT_TO_EXCEL".equals(request.getResourceID())) {
			exportToExcel(request, response);
		}else{	
			try {
				if (EVNT_DRIVER_SEARCH.equals(request.getResourceID())) {
					jsonString = searchDriver(request);
					logger.debug("Usage: TMS ::DOSummary: DOSummaryPortle class : serveResource(): DRIVER_SEARCH : end");
				} else if (EVNT_CUSTOMER_SEARCH.equals(request.getResourceID())) {
					jsonString = searchCustomer(request);
					logger.debug("Usage: TMS ::DOSummary: DOSummaryPortle class : serveResource(): CUSTOMER_SEARCH : end");
				} else if (EVNT_DOSUMMARY_SEARCH.equals(request.getResourceID())) {
					jsonString = doSummarySearch(request);
					logger.debug("Usage: TMS ::DOSummary: DOSummaryPortle class : serveResource(): DOSUMMARY_SEARCH : end");
				} else if (EVNT_RECIVED_DATE_UPDATE.equals(request.getResourceID())) {
					doRecivedDateUpdate(request);
					logger.debug("Usage: TMS ::DOSummary: DOSummaryPortle class : serveResource(): RECIVED_DATE_UPDATE : end");
				}
				
			} catch (DataAccessException e) {
				e.printStackTrace();
				jsonString = getBaseMessageJsonString(true,e.getMessage());
				logger.error("Usage : TMS :: DOSummary : DOSummaryPortle Class : serveResource() Method :jsonString " +jsonString);
				logger.error("Usage:TMS ::DOSummary: DOSummaryPortle  Class: serveResource() : Exception Message :: "+e.getMessage() +"\n Exception : ",e);
			} catch (ServiceException e) {
				e.printStackTrace();
				jsonString = getBaseMessageJsonString(true,e.getMessage());
				logger.error("Usage:TMS ::DOSummary: DOSummaryPortle  Class: serveResource() : Exception Message :: "+e.getMessage() +"\n Exception : ",e);
				logger.error("Usage : TMS :: DOSummary : DOSummaryPortle Class : serveResource() Method :jsonString " +jsonString); 
			}
			response.setContentType(MasterConstants.CONTENT_TYPE_JSON);
			response.getWriter().write(jsonString);
			response.getWriter().flush();
		}
	}
	
	
	private String searchDriver(ResourceRequest request) throws DataAccessException,ServiceException{
		String driverCode = request.getParameter("driverCode");
		String drivername = request.getParameter("driverName");
		String idCardNo = request.getParameter("driverIDN");
		dosummarylist = doSummaryService.driverSearch(driverCode,drivername, idCardNo);
		jsonString = gson.toJson(dosummarylist);
		return jsonString;
	}
	private String searchCustomer(ResourceRequest request) throws DataAccessException,ServiceException {
		String customerCode = request.getParameter("customerCode");
		String customerName = request.getParameter("customerName");
		//System.out.println("customerCode:::"+customerCode);
		//System.out.println("customerName:::"+customerName);
		dosummarylist=doSummaryService.customerSearch(customerCode,customerName );
		//System.out.println("dosummarylist:::"+dosummarylist.size());
		jsonString = gson.toJson(dosummarylist);
		return jsonString;
	}
	private String doSummarySearch(ResourceRequest request) throws DataAccessException,ServiceException {
		
		//added on 17/04/2015 for filtering loads according to logged-in User's branch code
		/*if(currentUserBranchName == null){
			getLoginUserDetails();
		}*/
		PortletSession session = request.getPortletSession();
		if(currentUserBranchName == null){
			currentUserBranchName = (String) session.getAttribute("currentUserBranchName");
		}
		String driverName=request.getParameter("driverName");
		String bookingCustomer=request.getParameter("bookingCustomer");
		String fromDate=request.getParameter("doFrom");
		String toDate=request.getParameter("doTo");
		String doCopyStatus=request.getParameter("doCopySt");
		//System.out.println("doCopyStatus :--------------> "+doCopyStatus);
		Map<String,String> criteriaMap = new HashMap<String,String>();
		criteriaMap.put("driverName",driverName);
		criteriaMap.put("bookingCustomer",bookingCustomer);
		criteriaMap.put("fromDate",fromDate);
		criteriaMap.put("toDate",toDate);
		criteriaMap.put("doCopyStatus",doCopyStatus);
		criteriaMap.put("branchCode",currentUserBranchName);
		List<DOSummaryVo> dosummarylist = doSummaryService.doSummarySearch(criteriaMap);
		
		//System.out.println("doSummaryList.size() : "+dosummarylist.size());
		jsonString = gson.toJson(dosummarylist);
		return jsonString;
	}
	

	private void  doRecivedDateUpdate(ResourceRequest request) throws DataAccessException, ServiceException {
		//System.out.println("doRecivedDateUpdater");
 
		 doSummaryVo=new DOSummaryVo();
		 String updateList=request.getParameter("doList");
		 updateList = updateList.substring(updateList.indexOf("rows\":") + 6,updateList.lastIndexOf("}"));
		 //System.out.println(updateList);
		 Type type = new TypeToken<ArrayList<DOSummaryVo>>() { }.getType();
		 List<DOSummaryVo>  voList = (ArrayList<DOSummaryVo>) new Gson().fromJson(updateList, type);
		 List<DOSummaryVo>  updateDOSmryVOList = new ArrayList<DOSummaryVo>();
		 for(DOSummaryVo doSmryVO:voList){
		 if(doSmryVO.getAssignedFlag() != null && doSmryVO.getAssignedFlag()==true) {
				   updateDOSmryVOList.add(doSmryVO);
			 	}
		 }
			doSummaryService.saveDosummaryDetails(updateDOSmryVOList);
	}
	
	/**********************Login User Details********************/
	public void getLoginUserDetails(RenderRequest request) {
		 loginUserRole=userProfileAccessUtil.getCurrentUserRole("TMS_DELIVERYORDER");
		 Map<String,String> currentUserDetails=userProfileAccessUtil.getCurrentUserDetails();
		 currentUserName = currentUserDetails.get(UserProfileAccessUtil.USER_NAME);
		 currentUserCompanyName = currentUserDetails.get(UserProfileAccessUtil.COMPANYNAME);
		 currentUserBranchName =  currentUserDetails.get(UserProfileAccessUtil.BRANCH_CODE);
		 
		 //setting the logged-in user credentials from PUMA to user session
		 PortletSession session = request.getPortletSession();
		 session.setAttribute("loginUserRole", loginUserRole);
		 session.setAttribute("currentUserName", currentUserName);
		 session.setAttribute("currentUserCompanyName", currentUserCompanyName);
		 session.setAttribute("currentUserBranchName", currentUserBranchName);
		 
		 
		 //System.out.println("loginUserRole >>>>>>>>> "+loginUserRole);
		 //System.out.println("currentUserName >>>>>>> "+currentUserName);
		 //System.out.println("currentCompanyName >>>>>>> "+currentUserCompanyName);
		 //System.out.println("currentUserBranchName >>>>>>>> "+currentUserBranchName);
	}
	
	//added on 19/12/2015 by ashishkumar_p for excel report and print preview
	/**
	 * This method is responsible for export the LIST of VO to EXCEL
	 * 
	 * @param request
	 * @param response
	 */
	private void exportToExcel(ResourceRequest request,
			ResourceResponse response) {
		try {
			PortletSession session = request.getPortletSession();
			if(currentUserBranchName == null){
				currentUserBranchName = (String) session.getAttribute("currentUserBranchName");
			}
			String driverName=request.getParameter("driverName");
			String bookingCustomer=request.getParameter("bkCustName");
			String fromDate=request.getParameter("fromDate");
			String toDate=request.getParameter("toDate");
			String doCopyStatus=request.getParameter("copyRcvd");
			String isPDFRpt = request.getParameter("isPDF");
				
			Map<String,String> criteriaMap = new HashMap<String,String>();
			criteriaMap.put("driverName",driverName);
			criteriaMap.put("bookingCustomer",bookingCustomer);
			criteriaMap.put("fromDate",fromDate);
			criteriaMap.put("toDate",toDate);
			criteriaMap.put("doCopyStatus",doCopyStatus);
			criteriaMap.put("branchCode",currentUserBranchName);
			//System.out.println("doCopyStatus :--------------> "+doCopyStatus);
			List<DOSummaryVo> doSummaryList = doSummaryService.doSummarySearch(criteriaMap);
			//System.out.println("doSummaryList.size() : "+doSummaryList.size());
			byte[] outArray = null;
			String pdfNamePrefix = null;
			if(isPDFRpt.equalsIgnoreCase("true")){ 
				outArray = generatePDF(doSummaryList);
				response.setContentType("application/pdf");
				pdfNamePrefix = "DOSummaryReport_" + System.currentTimeMillis() + ".pdf";
			}else{
				outArray = generateExcel(doSummaryList);
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				pdfNamePrefix = "DOSummaryReport_" + System.currentTimeMillis() + ".xlsx";
			}
			
			response.setProperty("Content-Disposition", "attachment; filename="	+ pdfNamePrefix);
			response.getPortletOutputStream().write(outArray);
			response.getPortletOutputStream().flush();
			response.getPortletOutputStream().close();
		} catch (Exception e) {
			logger.error("Usage::DOSummary : DOSummaryPortlet : exportToExcel() : Exception",e);
			System.out.println("************************************************");
			System.out.println("IF THE SYSTEM THROWS java.lang.IncompatibleClassChangeError...");
			System.out.println("PLEASE DELETE jakarta-poi.jar from 'IBM/WebSphere/PortalServer/lwo/prereq.odc/shared/app' FOLDER");
			System.out.println("************************************************ ");
		}
	}
	
	public byte[] generatePDF(List<DOSummaryVo> doSummaryList) throws Exception {
		GenerateTMSReportUtil generateReportUtil = new GenerateTMSReportUtil();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] pdf = null;
		try {
			if(doSummaryList!=null && doSummaryList.size()>0){
				pdf = generateReportUtil.generatePdf("DOSummaryExcelReport", baos,doSummaryList);
			}else{
				throw new ServiceException("No record exists with this search criteria, please change and search again");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return pdf;
	}
	public byte[] generateExcel(List<DOSummaryVo> doSummaryList) throws Exception {
		GenerateTMSReportUtil generateReportUtil = new GenerateTMSReportUtil();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] pdf = null;
		try {
			if(doSummaryList!=null && doSummaryList.size()>0){
				pdf = generateReportUtil.generateDoSummaryExcel("DOSummaryExcelReport", baos,doSummaryList);
			}else{
				throw new ServiceException("No record exists with this search criteria, please change and search again");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return pdf;
	}

}

	