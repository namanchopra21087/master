package com.giga.dms.services;

import java.io.IOException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.giga.dms.exception.ServiceException;
import com.giga.dms.vo.DocumentInfoVO;

@WebService
public interface DMSServices {
		
	@WebMethod public List<DocumentInfoVO> searchDocuments(@WebParam( name="document")DocumentInfoVO documentInfo) throws ServiceException;
	
	@WebMethod public List<DocumentInfoVO> uploadDocuments(@WebParam( name="documentCollection")List<DocumentInfoVO> documentInfo) throws ServiceException;
	
	@WebMethod public DocumentInfoVO viewDocument(@WebParam( name="document")DocumentInfoVO documentInfo) throws ServiceException,IOException;
	
	@WebMethod public String deleteDocument(@WebParam( name="document")DocumentInfoVO documentInfo) throws ServiceException;
	
	@WebMethod public List<DocumentInfoVO> downloadDocuments(@WebParam( name="documentCollection")List<DocumentInfoVO> documentInfo)throws ServiceException;

	@WebMethod public byte[] mergeDocuments(@WebParam( name="document") DocumentInfoVO documentInfo) throws ServiceException;	
}
