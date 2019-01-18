package com.giga.dms.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.giga.dms.util.MyHashMap;

/**
 * Value Object which provides getter and setter to hold DMS query search results.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentVO")
public class DocumentInfoVO {

	@XmlElement(name = "metadata")
	public MyHashMap metadata;
	
	@XmlElement(name = "content")
	public byte[] byteArrayStream;
	
	/**
	 * @return the metadata
	 */
	public MyHashMap getMetadata() {
		return metadata;
	}
	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(MyHashMap metadata) {
		this.metadata = metadata;
	}
	/**
	 * @return the byteArrayStream
	 */
	public byte[] getByteArrayStream() {
		return byteArrayStream;
	}
	/**
	 * @param byteArrayStream the byteArrayStream to set
	 */
	public void setByteArrayStream(byte[] byteArrayStream) {
		this.byteArrayStream = byteArrayStream;
	}
	
}
