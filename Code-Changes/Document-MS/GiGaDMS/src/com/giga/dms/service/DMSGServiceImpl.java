package com.giga.dms.service;


import java.util.HashMap;
import java.util.List;

import java.util.Map;

import org.apache.log4j.Logger;

import com.giga.base.LoggerUtility;
import com.giga.base.UserProfileAccessUtil;
import com.giga.base.service.GigaServiceLocator;
import com.giga.dms.IDMSService;
import com.giga.dms.dao.DMSGDao;
import com.giga.dms.services.ServiceException_Exception;
import com.giga.dms.util.DMSAsynServicesUtil;
import com.giga.dms.util.DMSGType;
import com.giga.dms.util.DMSServicesUtil;
import com.giga.dms.util.GiGaModuleType;
import com.giga.dms.util.DMSMetaDataKeys;
import com.giga.dms.vo.DMSDocumentVO;
import com.giga.dms.vo.DMSMailContentVO;
import com.giga.dms.vo.DMSResultObject;
import com.giga.dms.vo.DMSSearchVO;
import com.giga.dms.vo.DMSWebUploadVO;
import com.giga.exception.DataAccessException;




public class DMSGServiceImpl implements DMSGService{
	protected Logger logger = null; 
	private DMSGDao dmsDao=null;
	public DMSGServiceImpl() {  
		super();
		logger = LoggerUtility.getLogger(DMSGServiceImpl.class);
		try {
			System.out.println("DMSServiceImpl constructor..........");
			dmsDao = (DMSGDao) GigaServiceLocator.getService(IDMSService.daoLayer.DMSG_DAO);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DMSServiceImpl :0-param constructor : creating DMSGDao failed ");
		}
	}
	
	@Override
	public Map<String, String> loadDocumentLine(String systemLine)throws DataAccessException {
		
		return dmsDao.loadDocumentLine(systemLine);
	}
	@Override
	public Map<String, String> loadDocumentType(Map<String,String> docTypeDetailsMap)throws DataAccessException{
		
		return dmsDao.loadDocumentType(docTypeDetailsMap);
	}
	@Override
	public Map<String, String> loadDocumentTypeUpload(Map<String,String> docTypeDetailsMap)throws DataAccessException{
		
		return dmsDao.loadDocumentTypeUpload(docTypeDetailsMap);
	}
	
	@Override 
	public List<DMSSearchVO> getSearchDetails(DMSGType searchType,
			DMSSearchVO searchParameter)throws DataAccessException {
		 
		 System.out.println("DMSSearviceImpl:: getSearchDetails()...processs:: dmsDao : "+dmsDao);
		return dmsDao.getSearchDetails(searchType, searchParameter);
	}
	@Override
	public DMSWebUploadVO populateDetails(DMSGType searchType,
			DMSWebUploadVO searchParam) throws DataAccessException {
		
		return dmsDao.populateDetails(searchType, searchParam);
	}
	public DMSResultObject uploadByFilePathDMS(String completePathFileName,Map<DMSMetaDataKeys, String> mapObj, String userRole,GiGaModuleType moduleType) {
		System.out.println("DMSService:: uploadByFilePathDMS() :: Process.");
		DMSResultObject uploadResult=new DMSResultObject();
	 try{
			 if(completePathFileName!=null && !completePathFileName.isEmpty()){
					  uploadResult=new DMSServicesUtil().uploadByFilePath(completePathFileName,mapObj,userRole);
					  System.out.println("DMSService:: uploadByFilePathDMS() :: upload process completed.. :: "+uploadResult.getResultMessage());
			 }else{
				 uploadResult.setResultFlag(false);
				 uploadResult.setResultMessage("Uploading failed due to filePath is Empty ");
			 }
		}catch(Exception e){
			uploadResult.setResultFlag(false);
			uploadResult.setResultMessage("DMS Web Service instance creation problem");
		}
		 return uploadResult;
		
	}
	@Override
	public DMSResultObject uploadDMS(byte[] bytes, Map<DMSMetaDataKeys, String> mapObj,String userRole,GiGaModuleType moduleType){
		
		System.out.println("DMSService:: uploadDMS() :: Process.");
		DMSResultObject uploadResult=new DMSResultObject();
		try{
			 if(bytes!=null && bytes.length!=0){
					  uploadResult=new DMSServicesUtil().upload(bytes, mapObj,userRole);
					  System.out.println("DMSService:: uploadDMS() :: upload process completed.. :: "+uploadResult.getResultMessage());
			 }else{
				 uploadResult.setResultFlag(false);
				 uploadResult.setResultMessage("Byte Array Values null or size may be Zero");
			 }
		}catch(Exception e){
			uploadResult.setResultFlag(false);
			 uploadResult.setResultMessage("DMS Web Service instance creation problem");
		}
		 return uploadResult;
	}	
	@Override
	public DMSResultObject searchDMS(Map<DMSMetaDataKeys,String> metaDataKeys,String userRole,GiGaModuleType moduleType){
		
		logger.debug("DMSService:: searchDMS() :: Process.");
		DMSResultObject searchResults=new DMSResultObject();
		try{
			 if(metaDataKeys!=null && !metaDataKeys.isEmpty()){
					  searchResults=new DMSServicesUtil().search(metaDataKeys,userRole);
					  System.out.println("DMSService:: searchDMS() :: process completed.. :: "+searchResults.getResultMessage());
			 }else{
				 searchResults.setResultFlag(false);
				 searchResults.setResultMessage("Searching metadata map values are empty");
			 }
		}catch(Exception e){
			 searchResults.setResultFlag(false);
			 searchResults.setResultMessage(" DMS Web Service instance creation problem");
		}
		 return searchResults;
	}

