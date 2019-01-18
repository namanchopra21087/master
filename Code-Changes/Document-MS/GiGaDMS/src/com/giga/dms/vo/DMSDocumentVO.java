package com.giga.dms.vo;

import java.io.Serializable;
import java.util.Map;

import com.giga.dms.util.DMSMetaDataKeys;

public class DMSDocumentVO implements Serializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<DMSMetaDataKeys,String> metaDataValue;
  	private byte[] contentStream;
  	private String completeFileNamePath;
  
	public String getCompleteFileNamePath() {
		return completeFileNamePath;
	}
	public void setCompleteFileNamePath(String completeFileNamePath) {
		this.completeFileNamePath = completeFileNamePath;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Map<DMSMetaDataKeys, String> getMetaDataValue() {
		return metaDataValue;
	}
	public void setMetaDataValue(Map<DMSMetaDataKeys, String> metaDataValue) {
		this.metaDataValue = metaDataValue;
	}
	public byte[] getContentStream() {
		return contentStream;
	}
	public void setContentStream(byte[] contentStream) {
		this.contentStream = contentStream;
	}

}
