package com.giga.dms.vo;

import java.io.Serializable;
import java.util.Map;

public class DMSWebUploadVO  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String businessLine;
	private String systemLine;
	
	private Map<String,String> pods;
	private Map<String,String> serviceTypes;
	private Map<String,String> serviceLines;
	private Map<String,String> cargoTypes;
		
	private String bookingNo;
	private String serviceType;
	private String bookingCustomer;
	
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getSystemLine() {
		return systemLine;
	}
	public void setSystemLine(String systemLine) {
		this.systemLine = systemLine;
	}
	public String getBookingNo() {
		return bookingNo;
	}
	public void setBookingNo(String bookingNo) {
		this.bookingNo = bookingNo;
	}
	public String getBusinessLine() {
		return businessLine;
	}
	public void setBusinessLine(String businessLine) {
		this.businessLine = businessLine;
	}
	public Map<String, String> getPods() {
		return pods;
	}
	public void setPods(Map<String, String> pods) {
		this.pods = pods;
	}
	public Map<String, String> getServiceTypes() {
		return serviceTypes;
	}
	public void setServiceTypes(Map<String, String> serviceTypes) {
		this.serviceTypes = serviceTypes;
	}
	public Map<String, String> getServiceLines() {
		return serviceLines;
	}
	public void setServiceLines(Map<String, String> serviceLines) {
		this.serviceLines = serviceLines;
	}
	public Map<String, String> getCargoTypes() {
		return cargoTypes;
	}
	public void setCargoTypes(Map<String, String> cargoTypes) {
		this.cargoTypes = cargoTypes;
	}
	public String getBookingCustomer() {
		return bookingCustomer;
	}
	public void setBookingCustomer(String bookingCustomer) {
		this.bookingCustomer = bookingCustomer;
	}
}
