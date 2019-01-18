package com.giga.dms.service;

import java.util.List;
import java.util.Map;

import com.giga.base.service.GigaService;
import com.giga.dms.services.ServiceException_Exception;
import com.giga.dms.util.DMSGType;
import com.giga.dms.util.GiGaModuleType;
import com.giga.dms.util.DMSMetaDataKeys;
import com.giga.dms.vo.DMSDocumentVO;
import com.giga.dms.vo.DMSMailContentVO;
import com.giga.dms.vo.DMSResultObject;
import com.giga.dms.vo.DMSSearchVO;
import com.giga.dms.vo.DMSWebUploadVO;
import com.giga.exception.DataAccessException;

public interface DMSGService extends GigaService{
   Map<String,String> loadDocumentLine(String systemLine)throws DataAccessException ;
   public Map<String, String> loadDocumentType(Map<String,String> docTypeDetailsMap)throws DataAccessException;
   public Map<String, String> loadDocumentTypeUpload(Map<String, String> docTypeDetailsMap)throws DataAccessException;
   public List<DMSSearchVO> getSearchDetails(DMSGType searchType,DMSSearchVO searchParamVO) throws DataAccessException ;
   public DMSWebUploadVO populateDetails(DMSGType searchType,DMSWebUploadVO searchParam)  throws DataAccessException;
   
   public DMSResultObject uploadByFilePathDMS(String completePathFileName,Map<DMSMetaDataKeys, String> mapObj, String userRole,GiGaModuleType moduleType);
   public DMSResultObject uploadDMS(byte[] bytes, Map<DMSMetaDataKeys, String> mapObj,String userRole,GiGaModuleType moduleType);
   public DMSResultObject searchDMS(Map<DMSMetaDataKeys,String> metaDataKeys,String userRole,GiGaModuleType moduleType);
   public DMSResultObject multipleUploadDMS(List<DMSDocumentVO> documentListVO,String userRole,GiGaModuleType moduleType);
   public DMSResultObject deleteDMS(Map<DMSMetaDataKeys,String> metaDataKeys,String userRole,GiGaModuleType moduleType);
   public DMSResultObject viewDocumentDMS(Map<DMSMetaDataKeys, String> metaDataKeys, String userRole,GiGaModuleType moduleType);
   public DMSResultObject downLoadDMS(List<DMSDocumentVO> documentListVO, String userRole,GiGaModuleType moduleType);
   
   public DMSResultObject asynUploadByFilePathDMS(String completePathFileName,Map<DMSMetaDataKeys, String> mapObj, String userRole,DMSMailContentVO dmsMailContentVO,GiGaModuleType moduleType) ;
   public DMSResultObject asynMultipleUploadDMS(List<DMSDocumentVO> documentListVO,DMSMailContentVO dmsMailContentVO,String userRole,GiGaModuleType moduleType);
   public Boolean checkValidUserProfileDMS(String userGroupName);
   public Map<String, String> getDmsAccessDetials(String userRole);
   public byte[] mergeDMSInvoicePdfs(Map<DMSMetaDataKeys, String> metaDataKeys, String userRole) throws ServiceException_Exception;
  
}
