package com.giga.dms.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DMSResultObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean resultFlag;
	private String resultMessage;
	private List<DMSSearchConentVO> searchDocmentResult;
	private DMSDocumentVO viewDocumentResult;
	private List<DMSDocumentVO> downloadDocumentResult;
	
	public boolean isResultFlag() {
		return resultFlag;
	}
	public void setResultFlag(boolean resultFlag) {
		this.resultFlag = resultFlag;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	public List<DMSSearchConentVO> getSearchDocmentResult() {
		return searchDocmentResult;
	}
	public void setSearchDocmentResult(List<DMSSearchConentVO> searchDocmentResult) {
		this.searchDocmentResult = searchDocmentResult;
	}
	public DMSDocumentVO getViewDocumentResult() {
		return viewDocumentResult;
	}
	public void setViewDocumentResult(DMSDocumentVO viewDocumentResult) {
		this.viewDocumentResult = viewDocumentResult;
	}
	public List<DMSDocumentVO> getDownloadDocumentResult() {
		return downloadDocumentResult;
	}
	public void setDownloadDocumentResult(List<DMSDocumentVO> downloadDocumentResult) {
		this.downloadDocumentResult = downloadDocumentResult;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
