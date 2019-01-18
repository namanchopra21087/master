package com.giga.dms.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.collection.StringList;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.FilteredPropertyType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.core.UpdatingBatch;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.apiimpl.core.BatchItemHandleImpl;
import com.giga.dms.exception.ServiceException;
import com.giga.dms.util.AESencrp;
import com.giga.dms.util.CEManager;
import com.giga.dms.util.CommonUtil;
import com.giga.dms.util.MyHashMap;
import com.giga.dms.util.P8Util;
import com.giga.dms.util.PropertyReader;
import com.giga.dms.util.ServiceConstants;
import com.giga.dms.util.ServiceValidator;
import com.giga.dms.vo.DocumentInfoVO;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

@WebService(endpointInterface = "com.giga.dms.services.DMSServices", serviceName = "GMGDMSServices")
@SuppressWarnings({"unchecked","rawtypes"})
public class DMSServicesImpl implements DMSServices, ServiceConstants {

	@Resource
	WebServiceContext wsctx;

	private static final Logger LOG = LoggerFactory.getLogger(DMSServicesImpl.class);
	private CEManager ceManager = null;

	public void init() throws ServiceException {

		try {
			MessageContext mctx = wsctx.getMessageContext();
			// get detail from request headers
			Map http_headers = (Map) mctx.get(MessageContext.HTTP_REQUEST_HEADERS);
			List userList = (List) http_headers.get(BindingProvider.USERNAME_PROPERTY);
			List passList = (List) http_headers.get(BindingProvider.PASSWORD_PROPERTY);
			String ceURL = PropertyReader.get("ceURL");
			ceManager = CEManager.getInstance(ceURL, AESencrp.decrypt(userList.get(0).toString()),
					AESencrp.decrypt(passList.get(0).toString()));
		} catch (Exception e) {
			LOG.error("Connection Exception::" + e);
			e.printStackTrace();
			throw new ServiceException(HEADER_ERROR, HEADER_ERROR_MSG);
		}
	}

	/**
	 * This method is used to search the documents from FileNet
	 * 
	 * return List of DocumentInfoVO
	 * 
	 * @throws ServiceException
	 */
	@Override
	public List<DocumentInfoVO> searchDocuments(DocumentInfoVO documentInfo) throws ServiceException {
		List<DocumentInfoVO> documentListVO = new ArrayList<DocumentInfoVO>();
		try {
			LOG.debug("Usage:DMSServicesImpl : searchDocuments():: start");
			init();
			boolean mandatoryFieldCheck = true;
			mandatoryFieldCheck = ServiceValidator.validateDataFieldsForSearch(documentInfo);
			if (mandatoryFieldCheck) {
				RepositoryRowSet documentSet = generateSearchQuery(documentInfo);
				Iterator<RepositoryRow> iterator = documentSet.iterator();
				DocumentInfoVO documentInfoVO = new DocumentInfoVO();
				Properties properties;
				while (iterator.hasNext()) {
					RepositoryRow repositoryRow = (RepositoryRow) iterator.next();
					documentInfoVO = new DocumentInfoVO();
					properties = repositoryRow.getProperties();
					documentInfoVO = generateDocumentVO(properties);
					documentListVO.add(documentInfoVO);
				}
				LOG.debug("Seaarch Documents Results Size::" + documentListVO.size());
			} else {
				throw new ServiceException(MANDATORY_FIELD_MISSING_ERROR, MANDATORY_FIELD_MISSING_ERROR_MSG);
			}
		} catch (EngineRuntimeException e) {
			LOG.error("DMS Search Exception::" + e);
			e.printStackTrace();
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
		} catch (Exception e) {
			LOG.error("DMS common exception error::" + e);
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
		}
		LOG.debug("Usage:DMSServicesImpl : searchDocuments():: end");
		return documentListVO;
	}

