package com.giga.dms.vo;
import java.io.Serializable;
public class DMSSearchVO implements Serializable{
  

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//common-property
	private String systemLine="";
	private String bookingCustomer="";
	private String bookingNo="";
	private String bookingDate="";
	private String portOfDischarge="";
	private String voyageNo="";
	private String vesselName="";
	private String etaDate="";
	private String pickupLocation="";
	private String billingCustomer="";
	
//booking property
  private String bookingDateFrom="";
  private String bookingDateTo="";
  private String businessLine="";
  

//job property
  private String jobDateFrom="";
  private String jobDateTo="";
  private String jobNo="";
  
//si property
  private String siDateFrom="";
  private String siDateTo="";
  private String siNo="";

//claim property
  private String claimDateFrom="";
  private String claimDateTo="";
  private String claimNo="";
  private String incidentNo="";
  
//shipment property
  private String shipmentDateFrom="";
  private String shipmentDateTo="";
  private String shipmentNo="";
  
//invoice property
  private String invoiceDateFrom="";
  private String invoiceDateTo=""; 
  private String invoiceNo="";

//quotation property
  private String quotationDateFrom="";
  private String quotationDateTo="";
  private String quotationNo="";
  


//po property
  private String poDateFrom="";
  private String poDateTo="";
  private String vendorName="";
  private String poNo="";
 
//do property

  private String loadNo="";
  private String pickupDate="";
  private String deliveryDate="";
  private String doDateFrom="";
  private String doDateTo="";
  private String doNo="";
  
//load property
  private String deliveryLocation="";
  private String truckType="";
  
  
//booking customer property
  private String customerName="";
  private String customerCode="";  
  
 //company login user details
  private String branchCode="";
  private String companyCode="";
  
  //credit Note
  private String creditNoteNo="";
  private String cnDateForm="";
  private String cnDateTo="";
  
  //credit Note
  private String debitNoteNo="";
  private String dnDateFrom="";
  private String dnDateTo="";
  
