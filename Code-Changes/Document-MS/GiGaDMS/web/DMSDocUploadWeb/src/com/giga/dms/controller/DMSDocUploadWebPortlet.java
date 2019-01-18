package com.giga.dms.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.giga.dms.helper.*;

import javax.portlet.*;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.log4j.Logger;

import com.giga.base.LoggerUtility;
import com.giga.base.service.GigaServiceLocator;
import com.giga.dms.IDMSService;
import com.giga.dms.service.DMSGService;
import com.giga.dms.services.AESencrp;
import com.giga.dms.services.DMSServices;
import com.giga.dms.services.GMGDMSServices;
import com.giga.dms.services.Metadata;
import com.giga.dms.util.DMSGConstants;
import com.giga.dms.util.DMSGType;
import com.giga.dms.util.DMSMetaDataKeys;
import com.giga.dms.util.GiGaModuleType;
import com.giga.dms.vo.DMSDocumentVO;
import com.giga.dms.vo.DMSResultObject;
import com.giga.dms.vo.DMSSearchVO;
import com.giga.dms.vo.DMSWebUploadVO;
import com.giga.dms.vo.KeyValueVo;
import com.giga.exception.DataAccessException;

/**
 * A sample portlet based on GenericPortlet
 */
public class DMSDocUploadWebPortlet extends GenericPortlet {

	public static final String JSP_FOLDER = "/_DMSDocUploadWeb/jsp/"; // JSP
																		// folder
																		// name
	public static final String VIEW_JSP = "DMSDocUploadWebPortletView"; // JSP
																		// file
																		// name
																		// to be
																		// rendered
																		// on
																		// the
																		// view
																		// mode
	public static final String SESSION_BEAN = "DMSDocUploadWebPortletSessionBean"; // Bean
																					// name
																					// for
																					// the
																					// portlet
																					// session
	public static final String FORM_SUBMIT = "DMSDocUploadWebPortletFormSubmit"; // Action
																					// name
																					// for
																					// submit
																					// form
	public static final String FORM_TEXT = "DMSDocUploadWebPortletFormText"; // Parameter
																				// name
																				// for
																				// the
																				// text
																				// input
	public static final String JSON_RESPONSE = "application/json";

	@SuppressWarnings("static-access")
	private static Logger logger = LoggerUtility.getInstance().getLogger(
			DMSDocUploadWebPortlet.class);

	private static String SYS_LINE = "";
	private String WS_URL = null;
	private GMGDMSServices service = null;
	private DMSServices dms_services = null;
	private DMSGService dmsService = null;
	private BookingDMSHelper dmsHelper = null;
	private java.util.Map<String, Object> req_ctx = null;
	private java.util.Map<String, List<String>> headers = null;
	private String callFrom = "";

	/**
	 * @see javax.portlet.Portlet#init()
	 */
	public void init() throws PortletException {
		super.init();
		WS_URL = BookingDMSHelper.DMS_SERVICE_URL;
		service = new GMGDMSServices();
		dms_services = service.getDMSServicesImplPort();

		req_ctx = ((BindingProvider) dms_services).getRequestContext();
		req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, WS_URL);

		headers = new HashMap<String, List<String>>();