	/**
	 * This method is used to generate FileNet Query based on the user input
	 * 
	 * return List of DocumentInfoVO
	 * 
	 * @throws ServiceException
	 */
	public RepositoryRowSet generateSearchQuery(DocumentInfoVO docInfo) throws EngineRuntimeException, ServiceException {
		// LOG.debug("Usage:DMSServicesImpl : generateSearchQuery():: start");
		ObjectStore objectStoreName = null;
		String systemLine = docInfo.getMetadata().get(SYSTEMLINE);
		String osName = PropertyReader.get(systemLine);
		String temDocClass = PropertyReader.get(systemLine + DOCUMENTCLASS);
		final StringBuffer sqlToQuery1 = new StringBuffer();
		sqlToQuery1.append("Select * from");
		sqlToQuery1.append(EMPTYSTRING);
		sqlToQuery1.append(temDocClass);
		sqlToQuery1.append(EMPTYSTRING);
		sqlToQuery1.append(WHERECLAUSE);
		sqlToQuery1.append(EMPTYSTRING);
		sqlToQuery1.append(OPENBRACES);
		Iterator it = docInfo.getMetadata().entrySet().iterator();
		while (it.hasNext()) {
			MyHashMap.Entry pairs = (MyHashMap.Entry) it.next();
			if (!pairs.getKey().toString().equalsIgnoreCase(SEARCHDATEFROM)
					&& !pairs.getKey().toString().equalsIgnoreCase(SEARCHDATETO)) {
				if (!CommonUtil.isNullOrEmpty(pairs.getKey()) && !CommonUtil.isNullOrEmpty(pairs.getValue())) {
					sqlToQuery1.append(pairs.getKey() + EMPTYSTRING + LIKEOPERATOR + EMPTYSTRING + SINGLEQUOTE
							+ pairs.getValue() + SINGLEQUOTE);
					sqlToQuery1.append(EMPTYSTRING);
					sqlToQuery1.append(AND);
					sqlToQuery1.append(EMPTYSTRING);
				}
			}
		}
		if (docInfo.getMetadata().containsKey(SEARCHDATEFROM)) {
			String date = docInfo.getMetadata().get(SEARCHDATEFROM).toString();
			String filenetDate = P8Util.convertToFileNetDateFormat(date);
			sqlToQuery1.append(DATECREATED + GREATERTHANEQUAL + filenetDate);
			sqlToQuery1.append(EMPTYSTRING);
			sqlToQuery1.append(AND);
			sqlToQuery1.append(EMPTYSTRING);
		}
		if (docInfo.getMetadata().containsKey(SEARCHDATETO)) {
			String date = docInfo.getMetadata().get(SEARCHDATETO).toString();
			String filenetDate = P8Util.addOnedayPlusToFileNetDateFormat(date);
			sqlToQuery1.append(DATECREATED + LESSTHANEQUAL + filenetDate);
		}
		sqlToQuery1.append(CLOSEBRACES);
		sqlToQuery1.append(EMPTYSTRING).append(ORDERBYCLUASE);
		LOG.debug("SQlQUERY::" + sqlToQuery1.toString());
		objectStoreName = ceManager.getObjectStore(osName);
		SearchScope search = new SearchScope(objectStoreName);
		SearchSQL sqlObject = new SearchSQL();
		sqlObject.setDistinct();
		sqlObject.setQueryString(sqlToQuery1.toString());
		RepositoryRowSet documents = (RepositoryRowSet) search.fetchRows(sqlObject, null, null, true);
		// LOG.debug("Usage:DMSServicesImpl : generateSearchQuery():: end");
		return documents;
	}

	/**
	 * This method forms the DocumentInfoVO from the search results
	 * 
	 * @param objProperties
	 *            - Properties
	 * @return
	 * @throws ServiceException
	 */
	private DocumentInfoVO generateDocumentVO(Properties objProperties) throws ServiceException {
		// LOG.debug("Usage:DMSServicesImpl : generateDocumentVO():: start");
		DocumentInfoVO documentInfo = new DocumentInfoVO();
		Iterator objPropIter = objProperties.iterator();
		Property objProperty = null;
		String sysProp = PropertyReader.get("SystemProperty");
		List<String> sysPropList = Arrays.asList(sysProp.split("\\s*,\\s*"));
		String multiProp = PropertyReader.get("MultiValueProperty");
		List<String> multiPropList = Arrays.asList(multiProp.split("\\s*,\\s*"));
		MyHashMap filenetProp = new MyHashMap();
		if (objProperties != null) {
			while (objPropIter.hasNext()) {
				objProperty = (Property) objPropIter.next();
				if (objProperty.getPropertyName() != null) {
					if (!sysPropList.contains(objProperty.getPropertyName())) {
						if (objProperty.getPropertyName() != null && objProperty.getObjectValue() != null) {
							if (objProperty.getPropertyName().equalsIgnoreCase(DATECREATED)) {
								filenetProp.put(objProperty.getPropertyName().toString(), P8Util
										.convertToSimpleDateFormatforDisplay(objProperty.getObjectValue().toString()));
							} else {
								if (multiPropList.contains(objProperty.getPropertyName().toString())) {
									List<String> multiList = (List<String>) objProperty.getObjectValue();
									StringBuffer tempMultiValue = new StringBuffer();
									if (!multiList.isEmpty() && multiList != null) {
										Iterator<String> multiListItr = multiList.iterator();
										while (multiListItr.hasNext()) {
											String propValue = (String) multiListItr.next();
											tempMultiValue.append(propValue);
											if (multiListItr.hasNext()) {
												tempMultiValue.append(";");
											}
										}
										filenetProp.put(objProperty.getPropertyName().toString(),
												tempMultiValue.toString());
										// LOG.debug("MULTIVALUS LIST Value in Serch::"
										// + tempMultiValue.toString());
									}
								} else {
									filenetProp.put(objProperty.getPropertyName().toString(), objProperty
											.getObjectValue().toString());
								}
							}
						}
					}
				}
			}
			documentInfo.setMetadata(filenetProp);
		}
		// LOG.debug("Usage:DMSServicesImpl : generateDocumentVO():: end");
		return documentInfo;
	}

