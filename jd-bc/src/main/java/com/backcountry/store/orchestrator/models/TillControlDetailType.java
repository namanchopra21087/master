//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.01.12 at 10:00:19 AM EST 
//


package com.backcountry.store.orchestrator.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TillControlDetailType", propOrder = {
    "affectedTenderRepositoryID",
    "affectedWorkstationID",
    "oldAmount",
    "newAmount"
})
public class TillControlDetailType {

    @XmlElement(name = "AffectedTenderRepositoryID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String affectedTenderRepositoryID;
    @XmlElement(name = "AffectedWorkstationID", required = true)
    protected BigInteger affectedWorkstationID;
    @XmlElement(name = "OldAmount", required = true)
    protected BigDecimal oldAmount;
    @XmlElement(name = "NewAmount", required = true)
    protected BigDecimal newAmount;

    /**
     * Gets the value of the affectedTenderRepositoryID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAffectedTenderRepositoryID() {
        return affectedTenderRepositoryID;
    }

    /**
     * Sets the value of the affectedTenderRepositoryID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAffectedTenderRepositoryID(String value) {
        this.affectedTenderRepositoryID = value;
    }

    /**
     * Gets the value of the affectedWorkstationID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAffectedWorkstationID() {
        return affectedWorkstationID;
    }

    /**
     * Sets the value of the affectedWorkstationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAffectedWorkstationID(BigInteger value) {
        this.affectedWorkstationID = value;
    }

    /**
     * Gets the value of the oldAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOldAmount() {
        return oldAmount;
    }

    /**
     * Sets the value of the oldAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOldAmount(BigDecimal value) {
        this.oldAmount = value;
    }

    /**
     * Gets the value of the newAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNewAmount() {
        return newAmount;
    }

    /**
     * Sets the value of the newAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNewAmount(BigDecimal value) {
        this.newAmount = value;
    }

}