		try {
			headers.put(BindingProvider.USERNAME_PROPERTY,
					Collections.singletonList(AESencrp.encrypt("Author")));
			headers.put(BindingProvider.PASSWORD_PROPERTY,
					Collections.singletonList(AESencrp.encrypt("Password1")));
		} catch (Exception e) {
			logger.error("DMSBookingUpload:: Exception araised in init() call"
					+ e);
		}
		req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
		dmsHelper = BookingDMSHelper.getInstance();
		try {
			dmsService = (DMSGService) GigaServiceLocator
					.getService(IDMSService.businessLayer.DMSG_SERVICE);
			if (dmsHelper == null) {
				dmsHelper = BookingDMSHelper.getInstance();
			}
		} catch (Exception e) {
			
			logger.error("DMSPortlet:: doView(): dmsService classs creation failed....."
					+ e);
		}
	}

	/**
	 * Serve up the <code>view</code> mode.
	 * 
	 * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest,
	 *      javax.portlet.RenderResponse)
	 */
	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {

		response.setContentType(request.getResponseContentType());

		try {
			dmsService = (DMSGService) GigaServiceLocator
					.getService(IDMSService.businessLayer.DMSG_SERVICE);
			if (dmsHelper == null) {
				dmsHelper = BookingDMSHelper.getInstance();
			}
		} catch (Exception e) {
			logger.error("DMSPortlet:: doView(): dmsService classs creation failed....."
					+ e);
		}

		// IPC call from Booking Screen to Upload Screen
		PortletSession session = request.getPortletSession();
		String ipcValues = (String) session.getAttribute("uploadDetails");		
		String ipcValueTMS = (String) session.getAttribute("uploadDetails");
			
		session.removeAttribute("uploadDetails");
		
		Object bookingNumberIPC = "";

		if (ipcValues != null) {
			bookingNumberIPC = ipcValues;
		} else if (ipcValueTMS != null) {
			bookingNumberIPC = ipcValueTMS;
		}

		request.setAttribute("bookingNoIPC", bookingNumberIPC);
		logger.debug("Booking no from ipc : " + bookingNumberIPC);

		String profileModuleName = BookingDMSHelper
				.getUserProfileModuleName(session);

		// This method is used to get The ProfileModule Name as well as put all
		// the user profile details into session scope
		if (profileModuleName.equals(BookingDMSHelper.PROFILE_ACESS_FAILED)) {
			request.setAttribute(BookingDMSHelper.PROFILE_ACESS_FAILED,
					BookingDMSHelper.PROFILE_ACESS_FAILED);
		} else {
			session.setAttribute(BookingDMSHelper.SYSTEM_LINE,
					profileModuleName);
			request.setAttribute(BookingDMSHelper.SYSTEM_LINE,
					profileModuleName);

			SYS_LINE = profileModuleName;
			BookingDMSHelper.LOGGED_USER = (String) session
					.getAttribute(BookingDMSHelper.USER_NAME);

			try {
				Map<String, String> documentLineMap = dmsService
						.loadDocumentLine(profileModuleName.trim());// code and
																	// description
				logger.debug("Map size is : " + documentLineMap);
				if (documentLineMap != null ? !documentLineMap.isEmpty() ? true
						: false : false) {
					request.setAttribute("documentLineList",
							BookingDMSHelper.sortMapByValue(documentLineMap));
				}
			} catch (Exception de) {
				logger.debug("DataAccessException Loading failed to DocumentLine");
			}
		}
		if (callFrom.equalsIgnoreCase("upload")) {
			String message = request.getParameter("message");
			String messageData = request.getParameter("messageData");
			
			request.setAttribute("screenMessage", message);
			request.setAttribute("messageData", messageData);
			logger.debug("message" + message);
		}

		// Check if portlet session exists
		DMSDocUploadWebPortletSessionBean sessionBean = getSessionBean(request);
		if (sessionBean == null) {
			response.getWriter().println("<b>NO PORTLET SESSION YET</b>");
			return;
		}

		// Invoke the JSP to render
		PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(
				getJspFilePath(request, VIEW_JSP));
		rd.include(request, response);

		callFrom = "";
	}

	@Override
	public void processEvent(EventRequest request, EventResponse response)
			throws PortletException, IOException {
		logger.debug("Inside DMSUPLOAD-Existing Booking ipc  process Event method");
		Event event = request.getEvent();
		logger.debug("event methos" + event.getName());
		if (event.getName().equals("uploadDoc")) {
			logger.debug("IPC triggered from OFS !!");
			String uploadList = (String) event.getValue();
			logger.debug("upload list" + uploadList);
			PortletSession session = request.getPortletSession();
			session.setAttribute("uploadDetails", uploadList);
		} else if (event.getName().equals("DocUpload")) {
			logger.debug("IPC triggered from TMS !!");
			String uploadList = (String) event.getValue();
			logger.debug("upload list" + uploadList);

			PortletSession session = request.getPortletSession();
			session.setAttribute("uploadDetails", uploadList);
		}
	}

	/**
	 * Process an action request.
	 * 
	 * @see javax.portlet.Portlet#processAction(javax.portlet.ActionRequest,
	 *      javax.portlet.ActionResponse)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ProcessAction(name = "uploadDocuments")
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, java.io.IOException {
		logger.debug("WebDocumentUploadPortlet : UploadDocuments...................");
		logger.debug("WebDocumentUploadPortlet : callFrom :: "
				+ request.getParameter("callFrom"));
		logger.debug("WebDocumentUploadPortlet : PortletFileUpload.isMultipartContent(request) :: "
				+ PortletFileUpload.isMultipartContent(request));

		PortletSession session = request.getPortletSession();
		String documentLineValue = "";
		String documentTypeValue = "";
		String bookingCustomervalue = "";
		String bookingNoValue = "";
		String jobNoValue = "";
		String siNoValue = "";
		String businessLineValue = "";
		String serviceTypeValue = "";
		String serviceLineValue = "";
		String portOfDischargeValue = "";
		String cargoTypeValue = "";
		String claimNoValue = "";
		String uploadByValue = "";
		String vesselNameValue= "";
		String voyageNoValue= "";
		String file_name = null;

		InputStream is = null;
		byte[] bytes = null;

		List<DMSDocumentVO> documentList = new ArrayList<DMSDocumentVO>(0);
		String userRole = (String) session.getAttribute(BookingDMSHelper.USER_ROLE);
		String moduleGroupName = (String) session.getAttribute(BookingDMSHelper.USER_PROFILE_MODULE_NAME);
		
		if (PortletFileUpload.isMultipartContent(request)) {
			try {
				PortletFileUpload pfUpload = new PortletFileUpload(
						new DiskFileItemFactory());
				List<FileItem> multiparts = pfUpload.parseRequest(request);
				
				
				
				for (FileItem item : multiparts) {					
					if (item.isFormField()) {
						String value = item.getString();

						System.out.println("DMS ::YES item.getFieldName() "+item.getFieldName()+" and value is : "+value + " "+multiparts.indexOf(item));
						
						if (item.getFieldName().equalsIgnoreCase(
								"docLine[]")) {
							documentLineValue = value;
						} else if (item.getFieldName().equalsIgnoreCase(
								"docType[]")) {
							documentTypeValue = value;
						} else if (item.getFieldName().equalsIgnoreCase(
								"bookingCustF[]")) {
							bookingCustomervalue = value;
						} else if (item.getFieldName().equalsIgnoreCase(
								"bookingNo[]")) {
							bookingNoValue = value;
						} else if (item.getFieldName().equalsIgnoreCase(
								"jobNo[]")) {
							jobNoValue = value;
						} else if (item.getFieldName().equalsIgnoreCase(
								"siNo[]")) {
							siNoValue = value;
						} else if (item.getFieldName().equalsIgnoreCase(
								"businessLineF[]")) {
							businessLineValue = value;
						} else if (item.getFieldName().equalsIgnoreCase(
								"serviceTypeF[]")) {
							serviceTypeValue = value;
						} else if (item.getFieldName().equalsIgnoreCase(
								"serviceLineF[]")) {
							serviceLineValue = value;
						} else if (item.getFieldName().equalsIgnoreCase(
								"pod[]")) {
							portOfDischargeValue = value;
						} else if (item.getFieldName().equalsIgnoreCase(
								"cargoType[]")) {
							cargoTypeValue = value;
						} else if (item.getFieldName().equalsIgnoreCase(
								"claimNoF[]")) {
							claimNoValue = value;
						} else if (item.getFieldName().equalsIgnoreCase(
								"vesselNameF[]")) {
							vesselNameValue = value;
						}else if (item.getFieldName().equalsIgnoreCase(
								"voyageNoF[]")) {
							voyageNoValue = value;
						}else if (item.getFieldName().equalsIgnoreCase(
								"uploadById")) {
							uploadByValue = value;
						}
					}
					if (!item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("fileName")) {
							file_name = new File(item.getName()).getName();

							is = item.getInputStream();
							bytes = org.apache.commons.io.IOUtils.toByteArray(is);
							
							DMSDocumentVO dmsDocumentVo = new DMSDocumentVO();
							Map<DMSMetaDataKeys, String> metaData = new HashMap<DMSMetaDataKeys, String>(0);
											
							metaData.put(DMSMetaDataKeys.SYSTEM_LINE, moduleGroupName); // Mandatory
							metaData.put(DMSMetaDataKeys.DOCUMENT_LINE, documentLineValue); // Mandatory
							metaData.put(DMSMetaDataKeys.DOCUMENT_TYPE, documentTypeValue); // Mandatory
							metaData.put(DMSMetaDataKeys.BOOKIN_CUSTOMER,bookingCustomervalue);
							metaData.put(DMSMetaDataKeys.BOOKING_NUMBER, bookingNoValue);
														
							if(moduleGroupName.equals("OFS")){
								metaData.put(DMSMetaDataKeys.JOB_NUMBER, jobNoValue);
								metaData.put(DMSMetaDataKeys.SI_NUMBER, siNoValue);
								metaData.put(DMSMetaDataKeys.PORT_OF_DISCHARGE,portOfDischargeValue);
								metaData.put(DMSMetaDataKeys.SERVICE_TYPE, serviceTypeValue);
								metaData.put(DMSMetaDataKeys.SERVICE_LINE, serviceLineValue);					
																
							}
							metaData.put(DMSMetaDataKeys.BUSINESS_LINE, businessLineValue);
							metaData.put(DMSMetaDataKeys.CARGO_TYPE, cargoTypeValue);
							metaData.put(DMSMetaDataKeys.CLAIM_NUMBER, claimNoValue);
							metaData.put(DMSMetaDataKeys.VESSEL_NAME, vesselNameValue);
							metaData.put(DMSMetaDataKeys.VOYAGE_NUMBER, voyageNoValue);							
							metaData.put(DMSMetaDataKeys.UPLOAD_BY, uploadByValue);
							metaData.put(DMSMetaDataKeys.DOCUMENT_TITLE,file_name); // Mandatory						
														
							dmsDocumentVo.setMetaDataValue(metaData);
							dmsDocumentVo.setContentStream(bytes);
							
							System.out.println("DMS : BookingCustomer :: "+bookingCustomervalue);
							System.out.println("DMS : ClaimNo :: "+claimNoValue);
							System.out.println("DMS : ServiceType :: "+serviceTypeValue);
							System.out.println("DMS : ServiceLine :: "+serviceLineValue);
							System.out.println("DMS : BusinessLine :: "+businessLineValue);
							
							documentList.add(dmsDocumentVo);					
						}
					}					
				}
			} catch (Exception e) {
				logger.debug("Exception in processaction" + e);
			}

			if (!documentList.isEmpty()) {
				DMSResultObject uploadResults = dmsService.multipleUploadDMS(
						documentList, userRole, GiGaModuleType.DMS);
				if (uploadResults != null) {
					if (uploadResults.isResultFlag()) {
						String str = "";
						logger.debug("Usage :: DMS :: WebDocUPload :uploadDocuments(): : uploaded SuccessFully.......");
						response.setRenderParameter("message", BookingDMSHelper.UPLOAD_SUCCESS);
						for(DMSDocumentVO dmsDocumentVo : documentList){
							Map<DMSMetaDataKeys, String> dmsMetaDataKeyz = null;
							 dmsMetaDataKeyz = dmsDocumentVo.getMetaDataValue();
							 
							 str = str +
								   "Document Line : "+ dmsMetaDataKeyz.get(DMSMetaDataKeys.DOCUMENT_LINE)+", "+
								   "Document Type : "+ dmsMetaDataKeyz.get(DMSMetaDataKeys.DOCUMENT_TYPE)+", "+
								   "Booking No : "+ dmsMetaDataKeyz.get(DMSMetaDataKeys.BOOKING_NUMBER)+", "+								   
								   "File Name : "+ dmsMetaDataKeyz.get(DMSMetaDataKeys.DOCUMENT_TITLE) +"<br>";
						}
						response.setRenderParameter("messageData", str);
					} else {
						logger.debug("getmessage"
								+ uploadResults.getResultMessage());
						response.setRenderParameter("message", BookingDMSHelper.UPLOAD_FAILURE_BY_ERROR);
					}
					logger.debug("Usage :: DMS :: WebDocUPload :uploadDocuments(): is Uploaded ::: "
							+ uploadResults.isResultFlag()
							+ " \n upload-message :: "
							+ uploadResults.getResultMessage());
				}
			} else {
				logger.debug("Usage :: DMS :: WebDocUPload :uploadDocuments():: documentList is empty....");
				response.setRenderParameter("message", BookingDMSHelper.UPLOAD_FAILURE_BY_ERROR);
			}
			callFrom = "upload";
		} else {
			logger.debug("WebDocumentUploadPortlet : PortletFileUpload.isMultipartContent(request) is FALSE");
		}
		session.setAttribute("uploadDetails", bookingNoValue);
	}

	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws PortletException,
			IOException {
		logger.debug("DMSPortlet Controller:: serveResource AjaxCall: START :\n Request_Resource ID : "
				+ resourceRequest.getResourceID());
		PortletSession session = null;
		String jsonString = "";
		Boolean jsonContentCheckerflag = true;
		String systemLine = "";
		Metadata metadataObj = new Metadata();
		try {
			session = resourceRequest.getPortletSession();

			if (session == null) {
				logger.debug("DMSPortlet::ServeResourece():: session is null");
			} else {
				systemLine = (String) session
						.getAttribute(BookingDMSHelper.SYSTEM_LINE);
			}
			logger.debug("SystemLine :: " + systemLine);

			if (BookingDMSHelper.LOAD_DOC_TYPE.equals(resourceRequest
					.getResourceID())) {
				jsonString = loadDocTypeMap(resourceRequest);
			} else if (BookingDMSHelper.SEARCH_BOOKING.equals(resourceRequest
					.getResourceID())) { // --
				jsonString = searchDetails(resourceRequest,
						DMSGType.SEARCH_BOOKING, systemLine);
			} else if (BookingDMSHelper.SEARCH_JOB.equals(resourceRequest
					.getResourceID())) { // --
				jsonString = searchDetails(resourceRequest,
						DMSGType.SEARCH_JOB, systemLine);
			} else if (BookingDMSHelper.SEARCH_SI.equals(resourceRequest
					.getResourceID())) { // --
				jsonString = searchDetails(resourceRequest, DMSGType.SEARCH_SI,
						systemLine);
			} else if (BookingDMSHelper.SEARCH_CLAIM.equals(resourceRequest
					.getResourceID())) { // --
				jsonString = searchDetails(resourceRequest,
						DMSGType.SEARCH_CLAIM, systemLine);
			} else if (BookingDMSHelper.SEARCH_BOOKING_CUSTOMER
					.equals(resourceRequest.getResourceID())) {// --
				jsonString = searchDetails(resourceRequest,
						DMSGType.SEARCH_BOOKING_CUSTOMER, systemLine);
			} else if (BookingDMSHelper.LOAD_BOOKING_SERVICETYPES
					.equals(resourceRequest.getResourceID())) {
				jsonString = populateDetails(resourceRequest,
						DMSGType.DMS_BOOKINGNO_SERVICETYPES, systemLine);
			} else if (BookingDMSHelper.LOAD_DMS_BOOKING_SERVICELINES
					.equals(resourceRequest.getResourceID())) {
				jsonString = populateDetails(resourceRequest,
						DMSGType.DMS_BOOKINGNO_SERVICELINES, systemLine);
			} else if (BookingDMSHelper.LOAD_BOOKING_CARGOTYPES
					.equals(resourceRequest.getResourceID())) {
				jsonString = populateDetails(resourceRequest,
						DMSGType.DMS_BOOKINGNO_CARGOTYPES, systemLine);
			} else if (BookingDMSHelper.LOAD_BOOKING_PODS
					.equals(resourceRequest.getResourceID())) {
				jsonString = populateDetails(resourceRequest,
						DMSGType.DMS_BOOKINGNO_PODS, systemLine);
			} else if (BookingDMSHelper.LOAD_BOOKING_BUSINESSLINE
					.equals(resourceRequest.getResourceID())) {
				jsonString = populateDetails(resourceRequest,
						DMSGType.DMS_BOOKINGNO_BUSINESSLINE, systemLine);
			} else if (BookingDMSHelper.LOAD_BOOKINGCUS_AND_BUSINESSLINE
					.equals(resourceRequest.getResourceID())) {
				jsonString = populateBKCustomerNBussLine(resourceRequest,
						DMSGType.DMS_LOAD_BOOKINGCUS_AND_BUSINESSLINE,
						systemLine);
			} else if ("SOFT_ADD".equals(resourceRequest.getResourceID())) {

				String docLine = resourceRequest.getParameter("docLine");
				String docType = resourceRequest.getParameter("docType");
				String bookingNo = resourceRequest.getParameter("bookingNo");
				String jobNo = resourceRequest.getParameter("jobNo");
				String siNo = resourceRequest.getParameter("siNo");
				String pod = resourceRequest.getParameter("pod");
				String cargoType = resourceRequest.getParameter("cargoType");
				String dgJsonList = resourceRequest.getParameter("dgJsonList");

				String str = BookingDMSHelper.softAddToGrid(docLine, docType,
						bookingNo, jobNo, siNo, pod, cargoType, dgJsonList);

				resourceResponse.setContentType(JSON_RESPONSE);
				resourceResponse.getWriter().write(str);
				resourceResponse.getWriter().flush();

			}

		} catch (DataAccessException e) {
			jsonContentCheckerflag = false;
			logger.error("Service Exception Message : " + e.getMessage());
			jsonString = "";

			if (jsonString.equals(BookingDMSHelper.NO_DOCUMENT_FOUND_ERROR)) {
				jsonContentCheckerflag = false;
			}
		} catch (Exception e) {
			jsonContentCheckerflag = false;
			jsonString = e.getMessage();
			logger.error("DMSPortletWebPortlet :: ServeResource : AjaxCall : Exception"
					+ e);
		}
		logger.debug("DMSPortletWebPortlet :: ServeResource : AjaxCall : JSON Object ::: "
				+ jsonString);
		if (jsonContentCheckerflag) {
			logger.debug("ServeResource : ContentType : JSON");
			resourceResponse.setContentType("application/json");
			resourceResponse.getWriter().write(jsonString);
		} else {
			logger.debug("ServeResource : ContentType : HTML");
			resourceResponse.setContentType("text/html");
			resourceResponse.getWriter().write("Exception " + jsonString);
		}

	}

	private String loadDocTypeMap(ResourceRequest resRequest)
			throws DataAccessException {
		logger.debug("DMSPortlet::loadDocTypeMap : START.............");
		String jsonloadDocTypeMap = "";
		PortletSession ses = resRequest.getPortletSession();
		String systemLineS = (String) ses
				.getAttribute(BookingDMSHelper.SYSTEM_LINE);
		String companyCode = (String) ses
				.getAttribute(BookingDMSHelper.COMPANY_CODE);
		String branchCode = (String) ses
				.getAttribute(BookingDMSHelper.BRANCH_CODE);

		String docLineVal = stringNullChecker(resRequest
				.getParameter("DOC_LINE_VAL"));
		String docLineText = stringNullChecker(resRequest
				.getParameter("DOC_LINE_TEXT"));
		logger.debug("DMSPortlet::loadDocTypeMap :: docLineVal :: "
				+ docLineVal + "--docLineText :" + docLineText);
		if (nullEmptyCheckerFlag(docLineVal)) {
			if (docLineVal.contains(":")) {
				docLineVal = docLineVal.split(":")[1];
			}
		}
		logger.debug("DMSPortlet::loadDocTypeMap :: docLineVal :: "
				+ docLineVal);
		Map<String, String> docTypeDetailsMap = new HashMap<String, String>();
		docTypeDetailsMap.put(DMSGConstants.DOCTYPE_SYSTEMLINE, systemLineS);
		docTypeDetailsMap.put(DMSGConstants.DOCTYPE_DOCLINE, docLineVal);
		docTypeDetailsMap.put(DMSGConstants.DOCTYPE_COMPANY_GROUP_CODE,
				branchCode);
		docTypeDetailsMap.put(DMSGConstants.DOCTYPE_USER_COMPANY_CODE,
				companyCode);		
		Map<String, String> listOfDocTypeMap = dmsService
				.loadDocumentTypeUpload(docTypeDetailsMap);
		
		logger.debug("BackTrack :: listOfDocTypeMap : "+ listOfDocTypeMap);

		if (listOfDocTypeMap != null ? !listOfDocTypeMap.isEmpty() ? true
				: false : false) {
			List<KeyValueVo> docTypeListVo = new ArrayList<KeyValueVo>();
			docTypeListVo = dmsHelper.getDocTypeListVO(listOfDocTypeMap);

			KeyValueVo kvo = new KeyValueVo();
			kvo.setKey("");
			kvo.setValue("--Select a value--");
			docTypeListVo.add(0, kvo);

			jsonloadDocTypeMap = dmsHelper.convertToJson(docTypeListVo);
		}
		logger.debug("DMSPortlet::loadDocTypeMap : END...........jsonloadDocTypeMap : "
				+ jsonloadDocTypeMap);
		return jsonloadDocTypeMap;
	}

	private String searchDetails(ResourceRequest resourceRequest,
			DMSGType paramterType, String systemLine)
			throws DataAccessException {
		String searchJsonList = "";
		List<DMSSearchVO> dmsSearchVolist = null;
		String branchCode=(String)getSessionValues(resourceRequest, "BRANCH_CODE");
		String companyCode=(String)getSessionValues(resourceRequest, "COMPANY_CODE");
		DMSSearchVO dmsSearchVO = dmsHelper.createSearchVO(resourceRequest,
				paramterType, systemLine,branchCode,companyCode);
		if (dmsSearchVO != null) {
			dmsSearchVolist = dmsService.getSearchDetails(paramterType,
					dmsSearchVO);
		}
		if (dmsSearchVolist != null ? true : false) {
			searchJsonList = dmsHelper.convertToJson(dmsSearchVolist);
		}
		logger.debug("DMSPortlet :: searchDetails : searchJsonList : "
				+ searchJsonList);
		return searchJsonList;
	}
	/**
	 * This getSessionValues will give the session hold values
	 * @param request
	 * @param typeOfValue
	 * @return
	 */
     protected static Object getSessionValues(PortletRequest request,String typeOfValue){
    	 PortletSession session = request.getPortletSession();
 		if( session == null ){
 			System.out.println("getSessionValues :: Session Null : "+session);
 			return null;
 		}else{
 		 return session.getAttribute(typeOfValue);
 		}
	}

	private String populateDetails(ResourceRequest resourceRequest,
			DMSGType paramterType, String systemLine)
			throws DataAccessException {
		String searchJsonList = "";
		DMSWebUploadVO dmsSearchVo = null;
		List<KeyValueVo> docKeyListVo = new ArrayList<KeyValueVo>();
		Map<String, String> genericMap = new HashMap<String, String>(0);

		dmsSearchVo = dmsHelper.populateData(resourceRequest, paramterType,
				systemLine);

		if (dmsSearchVo != null) {
			dmsSearchVo = dmsService.populateDetails(paramterType, dmsSearchVo);
		}

		if (dmsSearchVo != null ? true : false) {
			if (paramterType.equals(DMSGType.DMS_BOOKINGNO_SERVICETYPES)) {
				genericMap = dmsSearchVo.getServiceTypes();
			} else if (paramterType.equals(DMSGType.DMS_BOOKINGNO_SERVICELINES)) {
				genericMap = dmsSearchVo.getServiceLines();
			} else if (paramterType.equals(DMSGType.DMS_BOOKINGNO_CARGOTYPES)) {
				genericMap = dmsSearchVo.getCargoTypes();
			} else if (paramterType.equals(DMSGType.DMS_BOOKINGNO_PODS)) {
				genericMap = dmsSearchVo.getPods();
			} else if (paramterType.equals(DMSGType.DMS_BOOKINGNO_BUSINESSLINE)) {

			}
			docKeyListVo = dmsHelper.getDocTypeListVO(genericMap);
			logger.debug(" docTypeListVo Key Value:: " + docKeyListVo);

			KeyValueVo kvo = new KeyValueVo();
			kvo.setKey("");
			kvo.setValue("--Select a value--");
			docKeyListVo.add(0, kvo);
			searchJsonList = dmsHelper.convertToJson(docKeyListVo);
		}
		logger.debug("DMSPortlet :: searchDetails : searchJsonList : "
				+ searchJsonList);
		return searchJsonList;
	}

	private String populateBKCustomerNBussLine(ResourceRequest resourceRequest,
			DMSGType paramterType, String systemLine)
			throws DataAccessException {
		DMSWebUploadVO dmsSearchVo = null;
		dmsSearchVo = dmsHelper.populateData(resourceRequest, paramterType,
				systemLine);

		if (dmsSearchVo != null) {
			dmsSearchVo = dmsService.populateDetails(paramterType, dmsSearchVo);
		}
		return new String("{\"bookingCustomer\":\""
				+ dmsSearchVo.getBookingCustomer() + "\"" + ","
				+ "\"businessLine\":" + "\"" + dmsSearchVo.getBusinessLine()
				+ "\"}");
	}

	private String stringNullChecker(String str) {
		return str != null ? !str.trim().isEmpty() ? str.trim() : "" : "";
	}

	private boolean nullEmptyCheckerFlag(String str) {
		return str != null ? str.trim().isEmpty() ? false : true : false;
	}

	/**
	 * Get SessionBean.
	 * 
	 * @param request
	 *            PortletRequest
	 * @return DMSDocUploadWebPortletSessionBean
	 */
	private static DMSDocUploadWebPortletSessionBean getSessionBean(
			PortletRequest request) {
		PortletSession session = request.getPortletSession();
		if (session == null)
			return null;
		DMSDocUploadWebPortletSessionBean sessionBean = (DMSDocUploadWebPortletSessionBean) session
				.getAttribute(SESSION_BEAN);
		if (sessionBean == null) {
			sessionBean = new DMSDocUploadWebPortletSessionBean();
			session.setAttribute(SESSION_BEAN, sessionBean);
		}
		return sessionBean;
	}

	/**
	 * Returns JSP file path.
	 * 
	 * @param request
	 *            Render request
	 * @param jspFile
	 *            JSP file name
	 * @return JSP file path
	 */
	private static String getJspFilePath(RenderRequest request, String jspFile) {
		String markup = request.getProperty("wps.markup");
		if (markup == null)
			markup = getMarkup(request.getResponseContentType());
		return JSP_FOLDER + markup + "/" + jspFile + "."
				+ getJspExtension(markup);
	}

	/**
	 * Convert MIME type to markup name.
	 * 
	 * @param contentType
	 *            MIME type
	 * @return Markup name
	 */
	private static String getMarkup(String contentType) {
		if ("text/vnd.wap.wml".equals(contentType))
			return "wml";
		else
			return "html";
	}

	/**
	 * Returns the file extension for the JSP file
	 * 
	 * @param markupName
	 *            Markup name
	 * @return JSP extension
	 */
	private static String getJspExtension(String markupName) {
		return "jsp";
	}

}