	@Override
	 public DMSResultObject multipleUploadDMS(List<DMSDocumentVO> documentListVO,String userRole,GiGaModuleType moduleType) {
		
		
		DMSResultObject multipleUploadResults=new DMSResultObject();
		try{
			    if(documentListVO!=null && !documentListVO.isEmpty()){
				   multipleUploadResults=new DMSServicesUtil().multipleUpload(documentListVO,userRole);
				   System.out.println("DMSService:: multipleUploadDMS() :: upload process completed.. :: "+multipleUploadResults.getResultMessage());
				 }else{
					 multipleUploadResults.setResultFlag(false);
					 multipleUploadResults.setResultMessage("DMSDocumentVo object NULL or Empty");
				}
		}catch(Exception e){
			multipleUploadResults.setResultFlag(false);
			multipleUploadResults.setResultMessage(" DMS Web Service instance creation problem");
		}
		 return multipleUploadResults;
		
	}


	@Override
	public DMSResultObject deleteDMS(Map<DMSMetaDataKeys, String> metaDataKeys, String userRole,GiGaModuleType moduleType) {
		
		DMSResultObject deleteResult=new DMSResultObject();
		try{
			    if(metaDataKeys!=null && !metaDataKeys.isEmpty()){
				   deleteResult=new DMSServicesUtil().delete(metaDataKeys,userRole);
				   System.out.println("DMSService:: deleteDMS() :: upload process completed.. :: "+deleteResult.getResultMessage());
				 }else{
					 deleteResult.setResultFlag(false);
					 deleteResult.setResultMessage("metaDataKeys object NULL or Empty");
				}
		}catch(Exception e){
			deleteResult.setResultFlag(false);
			deleteResult.setResultMessage(" DMS Web Service instance creation problem");
		}
		 return deleteResult;
	}
	
	@Override
	public DMSResultObject viewDocumentDMS(Map<DMSMetaDataKeys, String> metaDataKeys, String userRole,GiGaModuleType moduleType) {
		
		DMSResultObject viewResults=new DMSResultObject();
		try{
			    if(metaDataKeys!=null && !metaDataKeys.isEmpty()){
				   viewResults=new DMSServicesUtil().viewDocuments(metaDataKeys,userRole);
				   System.out.println("DMSService:: view() :: view process completed.. :: "+viewResults.getResultMessage());
				 }else{
					 viewResults.setResultFlag(false);
					 viewResults.setResultMessage("metaDataKeys object NULL or Empty");
				}
			    
		}catch(Exception e){
			viewResults.setResultFlag(false);
			viewResults.setResultMessage(" DMS Web Service instance creation problem");
		}
		 return viewResults;
	}

