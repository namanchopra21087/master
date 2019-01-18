package com.giga.dms.util;

/**
 * @author HCL
 *
 * This class is used to get CE Session. 
 */

import javax.security.auth.Subject;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.util.UserContext;
import com.giga.dms.exception.ServiceException;
import com.giga.dms.services.DMSServicesImpl;

/**
 * CEManager which manages CE connection and object store used by other programs
 * for multiple CE related operations
 * 
 */
public class CEManager implements ServiceConstants {
	
	//private static final Logger LOG = LoggerFactory.getLogger(CEManager.class);
	private static final Logger LOG = LoggerFactory.getLogger(DMSServicesImpl.class);

	private Domain domain;
	private Connection connection;

	private CEManager(String ceurl, String userId, String password)
			throws ServiceException {
		try {
			connection = Factory.Connection.getConnection(ceurl);
			UserContext userContext = UserContext.get();
			Subject subject = UserContext.createSubject(connection, userId,
					password, FILENET_STANZA);
			userContext.pushSubject(subject);
			domain = Factory.Domain.fetchInstance(connection, PropertyReader.get(FILENET_DOMAIN), null);
		} catch (EngineRuntimeException e) {
			LOG.error("Connection Error:"+e);
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
		}
	}

	public static CEManager getInstance(String ceURL, String userId,
			String password) throws ServiceException {
		try {
				CEManager ceOperations = new CEManager(ceURL, userId, password);
				return ceOperations;
		} catch (EngineRuntimeException e) {
			LOG.error("DMS Get Instance::"+e);
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG)	;
		}
	}

	/*
	 * Returns ObjectStore object for supplied object store name.
	 */
	public ObjectStore getObjectStore(String osName) throws ServiceException {
		try {
			ObjectStore os = Factory.ObjectStore.fetchInstance(domain, osName,
					null);
			return os;
		} catch (EngineRuntimeException e) {
			LOG.error("DMS get ObjectStore::"+e);
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
		}
	}

	/*
	 * Returns Domain object.
	 */
	public Domain getDomain() throws ServiceException {
		try {
			return domain;
		} catch (EngineRuntimeException e) {
			LOG.error("DMS get Domain::"+e);
			throw new ServiceException(TECHNICAL_ERROR, TECHNICAL_ERROR_MSG);
		}
	}
}
