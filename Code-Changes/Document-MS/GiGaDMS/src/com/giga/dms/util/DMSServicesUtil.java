package com.giga.dms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import org.apache.log4j.Logger;

import com.giga.base.LoggerUtility;
import com.giga.base.UserProfileAccessUtil;
import com.giga.base.service.GigaServiceLocator;
import com.giga.dms.IDMSService;
import com.giga.dms.dao.DMSGDao;
import com.giga.dms.service.DMSGServiceImpl;
import com.giga.dms.services.DMSServices;
import com.giga.dms.services.DocumentVO;
import com.giga.dms.services.GMGDMSServices;
import com.giga.dms.services.Metadata;
import com.giga.dms.services.ServiceException_Exception;
import com.giga.dms.vo.DMSDocumentVO;
import com.giga.dms.vo.DMSResultObject;
import com.giga.dms.vo.DMSSearchConentVO;

public class DMSServicesUtil {

	protected DMSServices web_services = null;
	GMGDMSServices gmgService = null;
	//private static String WS_URL = "";
	//private static String USERNAME = "";
	//private static String PASS = "";
	private static final String NO_RESULT_FOUND="NO_RESULT_FOUND";
	private static final String UPLOAD_SUCCESS="UPLOAD_SUCCESS";
	private static final String SERVICE_EXCEPTION="SERVICE_EXCEPTION";
	private static final String PROCESS_EXCEPTION="PROCESS_EXCEPTION";
	private static final String VIEW_SUCCESS="VIEW_SUCCESS";
	private static final String DOWNLOAD_SUCCESS="DOWNLOAD_SUCCESS";
	private static final String SEARCH_SUCCESS="SEARCH_SUCCESS";
	private static final String DELETE_SUCCESS="DELETE_SUCCESS";
	
	private static String DMS_ACC_USER_NAME="DMS_ACC_USER_NAME";
	private static String DMS_ACC_USER_PASS="DMS_ACC_USER_PASS";
	private static String DMS_ACC_URL="DMS_ACC_URL";
	private DMSGDao dmsDao=null;
	//private UserProfileAccessUtil gm=null;
	@SuppressWarnings("static-access")
	private static Logger logger = LoggerUtility.getInstance().getLogger(
			DMSServicesUtil.class);
	
	public DMSServicesUtil() {
		System.out
				.println("DMSServicesUtil : UploadDMS :: UploadDMS() Constructor : ObjectCreation.. Process");
		try {
			gmgService = new GMGDMSServices();
			web_services = gmgService.getDMSServicesImplPort();
			dmsDao = (DMSGDao) GigaServiceLocator.getService(IDMSService.daoLayer.DMSG_DAO);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			System.out.println("DMSServicesUtil : UploadDMS :: UploadDMS() Constructor : Exception msg"+ e.getMessage());
		}
	}

	/*private void setAuthenticationProperty(String userRole) throws Exception {
		Map<String, String> propertyMap = getPropertyValue();
		System.out.println("Logged UserRole :: UserRole ::"+userRole+"\n ::propertyMap ::" + propertyMap);
		try {
			if (propertyMap != null) {
				WS_URL = propertyMap.get("WEB_SERVICE_URL");
				USERNAME = propertyMap.get("USER_ROLE_AUTHOR");
				PASS = propertyMap.get("USER_PASS_AUTHOR");

				if (userRole != null && userRole.equalsIgnoreCase("APPROVER")) {
					USERNAME = propertyMap.get("USER_ROLE_APPROVER");
					PASS = propertyMap.get("USER_PASS_APPROVER");
				}
			} 
			System.out.println("setAuthenticationProperty :: UserName ::"+USERNAME+"\n ::user Pass ::" + PASS);
			System.out.println("setAuthenticationProperty :<<<<<<<ENCY >>>>>>: UserName ::"+AESencrp.encrypt(USERNAME)+"\n ::user Pass ::" + AESencrp.encrypt(PASS));
			java.util.Map<String, Object> req_ctx = ((BindingProvider) web_services)
					.getRequestContext();
			req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, WS_URL);
			java.util.Map<String, List<String>> headers = new HashMap<String, List<String>>();

			headers.put(BindingProvider.USERNAME_PROPERTY,
					Collections.singletonList(AESencrp.encrypt(USERNAME)));
			headers.put(BindingProvider.PASSWORD_PROPERTY,
					Collections.singletonList(AESencrp.encrypt(PASS)));
			req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("DMS Service Username and Password Properties missing...");
		}

	}*/