	@Override
	public DMSResultObject downLoadDMS(List<DMSDocumentVO> documentListVO, String userRole,GiGaModuleType moduleType) {
		
		DMSResultObject downloadResults=new DMSResultObject();
		try{
			    if(documentListVO!=null && !documentListVO.isEmpty()){
				   downloadResults=new DMSServicesUtil().downloadDocuments(documentListVO,userRole);
				   System.out.println("DMSService:: download() :: download process completed.. :: "+downloadResults.getResultMessage());
				 }else{
					 downloadResults.setResultFlag(false);
					 downloadResults.setResultMessage("documentListVO object NULL or Empty");
				}
			    
		}catch(Exception e){
			downloadResults.setResultFlag(false);
			downloadResults.setResultMessage(" DMS Web Service instance creation problem");
		}
		 return downloadResults;
	}
 
	@Override
	public DMSResultObject asynUploadByFilePathDMS(String completePathFileName,Map<DMSMetaDataKeys, String> mapObj, String userRole,DMSMailContentVO dmsMailContentVO, GiGaModuleType moduleType) {
		
		System.out.println("DMSService:: uploadByFilePathDMS() :: Process.");
		DMSResultObject uploadResult=new DMSResultObject();
	 try{
		 uploadResult=DMSAsynServicesUtil.asynUploadByFilePath(completePathFileName,mapObj,userRole,dmsMailContentVO);
		 System.out.println("DMSService:: asynUploadByFilePathDMS() :: .. :: "+uploadResult.getResultMessage());
		}catch(Exception e){
			uploadResult.setResultFlag(false);
			uploadResult.setResultMessage("DMS Web Service instance creation problem");
		}
		 return uploadResult;
	} 
	@Override
	public DMSResultObject asynMultipleUploadDMS(List<DMSDocumentVO> documentListVO,DMSMailContentVO dmsMailContentVO,String userRole,GiGaModuleType moduleType){
		
		System.out.println("DMSService:: uploadByFilePathDMS() :: Process.");
		DMSResultObject uploadResult=new DMSResultObject();
	 try{
		 uploadResult=DMSAsynServicesUtil.asynMultipleUpload(documentListVO, userRole, dmsMailContentVO);
		 System.out.println("DMSService:: asynUploadByFilePathDMS() :: .. :: "+uploadResult.getResultMessage());
		}catch(Exception e){
			uploadResult.setResultFlag(false);
			uploadResult.setResultMessage("DMS Web Service instance creation problem");
		}
		 return uploadResult;
	}

