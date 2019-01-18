package com.giga.dms.util;

import java.util.Date;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.giga.dms.exception.ServiceException;
import com.giga.dms.vo.DocumentInfoVO;

public class ServiceValidator implements ServiceConstants {

	private static final Logger LOG = LoggerFactory.getLogger(ServiceValidator.class);

	public static boolean validateDataFieldsForUpload(DocumentInfoVO documentInfo) throws ServiceException {

		String docName = documentInfo.getMetadata().get(DOCUMENTTITLE).toString();
		LOG.debug("validateDataFieldsForUpload >> Started for >> "+docName);

		if (CommonUtil.isNullOrEmpty(documentInfo.getMetadata().get(SYSTEMLINE))) {
			LOG.debug("Empty SYSTEMLINE >> File >> " + docName);
			throw new ServiceException(MANDATORY_FIELD_MISSING_ERROR, "Empty SYSTEMLINE >> File >> " + docName);
		}
		if (CommonUtil.isNullOrEmpty(documentInfo.getMetadata().get(DOCUMENTLINE))) {
			LOG.debug("Empty DOCUMENTLINE >> File >> " + docName);
			throw new ServiceException(MANDATORY_FIELD_MISSING_ERROR, "Empty DOCUMENTLINE >> File >> " + docName);
		}
		if (CommonUtil.isNullOrEmpty(documentInfo.getMetadata().get(DOCUMENTTYPE))) {
			LOG.debug("Empty DOCUMENTTYPE >> File >> " + docName);
			throw new ServiceException(MANDATORY_FIELD_MISSING_ERROR, "Empty DOCUMENTTYPE >> File >> " + docName);
		}
		if (CommonUtil.isNullOrEmpty(documentInfo.getMetadata().get(DOCUMENTTITLE))) {
			LOG.debug("Empty DOCUMENTTITLE >> File >> " + docName);
			throw new ServiceException(MANDATORY_FIELD_MISSING_ERROR, "Empty DOCUMENTTITLE >> File >> " + docName);
		}
		if (CommonUtil.isNullOrEmpty(documentInfo.getMetadata().get(COMPANYCODE))) {
			LOG.debug("Empty COMPANYCODE >> File >> " + docName);
			throw new ServiceException(MANDATORY_FIELD_MISSING_ERROR, "Empty COMPANYCODE >> File >> " + docName);
		}
		if (CommonUtil.isNullOrEmpty(documentInfo.getMetadata().get(UPLOADBY))) {
			LOG.debug("Empty UPLOADBY >> File >> " + docName);
			//throw new ServiceException(MANDATORY_FIELD_MISSING_ERROR, "Empty UPLOADBY >> File >> " + docName);
		}
		if (documentInfo.getByteArrayStream() == null) {
			LOG.debug("Empty Stream >> File >> " + docName);
			throw new ServiceException(MANDATORY_FIELD_MISSING_ERROR, "Empty Stream >> File >> " + docName);
		}

		if (docName.contains(".")) {
			return true;
		} else {
			throw new ServiceException(FILE_FORAMT_ERROR, FILE_FORAMT_EXTENSION_MSG + ":::" + "Error File Name: "
					+ docName);
		}
	}

	public static boolean validateDataFieldsForSearch(DocumentInfoVO documentInfo) throws ServiceException {
		if ((!CommonUtil.isNullOrEmpty(documentInfo.getMetadata().get(SYSTEMLINE))
				&& !CommonUtil.isNullOrEmpty(documentInfo.getMetadata().get(SEARCHDATEFROM))
				&& !CommonUtil.isNullOrEmpty(documentInfo.getMetadata().get(SEARCHDATETO)) && !CommonUtil
					.isNullOrEmpty(documentInfo.getMetadata().get(COMPANYCODE)))) {
			String fromdate = documentInfo.getMetadata().get(SEARCHDATEFROM).toString();
			Date dateFrom = P8Util.convertToDateFormat(fromdate);
			String toDate = documentInfo.getMetadata().get(SEARCHDATETO).toString();
			Date dateTo = P8Util.convertToDateFormat(toDate);
			if (dateFrom.before(dateTo) || dateFrom.equals(dateTo)) {
				return true;
			} else {
				throw new ServiceException(FROM_DATE_ERROR, FROM_DATE_ERROR_MSG);
			}
		}
		return false;
	}

	public static boolean validateDataFieldsForView(DocumentInfoVO documentInfo) {
		if (!CommonUtil.isNullOrEmpty(documentInfo.getMetadata().get(SYSTEMLINE))) {
			Iterator it = documentInfo.getMetadata().entrySet().iterator();
			int flag = 0;
			while (it.hasNext()) {
				MyHashMap.Entry pairs = (MyHashMap.Entry) it.next();
				if (!CommonUtil.isNullOrEmpty(pairs.getKey()) && !CommonUtil.isNullOrEmpty(pairs.getValue())) {
					LOG.debug("Key:Value>>" + pairs.getKey() + ":" + pairs.getValue());
					flag++;
				}
			}
			if (flag >= 2) {
				return true;
			}
		}
		return false;
	}

	public static boolean validateDataFieldsForDelete(DocumentInfoVO documentInfo) {
		if ((!CommonUtil.isNullOrEmpty(documentInfo.getMetadata().get(SYSTEMLINE))
				&& !CommonUtil.isNullOrEmpty(documentInfo.getMetadata().get(ID)) && !CommonUtil
					.isNullOrEmpty(documentInfo.getMetadata().get(DELETEBY)))) {
			return true;
		}
		return false;
	}

	public static boolean validateDataFieldsForDownload(DocumentInfoVO documentInfo) {
		if ((!CommonUtil.isNullOrEmpty(documentInfo.getMetadata().get(SYSTEMLINE)) && !CommonUtil
				.isNullOrEmpty(documentInfo.getMetadata().get(ID)))) {
			return true;
		}
		return false;
	}

	public static boolean validateDataFieldsForMerge(DocumentInfoVO documentInfo) {
		if ((!CommonUtil.isNullOrEmpty(documentInfo.getMetadata().get(VOYAGENO)) && !CommonUtil
				.isNullOrEmpty(documentInfo.getMetadata().get(DOCUMENTTYPE)))) {
			return true;
		}
		return false;
	}

}
