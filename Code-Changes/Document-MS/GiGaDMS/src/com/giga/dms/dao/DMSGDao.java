package com.giga.dms.dao;

import java.util.List;
import java.util.Map;

import com.giga.base.service.GigaService;
import com.giga.dms.util.DMSGType;
import com.giga.dms.vo.DMSDocumentAuditHistoryVO;
import com.giga.dms.vo.DMSResultObject;
import com.giga.dms.vo.DMSSearchVO;
import com.giga.dms.vo.DMSWebUploadVO;
import com.giga.exception.DataAccessException;

public interface DMSGDao extends GigaService{
	   Map<String,String> loadDocumentLine(String systemLine)throws DataAccessException ;
	   public Map<String, String> loadDocumentType(Map<String,String> docTypeDetailsMap)throws DataAccessException;
	   public List<DMSSearchVO> getSearchDetails(DMSGType searchType,DMSSearchVO searchParamVO) throws DataAccessException ;
	   public DMSWebUploadVO populateDetails(DMSGType searchType,DMSWebUploadVO searchParam)  throws DataAccessException;
	   public DMSResultObject logToDMSDocumnetHistory(List<DMSDocumentAuditHistoryVO> auditHistoryVo);
	   public Map<String, String> loadDocumentTypeUpload(Map<String, String> docTypeDetailsMap)throws DataAccessException;
	   public Map<String, String> getDmsAccessDetials(String userRole);
}
