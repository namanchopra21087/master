package com.giga.dms.vo;

import com.giga.base.BaseVO;

@SuppressWarnings("serial")
public class DMSDocumentAuditHistoryVO extends BaseVO{
	private String daId;
	private String documentNumber;
	private String sourceSystem;
	private String actionBy;
	private String actionDate;
	private String remarks; 
	
	public DMSDocumentAuditHistoryVO() {
		super();
	}

	public String getDaId() {
		return daId;
	}

	public void setDaId(String daId) {
		this.daId = daId;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getActionBy() {
		return actionBy;
	}

	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
	}

	public String getActionDate() {
		return actionDate;
	}

	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@Override
	public String toString(){
		return "DMSDocumentAuditHistoryVO [daId = "+daId+" ,documentNumber = "+documentNumber+" ,sourceSystem = "+sourceSystem+
				" ,actionBy = "+actionBy+", actionDate = "+actionDate+" ,remarks = "+remarks+"]";
	}
	
}
