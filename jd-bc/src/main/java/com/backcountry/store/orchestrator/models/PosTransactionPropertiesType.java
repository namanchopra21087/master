//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.01.12 at 10:00:19 AM EST 
//


package com.backcountry.store.orchestrator.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PosTransactionPropertiesType", namespace = "http://www.datavantagecorp.com/xstore/", propOrder = {
    "posTransactionPropertyCode",
    "posTransactionPropertyValue"
})
public class PosTransactionPropertiesType {

    @XmlElement(name = "PosTransactionPropertyCode", required = true)
    protected String posTransactionPropertyCode;
    @XmlElement(name = "PosTransactionPropertyValue", required = true)
    protected String posTransactionPropertyValue;

    /**
     * Gets the value of the posTransactionPropertyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPosTransactionPropertyCode() {
        return posTransactionPropertyCode;
    }

    /**
     * Sets the value of the posTransactionPropertyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPosTransactionPropertyCode(String value) {
        this.posTransactionPropertyCode = value;
    }

    /**
     * Gets the value of the posTransactionPropertyValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPosTransactionPropertyValue() {
        return posTransactionPropertyValue;
    }

    /**
     * Sets the value of the posTransactionPropertyValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPosTransactionPropertyValue(String value) {
        this.posTransactionPropertyValue = value;
    }

}
