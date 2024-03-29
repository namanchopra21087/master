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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "amount",
    "promotionID",
    "reasonCode"
})
@XmlRootElement(name = "TransactionDeal", namespace = "http://www.datavantagecorp.com/xstore/")
public class TransactionDeal {

    @XmlElement(name = "Amount", namespace = "http://www.datavantagecorp.com/xstore/", required = true)
    protected DtvAmountType amount;
    @XmlElement(name = "PromotionID", namespace = "http://www.datavantagecorp.com/xstore/", required = true)
    protected String promotionID;
    @XmlElement(name = "ReasonCode", namespace = "http://www.datavantagecorp.com/xstore/", required = true)
    protected String reasonCode;
    @XmlAttribute(name = "MethodCode", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String methodCode;
    @XmlAttribute(name = "VoidFlag", required = true)
    protected boolean voidFlag;

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link DtvAmountType }
     *     
     */
    public DtvAmountType getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link DtvAmountType }
     *     
     */
    public void setAmount(DtvAmountType value) {
        this.amount = value;
    }

    /**
     * Gets the value of the promotionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPromotionID() {
        return promotionID;
    }

    /**
     * Sets the value of the promotionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPromotionID(String value) {
        this.promotionID = value;
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
     * Gets the value of the methodCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethodCode() {
        return methodCode;
    }

    /**
     * Sets the value of the methodCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethodCode(String value) {
        this.methodCode = value;
    }

    /**
     * Gets the value of the voidFlag property.
     * 
     */
    public boolean isVoidFlag() {
        return voidFlag;
    }

    /**
     * Sets the value of the voidFlag property.
     * 
     */
    public void setVoidFlag(boolean value) {
        this.voidFlag = value;
    }

}
