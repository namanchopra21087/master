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
@XmlType(name = "CreditDebitType", propOrder = {
    "issuerIdentificationNumber",
    "primaryAccountNumber",
    "walletRequestId",
    "walletProviderId",
    "reconciliationCode",
    "expirationDateEncrypted",
    "maskedCardNumber",
    "authorizationToken",
    "hashedCardNumber",
    "customerNotPresentReasonCode"
})
public class CreditDebitType {

    @XmlElement(name = "IssuerIdentificationNumber")
    protected String issuerIdentificationNumber;
    @XmlElement(name = "PrimaryAccountNumber")
    protected String primaryAccountNumber;
    @XmlElement(name = "WalletRequestId")
    protected String walletRequestId;
    @XmlElement(name = "WalletProviderId")
    protected String walletProviderId;
    @XmlElement(name = "ReconciliationCode")
    protected String reconciliationCode;
    @XmlElement(name = "ExpirationDateEncrypted", namespace = "http://www.datavantagecorp.com/xstore/")
    protected String expirationDateEncrypted;
    @XmlElement(name = "MaskedCardNumber", namespace = "http://www.datavantagecorp.com/xstore/")
    protected String maskedCardNumber;
    @XmlElement(name = "AuthorizationToken", namespace = "http://www.datavantagecorp.com/xstore/")
    protected String authorizationToken;
    @XmlElement(name = "HashedCardNumber", namespace = "http://www.datavantagecorp.com/xstore/")
    protected String hashedCardNumber;
    @XmlElement(name = "CustomerNotPresentReasonCode")
    protected String customerNotPresentReasonCode;

    /**
     * Gets the value of the issuerIdentificationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuerIdentificationNumber() {
        return issuerIdentificationNumber;
    }

    /**
     * Sets the value of the issuerIdentificationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuerIdentificationNumber(String value) {
        this.issuerIdentificationNumber = value;
    }

    /**
     * Gets the value of the primaryAccountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimaryAccountNumber() {
        return primaryAccountNumber;
    }

    /**
     * Sets the value of the primaryAccountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimaryAccountNumber(String value) {
        this.primaryAccountNumber = value;
    }

    /**
     * Gets the value of the walletRequestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWalletRequestId() {
        return walletRequestId;
    }

    /**
     * Sets the value of the walletRequestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWalletRequestId(String value) {
        this.walletRequestId = value;
    }

    /**
     * Gets the value of the walletProviderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWalletProviderId() {
        return walletProviderId;
    }

    /**
     * Sets the value of the walletProviderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWalletProviderId(String value) {
        this.walletProviderId = value;
    }

    /**
     * Gets the value of the reconciliationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReconciliationCode() {
        return reconciliationCode;
    }

    /**
     * Sets the value of the reconciliationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReconciliationCode(String value) {
        this.reconciliationCode = value;
    }

    /**
     * Gets the value of the expirationDateEncrypted property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpirationDateEncrypted() {
        return expirationDateEncrypted;
    }

    /**
     * Sets the value of the expirationDateEncrypted property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpirationDateEncrypted(String value) {
        this.expirationDateEncrypted = value;
    }

    /**
     * Gets the value of the maskedCardNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    /**
     * Sets the value of the maskedCardNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaskedCardNumber(String value) {
        this.maskedCardNumber = value;
    }

    /**
     * Gets the value of the authorizationToken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthorizationToken() {
        return authorizationToken;
    }

    /**
     * Sets the value of the authorizationToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthorizationToken(String value) {
        this.authorizationToken = value;
    }

    /**
     * Gets the value of the hashedCardNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashedCardNumber() {
        return hashedCardNumber;
    }

    /**
     * Sets the value of the hashedCardNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashedCardNumber(String value) {
        this.hashedCardNumber = value;
    }

    /**
     * Gets the value of the customerNotPresentReasonCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerNotPresentReasonCode() {
        return customerNotPresentReasonCode;
    }

    /**
     * Sets the value of the customerNotPresentReasonCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerNotPresentReasonCode(String value) {
        this.customerNotPresentReasonCode = value;
    }

}