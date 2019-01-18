package com.giga.dms.vo;

import java.io.Serializable;

public class DMSSearchConentVO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String systemLine="";
	private String documentLine="";
	private String documentType="";
	private String documentTitle="";
	
	private String businessLine="";
	private String serviceType="";
	private String serviceLine="";
	
	  private String dmsId="";
	  
	
	  
	  private String bookingNo="";
	  private String jobNo="";
	  private String siNo="";
	  private String incidentNo="";
	  private String claimNo="";
	  private String shipmentNo="";
	  private String quotationNo="";
	  private String invoiceNo="";
	  private String doNo="";
	  private String poNo="";
	  private String creditNoteNo="";
	  private String debitNoteNo="";
	  private String loadNo="";
	  private String workOrderNo="";
	  private String vesselName="";
	  private String voyageNo="";
	  
	  private String cargoType="";
	  private String portOfDischarge="";
	  private String PortOfLoading="";
	  
	  private String bookingCustomer="";
	  private String mimeType="";
	  private String uploadedBy="";
	  private String deletedBy="";
	  private String uploadFromDate="";
	  private String uploadToDate="";
	  private String uploadedDate="";
	  private String quotationDate="";
	  private String creditNoteDate="";
	  private String debitNoteDate="";
	  private String invoiceDate="";
	  private String poDate="";
	  
	  //YMS ADDED
	  private String deliveryNoteNo="";
	  private String pullingOrderNo="";
	  private String forwardingAgent="";
	  private String incidentDetails="";
	  private String consignee="";
	  private String chassisNo="";
	  private String insuranceAgent="";
	  private String responsibleParty="";
	  private String isCancelled="";
	  private String claimType="";
	  private String claimDate="";
	  private String incidentDate="";
	  private String remarks="";
	  
	  
	public String getSystemLine() {
		return systemLine;
	}
	public void setSystemLine(String systemLine) {
		this.systemLine = systemLine;
	}
	public String getDocumentLine() {
		return documentLine;
	}
	public void setDocumentLine(String documentLine) {
		this.documentLine = documentLine;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getDocumentTitle() {
		return documentTitle;
	}
	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}
	public String getBusinessLine() {
		return businessLine;
	}
	public void setBusinessLine(String businessLine) {
		this.businessLine = businessLine;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getServiceLine() {
		return serviceLine;
	}
	public void setServiceLine(String serviceLine) {
		this.serviceLine = serviceLine;
	}
	public String getDmsId() {
		return dmsId;
	}
	public void setDmsId(String dmsId) {
		this.dmsId = dmsId;
	}
	public String getBookingNo() {
		return bookingNo;
	}
	public void setBookingNo(String bookingNo) {
		this.bookingNo = bookingNo;
	}
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
	public String getSiNo() {
		return siNo;
	}
	public void setSiNo(String siNo) {
		this.siNo = siNo;
	}
	public String getIncidentNo() {
		return incidentNo;
	}
	public void setIncidentNo(String incidentNo) {
		this.incidentNo = incidentNo;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getShipmentNo() {
		return shipmentNo;
	}
	public void setShipmentNo(String shipmentNo) {
		this.shipmentNo = shipmentNo;
	}
	public String getQuotationNo() {
		return quotationNo;
	}
	public void setQuotationNo(String quotationNo) {
		this.quotationNo = quotationNo;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getDoNo() {
		return doNo;
	}
	public void setDoNo(String doNo) {
		this.doNo = doNo;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getCreditNoteNo() {
		return creditNoteNo;
	}
	public void setCreditNoteNo(String creditNoteNo) {
		this.creditNoteNo = creditNoteNo;
	}
	public String getDebitNoteNo() {
		return debitNoteNo;
	}
	public void setDebitNoteNo(String debitNoteNo) {
		this.debitNoteNo = debitNoteNo;
	}
	public String getLoadNo() {
		return loadNo;
	}
	public void setLoadNo(String loadNo) {
		this.loadNo = loadNo;
	}
	public String getWorkOrderNo() {
		return workOrderNo;
	}
	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}
	public String getVesselName() {
		return vesselName;
	}
	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}
	public String getVoyageNo() {
		return voyageNo;
	}
	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}
	public String getCargoType() {
		return cargoType;
	}
	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}
	public String getPortOfDischarge() {
		return portOfDischarge;
	}
	public void setPortOfDischarge(String portOfDischarge) {
		this.portOfDischarge = portOfDischarge;
	}
	public String getPortOfLoading() {
		return PortOfLoading;
	}
	public void setPortOfLoading(String portOfLoading) {
		PortOfLoading = portOfLoading;
	}
	public String getBookingCustomer() {
		return bookingCustomer;
	}
	public void setBookingCustomer(String bookingCustomer) {
		this.bookingCustomer = bookingCustomer;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getUploadedBy() {
		return uploadedBy;
	}
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	public String getDeletedBy() {
		return deletedBy;
	}
	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}
	public String getUploadFromDate() {
		return uploadFromDate;
	}
	public void setUploadFromDate(String uploadFromDate) {
		this.uploadFromDate = uploadFromDate;
	}
	public String getUploadToDate() {
		return uploadToDate;
	}
	public void setUploadToDate(String uploadToDate) {
		this.uploadToDate = uploadToDate;
	}
	public String getUploadedDate() {
		return uploadedDate;
	}
	public void setUploadedDate(String uploadedDate) {
		this.uploadedDate = uploadedDate;
	}
	public String getQuotationDate() {
		return quotationDate;
	}
	public void setQuotationDate(String quotationDate) {
		this.quotationDate = quotationDate;
	}
	public String getCreditNoteDate() {
		return creditNoteDate;
	}
	public void setCreditNoteDate(String creditNoteDate) {
		this.creditNoteDate = creditNoteDate;
	}
	public String getDebitNoteDate() {
		return debitNoteDate;
	}
	public void setDebitNoteDate(String debitNoteDate) {
		this.debitNoteDate = debitNoteDate;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getPoDate() {
		return poDate;
	}
	public void setPoDate(String poDate) {
		this.poDate = poDate;
	}
	public String getIncidentDetails() {
		return incidentDetails;
	}
	public void setIncidentDetails(String incidentDetails) {
		this.incidentDetails = incidentDetails;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getChassisNo() {
		return chassisNo;
	}
	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}
	public String getInsuranceAgent() {
		return insuranceAgent;
	}
	public void setInsuranceAgent(String insuranceAgent) {
		this.insuranceAgent = insuranceAgent;
	}
	public String getResponsibleParty() {
		return responsibleParty;
	}
	public void setResponsibleParty(String responsibleParty) {
		this.responsibleParty = responsibleParty;
	}
	public String getIsCancelled() {
		return isCancelled;
	}
	public void setIsCancelled(String isCancelled) {
		this.isCancelled = isCancelled;
	}
	
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getClaimDate() {
		return claimDate;
	}
	public void setClaimDate(String claimDate) {
		this.claimDate = claimDate;
	}
	public String getIncidentDate() {
		return incidentDate;
	}
	public void setIncidentDate(String incidentDate) {
		this.incidentDate = incidentDate;
	}
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getDeliveryNoteNo() {
		return deliveryNoteNo;
	}
	public void setDeliveryNoteNo(String deliveryNoteNo) {
		this.deliveryNoteNo = deliveryNoteNo;
	}
	public String getPullingOrderNo() {
		return pullingOrderNo;
	}
	public void setPullingOrderNo(String pullingOrderNo) {
		this.pullingOrderNo = pullingOrderNo;
	}
	public String getForwardingAgent() {
		return forwardingAgent;
	}
	public void setForwardingAgent(String forwardingAgent) {
		this.forwardingAgent = forwardingAgent;
	}
	@Override
	public String toString() {
		return "DMSSearchConentVO [systemLine=" + systemLine
				+ ", documentLine=" + documentLine + ", documentType="
				+ documentType + ", documentTitle=" + documentTitle
				+ ", businessLine=" + businessLine + ", serviceType="
				+ serviceType + ", serviceLine=" + serviceLine + ", dmsId="
				+ dmsId + ", bookingNo=" + bookingNo + ", jobNo=" + jobNo
				+ ", siNo=" + siNo + ", incidentNo=" + incidentNo
				+ ", claimNo=" + claimNo + ", shipmentNo=" + shipmentNo
				+ ", quotationNo=" + quotationNo + ", invoiceNo=" + invoiceNo
				+ ", doNo=" + doNo + ", poNo=" + poNo + ", creditNoteNo="
				+ creditNoteNo + ", debitNoteNo=" + debitNoteNo + ", loadNo="
				+ loadNo + ", workOrderNo=" + workOrderNo + ", vesselName="
				+ vesselName + ", voyageNo=" + voyageNo + ", cargoType="
				+ cargoType + ", portOfDischarge=" + portOfDischarge
				+ ", PortOfLoading=" + PortOfLoading + ", bookingCustomer="
				+ bookingCustomer + ", mimeType=" + mimeType + ", uploadedBy="
				+ uploadedBy + ", deletedBy=" + deletedBy + ", uploadFromDate="
				+ uploadFromDate + ", uploadToDate=" + uploadToDate
				+ ", uploadedDate=" + uploadedDate + ", quotationDate="
				+ quotationDate + ", creditNoteDate=" + creditNoteDate
				+ ", debitNoteDate=" + debitNoteDate + ", invoiceDate="
				+ invoiceDate + ", poDate=" + poDate + ", deliveryNoteNo="
				+ deliveryNoteNo + ", pullingOrderNo=" + pullingOrderNo
				+ ", forwardingAgent=" + forwardingAgent + ", incidentDetails="
				+ incidentDetails + ", consignee=" + consignee + ", chassisNo="
				+ chassisNo + ", insuranceAgent=" + insuranceAgent
				+ ", responsibleParty=" + responsibleParty + ", isCancelled="
				+ isCancelled + ", claimType=" + claimType + ", claimDate="
				+ claimDate + ", incidentDate=" + incidentDate + ", remarks="
				+ remarks + "]";
	}
	
	


}

