package com.giga.dms.vo;

import java.io.Serializable;

import com.giga.base.BaseVO;

public class KeyValueVo extends BaseVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1498295590201164329L;
	private String key;
	private String value;
	private String code;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
