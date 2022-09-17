//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.01.12 at 10:00:19 AM EST 
//


package com.backcountry.store.orchestrator.models;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "entryCode",
    "workCode",
    "timecardEmployeeId",
    "clockInTime",
    "clockOutTime",
    "deleteFlag",
    "comment",
    "sellingFlag",
    "timecardEntryId"
})
@XmlRootElement(name = "TimeClockTransaction", namespace = "http://www.datavantagecorp.com/xstore/")
public class TimeClockTransaction {

    @XmlElement(name = "EntryCode", namespace = "http://www.datavantagecorp.com/xstore/")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String entryCode;
    @XmlElement(name = "WorkCode", namespace = "http://www.datavantagecorp.com/xstore/")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String workCode;
    @XmlElement(name = "TimecardEmployeeId", namespace = "http://www.datavantagecorp.com/xstore/")
    protected BigInteger timecardEmployeeId;
    @XmlElement(name = "ClockInTime", namespace = "http://www.datavantagecorp.com/xstore/")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar clockInTime;
    @XmlElement(name = "ClockOutTime", namespace = "http://www.datavantagecorp.com/xstore/")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar clockOutTime;
    @XmlElement(name = "DeleteFlag", namespace = "http://www.datavantagecorp.com/xstore/")
    protected Boolean deleteFlag;
    @XmlElement(name = "Comment", namespace = "http://www.datavantagecorp.com/xstore/")
    protected String comment;
    @XmlElement(name = "SellingFlag", namespace = "http://www.datavantagecorp.com/xstore/")
    protected Boolean sellingFlag;
    @XmlElement(name = "TimecardEntryId", namespace = "http://www.datavantagecorp.com/xstore/")
    protected BigInteger timecardEntryId;

    /**
     * Gets the value of the entryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntryCode() {
        return entryCode;
    }

    /**
     * Sets the value of the entryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntryCode(String value) {
        this.entryCode = value;
    }

    /**
     * Gets the value of the workCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkCode() {
        return workCode;
    }

    /**
     * Sets the value of the workCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkCode(String value) {
        this.workCode = value;
    }

    /**
     * Gets the value of the timecardEmployeeId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTimecardEmployeeId() {
        return timecardEmployeeId;
    }

    /**
     * Sets the value of the timecardEmployeeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTimecardEmployeeId(BigInteger value) {
        this.timecardEmployeeId = value;
    }

    /**
     * Gets the value of the clockInTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getClockInTime() {
        return clockInTime;
    }

    /**
     * Sets the value of the clockInTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setClockInTime(XMLGregorianCalendar value) {
        this.clockInTime = value;
    }

    /**
     * Gets the value of the clockOutTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getClockOutTime() {
        return clockOutTime;
    }

    /**
     * Sets the value of the clockOutTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setClockOutTime(XMLGregorianCalendar value) {
        this.clockOutTime = value;
    }

    /**
     * Gets the value of the deleteFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDeleteFlag() {
        return deleteFlag;
    }

    /**
     * Sets the value of the deleteFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDeleteFlag(Boolean value) {
        this.deleteFlag = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Gets the value of the sellingFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSellingFlag() {
        return sellingFlag;
    }

    /**
     * Sets the value of the sellingFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSellingFlag(Boolean value) {
        this.sellingFlag = value;
    }

    /**
     * Gets the value of the timecardEntryId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTimecardEntryId() {
        return timecardEntryId;
    }

    /**
     * Sets the value of the timecardEntryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTimecardEntryId(BigInteger value) {
        this.timecardEntryId = value;
    }

}