	private void setAuthenticationProperty(String userRole) throws Exception {
		
		Map<String, String> propertyMap = dmsDao.getDmsAccessDetials(userRole);
		System.out.println("Logged UserRole :: UserRole ::"+userRole+"\n ::propertyMap ::" + propertyMap);
		try {
			if (propertyMap != null ?propertyMap.isEmpty()?true:false:true) {
				System.out.println("DMSServiceUtil :setAuthenticationProperty() :: db data fetching failed.. please check into db property..");
				throw new Exception("DMS Service Username and Password Properties missing...");
				}
			//DMS_ACC_USER_NAME,DMS_ACC_USER_PASS,DMS_ACC_URL
			 String dmsUrl=propertyMap.get(DMS_ACC_URL);
			 String dmsUserName=propertyMap.get(DMS_ACC_USER_NAME);
			 String dmsPass=propertyMap.get(DMS_ACC_USER_PASS);
			System.out.println("setAuthenticationProperty :: : DMS Url :: "+dmsUrl+" ::: user Name : "+dmsUserName+"\n ::user Pass ::" + dmsPass);
			java.util.Map<String, Object> req_ctx = ((BindingProvider) web_services)
					.getRequestContext();
			req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, dmsUrl);
			java.util.Map<String, List<String>> headers = new HashMap<String, List<String>>();

			headers.put(BindingProvider.USERNAME_PROPERTY,
					Collections.singletonList(dmsUserName));
			headers.put(BindingProvider.PASSWORD_PROPERTY,
					Collections.singletonList(dmsPass));
			req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("DMS Service Username and Password Properties missing...");
		}

	}
	/**
	 * This method is used to convert FilePath to Byte ArrayList.. of DMSDocumentVO list
	 * @param documentListVO
	 * @return
	 */
	private List<DMSDocumentVO> readAndConvertFilePathToByteArrayDMSDocVOList(List<DMSDocumentVO> documentListVO){
		System.out.println("DMSServiceUtil::readAndConvertFilePathToByteArray :: ..");
		List<DMSDocumentVO> newBytedmsVOList=new ArrayList<DMSDocumentVO>();
		
		if(documentListVO!=null){
			try{
			for(DMSDocumentVO dmsDocV:documentListVO){
				if(dmsDocV.getContentStream()!=null){
					System.out.println("Byte Array Available..");
					newBytedmsVOList.add(dmsDocV);
				}else
				if(dmsDocV.getCompleteFileNamePath()!=null && !dmsDocV.getCompleteFileNamePath().isEmpty()){
					System.out.println("Reading from Path :: Available..");
					DMSDocumentVO dmsByteVo=new DMSDocumentVO();
					dmsByteVo.setMetaDataValue(dmsDocV.getMetaDataValue());
					byte[] fileByte=readFiles(dmsDocV.getCompleteFileNamePath());
					dmsByteVo.setContentStream(fileByte);
					newBytedmsVOList.add(dmsByteVo);
				}
			}
			}catch(Exception e){
				System.out.println("DMSServiceUtil::readAndConvertFilePathToByteArray :: .Exception....*************");
			}
		}else{
			System.out.println("DMSServiceUtil::readAndConvertFilePathToByteArray :: ..param documentListVO is Empty or NUll");
		}
		return newBytedmsVOList;
	}

	
	
	/**
	 * This Asynchronous method is responsible for uploading files in file net using Web
	 * Services
	 * 
	 * @param bytes
	 * @param mapObj
	 * @param userRole
	 * @return
	 */
	public DMSResultObject asynMultipleUploadByFilePath(List<DMSDocumentVO> documentListVO, String userRole) {
		System.out.println("DMSServicesUtil : UploadDMS :: uploadByFilePath() :Process..Start : MetaData Map Object :  "+ documentListVO);
		DMSResultObject uploadResult=new DMSResultObject();
				if(documentListVO!=null && documentListVO.size()>0){
					documentListVO=readAndConvertFilePathToByteArrayDMSDocVOList(documentListVO);
					uploadResult=multipleUpload(documentListVO, userRole);
				}else{
					uploadResult.setResultFlag(false);
					uploadResult.setResultMessage(PROCESS_EXCEPTION +" : "+" Upload failed due to, unable to read file from path");
				}
		System.out.println("DMSServicesUtil : UploadDMS :: uploadByFilePath() :Process..END :  "+ uploadResult.getResultMessage());
		return uploadResult;
	}
	
	/**
	 * This Asynchronous method is responsible for uploading files in file net using Web
	 * Services
	 * 
	 * @param bytes
	 * @param mapObj
	 * @param userRole
	 * @return
	 */
	public DMSResultObject asynUploadByFilePath(String filePath,List<DMSDocumentVO> documentListVO, String userRole) {
		System.out.println("DMSServicesUtil : UploadDMS :: uploadByFilePath() :Process..Start : MetaData Map Object :  "+ documentListVO);
		DMSResultObject uploadResult=new DMSResultObject();
		byte[] bytes=readFiles(filePath);
		if(bytes!=null){
			System.out.println("documentListVO size :: "+documentListVO.size());
			if(documentListVO.size()>0){
				DMSDocumentVO dvo=documentListVO.get(0);
				Map<DMSMetaDataKeys, String> mapObj=dvo.getMetaDataValue();
				uploadResult=upload(bytes,mapObj,userRole);
			}
		}else{
			uploadResult.setResultFlag(false);
			uploadResult.setResultMessage(PROCESS_EXCEPTION +" : "+" Upload failed due to, unable to read file from path");
		}
		System.out.println("DMSServicesUtil : UploadDMS :: uploadByFilePath() :Process..END :  "+ uploadResult.getResultMessage());
		return uploadResult;
	}
	
	/**
	 * This method is responsible for uploading files in file net using Web
	 * Services
	 * 
	 * @param bytes
	 * @param mapObj
	 * @param userRole
	 * @return
	 */
	public DMSResultObject uploadByFilePath(String filePath,Map<DMSMetaDataKeys, String> mapObj, String userRole) {
		System.out.println("DMSServicesUtil : UploadDMS :: uploadByFilePath() :Process..Start : MetaData Map Object :  "+ mapObj);
		DMSResultObject uploadResult=new DMSResultObject();
		byte[] bytes=readFiles(filePath);
		if(bytes!=null){
			uploadResult=upload(bytes,mapObj,userRole);
		}else{
			uploadResult.setResultFlag(false);
			uploadResult.setResultMessage(PROCESS_EXCEPTION +" : "+" Upload failed due to, unable to read file from path");
		}
		System.out.println("DMSServicesUtil : UploadDMS :: uploadByFilePath() :Process..END :  "+ uploadResult.getResultMessage());
		return uploadResult;
	}
	
	
	/**
	 * This method is responsible for uploading files in file net using Web
	 * Services
	 * 
	 * @param bytes
	 * @param mapObj
	 * @param userRole
	 * @return
	 */
	public DMSResultObject upload(byte[] bytes,Map<DMSMetaDataKeys, String> mapObj, String userRole) {
		String msg = "";
		System.out.println("DMSServicesUtil : UploadDMS :: upload() :Process..Start : MetaData Map Object :  "
						+ mapObj);
		System.out.println("DMSServicesUtil : UploadDMS :: upload() :Process..Start : MetaData Map Object :  "
				+ mapObj);
		DMSResultObject uploadResult=new DMSResultObject();
		List<DocumentVO> documentList = new ArrayList<DocumentVO>();
		DocumentVO document = new DocumentVO();
		Metadata metaData = new Metadata();

		try {
			if (mapObj == null) {
				uploadResult.setResultFlag(false);
				uploadResult.setResultMessage(PROCESS_EXCEPTION +" : "+" Parameter Map object NULL");
				return uploadResult; 
			}

			if (web_services == null) {
				uploadResult.setResultFlag(false);
				uploadResult.setResultMessage(PROCESS_EXCEPTION +" : "+"  Web Service instance creation failed..");
				return uploadResult;
			}
			
			

			Map<String, String> metaDataMap = getMetaPropertyTypeMap(mapObj);
			for (Map.Entry<String, String> entry : metaDataMap.entrySet()) {
				metaData.put(entry.getKey(), entry.getValue());
			}
			document.setMetadata(metaData);
			document.setContent(bytes);
			documentList.add(document);
			setAuthenticationProperty(userRole);
			List<DocumentVO> documentVoList= web_services.uploadDocuments(documentList);
			List<DMSSearchConentVO> dmsUploadContentVOList=new ArrayList<DMSSearchConentVO>();
			if(documentVoList!=null){
				dmsUploadContentVOList=toDMSSearchContentVO(documentVoList);
			}
			/** finally send the document list object DMS server **/
			msg="Successfully uploaded into DMS";
			System.out.println("DMSServicesUtil :: upload() : success msg : "+msg);
			uploadResult.setResultFlag(true);
			uploadResult.setResultMessage(UPLOAD_SUCCESS +" : "+msg);
			uploadResult.setSearchDocmentResult(dmsUploadContentVOList);

		} catch (ServiceException_Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
			logger.error("DMSServicesUtil :: upload() : ServiceException_Exception : msg"
					+ msg);
			uploadResult.setResultFlag(false);
			uploadResult.setResultMessage(SERVICE_EXCEPTION +" : "+msg);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
			logger.error("DMSServicesUtil :: upload() : Exception msg" + msg);
			uploadResult.setResultFlag(false);
			uploadResult.setResultMessage(PROCESS_EXCEPTION +" : "+ msg);
		}
		System.out.println("DMSServicesUtil :: upload() :Process..END:");
		return uploadResult;
	}
	
	/**
	 * This method is responsible for uploading files in file net using Web
	 * Services
	 * 
	 * @param bytes
	 * @param mapObj
	 * @param userRole
	 * @return
	 */
	public DMSResultObject multipleUpload(List<DMSDocumentVO> documentListVO, String userRole) {
		logger.error("DMSServicesUtil :: multipleUpload() : Uploading Documents");
		String msg = "";
		System.out.println("DMSServicesUtil : UploadDMS :: multipleUpload() :Process..Start : documentListVO Object :  "
				+ documentListVO);
		DMSResultObject multUpLoadResult=new DMSResultObject();
		DocumentVO document = new DocumentVO();
		List<DocumentVO> documentList = new ArrayList<DocumentVO>();
		try {
			if (web_services == null) {
				multUpLoadResult.setResultFlag(false);
				multUpLoadResult.setResultMessage(PROCESS_EXCEPTION +" : "+" Web Service instance creation failed..");				
				return multUpLoadResult;
			}

			
			documentList = toDocumentVOList(documentListVO);
			setAuthenticationProperty(userRole);
			List<DocumentVO> documentVoList = web_services.uploadDocuments(documentList);
			List<DMSSearchConentVO> dmsUploadContentVOList=new ArrayList<DMSSearchConentVO>();
			if(documentVoList!=null){
				dmsUploadContentVOList=toDMSSearchContentVO(documentVoList);
			}
			/** finally send the document list object DMS server **/
			msg="Successfully uploaded into DMS";
			System.out.println("DMSServicesUtil :: multipleUpload() : success msg : "+msg);
			multUpLoadResult.setResultFlag(true);
			multUpLoadResult.setResultMessage(UPLOAD_SUCCESS +" : "+msg);
			multUpLoadResult.setSearchDocmentResult(dmsUploadContentVOList);

		} catch (ServiceException_Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
			logger.error("DMSServicesUtil :: multipleUpload() : ServiceException_Exception : msg"
					+ msg);
			multUpLoadResult.setResultFlag(false);
			multUpLoadResult.setResultMessage(SERVICE_EXCEPTION+" : "+msg);
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
			logger.error("DMSServicesUtil :: multipleUpload() : Exception msg"
					+ msg);
			multUpLoadResult.setResultFlag(false);
			multUpLoadResult.setResultMessage(PROCESS_EXCEPTION+" : "+ msg);
		}
		System.out.println("DMSServicesUtil :: multipleUpload() :Process..END:");
		return multUpLoadResult;
	}
	/**
	 * This method is responsible for uploading files in file net using Web
	 * Services
	 * 
	 * @param bytes
	 * @param mapObj
	 * @param userRole
	 * @return
	 */

	public DMSResultObject search(Map<DMSMetaDataKeys, String> metaDataKeys, String userRole) {
		System.out.println("DMSServiceUtil >> search >> searchDocuments :START..");
		String msg = "";
		
		DocumentVO documentVo = new DocumentVO();
		List<DocumentVO> searchDocumentOutputVoList = new ArrayList<DocumentVO>();
		List<DMSSearchConentVO> dmsSearchContentVoList = null;
		Metadata metadataMapObject = new Metadata();
		DMSResultObject searchResults=new DMSResultObject();
		try {
			  
			  if(web_services == null) {
				  searchResults.setResultFlag(false);
				  searchResults.setResultMessage(PROCESS_EXCEPTION +" : "+ "Web Service instance creation failed..");
				return searchResults;
			}
			  
			
			Map<String, String> metaDataMap = getMetaPropertyTypeMap(metaDataKeys);
			for (Map.Entry<String, String> entry : metaDataMap.entrySet()) {
				metadataMapObject.put(entry.getKey(), entry.getValue());
			}
			documentVo.setMetadata(metadataMapObject);
			setAuthenticationProperty(userRole);
			searchDocumentOutputVoList = web_services
					.searchDocuments(documentVo);
			System.out
					.println("values of searchContent : searchDocumentOutputVoList : "
							+ searchDocumentOutputVoList);
			if (searchDocumentOutputVoList != null ? !searchDocumentOutputVoList.isEmpty() ? true : false : false) {
			    dmsSearchContentVoList=toDMSSearchContentVO(searchDocumentOutputVoList);
				System.out.println("DMSUploadUtil : Searching Completed Document Found::::::::::DocumentTitle:"
						+ dmsSearchContentVoList.iterator().next()
								.getDocumentTitle());
				searchResults.setResultFlag(true);
				searchResults.setResultMessage(SEARCH_SUCCESS+" : "+" Searching completed..successfuly..");
				searchResults.setSearchDocmentResult(dmsSearchContentVoList);
			} else {
				System.out.println("DMSUploadUtil : No document found for searchDocuments:::::::::::");
				searchResults.setResultFlag(false);
				searchResults.setResultMessage(NO_RESULT_FOUND+" : "+"Searching completed..Search Content result is empty..");
			}

		} catch (ServiceException_Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
			System.out.println("DMSServicesUtil ::  ServiceException_Exception : msg"+ msg);
			searchResults.setResultFlag(false);
			searchResults.setResultMessage(SERVICE_EXCEPTION +" : "+msg); 
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DMSServicesUtil::searchDMSContent : Exception in search content ");
			searchResults.setResultFlag(false);
			searchResults.setResultMessage(PROCESS_EXCEPTION +" : "+" EXCEPTION : in processing.. search : "+ e.getMessage());
		} finally {
			System.out
					.println("DMSServicesUtil values of searchContent : completed...");
		}
		return searchResults;
	} 

	
	public DMSResultObject delete(Map<DMSMetaDataKeys, String> metaDataKeys, String userRole) {
		System.out.println("DMSUploadUtil ::deleteDocument :START..");
		String msg = "";
		DocumentVO documentVo = new DocumentVO();
		Metadata metadataMapObject = new Metadata();
		DMSResultObject deleteResults=new DMSResultObject();
		try {
			String documentID = metaDataKeys.get(DMSMetaDataKeys.DMS_ID);
			if (documentID != null ? documentID.isEmpty() ? true : false: false) {
				deleteResults.setResultFlag(false);
				deleteResults.setResultMessage(PROCESS_EXCEPTION+" : "+"DMS_ID or DocumentID Should not be empty..");
				return deleteResults;
			}
			if (web_services == null) {
				deleteResults.setResultFlag(false);
				deleteResults.setResultMessage(PROCESS_EXCEPTION +" : "+"Web Service instance creation failed..");
				return deleteResults;
			}
			setAuthenticationProperty(userRole);
			metadataMapObject.put(MetaDataKeys.SYS_LINE, metaDataKeys.get(DMSMetaDataKeys.SYSTEM_LINE));
			metadataMapObject.put(MetaDataKeys.ID, metaDataKeys.get(DMSMetaDataKeys.DMS_ID));
			metadataMapObject.put(MetaDataKeys.DELETE_BY, metaDataKeys.get(DMSMetaDataKeys.DELETE_BY));
			
			/*Map<String, String> metaDataMap = getMetaPropertyTypeMap(metaDataKeys);
			for (Map.Entry<String, String> entry : metaDataMap.entrySet()) {
				System.out.println("delete() :  key : "+entry.getKey()+"--value : "+entry.getKey());
				metadataMapObject.put(entry.getKey(), entry.getValue());
			}*/
			documentVo.setMetadata(metadataMapObject);
			String documentDeletResult = web_services.deleteDocument(documentVo);
			System.out.println("DMSUploadUtil : documentDeleted ::::::::::: "+ documentDeletResult);
			deleteResults.setResultFlag(true);
			deleteResults.setResultMessage(DELETE_SUCCESS + " : "+ documentDeletResult);
			
			//setSearchResults(deleteResults,metaDataKeys,userRole);

		} catch (ServiceException_Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
			System.out.println("DMSServicesUtil :: delete() : ServiceException_Exception :: "+ msg);
			deleteResults.setResultFlag(false);
			deleteResults.setResultMessage(SERVICE_EXCEPTION +" : "+msg);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DMSServicesUtil::delete : Exception in  process deletion ");
			deleteResults.setResultFlag(false);
			deleteResults.setResultMessage(PROCESS_EXCEPTION+" : "+" DMSServicesUtil : Exception in processing.. delete : "+ e.getMessage());
		} finally {
			System.out.println("DMSServicesUtil : document deletion completed : completed...");
		}
		return deleteResults;
	}
	
	/*private void setSearchResults(DMSResultObject deleteResults,
			Map<DMSMetaDataKeys, String> metaDataKeys,String userRole) {
		
		metaDataKeys.put(DMSMetaDataKeys.DMS_ID,null);
		DMSResultObject dmsSResult=search(metaDataKeys,userRole);
		deleteResults.setSearchDocmentResult(dmsSResult.getSearchDocmentResult());
	}*/

	public DMSResultObject viewDocuments(Map<DMSMetaDataKeys, String> metaDataKeys, String userRole) {
		System.out.println("DMSUploadUtil ::viewDocuments :START..");
		String msg = "";
		DocumentVO documentVo = new DocumentVO();
		Metadata metadataMapObject = new Metadata();
		DMSResultObject viewDocResults=new DMSResultObject();
		try {
			String documentID = metaDataKeys.get(DMSMetaDataKeys.DMS_ID);
			if (documentID != null ? documentID.isEmpty() ? true : false: false) {
				viewDocResults.setResultFlag(false);
				viewDocResults.setResultMessage(PROCESS_EXCEPTION +" : "+" DMS_ID or DocumentID Should not be empty..");
				return viewDocResults;
			}
			if (web_services == null) {
				viewDocResults.setResultFlag(false);
				viewDocResults.setResultMessage(PROCESS_EXCEPTION +" : "+" Web Service instance creation failed..");
				return viewDocResults;
			}
			setAuthenticationProperty(userRole);
			Map<String, String> metaDataMap = getMetaPropertyTypeMap(metaDataKeys);
			System.out.println("viewDocuments :: mapEntry :: "+metaDataMap);
			for (Map.Entry<String, String> entry : metaDataMap.entrySet()) {
				metadataMapObject.put(entry.getKey(), entry.getValue());
			}
			documentVo.setMetadata(metadataMapObject);
			DocumentVO documentVoResult = web_services.viewDocument(documentVo);
			DMSDocumentVO dmsDocVO = getDoumentDetails(documentVoResult);

			System.out.println("values of searchContent : viewDocuments : "
					+ documentVoResult);
			System.out.println("DMSUploadUtil : viewDocuments :::::::::::");
			viewDocResults.setResultFlag(true);
			viewDocResults.setResultMessage(VIEW_SUCCESS + " : "+" Document view Successfully..");
			viewDocResults.setViewDocumentResult(dmsDocVO);

		} catch (ServiceException_Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
			System.out.println("DMSServicesUtil :: viewDocuments() : ServiceException_Exception : msg"
					+ msg);
			viewDocResults.setResultFlag(false);
			viewDocResults.setResultMessage(SERVICE_EXCEPTION +" :  "+ msg);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DMSServicesUtil::viewDocuments : Exception in DocumentView content ");
			viewDocResults.setResultFlag(false);
			viewDocResults.setResultMessage(PROCESS_EXCEPTION + " : " +"DMSServicesUtil :: Exception in processing.. view : "+ e.getMessage());
		} finally {
			System.out.println("DMSServicesUtil : document deletion completed : completed...");
		}
		return viewDocResults;
	}
	
	
	/**
	 * 
	 * @param metaDataKeys
	 * @param userRole
	 * @return
	 */
	public DMSResultObject downloadDocuments(List<DMSDocumentVO> documentListVO, String userRole) {
		System.out.println("DMSUploadUtil ::downloadDocuments :START..");
		String msg = "";
		boolean dFlag=true;
		List<DocumentVO> documentList=new ArrayList<DocumentVO>();
		List<DMSDocumentVO> dmsDocVoList=new ArrayList<DMSDocumentVO>();
		DMSResultObject downloadResults=new DMSResultObject();
		String message="";
		try {
			
			if (web_services == null) {
				downloadResults.setResultFlag(false);
				downloadResults.setResultMessage(PROCESS_EXCEPTION + " : "+ "Web Service instance creation failed..");
				return downloadResults;
			}
			setAuthenticationProperty(userRole);
			documentList = toDocumentVOList(documentListVO);
			if(documentList!=null){
				List<DocumentVO> documentVoResult = web_services.downloadDocuments(documentList);
				if(documentVoResult!=null &&!documentVoResult.isEmpty()){
					
					for(DocumentVO docVo:documentVoResult){
							DMSDocumentVO dmsDocVO = getDoumentDetails(docVo);
							dmsDocVoList.add(dmsDocVO);
					}
					dFlag=true;
					message="Document found to download...";
				}else{
					dFlag=false;
					message="No Document found to download";
				}
				System.out.println("values of downloadDocuments : download : "+ documentVoResult);
			}else{
				dFlag=false;
				message="documentList is Empty.. or Null";
			}
			
			System.out.println("DMSUploadUtil : download :::::::::::");
			if(dFlag){
			downloadResults.setResultFlag(true);
			downloadResults.setResultMessage(DOWNLOAD_SUCCESS+ " : " + message);
			downloadResults.setDownloadDocumentResult(dmsDocVoList);
		   }else{
			    downloadResults.setResultFlag(false);
				downloadResults.setResultMessage(NO_RESULT_FOUND+ " : " + message);
				downloadResults.setDownloadDocumentResult(dmsDocVoList);   
		   }

		} catch (ServiceException_Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
			System.out.println("DMSServicesUtil :: download() : ServiceException_Exception : msg"+ msg);
			downloadResults.setResultFlag(false);
			downloadResults.setResultMessage(SERVICE_EXCEPTION + " : "+msg);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DMSServicesUtil::download : Exception in download process content "+e);
			downloadResults.setResultFlag(false);
			downloadResults.setResultMessage(PROCESS_EXCEPTION + " : "+ "DMSServicesUtil :: Exception in processing.. download : "+ e.getMessage());
		} finally {
			System.out.println("DMSServicesUtil : document download completed ...");
		}
		return downloadResults;
	}
	
	/**
	 * This method is used to convert List of DocuementVO to List of DMSSearchContentVO
	 * @param searchDocumentOutputVoList
	 * @return
	 */
	private List<DMSSearchConentVO> toDMSSearchContentVO(List<DocumentVO> searchDocumentOutputVoList) {
		
		List<DMSSearchConentVO> dmsSearchContentVoList=new ArrayList<DMSSearchConentVO>();
		if (searchDocumentOutputVoList != null ? !searchDocumentOutputVoList
				.isEmpty() ? true : false : false) {
			for (DocumentVO docVO : searchDocumentOutputVoList) {
				Metadata metadataMap = docVO.getMetadata();
				System.out.println("Search : DMS : MetadataMap :: "
						+ metadataMap);
				String systemLine = (String) metadataMap.get(MetaDataKeys.SYS_LINE);
				String documentTitle = (String) metadataMap.get(MetaDataKeys.DOC_TITLE);
				String dmsId = (String) metadataMap.get(MetaDataKeys.ID);
				String documentLineDMS = (String) metadataMap.get(MetaDataKeys.DOC_LINE);
				String documentTypeDMS = (String) metadataMap.get(MetaDataKeys.DOC_TYPE);
				
				String businessLineDMS = (String) metadataMap.get(MetaDataKeys.BUS_LINE);
				String serviceLineDMS = (String) metadataMap.get(MetaDataKeys.SERV_LINE);
				String serviceTypeDMS = (String) metadataMap.get(MetaDataKeys.SERV_TYPE);
				
				
				String bookingNoDMS = (String) metadataMap.get(MetaDataKeys.BOOKING_NO);
				String jobNoDMS = (String) metadataMap.get(MetaDataKeys.JOB_NO);
				String siNoDMS = (String) metadataMap.get(MetaDataKeys.SI_NO);
				String incidentNoDMS = (String) metadataMap.get(MetaDataKeys.INCIDENT_NO);
				String claimNoDMS = (String) metadataMap.get(MetaDataKeys.CLAIM_NO);
				String shipmentNoDMS = (String) metadataMap.get(MetaDataKeys.SHIPMENT_NO);
				String invoiceNoDMS = (String) metadataMap.get(MetaDataKeys.INVOICE_NO);
				String quotationNoDMS = (String) metadataMap.get(MetaDataKeys.QUOT_NO);
				String doNoDMS = (String) metadataMap.get(MetaDataKeys.DO_NO);
				String poNoDMS = (String) metadataMap.get(MetaDataKeys.PO_NO);
				String creditNoteNoDMS = (String) metadataMap.get(MetaDataKeys.CREDIT_NOTE_NO);
				String debitNoteNoDMS = (String) metadataMap.get(MetaDataKeys.DEBIT_NOTE_NO);
				String loadNoDMS = (String) metadataMap.get(MetaDataKeys.LOAD_NO);
				String workOrderNoDMS = (String) metadataMap.get(MetaDataKeys.WORKORDER_NO);
				String bookingCustomerDMS = (String) metadataMap.get(MetaDataKeys.BOOK_CUST);
				

				
				
				String deleteByDMS = (String) metadataMap.get(MetaDataKeys.DELETE_BY);
				String uploadByDMS = (String) metadataMap.get(MetaDataKeys.UPLOAD_BY);
				String mimeTypeDMS = (String) metadataMap.get(MetaDataKeys.MIME_TYPE);
				
				String uploadedDate = (String) metadataMap.get(MetaDataKeys.UPLOADED_DATE);
				
				String quotationDate = (String) metadataMap.get(MetaDataKeys.QUOTATION_DATE);
				String creditNoteDate = (String) metadataMap.get(MetaDataKeys.CREDITNOTE_DATE);
				String debitNoteDate = (String) metadataMap.get(MetaDataKeys.DEBITNOTE_DATE);
				String invoiceDate = (String) metadataMap.get(MetaDataKeys.INVOICE_DATE);
				String poDate = (String) metadataMap.get(MetaDataKeys.PODATE);
				
				//new YMS - Changes
				String incidentDetials = (String) metadataMap.get(MetaDataKeys.INCIDENT_DETAILS);
				String consignee = (String) metadataMap.get(MetaDataKeys.CONSIGNEE);//--get multiple values with semicolon(;) separate 
				String chassisNo = (String) metadataMap.get(MetaDataKeys.CHASSIS_NO);//--get multiple values with semicolon(;) separate
				String insuranceAgent = (String) metadataMap.get(MetaDataKeys.INSURANCE_AGENT);
				String responsibleParty = (String) metadataMap.get(MetaDataKeys.RESPONSIBLE_PARTY);
				String isCancelled = (String) metadataMap.get(MetaDataKeys.IS_CANCELLED);
				String claimType = (String) metadataMap.get(MetaDataKeys.CLAIM_TYPE);
				String claimDate = (String) metadataMap.get(MetaDataKeys.CLAIM_DATE);
				String incidentDate = (String) metadataMap.get(MetaDataKeys.INCIDENT_DATE);
				String remarks = (String) metadataMap.get(MetaDataKeys.REMARKS);
				
				String deliveryNoteNo = (String) metadataMap.get(MetaDataKeys.DELIVERYNOTE_NO);
				String pullingOrderNo = (String) metadataMap.get(MetaDataKeys.PULLINGORDER_NO);
				String forwardingAgentName = (String) metadataMap.get(MetaDataKeys.FORWARDINGAGENT_NAME);
			
				DMSSearchConentVO dmsSearchVo = new DMSSearchConentVO();
				dmsSearchVo.setSystemLine(stringNullChecker(systemLine));
				dmsSearchVo.setDocumentTitle(stringNullChecker(documentTitle));
				dmsSearchVo.setDmsId(stringNullChecker(dmsId));
				dmsSearchVo.setDocumentLine(stringNullChecker(documentLineDMS));
				dmsSearchVo.setDocumentType(stringNullChecker(documentTypeDMS));
				
				dmsSearchVo.setBusinessLine(stringNullChecker(businessLineDMS));
				dmsSearchVo.setServiceLine(stringNullChecker(serviceLineDMS));
				dmsSearchVo.setServiceType(stringNullChecker(serviceTypeDMS));
				
				dmsSearchVo.setBookingNo(stringNullChecker(bookingNoDMS));
				dmsSearchVo.setJobNo(stringNullChecker(jobNoDMS));
				dmsSearchVo.setSiNo(stringNullChecker(siNoDMS));
				dmsSearchVo.setIncidentNo(stringNullChecker(incidentNoDMS));
				dmsSearchVo.setClaimNo(stringNullChecker(claimNoDMS));
				dmsSearchVo.setShipmentNo(stringNullChecker(shipmentNoDMS));
				dmsSearchVo.setInvoiceNo(stringNullChecker(invoiceNoDMS));
				dmsSearchVo.setQuotationNo(stringNullChecker(quotationNoDMS));
				dmsSearchVo.setDoNo(stringNullChecker(doNoDMS));
				dmsSearchVo.setPoNo(stringNullChecker(poNoDMS));
				dmsSearchVo.setLoadNo(stringNullChecker(loadNoDMS));
				dmsSearchVo.setWorkOrderNo(stringNullChecker(workOrderNoDMS));
				dmsSearchVo.setBookingCustomer(stringNullChecker(bookingCustomerDMS));
				
				dmsSearchVo.setCreditNoteNo(stringNullChecker(creditNoteNoDMS));
				dmsSearchVo.setDebitNoteNo(stringNullChecker(debitNoteNoDMS));
				dmsSearchVo.setUploadedBy(stringNullChecker(uploadByDMS));
				dmsSearchVo.setDeletedBy(stringNullChecker(deleteByDMS));
				dmsSearchVo.setMimeType(stringNullChecker(mimeTypeDMS));
				dmsSearchVo.setUploadedDate(stringNullChecker(uploadedDate));
				dmsSearchVo.setQuotationDate(stringNullChecker(quotationDate));
				dmsSearchVo.setCreditNoteDate(stringNullChecker(creditNoteDate));
				dmsSearchVo.setDebitNoteDate(stringNullChecker(debitNoteDate));
				dmsSearchVo.setInvoiceDate(stringNullChecker(invoiceDate));
				dmsSearchVo.setPoDate(stringNullChecker(poDate));
				dmsSearchVo.setIncidentDetails(stringNullChecker(incidentDetials));
				dmsSearchVo.setConsignee(stringNullChecker(consignee));
				dmsSearchVo.setChassisNo(stringNullChecker(chassisNo));
				dmsSearchVo.setInsuranceAgent(stringNullChecker(insuranceAgent));
				dmsSearchVo.setResponsibleParty(stringNullChecker(responsibleParty));
				dmsSearchVo.setIsCancelled(stringNullChecker(isCancelled));				
				dmsSearchVo.setClaimType(stringNullChecker(claimType));
				dmsSearchVo.setClaimDate(stringNullChecker(claimDate));
				dmsSearchVo.setIncidentDate(stringNullChecker(incidentDate));
				dmsSearchVo.setRemarks(stringNullChecker(remarks));
				
				dmsSearchVo.setDeliveryNoteNo(deliveryNoteNo);
				dmsSearchVo.setPullingOrderNo(pullingOrderNo);
				dmsSearchVo.setForwardingAgent(forwardingAgentName);
				
				System.out.println("dmsSearchVo :: val :  " + dmsSearchVo);
				dmsSearchContentVoList.add(dmsSearchVo);
			}
		 }
		return dmsSearchContentVoList;
	}