	/**
	 * This method is used to Upload the documents from FileNet
	 * 
	 * return String
	 * 
	 * @throws ServiceException
	 */
	@Override
	public List<DocumentInfoVO> uploadDocuments(List<DocumentInfoVO> documentInfo) throws ServiceException {
		LOG.debug("Usage:DMSServicesImpl : uploadDocuments():: start >> documentInfo >> " + documentInfo);
		ObjectStore objectStoreName = null;
		UpdatingBatch batchUpdate = null;
		try {
			init();
			String systemLine = null;
			Iterator<DocumentInfoVO> mandatoryIterator = documentInfo.iterator();
			while (mandatoryIterator.hasNext()) {
				DocumentInfoVO docInfo = (DocumentInfoVO) mandatoryIterator.next();
				systemLine = docInfo.getMetadata().get(SYSTEMLINE);
				if (CommonUtil.isNullOrEmpty(docInfo.getMetadata().get(UPLOADBY))) {
					docInfo.getMetadata().put("UPLOADBY", "Chitra");
				}
				boolean mandatoryFieldCheck = false;
				mandatoryFieldCheck = ServiceValidator.validateDataFieldsForUpload(docInfo);
				if (!mandatoryFieldCheck) {
					throw new ServiceException(MANDATORY_FIELD_MISSING_ERROR, MANDATORY_FIELD_MISSING_ERROR_MSG);
				}
			}
			String osName = PropertyReader.get(systemLine);
			String docClass = PropertyReader.get(systemLine + DOCUMENTCLASS);
			Domain domain = ceManager.getDomain();
			batchUpdate = UpdatingBatch.createUpdatingBatchInstance(domain, RefreshMode.REFRESH);
			objectStoreName = ceManager.getObjectStore(osName);
			String folderName = BACKSLASH + PropertyReader.get("FOLDER_NAME");
			Folder folder = Factory.Folder.getInstance(objectStoreName, null, folderName);
			Iterator<DocumentInfoVO> iterator = documentInfo.iterator();
			String multiProp = PropertyReader.get("MultiValueProperty");
			List<String> multiPropList = Arrays.asList(multiProp.split("\\s*,\\s*"));
			while (iterator.hasNext()) {
				DocumentInfoVO docInfo = (DocumentInfoVO) iterator.next();
				Document document = Factory.Document.createInstance(objectStoreName, docClass);
				Properties property = document.getProperties();
				Iterator propIterator = docInfo.getMetadata().entrySet().iterator();
				while (propIterator.hasNext()) {
					MyHashMap.Entry pairs = (MyHashMap.Entry) propIterator.next();
					LOG.debug("Key:Value>>" + pairs.getKey() + ":" + pairs.getValue());
					if (pairs.getKey() != null && pairs.getValue() != null) {
						if (multiPropList.contains(pairs.getKey().trim())) {
							List<String> getMultiPropList = new ArrayList<String>(Arrays.asList(pairs.getValue().split(
									";")));
							Iterator<String> tempList = getMultiPropList.iterator();
							StringList multiProplist = Factory.StringList.createList();
							while (tempList.hasNext()) {
								multiProplist.add(tempList.next());
							}
							property.putValue(pairs.getKey(), multiProplist);
						} else {
							if (pairs.getKey().toString().trim().equals(DOCUMENTTITLE)) {
								String docTitle = pairs.getValue().toString().trim();
								String ext = null;
								DateFormat cmsFormatter = new SimpleDateFormat(DOC_TIME_FORMAT);
								String timeStamp = cmsFormatter.format(new Date()).toString();
								int dot = docTitle.lastIndexOf(DOT);
								String baseFileName = (dot == -1) ? docTitle : docTitle.substring(0, dot);
								if (dot > 0) {
									ext = docTitle.substring(dot + 1);
								}
								property.putValue(pairs.getKey().toString(), baseFileName + UNDERSCORE + timeStamp
										+ DOT + ext);
							} else {
								property.putValue(pairs.getKey().toString(), pairs.getValue().toString());
							}
						}
					}
				}
				ContentElementList contentList = null;
				contentList = createContentElements(docInfo);
				if (contentList != null) {
					document.set_ContentElements(contentList);
				}
				document.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
				batchUpdate.add(document, null);
				ReferentialContainmentRelationship reference = folder.file(document, AutoUniqueName.NOT_AUTO_UNIQUE,
						null, DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
				batchUpdate.add(reference, null);
			}
			batchUpdate.updateBatch();
			// /****************** This is to set GUID to the VO List
			List<BatchItemHandleImpl> l = batchUpdate.getBatchItemHandles(null);
			for (int i = 0; i < l.size(); i++) {
				if (i % 2 == 0) {
					BatchItemHandleImpl object = l.get(i);
					String guid = object.getObject().getProperties().getIdValue(ID).toString();
					documentInfo.get(i / 2).getMetadata().put(ID, guid);
				}
			}
			// ////////////////**************** END
			LOG.debug(documentInfo.size() + " Docs created sucessfully");
		} catch (IOException e) {
			LOG.error("DMS Upload Parsing Error::" + e);
			throw new ServiceException(DOCUMENT_PARSING_ERROR, DOCUMENT_PARSING_ERROR_MSG);
		} catch (EngineRuntimeException e) {
			LOG.error("DMS Upload Error::" + e);
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
		} catch (Exception e) {
			LOG.error("DMS common exception error::" + e);
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
		}
		LOG.debug("Usage:DMSServicesImpl : uploadDocuments():: end");
		return documentInfo;
	}

	/**
	 * This method is used to create content element list for uploading
	 * documents
	 * 
	 * return List of DocumentInfoVO
	 * 
	 * @throws ServiceException
	 *             IO Exception
	 */

	private ContentElementList createContentElements(DocumentInfoVO documentInfo) throws ServiceException, IOException {
		LOG.debug("Usage:DMSServicesImpl : createContentElements():: start");
		ContentElementList contentElements = null;
		byte[] bytes = null;
		ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
		bytes = documentInfo.getByteArrayStream();
		InputStream stream = new ByteArrayInputStream(bytes);
		if (stream != null) {
			contentTransfer.setCaptureSource(stream);
		}
		String tmpmimeType = documentInfo.getMetadata().get(DOCUMENTTITLE);
		String mimeType = null;
		int i = tmpmimeType.lastIndexOf('.');
		if (i > 0) {
			mimeType = tmpmimeType.substring(i + 1);
		}
		if (mimeType.equalsIgnoreCase(PDF)) {
			mimeType = MIME_TYPE_PDF;
		} else if (mimeType.equalsIgnoreCase(JPG) || mimeType.equalsIgnoreCase(JPEG)) {
			mimeType = MIME_TYPE_JPG_OR_JPEG;
		} else if (mimeType.equalsIgnoreCase(GIF)) {
			mimeType = MIME_TYPE_GIF;
		} else if (mimeType.equalsIgnoreCase(PNG)) {
			mimeType = MIME_TYPE_PNG;
		} else if (mimeType.equalsIgnoreCase(GIF)) {
			mimeType = MIME_TYPE_BMP;
		} else if (mimeType.equalsIgnoreCase(TIFF) || mimeType.equalsIgnoreCase(TIF)) {
			mimeType = MIME_TYPE_TIFF_OR_TIF;
		} else if (mimeType.equalsIgnoreCase(XLS)) {
			mimeType = MIME_TYPE_XLS;
		} else if (mimeType.equalsIgnoreCase(XLSX)) {
			mimeType = MIME_TYPE_XLSX;
		} else if (mimeType.equalsIgnoreCase(DOC)) {
			mimeType = MIME_TYPE_DOC;
		} else if (mimeType.equalsIgnoreCase(DOCX)) {
			mimeType = MIME_TYPE_DOCX;
		} else {
			throw new ServiceException(FILE_FORAMT_ERROR, FILE_FORAMT_ERROR_MSG + ":::" + "Error File Name: "
					+ tmpmimeType);
		}
		contentTransfer.set_ContentType(mimeType);
		contentElements = Factory.ContentElement.createList();
		contentElements.add(contentTransfer);
		LOG.debug("Usage:DMSServicesImpl : createContentElements():: end");
		return contentElements;
	}

	/**
	 * This method is used to get the content stream of the documents from
	 * FileNet
	 * 
	 * return DocumentInfoVO
	 * 
	 * @throws ServiceException
	 */
	@Override
	public DocumentInfoVO viewDocument(DocumentInfoVO documentInfo) throws ServiceException {
		LOG.debug("Usage:DMSServicesImpl : viewDocument():: start");
		ObjectStore objectStoreName = null;
		DocumentInfoVO contentInfo = null;
		String documentId = null;
		boolean mandatoryFieldCheck = false;
		init();
		mandatoryFieldCheck = ServiceValidator.validateDataFieldsForView(documentInfo);
		if (mandatoryFieldCheck) {
			String systemLine = documentInfo.getMetadata().get(SYSTEMLINE);
			String osName = PropertyReader.get(systemLine);
			String temDocClass = PropertyReader.get(systemLine + DOCUMENTCLASS);
			try {
				objectStoreName = ceManager.getObjectStore(osName);
				if (!CommonUtil.isNullOrEmpty(documentInfo.getMetadata().get("Id"))) {
					documentId = documentInfo.getMetadata().get(ID);
					contentInfo = getContentStream(objectStoreName, documentId);
				} else {
					final StringBuffer sqlToQuery = new StringBuffer();
					sqlToQuery.append("Select Top 1 * from");
					sqlToQuery.append(EMPTYSTRING);
					sqlToQuery.append(temDocClass);
					sqlToQuery.append(EMPTYSTRING);
					sqlToQuery.append(WHERECLAUSE);
					sqlToQuery.append(EMPTYSTRING);
					sqlToQuery.append(OPENBRACES);
					Iterator it = documentInfo.getMetadata().entrySet().iterator();
					int flag = 0;
					while (it.hasNext()) {
						MyHashMap.Entry pairs = (MyHashMap.Entry) it.next();
						if (!CommonUtil.isNullOrEmpty(pairs.getKey()) && !CommonUtil.isNullOrEmpty(pairs.getValue())) {
							if (flag != 0) {
								sqlToQuery.append(EMPTYSTRING);
								sqlToQuery.append(AND);
								sqlToQuery.append(EMPTYSTRING);
							}
							sqlToQuery.append(pairs.getKey() + EMPTYSTRING + LIKEOPERATOR + EMPTYSTRING + SINGLEQUOTE
									+ pairs.getValue() + SINGLEQUOTE);
							flag++;
						}
					}
					sqlToQuery.append(CLOSEBRACES);
					sqlToQuery.append(ORDERBYCLUASE);
					LOG.debug("SQlQUERY::" + sqlToQuery.toString());
					objectStoreName = ceManager.getObjectStore(osName);
					SearchScope search = new SearchScope(objectStoreName);
					SearchSQL sqlObject = new SearchSQL();
					sqlObject.setDistinct();
					sqlObject.setQueryString(sqlToQuery.toString());
					RepositoryRowSet documents = (RepositoryRowSet) search.fetchRows(sqlObject, null, null, true);
					Iterator iterator = documents.iterator();
					while (iterator.hasNext()) {
						RepositoryRow repositoryRow = (RepositoryRow) iterator.next();
						documentId = repositoryRow.getProperties().getIdValue(ID).toString();
					}
					if (documentId != null) {
						contentInfo = getContentStream(objectStoreName, documentId);
					}
				}
			} catch (EngineRuntimeException e) {
				LOG.error("DMS View Error::" + e);
				ExceptionCode code = e.getExceptionCode();
				if (code.equals(ExceptionCode.E_BAD_CLASSID)) {
					throw new ServiceException(E_BAD_CLASSID, E_BAD_CLASSID_ERROR_MSG + EMPTYSTRING + OPENBRACES
							+ documentId + CLOSEBRACES);
				} else if (code.equals(ExceptionCode.E_NULL_OR_INVALID_PARAM_VALUE)) {
					throw new ServiceException(E_NULL_OR_INVALID_PARAM_VALUE, e.getMessage());
				} else if (code.equals(ExceptionCode.E_OBJECT_NOT_FOUND)) {
					throw new ServiceException(E_OBJECT_NOT_FOUND, E_OBJECT_NOT_FOUND_ERROR_MSG);
				} else {
					throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
				}
			} catch (IOException e) {
				LOG.error("DMS View Content Error::" + e);
				throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
			} catch (Exception e) {
				LOG.error("DMS common exception error::" + e);
				throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
			}
		} else {
			throw new ServiceException(MANDATORY_FIELD_MISSING_ERROR, MANDATORY_FIELD_MISSING_ERROR_MSG);
		}
		LOG.debug("Usage:DMSServicesImpl : viewDocument():: end");
		return contentInfo;

	}

	/**
	 * This method is used to get the content stream from FileNet
	 * 
	 * @param ObjectStore
	 *            - osName, String - documentId
	 * @return
	 * 
	 */
	public DocumentInfoVO getContentStream(ObjectStore osName, String documentId) throws IOException {
		LOG.debug("Usage:DMSServicesImpl : getContentStream():: start");
		DocumentInfoVO contentInfo = null;
		InputStream is = null;
		PropertyFilter contentPropertyFilter = new PropertyFilter();
		contentPropertyFilter.addIncludeType(0, null, Boolean.TRUE, FilteredPropertyType.ANY, null);
		Document doc = Factory.Document.fetchInstance(osName, documentId, contentPropertyFilter);
		// Get content elements and iterate list.
		ContentElementList docContentList = doc.get_ContentElements();
		Iterator iter = docContentList.iterator();
		while (iter.hasNext()) {
			contentInfo = new DocumentInfoVO();
			ContentTransfer ct = (ContentTransfer) iter.next();
			is = ct.accessContentStream();
			byte[] bytes = IOUtils.toByteArray(is);
			contentInfo.setByteArrayStream(bytes);
			String filenetMimetype = ct.get_ContentType().trim();
			String mimeType = getMimeType(filenetMimetype);
			MyHashMap filenetProp = new MyHashMap();
			filenetProp.put(MIMETYPE, mimeType);
			filenetProp.put(DOCUMENTTITLE, doc.getProperties().getStringValue(DOCUMENTTITLE));
			contentInfo.setMetadata(filenetProp);
		}
		LOG.debug("Usage:DMSServicesImpl : getContentStream():: end");
		return contentInfo;

	}

	/**
	 * This method is used to convert the FileNet mime types to normal file
	 * extension
	 * 
	 * @param String
	 *            - mimeType
	 * @return
	 * 
	 */

	public String getMimeType(String mimeType) {
		String convertedMimeType = null;
		if (mimeType.equalsIgnoreCase(MIME_TYPE_PDF)) {
			convertedMimeType = PDF;
		} else if (mimeType.equalsIgnoreCase(MIME_TYPE_JPG_OR_JPEG)) {
			convertedMimeType = JPEG;
		} else if (mimeType.equalsIgnoreCase(MIME_TYPE_GIF)) {
			convertedMimeType = GIF;
		} else if (mimeType.equalsIgnoreCase(MIME_TYPE_PNG)) {
			convertedMimeType = PNG;
		} else if (mimeType.equalsIgnoreCase(MIME_TYPE_BMP)) {
			convertedMimeType = BMP;
		} else if (mimeType.equalsIgnoreCase(MIME_TYPE_TIFF_OR_TIF)) {
			convertedMimeType = TIFF;
		} else if (mimeType.equalsIgnoreCase(MIME_TYPE_XLS)) {
			convertedMimeType = XLS;
		} else if (mimeType.equalsIgnoreCase(MIME_TYPE_XLSX)) {
			convertedMimeType = XLSX;
		} else if (mimeType.equalsIgnoreCase(MIME_TYPE_DOC)) {
			convertedMimeType = DOC;
		} else if (mimeType.equalsIgnoreCase(MIME_TYPE_DOCX)) {
			convertedMimeType = DOCX;
		}
		return convertedMimeType;
	}

	/**
	 * This method is used to delete the documents from FileNet
	 * 
	 * return String
	 * 
	 * @throws ServiceException
	 */
	@Override
	public String deleteDocument(DocumentInfoVO documentInfo) throws ServiceException {
		LOG.debug("Usage:DMSServicesImpl : deleteDocument():: start");
		ObjectStore objectStoreName = null;
		String documentId = null;
		String deleteBy = null;
		try {
			init();
			boolean mandatoryFieldCheck = false;
			mandatoryFieldCheck = ServiceValidator.validateDataFieldsForDelete(documentInfo);
			if (mandatoryFieldCheck) {
				String systemLine = documentInfo.getMetadata().get(SYSTEMLINE);
				String osName = PropertyReader.get(systemLine);
				objectStoreName = ceManager.getObjectStore(osName);
				documentId = documentInfo.getMetadata().get(ID);
				deleteBy = documentInfo.getMetadata().get(DELETEBY);
				Document doc = Factory.Document.fetchInstance(objectStoreName, documentId, null);
				Properties props = doc.getProperties();
				// Change property value.
				props.putValue("DeleteBy", deleteBy);
				doc.save(RefreshMode.REFRESH);
				doc.delete();
				doc.save(RefreshMode.NO_REFRESH);
			} else {
				throw new ServiceException(MANDATORY_FIELD_MISSING_ERROR, MANDATORY_FIELD_MISSING_ERROR_MSG);
			}
		} catch (EngineRuntimeException exception) {
			LOG.error("DMS Delete Document Error::" + exception);
			ExceptionCode code = exception.getExceptionCode();
			if (code.equals(ExceptionCode.E_ACCESS_DENIED) || code.equals(ExceptionCode.E_NOT_AUTHENTICATED)) {
				throw new ServiceException(E_ACCESS_DENIED, E_ACCESS_DENIED_ERROR_MSG);
			} else if (code.equals(ExceptionCode.E_OBJECT_NOT_FOUND)) {
				throw new ServiceException(E_OBJECT_NOT_FOUND, E_OBJECT_NOT_FOUND_ERROR_MSG);
			} else if (code.equals(ExceptionCode.E_BAD_CLASSID)) {
				throw new ServiceException(E_BAD_CLASSID, E_BAD_CLASSID_ERROR_MSG + EMPTYSTRING + OPENBRACES
						+ documentId + CLOSEBRACES);
			} else {
				throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
			}
		} catch (Exception e) {
			LOG.error("DMS common exception error::" + e);
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
		}
		LOG.debug("Usage:DMSServicesImpl : deleteDocument():: end");
		return DELETE_SUCCESS_MESSAGE;
	}

	/**
	 * This method is used to get the List of Document stream from FileNet
	 * 
	 * return List<DocumentInfoVO?
	 * 
	 * @throws ServiceException
	 */

	@Override
	public List<DocumentInfoVO> downloadDocuments(List<DocumentInfoVO> documentInfo) throws ServiceException {
		LOG.debug("Usage:DMSServicesImpl : downloadDocuments():: start");
		List<DocumentInfoVO> documentInfoList = new ArrayList<DocumentInfoVO>();
		DocumentInfoVO contentInfo = null;
		ObjectStore objectStoreName = null;
		String documentId = null;
		try {
			init();
			Iterator<DocumentInfoVO> mandatoryIterator = documentInfo.iterator();
			while (mandatoryIterator.hasNext()) {
				DocumentInfoVO docInfo = (DocumentInfoVO) mandatoryIterator.next();
				boolean mandatoryFieldCheck = false;
				mandatoryFieldCheck = ServiceValidator.validateDataFieldsForDownload(docInfo);
				if (!mandatoryFieldCheck) {
					throw new ServiceException(MANDATORY_FIELD_MISSING_ERROR, MANDATORY_FIELD_MISSING_ERROR_MSG);
				}
			}
			Iterator<DocumentInfoVO> iterator = documentInfo.iterator();
			while (iterator.hasNext()) {
				DocumentInfoVO documentInfoitr = (DocumentInfoVO) iterator.next();
				String systemLine = documentInfoitr.getMetadata().get(SYSTEMLINE);
				String osName = PropertyReader.get(systemLine);
				objectStoreName = ceManager.getObjectStore(osName);
				if (!CommonUtil.isNullOrEmpty(documentInfoitr.getMetadata().get("Id"))) {
					documentId = documentInfoitr.getMetadata().get(ID);
					contentInfo = getContentStream(objectStoreName, documentId);
				}
				documentInfoList.add(contentInfo);
			}
		} catch (IOException e) {
			LOG.error("DMS Download Error::" + e);
			throw new ServiceException(DOCUMENT_PARSING_ERROR, DOCUMENT_PARSING_ERROR_MSG);
		} catch (EngineRuntimeException e) {
			LOG.error("DMS Download Error::" + e);
			ExceptionCode code = e.getExceptionCode();
			if (code.equals(ExceptionCode.E_BAD_CLASSID)) {
				throw new ServiceException(E_BAD_CLASSID, E_BAD_CLASSID_ERROR_MSG + EMPTYSTRING + OPENBRACES
						+ documentId + CLOSEBRACES);
			} else if (code.equals(ExceptionCode.E_NULL_OR_INVALID_PARAM_VALUE)) {
				throw new ServiceException(E_NULL_OR_INVALID_PARAM_VALUE, e.getMessage());
			} else if (code.equals(ExceptionCode.E_OBJECT_NOT_FOUND)) {
				throw new ServiceException(E_OBJECT_NOT_FOUND, E_OBJECT_NOT_FOUND_ERROR_MSG);
			} else {
				throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
			}
		} catch (Exception e) {
			LOG.error("DMS common exception error::" + e);
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
		}
		LOG.debug("Usage:DMSServicesImpl : downloadDocuments():: end");
		return documentInfoList;
	}

	/**
	 * This method is used to merge the Document content stream from FileNet
	 * 
	 * return Byte []
	 * 
	 * @throws ServiceException
	 */
	@Override
	public byte[] mergeDocuments(DocumentInfoVO documentInfo) throws ServiceException {
		LOG.debug("Usage:DMSServicesImpl : mergeDocuments():: start");
		byte[] mergedBytes = null;
		List<InputStream> isList = new ArrayList<InputStream>();
		try {
			init();
			boolean mandatoryFieldCheck = false;
			String docTitle = documentInfo.getMetadata().get("DocumentType");
			mandatoryFieldCheck = ServiceValidator.validateDataFieldsForMerge(documentInfo);
			if (mandatoryFieldCheck) {
				
				String osName = PropertyReader.get("OFS");
				String docClass = PropertyReader.get("OFSDocumentClass");
				ObjectStore os = ceManager.getObjectStore(osName);
				final StringBuffer sqlToQuery = new StringBuffer();
				sqlToQuery.append("Select * from");
				sqlToQuery.append(EMPTYSTRING);
				sqlToQuery.append(docClass);
				sqlToQuery.append(EMPTYSTRING);
				sqlToQuery.append(WHERECLAUSE);
				sqlToQuery.append(EMPTYSTRING);
				sqlToQuery.append(OPENBRACES);
				Iterator it = documentInfo.getMetadata().entrySet().iterator();
				int flag = 0;
				while (it.hasNext()) {
					MyHashMap.Entry pairs = (MyHashMap.Entry) it.next();
					if (!pairs.getKey().toString().equalsIgnoreCase(SEARCHDATEFROM)
							&& !pairs.getKey().toString().equalsIgnoreCase(SEARCHDATETO)) {
						if (!CommonUtil.isNullOrEmpty(pairs.getKey()) && !CommonUtil.isNullOrEmpty(pairs.getValue())) {
							if (flag != 0) {
								sqlToQuery.append(EMPTYSTRING);
								sqlToQuery.append(AND);
								sqlToQuery.append(EMPTYSTRING);
							}
							sqlToQuery.append(pairs.getKey() + EMPTYSTRING + EQUALTOOPERATOR + EMPTYSTRING
									+ SINGLEQUOTE + pairs.getValue() + SINGLEQUOTE);
							flag++;
						}
					}
				}
				sqlToQuery.append(CLOSEBRACES);
				sqlToQuery.append(EMPTYSTRING).append(ORDERBYCLUASE);
				LOG.debug("SQlQUERY::" + sqlToQuery.toString());
				SearchScope search = new SearchScope(os);
				SearchSQL sqlObject = new SearchSQL();
				sqlObject.setDistinct();
				sqlObject.setQueryString(sqlToQuery.toString());
				
				RepositoryRowSet documents = (RepositoryRowSet) search.fetchRows(sqlObject, null, null, true);
				
				if (!documents.isEmpty()) {
					Iterator<RepositoryRow> iterator = documents.iterator();
					String documentId = null;
					String tempValue = null;
					TreeMap<String, String> unSortedMap = new TreeMap<String, String>();
					while (iterator.hasNext()) {
						RepositoryRow repositoryRow = (RepositoryRow) iterator.next();
						documentId = repositoryRow.getProperties().getIdValue(ID).toString();
						if (documentInfo.getMetadata().get(DOCUMENTTYPE).toString().trim()
								.equalsIgnoreCase(LINER_INVOICE.trim())) {
							tempValue = repositoryRow.getProperties().getStringListValue(INVOICENO).get(0).toString();
							unSortedMap.put(tempValue, documentId);
						} else if (documentInfo.getMetadata().get(DOCUMENTTYPE).toString().trim()
								.equalsIgnoreCase(LINER_DEBITNOTE.trim())) {
							tempValue = repositoryRow.getProperties().getStringListValue(DEBITNOTENO).get(0).toString();
							unSortedMap.put(tempValue, documentId);
						} else if (documentInfo.getMetadata().get(DOCUMENTTYPE).toString().trim()
								.equalsIgnoreCase(LINER_CREDITNOTE.trim())) {
							tempValue = repositoryRow.getProperties().getStringListValue(CREDITNOTENO).get(0)
									.toString();
							unSortedMap.put(tempValue, documentId);
						}
					}
					if (!unSortedMap.isEmpty()) {
						for (Entry<String, String> entry : unSortedMap.entrySet()) {
							//String key = entry.getKey();
							String value = entry.getValue();
							// LOG.debug("Key=>Value" + key + " => " + value);
							InputStream is = getDocumentContentStream(os, value.toString());
							isList.add(is);
						}
					} else {
						throw new ServiceException(DOCUMENT_MERGR_MAP_EMPTY_ERROR, DOCUMENT_MERGR_MAP_EMPTY_ERROR_MSG);
					}
					if (!isList.isEmpty()) {
						mergedBytes = doMerge(isList, docTitle);
						LOG.debug("Merged byte Length::" + mergedBytes.length);
					}
				} else {
					throw new ServiceException("NO_DOCUMENTS", "No documents found");
				}
			} else {
				throw new ServiceException(MANDATORY_FIELD_MISSING_ERROR, MANDATORY_FIELD_MISSING_ERROR_MSG);
			}
		} catch (DocumentException e) {
			LOG.error("DMS Merge doc Exception::" + e);
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
		} catch (EngineRuntimeException e) {
			LOG.error("DMS Search Exception::" + e);
			e.printStackTrace();
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
		} catch (IOException e) {
			LOG.error("IO Exception::" + e);
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
		} catch (Exception e) {
			LOG.error("DMS common exception error::" + e);
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
		}
		LOG.debug("Usage:DMSServicesImpl : mergeDocuments():: end");
		return mergedBytes;
	}

	/**
	 * This method is used to get the content stream from FileNet
	 * 
	 * @param ObjectStore
	 *            - osName, String - documentId
	 * @return
	 * 
	 */
	public InputStream getDocumentContentStream(ObjectStore osName, String documentId) throws EngineRuntimeException {
		InputStream is = null;
		PropertyFilter contentPropertyFilter = new PropertyFilter();
		contentPropertyFilter.addIncludeType(0, null, Boolean.TRUE, FilteredPropertyType.ANY, null);
		Document doc = Factory.Document.fetchInstance(osName, documentId, contentPropertyFilter);
		// Get content elements and iterate list.
		ContentElementList docContentList = doc.get_ContentElements();
		Iterator iter = docContentList.iterator();
		while (iter.hasNext()) {
			ContentTransfer ct = (ContentTransfer) iter.next();
			is = ct.accessContentStream();
		}
		return is;
	}

	/**
	 * Merge multiple pdf into one pdf
	 * 
	 * @throws DocumentException
	 * @throws IOException
	 */
	public byte[] doMerge(List<InputStream> inputStream, String docTitle) throws DocumentException, IOException {
		DateFormat cmsFormatter = new SimpleDateFormat("yyyy-MM-dd'_'HHmmssSSS");
		String timeStamp = cmsFormatter.format(new Date()).toString();
		String documentTitle = docTitle + UNDERSCORE + timeStamp + FILE_EXTENSION_PDF;
		OutputStream outputStream = new FileOutputStream(new File("D:/Downloads/" + documentTitle));
		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		Iterator itr = inputStream.iterator();
		while (itr.hasNext()) {
			InputStream is = (InputStream) itr.next();
			PdfReader reader = new PdfReader(is);
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				document.newPage();
				// import the page from source pdf
				PdfImportedPage page = writer.getImportedPage(reader, i);
				// add the page to the destination pdf
				cb.addTemplate(page, 0, 0);
			}
		}
		outputStream.flush();
		document.close();
		outputStream.close();
		String path = "D:/Downloads/" + documentTitle;
		InputStream stream = new FileInputStream(path);
		byte[] bytes = IOUtils.toByteArray(stream);
		return bytes;
	}
}
