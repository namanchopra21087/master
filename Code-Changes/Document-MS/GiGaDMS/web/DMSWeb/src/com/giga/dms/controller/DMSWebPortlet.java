package com.giga.dms.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import com.giga.base.LoggerUtility;
import com.giga.base.service.GigaServiceLocator;
import com.giga.dms.IDMSService;
import com.giga.dms.helper.DMSHelper;
import com.giga.dms.service.DMSGService;
import com.giga.dms.util.DMSGConstants;
import com.giga.dms.util.DMSGType;
import com.giga.dms.util.DMSMetaDataKeys;
import com.giga.dms.util.GiGaModuleType;
import com.giga.dms.vo.DMSDocumentVO;
import com.giga.dms.vo.DMSResultObject;
import com.giga.dms.vo.DMSSearchConentVO;
import com.giga.dms.vo.DMSSearchVO;
import com.giga.dms.vo.KeyValueVo;
import com.giga.exception.DataAccessException;
import com.giga.master.IMasterServices;
import com.giga.master.service.MasterService;


/**
 * A sample portlet based on GenericPortlet
 */
public class DMSWebPortlet extends BaseDMSWebPortlet {  
	
	DMSGService dmsGService = null;
	DMSHelper dmsHelper = null;
    private static final String SEARCH_DMS_CONTENT_VO_SESSION_KEY="SEARCH_DMS_CONTENT_VO_SESSION_KEY";
    private static final String DELETED_ID="DELETED_ID";
    private static Logger logger = LoggerUtility.getInstance().getLogger(DMSWebPortlet.class);
    @Override
	public void initialize() {
		System.out.print("Usage :: DMS :: DMSWebPortlet :: initialize().......START");
		try {
			// masterDynaCache=DynaCache.getInstanceForDynaCache();
			dmsHelper = DMSHelper.getInstance();
			dmsGService = (DMSGService) GigaServiceLocator.getService(IDMSService.businessLayer.DMSG_SERVICE);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		System.out.print("Usage :: DMS :: DMSWebPortlet :: initialize().......END");
	}

	
	/**
	 * Serve up the <code>view</code> mode.
	 * 
	 * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest,
	 *      javax.portlet.RenderResponse)
	 */
	@SuppressWarnings("unused")
	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		// Set the MIME type for the render response
		response.setContentType(request.getResponseContentType());
		String profileModuleName = null;
		try {
			setUserProfileInfoIntoSession(getCurrentPortletSession(request));
			profileModuleName = stringNullChecker((String) getSessionValues(request, USER_PROFILE_MODULE_NAME));
			setSessionValues(request, DMSHelper.SYSTEM_LINE, profileModuleName);
			request.setAttribute(DMSHelper.SYSTEM_LINE, profileModuleName);
			request.setAttribute(USER_ROLE, getSessionValues(request,USER_ROLE)); 
			request.setAttribute(DMS_APPROVER, getSessionValues(request,DMS_APPROVER));
			//VIEW_JSP = getRequestedJSPViewPage(request, response);
			logger.debug("doView() ::: VIEW_JSP page ::::" + VIEW_JSP);
			DMSHelper.LOGGED_USER = (String) getSessionValues(request,USER_NAME);
			if (VIEW_JSP.equals(SEARCH_DOCUMENT_VIEW_JSP)) {
				logger.debug("doView():: " + VIEW_JSP+ " block... process...");
				try {
					if (profileModuleName != null) {
						Map<String, String> documentLineMap = dmsGService.loadDocumentLine(profileModuleName.trim());// code and description
						 logger.debug("Usage :: DMS :: DMSWebPortlet :: documentLineMap :: "+documentLineMap);
						if (documentLineMap != null ? !documentLineMap.isEmpty() ? true : false : false) {
							request.setAttribute("documentLineList",DMSHelper.sortMapByValue(documentLineMap));
							// dmsHelper.convertToJson(documentLineMap)
						}
						MasterService masterService = (MasterService) GigaServiceLocator.getService(IMasterServices.businessLayer.MASTER_SERVICE);
						if (masterService != null) {
							Map<String, String> businessLineMap = masterService.loadMasterSetupDetail("BUSINESS LINE","setupDescription");
							logger.debug("BusinessLine Load :: businessLineMap : "+ businessLineMap);
							if (businessLineMap != null ? !businessLineMap.isEmpty() ? true : false : false) {
								request.setAttribute("businessLineList",businessLineMap);
							} else {
								logger.debug("BusinessLine fetching falues are NULL..................");
							}
						} else {
							logger.debug("masterService :: creation failed.. So,BusinessLine fetching failed......>>> ");
						}
					} else {
						logger.debug("SEARCH_DOCUMENT_VIEW_JSP :: profileModuleName :: is NULL (so documentLineMap generation failed..) ");
					}
				} catch (Exception de) {
					//de.printStackTrace();
					logger.debug("DataAccessException Loading failed to DocuemtnLine");
					throw new Exception("DATA_LOADING_FAILED",de);
					
				}
			}			
			
		} catch (Exception e) {
			String msgEx=e.getMessage();
			request.setAttribute(msgEx, msgEx);
		    logger.error("Exception in DoView... MESSSAGE : "+msgEx);
			logger.debug("Exception in DoView... USER_PROFILE...",e);
		}
		
		PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(
				getJspFilePath(request, VIEW_JSP));
		rd.include(request, response);
	}

