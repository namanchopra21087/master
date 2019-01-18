package com.giga.dms.exception;

public class ServiceException extends Exception {
	
	private String errorCd ;
	private String errorMsg;
	
	public ServiceException(String errorCd, String errMsg) {
		super(errMsg);
		this.errorCd = errorCd;
		this.errorMsg = errMsg;
	}

	public ServiceException(String errorCd, String errMsg,Throwable t) {
		super(errMsg);
		this.errorCd = errorCd;
		this.errorMsg = errMsg;
	}

	
	

	public String getErrorCd() {
		return errorCd;
	}

	public void setErrorCd(String errorCd) {
		this.errorCd = errorCd;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
