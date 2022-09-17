//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.01.12 at 10:00:19 AM EST 
//


package com.backcountry.store.orchestrator.models;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionLinkType", propOrder = {
    "retailStoreId",
    "workstationId",
    "sequenceNumber",
    "lineItemSequenceNumber",
    "businessDayDate",
    "linkTypeCode"
})
public class TransactionLinkType {

    @XmlElement(name = "RetailStoreID", required = true)
    protected String retailStoreId;
    @XmlElement(name = "WorkstationID", required = true)
    protected String workstationId;
    @XmlElement(name = "SequenceNumber", required = true)
    protected String sequenceNumber;
    @XmlElement(name = "LineItemSequenceNumber")
    protected BigInteger lineItemSequenceNumber;
    @XmlElement(name = "BusinessDayDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar businessDayDate;
    @XmlElement(name = "LinkTypeCode", namespace = "http://www.datavantagecorp.com/xstore/")
    protected String linkTypeCode;
    @XmlAttribute(name = "ReasonCode")
    protected String reasonCode;
    @XmlAttribute(name = "EntryMethod")
    protected String entryMethod;
    @XmlTransient
    protected String transactionId;

    /**
     * Gets the value of the retailStoreID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetailStoreId() {
        return retailStoreId;
    }

    /**
     * Sets the value of the retailStoreID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetailStoreId(String value) {
        this.retailStoreId = value;
    }

    /**
     * Gets the value of the workstationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkstationId() {
        return workstationId;
    }

    /**
     * Sets the value of the workstationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkstationId(String value) {
        this.workstationId = value;
    }

    /**
     * Gets the value of the sequenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Sets the value of the sequenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSequenceNumber(String value) {
        this.sequenceNumber = value;
    }

    /**
     * Gets the value of the lineItemSequenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLineItemSequenceNumber() {
        return lineItemSequenceNumber;
    }

    /**
     * Sets the value of the lineItemSequenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLineItemSequenceNumber(BigInteger value) {
        this.lineItemSequenceNumber = value;
    }

    /**
     * Gets the value of the businessDayDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBusinessDayDate() {
        return businessDayDate;
    }

    /**
     * Sets the value of the businessDayDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBusinessDayDate(XMLGregorianCalendar value) {
        this.businessDayDate = value;
    }

    /**
     * Gets the value of the linkTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkTypeCode() {
        return linkTypeCode;
    }

    /**
     * Sets the value of the linkTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkTypeCode(String value) {
        this.linkTypeCode = value;
    }

    /**
     * Gets the value of the reasonCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * Sets the value of the reasonCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReasonCode(String value) {
        this.reasonCode = value;
    }

    /**
     * Gets the value of the entryMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntryMethod() {
        return entryMethod;
    }

    /**
     * Sets the value of the entryMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntryMethod(String value) {
        this.entryMethod = value;
    }

    public String getTransactionId() {
        this.generateTransactionId();
        return transactionId;
    }

    /*
    transactionId is a custom value composed of retailStoreId, workstationId and sequenceNumber
    since we can not control what order will this values show in the XML, then we are managing to execute
    this code to generate transactionId when we are sure its composing attributes are set
    */
    public void generateTransactionId() {
        if (StringUtils.isBlank(this.transactionId) &&
                StringUtils.isNotBlank(this.retailStoreId) &&
                StringUtils.isNotBlank(this.workstationId) &&
                StringUtils.isNotBlank(this.sequenceNumber) &&
                this.businessDayDate != null) {
            SimpleDateFormat transactionDateFormat = new SimpleDateFormat("MMddyyyy");
            String StoreId = StringUtils.leftPad(this.retailStoreId, 4, "0");
            String workstationId = StringUtils.leftPad(this.workstationId, 3, "0");
            String sequenceNumber = StringUtils.leftPad(this.sequenceNumber, 6, "0");
            String businessDay = transactionDateFormat.format(this.businessDayDate.toGregorianCalendar().getTime());
            this.transactionId = StoreId + workstationId + sequenceNumber + businessDay;
        }
    }
}
