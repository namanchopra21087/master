package com.giga.dms.util.mail;

public interface DMSMailStereoTokens {
	String PREFIX = "$_$_";
	String SUFFIX = "_$_$";

	
	
/**************************Upload DMS START*****************************/
interface sendUploadDMSMail {
	String DOCUMENT_NAME = PREFIX + "DOCUMENTNAME" + SUFFIX;
	String DOCUMENT_TYPE = PREFIX + "DOCUMENTTYPE" + SUFFIX;
	String DOCUMENTTYPE_NO  = PREFIX + "DOCUMENTTYPENO" + SUFFIX;
	String REMARKS = PREFIX + "REMARKS" + SUFFIX;
}
/**************************Upload DMS END*****************************/

}
