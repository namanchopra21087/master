package com.giga.dms;

/**
 * Interface that has all the SERVICE NAME KEYS.
 * These KEYS are the keys that are specified in gigaservices.properties file
 * example: CHARGE_CODE_SERVICE = com.giga.master.service.ChargeCodeServiceImpl  
 * 
 * @author ganeshkumar.t
 *
 */
public interface IDMSService {
	
	/** place all the service names from BUSINESS LAYER **/ 
	public interface businessLayer {
		String DMSG_SERVICE = "DMSGService";

	}


	/** place all the service names from DAO LAYER **/
	public interface daoLayer{
		String DMSG_DAO = "DMSGDao";
	}
}