/**
 *  This method is used to get the DMSDocumentVO by passing the parameter as DocumentVO
 * @param documentVoResult
 * @return 
 */
	private DMSDocumentVO getDoumentDetails(DocumentVO documentVoResult) {

		DMSDocumentVO dmsDocmentVO = new DMSDocumentVO();

		Map<DMSMetaDataKeys, String> metaDataValue = new HashMap<DMSMetaDataKeys, String>();
		Metadata meataData = documentVoResult.getMetadata();
		metaDataValue.put(DMSMetaDataKeys.MIME_TYPE,meataData.get(MetaDataKeys.MIME_TYPE));
		metaDataValue.put(DMSMetaDataKeys.DMS_ID,meataData.get(MetaDataKeys.ID));
		metaDataValue.put(DMSMetaDataKeys.SYSTEM_LINE,meataData.get(MetaDataKeys.SYS_LINE)); // Mandatory
		metaDataValue.put(DMSMetaDataKeys.DOCUMENT_LINE,meataData.get(MetaDataKeys.DOC_LINE)); // Mandatory
		metaDataValue.put(DMSMetaDataKeys.DOCUMENT_TYPE,meataData.get(MetaDataKeys.DOC_TYPE)); // Mandatory
		metaDataValue.put(DMSMetaDataKeys.DOCUMENT_TITLE,meataData.get(MetaDataKeys.DOC_TITLE));

		metaDataValue.put(DMSMetaDataKeys.SHIPMENT_NUMBER,meataData.get(MetaDataKeys.SHIPMENT_NO));
		metaDataValue.put(DMSMetaDataKeys.BOOKING_NUMBER,meataData.get(MetaDataKeys.BOOKING_NO));
		metaDataValue.put(DMSMetaDataKeys.BOOKIN_CUSTOMER,meataData.get(MetaDataKeys.BOOK_CUST));
		metaDataValue.put(DMSMetaDataKeys.JOB_NUMBER,meataData.get(MetaDataKeys.JOB_NO));
		metaDataValue.put(DMSMetaDataKeys.SI_NUMBER,meataData.get(MetaDataKeys.SI_NO));
		metaDataValue.put(DMSMetaDataKeys.CLAIM_NUMBER,meataData.get(MetaDataKeys.CLAIM_NO));
		metaDataValue.put(DMSMetaDataKeys.QUOTATION_NO,meataData.get(MetaDataKeys.QUOT_NO));
		metaDataValue.put(DMSMetaDataKeys.INVOICE_NUMBER,meataData.get(MetaDataKeys.INVOICE_NO));

		metaDataValue.put(DMSMetaDataKeys.BUSINESS_LINE,meataData.get(MetaDataKeys.BUS_LINE));
		metaDataValue.put(DMSMetaDataKeys.SERVICE_TYPE,meataData.get(MetaDataKeys.SERV_TYPE));
		metaDataValue.put(DMSMetaDataKeys.SERVICE_LINE,meataData.get(MetaDataKeys.SERV_LINE));
		metaDataValue.put(DMSMetaDataKeys.PORT_OF_DISCHARGE,meataData.get(MetaDataKeys.POD));
		metaDataValue.put(DMSMetaDataKeys.CARGO_TYPE,meataData.get(MetaDataKeys.CARGO_TYPE));
		metaDataValue.put(DMSMetaDataKeys.VOYAGE_NUMBER,meataData.get(MetaDataKeys.VOYAGE_NO));
		metaDataValue.put(DMSMetaDataKeys.VESSEL_NAME,meataData.get(MetaDataKeys.VESSEL_NAME));
		metaDataValue.put(DMSMetaDataKeys.PO_NO,meataData.get(MetaDataKeys.PO_NO));
		metaDataValue.put(DMSMetaDataKeys.DO_NO,meataData.get(MetaDataKeys.DO_NO));
		metaDataValue.put(DMSMetaDataKeys.PORT_OF_LOADING,meataData.get(MetaDataKeys.POL));
		//DELETE_BY
		metaDataValue.put(DMSMetaDataKeys.DELETE_BY,meataData.get(MetaDataKeys.DELETE_BY));
		metaDataValue.put(DMSMetaDataKeys.UPLOAD_BY,meataData.get(MetaDataKeys.UPLOAD_BY));
		metaDataValue.put(DMSMetaDataKeys.UPLOAD_FROM_DATE,meataData.get(MetaDataKeys.FROM_DATE));
		metaDataValue.put(DMSMetaDataKeys.UPLOAD_TO_DATE,meataData.get(MetaDataKeys.TO_DATE));
		metaDataValue.put(DMSMetaDataKeys.CREDIT_NOTE_NO,meataData.get(MetaDataKeys.CREDIT_NOTE_NO));
		metaDataValue.put(DMSMetaDataKeys.DEBIT_NOTE_NO,meataData.get(MetaDataKeys.DEBIT_NOTE_NO));
		metaDataValue.put(DMSMetaDataKeys.QUOTATION_DATE,meataData.get(MetaDataKeys.QUOTATION_DATE));
		metaDataValue.put(DMSMetaDataKeys.CREDITNOTE_DATE,meataData.get(MetaDataKeys.CREDITNOTE_DATE));
		metaDataValue.put(DMSMetaDataKeys.DEBITNOTE_DATE,meataData.get(MetaDataKeys.DEBITNOTE_DATE));
		metaDataValue.put(DMSMetaDataKeys.INVOICE_DATE,meataData.get(MetaDataKeys.INVOICE_DATE));
		metaDataValue.put(DMSMetaDataKeys.PODATE,meataData.get(MetaDataKeys.PODATE));

		
		//yms--Changes
		metaDataValue.put(DMSMetaDataKeys.INCIDENT_DETAILS,meataData.get(MetaDataKeys.INCIDENT_DETAILS));
		metaDataValue.put(DMSMetaDataKeys.CONSIGNEE,meataData.get(MetaDataKeys.CONSIGNEE));
		metaDataValue.put(DMSMetaDataKeys.CHASSIS_NO,meataData.get(MetaDataKeys.CHASSIS_NO));
		metaDataValue.put(DMSMetaDataKeys.INSURANCE_AGENT,meataData.get(MetaDataKeys.INSURANCE_AGENT));
		metaDataValue.put(DMSMetaDataKeys.RESPONSIBLE_PARTY,meataData.get(MetaDataKeys.RESPONSIBLE_PARTY));
		metaDataValue.put(DMSMetaDataKeys.IS_CANCELLED,meataData.get(MetaDataKeys.IS_CANCELLED));
		metaDataValue.put(DMSMetaDataKeys.CLAIM_DATE,meataData.get(MetaDataKeys.CLAIM_DATE));
		metaDataValue.put(DMSMetaDataKeys.INCIDENT_DATE,meataData.get(MetaDataKeys.INCIDENT_DATE));
		metaDataValue.put(DMSMetaDataKeys.CLAIM_TYPE,meataData.get(MetaDataKeys.CLAIM_TYPE));
		metaDataValue.put(DMSMetaDataKeys.REMARKS,meataData.get(MetaDataKeys.REMARKS));
		
		
		metaDataValue.put(DMSMetaDataKeys.DELIVERYNOTE_NO,meataData.get(MetaDataKeys.DELIVERYNOTE_NO));
		metaDataValue.put(DMSMetaDataKeys.PULLINGORDER_NO,meataData.get(MetaDataKeys.PULLINGORDER_NO));
		metaDataValue.put(DMSMetaDataKeys.FORWARDINGAGENT_NAME,meataData.get(MetaDataKeys.FORWARDINGAGENT_NAME));
		dmsDocmentVO.setMetaDataValue(metaDataValue);
		dmsDocmentVO.setContentStream(documentVoResult.getContent());
		
		System.out.println("DMSServicesUtil :: getDoumentDetails(): getContentByte[] :::: "+documentVoResult.getContent()+"--MimeType : "+meataData.get(MetaDataKeys.MIME_TYPE)+
				"---- FileName(DocumentTitle):"+meataData.get(MetaDataKeys.DOC_TITLE));
		return dmsDocmentVO;
	}

	/**
	 * This method is used get the WebServieces type DocumentVoList object by
	 * passing the DMSDocumentVO List values
	 * 
	 * @param documentListVO
	 * @return
	 */
	private List<DocumentVO> toDocumentVOList(
			List<DMSDocumentVO> documentListVO) {
		
		List<DocumentVO> documentList = new ArrayList<DocumentVO>();
		try{
		System.out.println("toDocumentVOList :: DMSdocumentVO : List Size : "+documentListVO.size());
		for (DMSDocumentVO dmsDocVo : documentListVO) {
			DocumentVO documentVO = new DocumentVO();
			Metadata metadataMap = new Metadata();
			Map<DMSMetaDataKeys, String> metaDataVal = dmsDocVo.getMetaDataValue();
			Map<String, String> mapVal = getMetaPropertyTypeMap(metaDataVal);
			System.out.println("toDocumentVOList :: mapEntry :: "+mapVal);
			for (Map.Entry<String, String> entry : mapVal.entrySet()) {
				System.out.println("dmsDocVO >> :: {Key : "+entry.getKey()+" }{ value :  "+entry.getValue()+"}");
				metadataMap.put(entry.getKey(), entry.getValue());
			}
			documentVO.setMetadata(metadataMap);
			if(dmsDocVo.getContentStream()!=null)
			documentVO.setContent(dmsDocVo.getContentStream());
			documentList.add(documentVO);
		}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("DMSServiceUtil : toDocumentVOList() ::  Exception in convertion .."+e);
		}
		System.out.println("toDocumentVOList :: documentList : "+documentList);
		return documentList;
	}

	private static Map<String, String> getMetaPropertyTypeMap(Map<DMSMetaDataKeys, String> mapMetaData) {
		System.out.println("getMetaPropertyTypeMap : processing start : "+mapMetaData);
		String companyCode=null;
		Map<String, String> metadataMapTemp = new HashMap<String, String>();
         
		for (Map.Entry<DMSMetaDataKeys, String> entry : mapMetaData.entrySet()) {
			DMSMetaDataKeys metaDKey = entry.getKey();
			String key = "";
			switch (metaDKey) {
			case DMS_ID:
				key = MetaDataKeys.ID;
				break;
			case SYSTEM_LINE:
				key = MetaDataKeys.SYS_LINE;
				break;
			case DOCUMENT_LINE:
				key = MetaDataKeys.DOC_LINE;
				break;
			case DOCUMENT_TYPE:
				key = MetaDataKeys.DOC_TYPE;
				break;
			case BOOKING_NUMBER:
				key = MetaDataKeys.BOOKING_NO;
				break;
			case JOB_NUMBER:
				key = MetaDataKeys.JOB_NO;
				break;
			case SI_NUMBER:
				key = MetaDataKeys.SI_NO;
				break;
			case CLAIM_NUMBER:
				key = MetaDataKeys.CLAIM_NO;
				break;
			case SHIPMENT_NUMBER:
				key = MetaDataKeys.SHIPMENT_NO;
				break;
			case INVOICE_NUMBER:
				key = MetaDataKeys.INVOICE_NO;
				break;
			case QUOTATION_NO:
				key = MetaDataKeys.QUOT_NO;
				break;
			case BUSINESS_LINE:
				key = MetaDataKeys.BUS_LINE;
				break;
			case SERVICE_TYPE:
				key = MetaDataKeys.SERV_TYPE;
				break;
			case SERVICE_LINE:
				key = MetaDataKeys.SERV_LINE;
				break;
			case BOOKIN_CUSTOMER:
				key = MetaDataKeys.BOOK_CUST;
				break;
			case PORT_OF_DISCHARGE:
				key = MetaDataKeys.POD;
				break;
			case CARGO_TYPE:
				key = MetaDataKeys.CARGO_TYPE;
				break;
			case VESSEL_NAME:
				key = MetaDataKeys.VESSEL_NAME;
				break;
			case VOYAGE_NUMBER:
				key = MetaDataKeys.VOYAGE_NO;
				break;
			case UPLOAD_BY:
				key = MetaDataKeys.UPLOAD_BY;
				break;
			case DOCUMENT_TITLE:
				key = MetaDataKeys.DOC_TITLE;
				break;
			case UPLOAD_FROM_DATE:
				key = MetaDataKeys.FROM_DATE;
				break;
			case UPLOAD_TO_DATE:
				key = MetaDataKeys.TO_DATE;
				break;
			case PO_NO:
				key = MetaDataKeys.PO_NO;
				break;
			case DO_NO:
				key = MetaDataKeys.DO_NO;
				break;
			case PORT_OF_LOADING:
				key = MetaDataKeys.POL;
				break;
			case DELETE_BY:
				key = MetaDataKeys.DELETE_BY;
				break;
			case LOAD_NO:
				key = MetaDataKeys.LOAD_NO;
				break;	
			case CREDIT_NOTE_NO:
				key = MetaDataKeys.CREDIT_NOTE_NO;
				break;
			case DEBIT_NOTE_NO:
				key = MetaDataKeys.DEBIT_NOTE_NO;
				break;
			case INCIDENT_NO:
				key = MetaDataKeys.INCIDENT_NO;				
				break;
			case QUOTATION_DATE:
				key = MetaDataKeys.QUOTATION_DATE;				
				break;
			case CREDITNOTE_DATE:
				key = MetaDataKeys.CREDITNOTE_DATE;				
				break;
			case DEBITNOTE_DATE:
				key = MetaDataKeys.DEBITNOTE_DATE;				
				break;
			case INVOICE_DATE:
				key = MetaDataKeys.INVOICE_DATE;				
				break;
			case PODATE:
				key = MetaDataKeys.PODATE;				
				break;
			case COMPANY_CODE:
				key = MetaDataKeys.COMPANY_CODE;				
				break;
			case INCIDENT_DETAILS:
				key = MetaDataKeys.INCIDENT_DETAILS;				
				break;
			case CONSIGNEE:
				key = MetaDataKeys.CONSIGNEE;				
				break;
			case CHASSIS_NO:
				key = MetaDataKeys.CHASSIS_NO;				
				break;
			case INSURANCE_AGENT:
				key = MetaDataKeys.INSURANCE_AGENT;				
				break;
			case RESPONSIBLE_PARTY:
				key = MetaDataKeys.RESPONSIBLE_PARTY;				
				break;
			case IS_CANCELLED:
				key = MetaDataKeys.IS_CANCELLED;				
				break;
			case CLAIM_TYPE:
				key = MetaDataKeys.CLAIM_TYPE;				
				break;
			case CLAIM_DATE:
				key = MetaDataKeys.CLAIM_DATE;				
				break;
			case INCIDENT_DATE:
				key = MetaDataKeys.INCIDENT_DATE;				
				break;	
			case REMARKS:
				key = MetaDataKeys.REMARKS;				
				break;	
			case DELIVERYNOTE_NO:
				key = MetaDataKeys.DELIVERYNOTE_NO;				
				break;
			case PULLINGORDER_NO:
				key = MetaDataKeys.PULLINGORDER_NO;				
				break;	
			case FORWARDINGAGENT_NAME:
				key = MetaDataKeys.FORWARDINGAGENT_NAME;				
				break;	
			default:
				break;
			}
			metadataMapTemp.put(key, entry.getValue()!=null?entry.getValue().trim().toUpperCase():entry.getValue());
		}

		if(metadataMapTemp.get(MetaDataKeys.COMPANY_CODE)==null){
			
			companyCode=getCompanyCode();
			if(companyCode!=null){
				System.out.println("companyCode isSet to Metadata In DMSServiceUtil...");
				metadataMapTemp.put(MetaDataKeys.COMPANY_CODE, companyCode);
			}
		}
		System.out.println("MetaDataMap Temp :: " + metadataMapTemp);
		
		return metadataMapTemp;
	}

	
	/**
	 * This method is used to get the company code who has logged into DMS upload,search,delete,download or view the documents
	 * @return
	 */
	public static String getCompanyCode(){
		System.out.println("DMS::DMSServicesUtil :getCompanyCode():: start ::");
		Map<String,String> userDetails=null;
		String companyBranchCode=null;
		String loggedUser=null;
		UserProfileAccessUtil gm=null;
		try {
			if(gm==null){
				gm = UserProfileAccessUtil.getInstance();
			}
			
			userDetails=gm.getCurrentUserDetails();
			if(userDetails!=null){
				companyBranchCode = userDetails.get(UserProfileAccessUtil.COMPANYNAME);
				loggedUser = userDetails.get(UserProfileAccessUtil.USER_NAME);
								
			}else{
				System.out.println("DMS::DMSServicesUtil :getCompanyCode():unable to get the USerDetails Info from PUMA . it NULL..");
			}
			logger.debug("PUMA : Looged User details are :: companyBranchCode : "+companyBranchCode+"---loggedUser : "+loggedUser);
			System.out.println("PUMA : Looged User details are :: companyBranchCode : "+companyBranchCode+"---loggedUser : "+loggedUser);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			System.out.println("DMS::DMSServicesUtil :getCompanyCode(): Exception in getting Puma information.. "+e);
		}
		System.out.println("DMS::DMSServicesUtil :getCompanyCode():: End ::");
		return companyBranchCode;
	}

	
	private byte[] readFiles(String filePath) {
		 System.out.println("readFiles :: filePath : "+filePath);
		 FileInputStream fileInputStream=null;
		 File file=null;
		 byte[] b=null;
		 try {
			  file = new File(filePath);
			  b = new byte[(int) file.length()];
	             fileInputStream = new FileInputStream(file);
	             fileInputStream.read(b);
	             org.apache.commons.io.FileUtils.readFileToByteArray(file);
	        } catch (FileNotFoundException e) {
	                    System.out.println("File Not Found.");
	                    e.printStackTrace();
	                    try {
							fileInputStream.close();
						} catch (IOException e1) {
							
							e1.printStackTrace();
						}
	        }
	        catch (IOException e1) {
	                 System.out.println("Error Reading The File.");
	                  e1.printStackTrace();
	                    try {
							fileInputStream.close();
						} catch (IOException e2) {
							
							e1.printStackTrace();
				 } 
	        }
		// System.out.println("byte Size :: "+b!=null?b.length:b);
		return b;
	} 
	private String stringNullChecker(String str) {
		// System.out.println("String :: val :  "+str);
		return str != null ? !str.trim().isEmpty() ? str.trim() : "" : "";
	}

