//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.01.12 at 10:00:19 AM EST 
//


package com.backcountry.store.orchestrator.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TenderDenominationCountType", namespace = "http://www.datavantagecorp.com/xstore/", propOrder = {
    "tenderDenominationTotal"
})
public class TenderDenominationCountType {

    @XmlElement(name = "TenderDenominationTotal", required = true)
    protected TenderDenominationTotalType tenderDenominationTotal;
    @XmlAttribute(name = "TenderDenominationId", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String tenderDenominationId;

    /**
     * Gets the value of the tenderDenominationTotal property.
     * 
     * @return
     *     possible object is
     *     {@link TenderDenominationTotalType }
     *     
     */
    public TenderDenominationTotalType getTenderDenominationTotal() {
        return tenderDenominationTotal;
    }

    /**
     * Sets the value of the tenderDenominationTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link TenderDenominationTotalType }
     *     
     */
    public void setTenderDenominationTotal(TenderDenominationTotalType value) {
        this.tenderDenominationTotal = value;
    }

    /**
     * Gets the value of the tenderDenominationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTenderDenominationId() {
        return tenderDenominationId;
    }

    /**
     * Sets the value of the tenderDenominationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTenderDenominationId(String value) {
        this.tenderDenominationId = value;
    }

}