	public void serveResource(ResourceRequest resourceRequest,ResourceResponse resourceResponse) throws PortletException,
			IOException {
		
		logger.debug("Usage :: DMS :: DMSWebPortlet :: serveResource AjaxCall: START :\n Request_Resource ID : "+ resourceRequest.getResourceID());
		PortletSession session = null;
		String jsonString = "";
		Boolean jsonContentCheckerflag = true;
		String systemLine = "";
		String userRole="";
		String userName="";
		Map<DMSMetaDataKeys,String> metadataObj = new HashMap<DMSMetaDataKeys,String>();
		try {
			session = resourceRequest.getPortletSession();
			if (session == null) {
				logger.debug("Usage :: DMS :: DMSWebPortlet ::ServeResourece():: session is null");
			} else {
				systemLine = (String) session.getAttribute(DMSHelper.SYSTEM_LINE);
				userRole= (String) session.getAttribute(USER_ROLE);
				userName= (String) session.getAttribute(USER_NAME);
			}
			
			logger.debug("SystemLine :: " + systemLine);

			if (DMSHelper.DMS_SEARCH_DCUMENTS.equals(resourceRequest.getResourceID())) {
				logger.debug("DMS_SEARCH_DCUMENTS :process:paramvalue : ");
				metadataObj = getMetaDetaDetails(resourceRequest);
				metadataObj.put(DMSMetaDataKeys.SYSTEM_LINE,systemLine);
				jsonString = searchDocuments(resourceRequest,metadataObj,userRole);
				
			}/* else if (DMSHelper.DMS_VIEW_DOCUMENTS.equals(resourceRequest.getResourceID())) {
				logger.debug("DMS_VIEW_DOCUMENTS :process:paramvalue : ");
				// metadataObj=getMetaDetaDetails(resourceRequest);
				
				String documentID = resourceRequest.getParameter(DMSHelper.DMS_DCUMENT_ID);
				logger.debug("DMS_VIEW_DOCUMENTS:::::::::>>>>documentID :  "+ documentID);
				metadataObj.put(DMSMetaDataKeys.DMS_ID, documentID.trim());
				metadataObj.put(DMSMetaDataKeys.SYSTEM_LINE,systemLine);
				jsonString = viewDocuments(resourceRequest,resourceResponse,metadataObj,userRole);
				return;
			} */else if (DMSHelper.DMS_DOWNLOAD_DOCUMENTS.equals(resourceRequest.getResourceID())) {
				logger.debug("DMS_DOWNLOAD_DOCUMENTS :process: paramvalue : ");
				// metadataObj=getMetaDetaDetails(resourceRequest);
				//metadataObj = new Metadata();
				String documentID = resourceRequest.getParameter(DMSHelper.DMS_DCUMENT_ID);
				logger.debug("DMS_DOWNLOAD_DOCUMENTS:::::::::>>>>documentID : "+ documentID);

				
				metadataObj.put(DMSMetaDataKeys.DMS_ID, documentID.trim());
				metadataObj.put(DMSMetaDataKeys.SYSTEM_LINE,systemLine);
				jsonString = downloadDocuments(resourceRequest,resourceResponse, metadataObj,userRole);
				return;
			} else if (DMSHelper.DMS_DELETE_DOCUMENTS.equals(resourceRequest.getResourceID())) {
				logger.debug("DMS_DELETE_DOCUMENTS :: process : ");
				metadataObj=getMetaDetaDetails(resourceRequest);
				String documentID = resourceRequest.getParameter(DMSHelper.DMS_DCUMENT_ID);
				logger.debug("DMS_VIEW_DOCUMENTS:::::::::>>>>documentID :  "+ documentID);
				metadataObj.put(DMSMetaDataKeys.DMS_ID, documentID.trim());
				metadataObj.put(DMSMetaDataKeys.SYSTEM_LINE,systemLine);
				metadataObj.put(DMSMetaDataKeys.DELETE_BY,userName);
				jsonString = deleteDocuments(resourceRequest, resourceResponse,metadataObj,userRole);
			} else if (DMSHelper.LOAD_DOC_TYPE.equals(resourceRequest
					.getResourceID())) {
				logger.debug("LOAD_DOC_TYPE :: Document Type process...");
				jsonString = loadDocTypeMap(resourceRequest);
			} else if (DMSHelper.SEARCH_BOOKING.equals(resourceRequest
					.getResourceID())) {
				jsonString = searchDetails(resourceRequest,
						DMSGType.SEARCH_BOOKING, systemLine);
			} else if (DMSHelper.SEARCH_JOB.equals(resourceRequest
					.getResourceID())) {
				jsonString = searchDetails(resourceRequest,
						DMSGType.SEARCH_JOB, systemLine);
			} else if (DMSHelper.SEARCH_SI.equals(resourceRequest
					.getResourceID())) {
				jsonString = searchDetails(resourceRequest, DMSGType.SEARCH_SI,
						systemLine);
			} else if (DMSHelper.SEARCH_CLAIM.equals(resourceRequest
					.getResourceID())) {
				jsonString = searchDetails(resourceRequest,
						DMSGType.SEARCH_CLAIM, systemLine);
			} else if (DMSHelper.SEARCH_SHIPMENT.equals(resourceRequest
					.getResourceID())) {
				jsonString = searchDetails(resourceRequest,
						DMSGType.SEARCH_SHIPMENT, systemLine);
			} else if (DMSHelper.SEARCH_INVOICE.equals(resourceRequest.getResourceID())) {
				jsonString = searchDetails(resourceRequest,DMSGType.SEARCH_INVOICE, systemLine);
			} else if (DMSHelper.SEARCH_QUOTATION.equals(resourceRequest
					.getResourceID())) {
				jsonString = searchDetails(resourceRequest,
						DMSGType.SEARCH_QUOTATION, systemLine);
			} else if (DMSHelper.SEARCH_PO.equals(resourceRequest
					.getResourceID())) {
				jsonString = searchDetails(resourceRequest, DMSGType.SEARCH_PO,
						systemLine);
			} else if (DMSHelper.SEARCH_DO.equals(resourceRequest
					.getResourceID())) {
				jsonString = searchDetails(resourceRequest, DMSGType.SEARCH_DO,
						systemLine);
			} else if (DMSHelper.SEARCH_LOAD.equals(resourceRequest
					.getResourceID())) {
				jsonString = searchDetails(resourceRequest,
						DMSGType.SEARCH_LOAD, systemLine);
			} else if (DMSHelper.SEARCH_BOOKING_CUSTOMER.equals(resourceRequest
					.getResourceID())) {
				jsonString = searchDetails(resourceRequest,
						DMSGType.SEARCH_BOOKING_CUSTOMER, systemLine);
			}else if (DMSHelper.SEARCH_CREDITNOTE.equals(resourceRequest
					.getResourceID())) {
				jsonString = searchDetails(resourceRequest,DMSGType.SEARCH_CREDITNOTE, systemLine);
			}else if (DMSHelper.SEARCH_DEBITNOTE.equals(resourceRequest
					.getResourceID())) {
				jsonString = searchDetails(resourceRequest,DMSGType.SEARCH_DEBITNOTE, systemLine);
			}else if (DMSHelper.SEARCH_DELIVERYNOTE.equals(resourceRequest
					.getResourceID())) {
				jsonString = searchDetails(resourceRequest,DMSGType.SEARCH_DELIVERYNOTE, systemLine);
			}else if (DMSHelper.SEARCH_PULLINGORDER.equals(resourceRequest
					.getResourceID())) {
				jsonString = searchDetails(resourceRequest,DMSGType.SEARCH_PULLINGORDER, systemLine);
			}
		} catch (DataAccessException e) {
			jsonString=e.getMessage();
			logger.debug("Usage :: DMS :: DMSWebPortlet :: DataAccess Exception Message : " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			jsonString = e.getMessage();
			logger.debug("Usage :: DMS :: DMSWebPortlet :: ServeResource : AjaxCall : Exception"+ e);
		}
		
		logger.debug("Usage :: DMS :: DMSWebPortlet :: ServeResource : AjaxCall : JSON Object ::: "+ jsonString);
		
		if(jsonString.equals(DMSHelper.NO_DOCUMENT_FOUND_ERROR)||
				jsonString.equals(DMSHelper.DELETE_DOCUMENT_EXCEPTION)||
				jsonString.equals(DMSHelper.VIEW_DOCUMENT_EXCEPTION)||
				jsonString.equals(DMSHelper.DOWNLOAD_DOCUMENT_EXCEPTION)||
				jsonString.equals(DMSHelper.DOWNLOAD_DOCUMENT_EXCEPTION)){
			jsonContentCheckerflag=false;
		}
		
		if (jsonContentCheckerflag) {
			logger.debug("Usage :: DMS :: DMSWebPortlet : ServeResource : ContentType : JSON");
			resourceResponse.setContentType("application/json");
			resourceResponse.getWriter().write(jsonString);
		} else {
			logger.debug("Usage :: DMS :: DMSWebPortlet : ServeResource : ContentType : HTML");
			resourceResponse.setContentType("text/html");
			resourceResponse.getWriter().write("Exception " + jsonString);
		}
	}

	/**
	 * This method is used to delete the docments form DMS based on the selected
	 * documents
	 * 
	 * @param resourceRequest
	 * @param resourceResponse
	 * @param metadataMap
	 * @return
	 * @throws Exception
	 */
	private String deleteDocuments(ResourceRequest resourceRequest,ResourceResponse resourceResponse, Map<DMSMetaDataKeys,String> metadataMap,String userRole)
			throws Exception {
		
		logger.debug("Usage :: DMS :: DMSWebPortlet ::deleteDocuments :START..");
		String deleteMessage = "";
		DMSResultObject deleteResults =new DMSResultObject();
		logger.debug("deleteDocuments : " + metadataMap);
		try {
			 setSessionValues(resourceRequest, DELETED_ID, metadataMap.get(DMSMetaDataKeys.DMS_ID));
			 deleteResults = dmsGService.deleteDMS(metadataMap, userRole, GiGaModuleType.DMS);
			 logger.debug("values of deleteDocuments : searchDocumentOutputVoList : "+ deleteResults.isResultFlag()+"-- message  : "+deleteResults.getResultMessage());
			if(deleteResults.isResultFlag()){
				String deletedID=(String)getSessionValues(resourceRequest,DELETED_ID);
				List<DMSSearchConentVO> searchVoList=(List<DMSSearchConentVO>)getSessionValues(resourceRequest,SEARCH_DMS_CONTENT_VO_SESSION_KEY);
				
				  if(deletedID!=null &&searchVoList!=null ){ 
					  List<DMSSearchConentVO> dmsSCVOList=new  ArrayList<DMSSearchConentVO>();
					   logger.debug("DMSWebPortlet :: Before Deleted from List: deletedID :: "+deletedID+"---list Size :: "+searchVoList.size());
					   for(DMSSearchConentVO dmsSVo:searchVoList){
						   logger.debug("List of values are :: "+dmsSVo.getDmsId());
						     if(dmsSVo.getDmsId().equals(deletedID)){
							   logger.debug("RemoveInstance :: "+dmsSVo.getDmsId()+"--deletedID : "+deletedID);
							 }else{
								  dmsSCVOList.add(dmsSVo);
							 }
					   }
					   
					   logger.debug("DMSWebPortlet :: After Deleted from List ---list Size :: "+dmsSCVOList.size());
					   setSessionValues(resourceRequest, SEARCH_DMS_CONTENT_VO_SESSION_KEY, null);
					   setSessionValues(resourceRequest, SEARCH_DMS_CONTENT_VO_SESSION_KEY, dmsSCVOList);
					   deleteMessage = dmsHelper.convertToJson(dmsSCVOList);
				  }
				  
				//logger.debug("DMSWebPortlet :deleteDocuments(): Data Deletion complete and Results is After Delete :"+deleteResults.getSearchDocmentResult());
				//deleteMessage = dmsHelper.convertToJson(deleteResults.getSearchDocmentResult());
			  }else {
				logger.debug("No document found for deleteDocuments:::::::::::");
				deleteMessage = DMSHelper.NO_DOCUMENT_FOUND_ERROR;
			}

		} catch (Exception e) {
			deleteMessage = DMSHelper.DELETE_DOCUMENT_EXCEPTION;
			logger.error("Usage :: DMS :: DMSWebPortlet : Exception :: ", e);
			throw new Exception(deleteMessage, e);
		} finally {
			logger.debug("Usage :: DMS :: DMSWebPortlet ::deleteDocuments :END.. : deleteMessage :"+ deleteMessage);
		}
		return deleteMessage;
	}

	/**
	 * This method is used to download the selected documents
	 * 
	 * @param resourceRequest
	 * @param resourceResponse
	 * @param metadataMap
	 * @return
	 * @throws Exception
	 */
	private String downloadDocuments(ResourceRequest resourceRequest,ResourceResponse resourceResponse, Map<DMSMetaDataKeys,String> metadataMap,String userRole)throws Exception {
		
		logger.debug("Usage :: DMS :: DMSWebPortlet ::downloadDocuments :START..");
		String downloadMessage = "";
		String mimeType = "";
		String fileName = "";
		OutputStream outputStream = null;
		logger.debug("downloadDocuments : " + metadataMap);
		List<DMSDocumentVO> dmsDocVolist=new ArrayList<DMSDocumentVO>();
			DMSDocumentVO dmsDocumentVo=new DMSDocumentVO();
			dmsDocumentVo.setMetaDataValue(metadataMap);
			dmsDocVolist.add(dmsDocumentVo);
		try {
			DMSResultObject  dmsResultVOList=dmsGService.downLoadDMS(dmsDocVolist,userRole,GiGaModuleType.DMS);
			if(dmsResultVOList!=null){
				 logger.debug("Usage :: DMS :: DMSWebPortlet : Search Content :: SearchFlag :  "+dmsResultVOList.isResultFlag()+"--Search Message : "+dmsResultVOList.getResultMessage());
				 List<DMSDocumentVO>  voList=(List<DMSDocumentVO>)dmsResultVOList.getDownloadDocumentResult();
				 if(voList!=null){
					 mimeType=voList.get(0).getMetaDataValue().get(DMSMetaDataKeys.MIME_TYPE);
					 fileName=voList.get(0).getMetaDataValue().get(DMSMetaDataKeys.DOCUMENT_TITLE);
				    byte[] byteVal=voList.get(0).getContentStream();
					 logger.debug("DMSController :: MimeType : "+mimeType +"---DocumentTitle  ::"+fileName +"-- file byteSize :: "+byteVal);
					resourceResponse.setContentType(DMSHelper.getExtenstionMatch(mimeType));
					resourceResponse.setProperty("Content-Disposition","attachment; filename=" + fileName);
					outputStream = resourceResponse.getPortletOutputStream();
					outputStream.write(byteVal);
				 }else{
					 logger.debug("No document found for download:::::::::::");
					 downloadMessage = DMSHelper.NO_DOCUMENT_FOUND_ERROR;
				 }
			}
			else {
				logger.debug("No document found for download:::::::::::");
			// dmsSearchContentVoList
				downloadMessage = DMSHelper.NO_DOCUMENT_FOUND_ERROR;
		    }

		} catch (Exception e) {
			if (outputStream != null)
				try {
					outputStream.close();
				} catch (IOException e1) {
					

					e1.printStackTrace();
				}

			downloadMessage = DMSHelper.DOWNLOAD_DOCUMENT_EXCEPTION;
			logger.debug("Exception :: " + e);
			throw new Exception(downloadMessage, e);
		} finally {
			if (outputStream != null)
				try {
					outputStream.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			System.out
					.println("Usage :: DMS :: DMSWebPortlet ::deleteDocuments :END.. : downloadMessage :"
							+ downloadMessage);
		}
		return downloadMessage;
	}

	/***
	 * This method is used to view the Selected Documents
	 * 
	 * @param resourceRequest
	 * @param resourceResponse
	 * @param metadataMap
	 * @return
	 * @throws Exception
	 */
	/*private String viewDocuments(ResourceRequest resourceRequest,ResourceResponse resourceResponse,Map<DMSMetaDataKeys,String> metadataMap,String userRole)throws Exception {
		
		logger.debug("Usage :: DMS :: DMSWebPortlet ::viewDocuments :START..");
		String viewMessage = "";
		String mimeType = "";
		String fileName = "";
		DMSResultObject viewResults=null;
		OutputStream outputStream = null;
		DMSDocumentVO dmsDocumentVo=new DMSDocumentVO();
		logger.debug("viewDocuments : " + metadataMap);
		try {
			viewResults = dmsGService.viewDocumentDMS(metadataMap,userRole,GiGaModuleType.DMS);
			if(viewResults.isResultFlag()){
				 logger.debug("Usage :: DMS :: DMSWebPortlet : Search Content :: SearchFlag :  "+viewResults.isResultFlag()+"--Search Message : "+viewResults.getResultMessage());
				 dmsDocumentVo=(DMSDocumentVO)viewResults.getViewDocumentResult();
				 if(dmsDocumentVo!=null){
					 Map<DMSMetaDataKeys, String> viewMetadatMapResult=dmsDocumentVo.getMetaDataValue();
					 mimeType=viewMetadatMapResult.get(DMSMetaDataKeys.MIME_TYPE);
					 fileName=viewMetadatMapResult.get(DMSMetaDataKeys.DOCUMENT_TITLE);
					 byte[] byteVal=dmsDocumentVo.getContentStream();
					 logger.debug("DMSController :: MimeType : "+mimeType +"---DocumentTitle  ::"+fileName +"-- file byteSize :: "+byteVal);
				    resourceResponse.setContentType(DMSHelper.getExtenstionMatch(mimeType));
					resourceResponse.setProperty("Content-Disposition","inline; filename=" + fileName);
					outputStream = resourceResponse.getPortletOutputStream();
					outputStream.write(byteVal);
				 }else{
					 logger.debug("No document found for viewDocuments:::::::::::");
					 viewMessage = DMSHelper.NO_DOCUMENT_FOUND_ERROR;
				 }
			}
			else {
				logger.debug("No document found for searchDocuments:::::::::::");
			// dmsSearchContentVoList
				viewMessage = DMSHelper.NO_DOCUMENT_FOUND_ERROR;
		    }
		} catch (Exception e) {
			if (outputStream != null)
				try {
					outputStream.close();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
			viewMessage = DMSHelper.VIEW_DOCUMENT_EXCEPTION;
			logger.debug("Exception :: " + e);
			throw new Exception(viewMessage, e);
		} finally {
			if (outputStream != null)
				try {
					outputStream.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				logger.debug("Usage :: DMS :: DMSWebPortlet ::deleteDocuments :END.. : viewMessage :"+ viewMessage);
		}
		return viewMessage;
	}*/

	/***
	 * This method is used to Search the Documents based on search parameter
	 * 
	 * @param resourceRequest
	 * @param resourceResponse
	 * @param metadataMap
	 * @return
	 * @throws Exception
	 */

	private String searchDocuments(ResourceRequest resourceRequest,Map<DMSMetaDataKeys,String>metadataObj,String userRole)
			throws Exception {
		logger.debug("Usage :: DMS :: DMSWebPortlet ::searchDocuments :START..");
		String searchResults = "";
		List<DMSSearchConentVO> dmsSearchContentVoList = new ArrayList<DMSSearchConentVO>();
		Map<DMSMetaDataKeys,String> metadataMapObject = metadataObj;
		try {
			setSessionValues(resourceRequest, SEARCH_DMS_CONTENT_VO_SESSION_KEY, null);
			DMSResultObject dmsResultMap=dmsGService.searchDMS(metadataMapObject,userRole,GiGaModuleType.DMS);
			String sysLine = metadataMapObject.get(DMSMetaDataKeys.SYSTEM_LINE);
			logger.debug("values of searchContent : searchDocumentOutputVoList : "+ dmsResultMap);
				if(dmsResultMap!=null && dmsResultMap.isResultFlag()){
					 logger.debug("Usage :: DMS :: DMSWebPortlet : Search Content :: SearchFlag :  "+dmsResultMap.isResultFlag()+"--Search Message : "+dmsResultMap.getResultMessage());
					 dmsSearchContentVoList=(List<DMSSearchConentVO>)dmsResultMap.getSearchDocmentResult();
					 
					 //Filter code added on 24 Aug 2015 for Invoice and Quotation
					 if(!"TMS".equals(sysLine) && !"YMS".equals(sysLine) && !isQuotationInvoiceMapped()){
						 for (Iterator<DMSSearchConentVO> iter = dmsSearchContentVoList.listIterator(); iter.hasNext(); ) {
							 DMSSearchConentVO searchEle = iter.next();
							 if( searchEle.getDocumentTitle().startsWith("INVOICE_") 
									 || searchEle.getDocumentTitle().startsWith("QUOTATION_")){
								 iter.remove();
							 }
						 }
					 }
					 setSessionValues(resourceRequest, SEARCH_DMS_CONTENT_VO_SESSION_KEY, dmsSearchContentVoList);
					 //sessionSearch.setAttribute(SEARCH_DMS_CONTENT_VO_SESSION_KEY, dmsSearchContentVoList);
					 searchResults = dmsHelper.convertToJson(dmsSearchContentVoList);
				}
				else {
				 logger.debug("No document found for searchDocuments:::::::::::");
				 searchResults = DMSHelper.NO_DOCUMENT_FOUND_ERROR;
				}
		} catch (Exception e) {
			logger.error("DMSPortlet::searchDMSContent : Exception in search content ", e);
			searchResults = DMSHelper.SEARCH_DOCUMENT_EXCEPTION;
			throw new Exception(searchResults, e);
		} finally {
			logger.debug("values of searchContent : jSon values : "+ searchResults);
		}
		return searchResults;
	}

	private String loadDocTypeMap(ResourceRequest resRequest)
			throws DataAccessException {
		
		logger.debug("DMSPortlet::loadDocTypeMap : START.............");
		String jsonloadDocTypeMap = "";
		PortletSession ses = resRequest.getPortletSession();
		String systemLineS = (String) ses.getAttribute(DMSHelper.SYSTEM_LINE);
		String companyCode = (String) ses.getAttribute(COMPANY_CODE);
		String branchCode = (String) ses.getAttribute(BRANCH_CODE);
		// DOC_LINE_VAL,DOC_LINE_TEXT
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
		Map<String, String> listOfDocTypeMap = dmsGService
				.loadDocumentType(docTypeDetailsMap);
		if (listOfDocTypeMap != null ? !listOfDocTypeMap.isEmpty() ? true : false : false) {
			List<KeyValueVo> docTypeListVo = new ArrayList<KeyValueVo>();
			docTypeListVo = dmsHelper.getDocTypeListVO(listOfDocTypeMap);
			KeyValueVo kvo = new KeyValueVo();
			kvo.setKey("");
			kvo.setValue("--Select a value--");
			docTypeListVo.add(0, kvo);

			jsonloadDocTypeMap = dmsHelper.convertToJson(docTypeListVo);
		}
		System.out
				.println("DMSPortlet::loadDocTypeMap : END...........jsonloadDocTypeMap : "
						+ jsonloadDocTypeMap);
		return jsonloadDocTypeMap;
	}

	
	private String searchDetails(ResourceRequest resourceRequest,
			DMSGType paramterType, String systemLine)
			throws DataAccessException {
		
		String searchJsonList = "";
		List<DMSSearchVO> dmsSearchVolist = null;
		 String branchCode=(String)getSessionValues(resourceRequest, BRANCH_CODE);
		 String companyCode=(String)getSessionValues(resourceRequest, COMPANY_CODE);
		 DMSSearchVO dmsSearchVO = dmsHelper.createSearchVO(resourceRequest,paramterType, systemLine,branchCode,companyCode);
		if (dmsSearchVO != null) {
			dmsSearchVolist = dmsGService.getSearchDetails(paramterType,dmsSearchVO);
		}
		if (dmsSearchVolist != null ? true : false) {
			searchJsonList = dmsHelper.convertToJson(dmsSearchVolist);
		}
		logger.debug("DMSPortlet :: searchDetails : searchJsonList : "
				+ searchJsonList);
		return searchJsonList;
	}

	/**
	 * This method is uesed for generate Metadata map object based on the
	 * request parameter values
	 * 
	 * @param resourceRequest
	 * @return
	 */
	private Map<DMSMetaDataKeys,String> getMetaDetaDetails(ResourceRequest resourceRequest) {
		logger.debug("getMetaDetaDetails :process: ........");
		Map<DMSMetaDataKeys,String> metadataObj = new HashMap<DMSMetaDataKeys,String>();
		String systemLine = (String) resourceRequest.getPortletSession().getAttribute(DMSHelper.SYSTEM_LINE);
		String documentID = resourceRequest
				.getParameter(DMSHelper.DMS_DCUMENT_ID);
		String documentLine = resourceRequest
				.getParameter(DMSHelper.DMS_DOCUMENT_LINE);
		String documentType = resourceRequest
				.getParameter(DMSHelper.DMS_DOCUMENT_TYPE);

		String documentTitle = resourceRequest
				.getParameter(DMSHelper.DMS_DOCUMENT_TITLE);
		String bookingNo = resourceRequest
				.getParameter(DMSHelper.DMS_BOOKING_NO);
		String jobNo = resourceRequest.getParameter(DMSHelper.DMS_JOB_NO);
		String siNo = resourceRequest.getParameter(DMSHelper.DMS_SI_NO);
		String claimNo = resourceRequest.getParameter(DMSHelper.DMS_CLAIM_NO);
		String shipmentNo = resourceRequest
				.getParameter(DMSHelper.DMS_SHIPMENT_NO);
		String invoiceNo = resourceRequest
				.getParameter(DMSHelper.DMS_INVOICE_NO);
		String quotationNo = resourceRequest
				.getParameter(DMSHelper.DMS_QUOTATION_NO);
		String doNo = resourceRequest.getParameter(DMSHelper.DMS_DO_NO);
		String poNo = resourceRequest.getParameter(DMSHelper.DMS_PO_NO);
		String loadNo = resourceRequest.getParameter(DMSHelper.DMS_LOAD_NO);
		String bookingCustomer = resourceRequest
				.getParameter(DMSHelper.DMS_BOOKING_CUSTOMER);
		String creditNoteNo = resourceRequest
				.getParameter(DMSHelper.DMS_CN_NO);
		String debitNoteNo = resourceRequest
				.getParameter(DMSHelper.DMS_DN_NO);
		
		String uploadFromDate = resourceRequest.getParameter(DMSHelper.DMS_UPLOAD_FORM_DATE);
			   uploadFromDate=DMSHelper.toFormat(uploadFromDate);/// convert the dd/MM/yyy to MM/dd/yyy
		String uploadToDate = resourceRequest.getParameter(DMSHelper.DMS_UPLOAD_TO_DATE);
			   uploadToDate=DMSHelper.toFormat(uploadToDate);/// convert the dd/MM/yyy to MM/dd/yyy
		logger.debug("Usage :: DMS :: DMSWebPortlet :: getMetaDetaDetails :: [(systemLine : "
						+ systemLine
						+ "),(documentLine : "
						+ documentLine
						+ "),(documentType : "
						+ documentType
						+ "),(bookingNo : "
						+ bookingNo
						+ "),(jobNo : "
						+ jobNo
						+ "),(siNo : "
						+ siNo
						+ "),(claimNo : "
						+ claimNo
						+ "),(shipmentNo : "
						+ shipmentNo
						+ "),(invoiceNo : "
						+ invoiceNo
						+ "),(quotationNo : "
						+ quotationNo
						+ "),(doNo : "
						+ doNo
						+ "),(poNo : "
						+ poNo
						+ "),(loadNo : "
						+ loadNo
						+ "),(bookingCustomer : "
						+ bookingCustomer
						+ "),(creditNoteNo : "
								+ creditNoteNo
								+ 
								 "),(debitNoteNo : "
						+ debitNoteNo
						+ "),(uploadFromDate : "
						+ uploadFromDate
						+ "),(uploadToDate : "
						+ uploadToDate
						+ "]");

		if (nullEmptyCheckerFlag(documentTitle)) {
			metadataObj.put(DMSMetaDataKeys.DOCUMENT_TITLE, documentTitle.trim());
		}
		if (nullEmptyCheckerFlag(documentID)) {
			metadataObj.put(DMSMetaDataKeys.DMS_ID, documentID.trim());
		}
		if (nullEmptyCheckerFlag(systemLine)) {
			metadataObj.put(DMSMetaDataKeys.SYSTEM_LINE, systemLine.trim());
		}

		if (nullEmptyCheckerFlag(documentLine)) {
			metadataObj.put(DMSMetaDataKeys.DOCUMENT_LINE, documentLine.trim());
		}

		if (nullEmptyCheckerFlag(documentType)) {
			metadataObj.put(DMSMetaDataKeys.DOCUMENT_TYPE, documentType.trim());
		}

		if (nullEmptyCheckerFlag(bookingNo)) {
			metadataObj.put(DMSMetaDataKeys.BOOKING_NUMBER, bookingNo.trim());
		}

		if (nullEmptyCheckerFlag(jobNo)) {
			metadataObj.put(DMSMetaDataKeys.JOB_NUMBER, jobNo.trim());
		}

		if (nullEmptyCheckerFlag(siNo)) {
			metadataObj.put(DMSMetaDataKeys.SI_NUMBER, siNo.trim());
		}

		if (nullEmptyCheckerFlag(claimNo)) {
			metadataObj.put(DMSMetaDataKeys.CLAIM_NUMBER, claimNo.trim());
		}

		if (nullEmptyCheckerFlag(shipmentNo)) {
			metadataObj.put(DMSMetaDataKeys.SHIPMENT_NUMBER, shipmentNo.trim());
		}

		if (nullEmptyCheckerFlag(invoiceNo)) {
			metadataObj.put(DMSMetaDataKeys.INVOICE_NUMBER, invoiceNo.trim());
		}

		if (nullEmptyCheckerFlag(quotationNo)) {
			metadataObj.put(DMSMetaDataKeys.QUOTATION_NO, quotationNo.trim());
		}

		if (nullEmptyCheckerFlag(bookingCustomer)) {
			metadataObj.put(DMSMetaDataKeys.BOOKIN_CUSTOMER, bookingCustomer.trim());
		}
		if (nullEmptyCheckerFlag(doNo)) {
			metadataObj.put(DMSMetaDataKeys.DO_NO, doNo.trim());
		}
		if (nullEmptyCheckerFlag(poNo)) {
			metadataObj.put(DMSMetaDataKeys.PO_NO, poNo.trim());
		}
		if (nullEmptyCheckerFlag(loadNo)) {
			metadataObj.put(DMSMetaDataKeys.LOAD_NO, loadNo.trim());
		}
		if (nullEmptyCheckerFlag(creditNoteNo)) {
			metadataObj.put(DMSMetaDataKeys.CREDIT_NOTE_NO, creditNoteNo.trim());
		}
		if (nullEmptyCheckerFlag(debitNoteNo)) {
			metadataObj.put(DMSMetaDataKeys.DEBIT_NOTE_NO, debitNoteNo.trim());
		}
		if (nullEmptyCheckerFlag(uploadFromDate)) {
			metadataObj.put(DMSMetaDataKeys.UPLOAD_FROM_DATE, uploadFromDate.trim());
		}

		if (nullEmptyCheckerFlag(uploadToDate)) {
			metadataObj.put(DMSMetaDataKeys.UPLOAD_TO_DATE, uploadToDate.trim());
		}
		logger.debug("getMetaDetaDetails..processEND : MatadataObject: "+ metadataObj.toString());
		return metadataObj;
	}
	
  
	
   /**
    * This method is used to check and validate the string is NULL or empty and return the true or false
    *  true :  NOT NULL and NOT Empty
    *  false : NULL or empty
    * @param str  String
    * @return boolean 
    */
	private boolean nullEmptyCheckerFlag(String str) {
		return str != null ? str.trim().isEmpty() ? false : true : false;
	}
    
   /**
    * This method is used to check and validate the string is NULL or empty and return the values  or empty string
    *  values :  NOT NULL and NOT Empty
    *  empty string : NULL or empty
    * @param str  String
    * @return boolean 
    */
	private String stringNullChecker(String str) {
		return str != null ? !str.trim().isEmpty() ? str.trim() : "" : "";
	}
}