//This method will return merged documents in OFS Invoice module
	public byte[] mergeDMSInvoicePdfs(Map<DMSMetaDataKeys, String> metaDataKeys, String userRole)throws ServiceException_Exception{
		System.out.println("DMSUploadUtil ::mergeDMSInvoicePdfs :START..");
		String msg = "";
		
		DocumentVO documentVo = new DocumentVO();
		Metadata metadataMapObject = new Metadata();
		byte[] mergedByteArray = null;
		
		try {
			  setAuthenticationProperty(userRole);
			  if(web_services == null) {
				System.out.println(PROCESS_EXCEPTION +" : "+ "Web Service instance creation failed in mergeDMSInvoicePdfs method !..");
				return null;
			}
			  			
			Map<String, String> metaDataMap = getMetaPropertyTypeMap(metaDataKeys);
			for (Map.Entry<String, String> entry : metaDataMap.entrySet()) {
				metadataMapObject.put(entry.getKey(), entry.getValue());
			}
			documentVo.setMetadata(metadataMapObject);
			mergedByteArray= web_services.mergeDocuments(documentVo);

		} 
		catch(ServiceException_Exception se_ex){
			System.out.println("DMSServicesUtil::mergeDMSInvoicePdfs : "+se_ex);
			throw se_ex;
		}	
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("DMSServicesUtil::mergeDMSInvoicePdfs : Exception in search content ");
		} finally {
			System.out
					.println("DMSServicesUtil method mergeDMSInvoicePdfs : completed...");
		}
		return mergedByteArray;
	}
		
	
	
		
	
	
	
 
}

