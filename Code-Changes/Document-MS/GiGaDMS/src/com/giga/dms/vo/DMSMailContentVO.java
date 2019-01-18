package com.giga.dms.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.giga.dms.util.DMSEmailContentKey;

public class DMSMailContentVO implements Serializable {

	public List<String> getToMailId() {
		return toMailId;
	}
	public void setToMailId(List<String> toMailId) {
		this.toMailId = toMailId;
	}
	public List<String> getCcAddress() {
		return ccAddress;
	}
	public void setCcAddress(List<String> ccAddress) {
		this.ccAddress = ccAddress;
	}
	public Map<DMSEmailContentKey, String> getMailData() {
		return mailData;
	}
	public void setMailData(Map<DMSEmailContentKey, String> mailData) {
		this.mailData = mailData;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> toMailId;
	private List<String> ccAddress;
	private Map<DMSEmailContentKey,String> mailData= new HashMap<DMSEmailContentKey,String>();

}