  //Yms Content
  private String deliveryNoteNo="";
  private String deliveryNoteType="";
  private String deliveryNoteDateFrom="";
  private String deliveryNoteDateTo=""; 
  private String forwardingAgent="";
  
//pulling order
  private String pullingOrderNo="";
  private String ploDateFrom="";
  private String ploDateTo="";
  
  
	  public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}



	public String getSystemLine() {
		return systemLine;
	}
	public void setSystemLine(String systemLine) {
		this.systemLine = systemLine;
	}
	public String getBookingCustomer() {
		return bookingCustomer;
	}
	public void setBookingCustomer(String bookingCustomer) {
		this.bookingCustomer = bookingCustomer;
	}
	public String getBookingNo() {
		return bookingNo;
	}
	public void setBookingNo(String bookingNo) {
		this.bookingNo = bookingNo;
	}
	public String getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}
	public String getPortOfDischarge() {
		return portOfDischarge;
	}
	public void setPortOfDischarge(String portOfDischarge) {
		this.portOfDischarge = portOfDischarge;
	}
	public String getVoyageNo() {
		return voyageNo;
	}
	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}
	public String getVesselName() {
		return vesselName;
	}
	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}
	public String getEtaDate() {
		return etaDate;
	}
	public void setEtaDate(String etaDate) {
		this.etaDate = etaDate;
	}
	public String getPickupLocation() {
		return pickupLocation;
	}
	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}
	public String getBookingDateFrom() {
		return bookingDateFrom;
	}
	public void setBookingDateFrom(String bookingDateFrom) {
		this.bookingDateFrom = bookingDateFrom;
	}
	public String getBookingDateTo() {
		return bookingDateTo;
	}
	public void setBookingDateTo(String bookingDateTo) {
		this.bookingDateTo = bookingDateTo;
	}
	public String getBusinessLine() {
		return businessLine;
	}
	public void setBusinessLine(String businessLine) {
		this.businessLine = businessLine;
	}
	public String getJobDateFrom() {
		return jobDateFrom;
	}
	public void setJobDateFrom(String jobDateFrom) {
		this.jobDateFrom = jobDateFrom;
	}
	public String getJobDateTo() {
		return jobDateTo;
	}
	public void setJobDateTo(String jobDateTo) {
		this.jobDateTo = jobDateTo;
	}
	public String getJobNo() {
		return jobNo;
	}
	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}
	public String getSiDateFrom() {
		return siDateFrom;
	}
	public void setSiDateFrom(String siDateFrom) {
		this.siDateFrom = siDateFrom;
	}
	public String getSiDateTo() {
		return siDateTo;
	}
	public void setSiDateTo(String siDateTo) {
		this.siDateTo = siDateTo;
	}
	public String getSiNo() {
		return siNo;
	}
	public void setSiNo(String siNo) {
		this.siNo = siNo;
	}
	public String getClaimDateFrom() {
		return claimDateFrom;
	}
	public void setClaimDateFrom(String claimDateFrom) {
		this.claimDateFrom = claimDateFrom;
	}
	public String getClaimDateTo() {
		return claimDateTo;
	}
	public void setClaimDateTo(String claimDateTo) {
		this.claimDateTo = claimDateTo;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getIncidentNo() {
		return incidentNo;
	}
	public void setIncidentNo(String incidentNo) {
		this.incidentNo = incidentNo;
	}
	public String getShipmentDateFrom() {
		return shipmentDateFrom;
	}
	public void setShipmentDateFrom(String shipmentDateFrom) {
		this.shipmentDateFrom = shipmentDateFrom;
	}
	public String getShipmentDateTo() {
		return shipmentDateTo;
	}
	public void setShipmentDateTo(String shipmentDateTo) {
		this.shipmentDateTo = shipmentDateTo;
	}
	public String getShipmentNo() {
		return shipmentNo;
	}
	public void setShipmentNo(String shipmentNo) {
		this.shipmentNo = shipmentNo;
	}
	public String getInvoiceDateFrom() {
		return invoiceDateFrom;
	}
	public void setInvoiceDateFrom(String invoiceDateFrom) {
		this.invoiceDateFrom = invoiceDateFrom;
	}
	public String getInvoiceDateTo() {
		return invoiceDateTo;
	}
	public void setInvoiceDateTo(String invoiceDateTo) {
		this.invoiceDateTo = invoiceDateTo;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getQuotationDateFrom() {
		return quotationDateFrom;
	}
	public void setQuotationDateFrom(String quotationDateFrom) {
		this.quotationDateFrom = quotationDateFrom;
	}
	public String getQuotationDateTo() {
		return quotationDateTo;
	}
	public void setQuotationDateTo(String quotationDateTo) {
		this.quotationDateTo = quotationDateTo;
	}
	public String getQuotationNo() {
		return quotationNo;
	}
	public void setQuotationNo(String quotationNo) {
		this.quotationNo = quotationNo;
	}
	public String getPoDateFrom() {
		return poDateFrom;
	}
	public void setPoDateFrom(String poDateFrom) {
		this.poDateFrom = poDateFrom;
	}
	public String getPoDateTo() {
		return poDateTo;
	}
	public void setPoDateTo(String poDateTo) {
		this.poDateTo = poDateTo;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getBillingCustomer() {
		return billingCustomer;
	}
	public void setBillingCustomer(String billingCustomer) {
		this.billingCustomer = billingCustomer;
	}
	public String getLoadNo() {
		return loadNo;
	}
	public void setLoadNo(String loadNo) {
		this.loadNo = loadNo;
	}
	public String getPickupDate() {
		return pickupDate;
	}
	public void setPickupDate(String pickupDate) {
		this.pickupDate = pickupDate;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getDoDateFrom() {
		return doDateFrom;
	}
	public void setDoDateFrom(String doDateFrom) {
		this.doDateFrom = doDateFrom;
	}
	public String getDoDateTo() {
		return doDateTo;
	}
	public void setDoDateTo(String doDateTo) {
		this.doDateTo = doDateTo;
	}
	public String getDoNo() {
		return doNo;
	}
	public void setDoNo(String doNo) {
		this.doNo = doNo;
	}
	public String getDeliveryLocation() {
		return deliveryLocation;
	}
	public void setDeliveryLocation(String deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}
	public String getTruckType() {
		return truckType;
	}
	public void setTruckType(String truckType) {
		this.truckType = truckType;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * @return the debitNoteNo
	 */
	public String getDebitNoteNo() {
		return debitNoteNo;
	}
	/**
	 * @param debitNoteNo the debitNoteNo to set
	 */
	public void setDebitNoteNo(String debitNoteNo) {
		this.debitNoteNo = debitNoteNo;
	}
	/**
	 * @return the creditNoteNo
	 */
	public String getCreditNoteNo() {
		return creditNoteNo;
	}
	/**
	 * @param creditNoteNo the creditNoteNo to set
	 */
	public void setCreditNoteNo(String creditNoteNo) {
		this.creditNoteNo = creditNoteNo;
	}

	/**
	 * @return the cnDateForm
	 */
	public String getCnDateForm() {
		return cnDateForm;
	}
	/**
	 * @param cnDateForm the cnDateForm to set
	 */
	public void setCnDateForm(String cnDateForm) {
		this.cnDateForm = cnDateForm;
	}
	/**
	 * @return the dnDateFrom
	 */
	public String getDnDateFrom() {
		return dnDateFrom;
	}
	/**
	 * @param dnDateFrom the dnDateFrom to set
	 */
	public void setDnDateFrom(String dnDateFrom) {
		this.dnDateFrom = dnDateFrom;
	}
	/**
	 * @return the cnDateTo
	 */
	public String getCnDateTo() {
		return cnDateTo;
	}
	/**
	 * @param cnDateTo the cnDateTo to set
	 */
	public void setCnDateTo(String cnDateTo) {
		this.cnDateTo = cnDateTo;
	}
	/**
	 * @return the dnDateTo
	 */
	public String getDnDateTo() {
		return dnDateTo;
	}
	/**
	 * @param dnDateTo the dnDateTo to set
	 */
	public void setDnDateTo(String dnDateTo) {
		this.dnDateTo = dnDateTo;
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
	public String getDeliveryNoteType() {
		return deliveryNoteType;
	}
	public void setDeliveryNoteType(String deliveryNoteType) {
		this.deliveryNoteType = deliveryNoteType;
	}
	public String getDeliveryNoteDateFrom() {
		return deliveryNoteDateFrom;
	}
	public void setDeliveryNoteDateFrom(String deliveryNoteDateFrom) {
		this.deliveryNoteDateFrom = deliveryNoteDateFrom;
	}
	public String getDeliveryNoteDateTo() {
		return deliveryNoteDateTo;
	}
	public void setDeliveryNoteDateTo(String deliveryNoteDateTo) {
		this.deliveryNoteDateTo = deliveryNoteDateTo;
	}
	public String getPloDateFrom() {
		return ploDateFrom;
	}
	public void setPloDateFrom(String ploDateFrom) {
		this.ploDateFrom = ploDateFrom;
	}
	public String getPloDateTo() {
		return ploDateTo;
	}
	public void setPloDateTo(String ploDateTo) {
		this.ploDateTo = ploDateTo;
	}
	
	@Override
	public String toString() {
		return "DMSSearchVO [systemLine=" + systemLine + ", bookingCustomer="
				+ bookingCustomer + ", bookingNo=" + bookingNo
				+ ", bookingDate=" + bookingDate + ", portOfDischarge="
				+ portOfDischarge + ", voyageNo=" + voyageNo + ", vesselName="
				+ vesselName + ", etaDate=" + etaDate + ", pickupLocation="
				+ pickupLocation + ", billingCustomer=" + billingCustomer
				+ ", bookingDateFrom=" + bookingDateFrom + ", bookingDateTo="
				+ bookingDateTo + ", businessLine=" + businessLine
				+ ", jobDateFrom=" + jobDateFrom + ", jobDateTo=" + jobDateTo
				+ ", jobNo=" + jobNo + ", siDateFrom=" + siDateFrom
				+ ", siDateTo=" + siDateTo + ", siNo=" + siNo
				+ ", claimDateFrom=" + claimDateFrom + ", claimDateTo="
				+ claimDateTo + ", claimNo=" + claimNo + ", incidentNo="
				+ incidentNo + ", shipmentDateFrom=" + shipmentDateFrom
				+ ", shipmentDateTo=" + shipmentDateTo + ", shipmentNo="
				+ shipmentNo + ", invoiceDateFrom=" + invoiceDateFrom
				+ ", invoiceDateTo=" + invoiceDateTo + ", invoiceNo="
				+ invoiceNo + ", quotationDateFrom=" + quotationDateFrom
				+ ", quotationDateTo=" + quotationDateTo + ", quotationNo="
				+ quotationNo + ", poDateFrom=" + poDateFrom + ", poDateTo="
				+ poDateTo + ", vendorName=" + vendorName + ", poNo=" + poNo
				+ ", loadNo=" + loadNo + ", pickupDate=" + pickupDate
				+ ", deliveryDate=" + deliveryDate + ", doDateFrom="
				+ doDateFrom + ", doDateTo=" + doDateTo + ", doNo=" + doNo
				+ ", deliveryLocation=" + deliveryLocation + ", truckType="
				+ truckType + ", customerName=" + customerName
				+ ", customerCode=" + customerCode + ", branchCode="
				+ branchCode + ", companyCode=" + companyCode
				+ ", creditNoteNo=" + creditNoteNo + ", cnDateForm="
				+ cnDateForm + ", cnDateTo=" + cnDateTo + ", debitNoteNo="
				+ debitNoteNo + ", dnDateFrom=" + dnDateFrom + ", dnDateTo="
				+ dnDateTo + ", deliveryNoteNo=" + deliveryNoteNo
				+ ", deliveryNoteType=" + deliveryNoteType
				+ ", deliveryNoteDateFrom=" + deliveryNoteDateFrom
				+ ", deliveryNoteDateTo=" + deliveryNoteDateTo
				+ ", forwardingAgent=" + forwardingAgent + ", pullingOrderNo="
				+ pullingOrderNo + ", ploDateFrom=" + ploDateFrom
				+ ", ploDateTo=" + ploDateTo + "]";
	}
	
	
	
}