	@Override
	public Boolean checkValidUserProfileDMS(String userGroupName) {
		
		System.out.println("DMSGServiceImpl :: checkValidUserProfileDMS() :processs.....::userGroupName :: "+userGroupName);
			  Boolean validateUserFlag=false;	 
			  userGroupName=userGroupName.trim();
			  String profileGroupNameDMS="";
		 	  String loggedModuleNameDMS="";
		 	  String userRoleDMS="";
		 	  String userRole1="";
			  Boolean profileflag=false;
			  Map<String,String> pumaMap=new HashMap<String,String>();
			  Map<String,String> moduleMap=new HashMap<String,String>();
			  try {
				   UserProfileAccessUtil gm = UserProfileAccessUtil.getInstance();
					if(gm!=null){
						for(com.ibm.portal.um.Group grp:gm.getLoggerInUserGroups()){
							 Map<String,Object> groupAtt=gm.getGroupsAttributes(grp);
							 System.out.println("groupAttr : groupAtt Map :: "+groupAtt);
							 if(groupAtt!=null?!groupAtt.isEmpty()?true:false:false){ 
								 profileGroupNameDMS=(String)groupAtt.get(groupAtt.keySet().iterator().next().toString());
								 profileflag=profileGroupNameDMS!=null?profileGroupNameDMS.trim().contains("_")?true:false:false;
								 System.out.println("groupAttr : groupAtt :: "+profileGroupNameDMS + "------ profileflag : "+profileflag);
								 
								 //all moduleType
								 if(profileflag && profileGroupNameDMS.split("_").length>=2){
									 loggedModuleNameDMS =profileGroupNameDMS.split("_")[1];
									 if(loggedModuleNameDMS!=null)
									 pumaMap.put(loggedModuleNameDMS.trim(), profileGroupNameDMS);
									 
								 }else{
									 pumaMap.put(profileGroupNameDMS.trim(), profileGroupNameDMS);
								 }
								 System.out.println("in Group :: profileflag : "+profileflag+"---pumaMap : "+pumaMap);
							 }
							 System.out.println("in Group :: continue......"); 
						 }
						
						
						 System.out.println(" profile flag :: "+profileflag+"--pumaMap.size :: "+pumaMap.size()); 
							if(!pumaMap.isEmpty()&&pumaMap.size()>0){
								System.out.println("PumaMap :: is :: "+ pumaMap );
								if(pumaMap.get("DMS")!=null){
									System.out.println("PumaMap :: is contains : DMS : "+ pumaMap+" &&  userGroupName : "+userGroupName );							
									//DMS user profile validate**************************************
									String profileGroupName=pumaMap.get("DMS");
									String loggedModuleName =profileGroupName.split("_")[1];
									
									userRoleDMS= gm.getCurrentUserRole(loggedModuleName);
									if(userRoleDMS!=null&&userRoleDMS.trim().equalsIgnoreCase("Approver")){
										validateUserFlag=true;
									}
								}else{
									validateUserFlag=false;
									System.out.println("ModuleMap :: is not contains DMS :  "+ moduleMap );
									userRole1= gm.getCurrentUserRole(userGroupName);
									for (Map.Entry<String, String> entry : moduleMap.entrySet()) {
										String profileGroupNameAll=entry.getValue();
										String loggedModuleNameAll=entry.getKey();
										String userRoleAll= gm.getCurrentUserRole(loggedModuleNameDMS);
										System.out.println("ModuleMap :: Other List of Values ::: { profileGroupNameAll : "+ profileGroupNameAll+"},{ loggedModuleNameAll : "+loggedModuleNameAll+
												"},{ userRoleAll : "+userRoleAll+"},{ userRole1 : "+userRole1+"},{ dmsApperoverFlag : "+validateUserFlag+"}" );
									}//--if - All profile
									
								}//DMS user check--if
								
								System.out.println("[PumaMap :: is contains : DMS : { profileModuleName1 : "+ profileGroupNameDMS+"},{ loggedModuleName1 : "+loggedModuleNameDMS+
										"},{ userRoleDMS : "+userRoleDMS +"} ,{ dmsApperoverFlag : "+validateUserFlag+"}]");
									if(validateUserFlag){
										return true;
									}else{
										return false;
									}
							}
							else{
								 System.out.println("BaseDMSWebPortlet : checkValidUserProfileDMS : unable to find the DMS group user...");
								 validateUserFlag=false;
							}//Dms Map--if-else
					}else{
						 System.out.println("BaseDMSWebPortlet : checkValidUserProfileDMS : Userprofile gm is Null , unable to get profile instance");
						 validateUserFlag=false;					
					}
					
				//return request;
			  	} catch (Exception e) { 
				  System.out.println("BaseDMSWebPortlet : checkValidUserProfileDMS :EXCEPTION : USER Checker Exception unable to process the Profile.............."+e);
				  validateUserFlag=false;
			  }
		  
		return validateUserFlag;
	}
	@Override
	public Map<String, String> getDmsAccessDetials(String userRole) {
		
		return dmsDao.getDmsAccessDetials(userRole);
	}
	@Override
	public byte[] mergeDMSInvoicePdfs(Map<DMSMetaDataKeys,String> metaDataKeys, String userRole) throws ServiceException_Exception{
		byte[] searchResults = null;
		searchResults=new DMSServicesUtil().mergeDMSInvoicePdfs(metaDataKeys, userRole);
		 return searchResults;
	}
}
