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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommentType", propOrder = {
    "commentType",
    "commentText",
    "commentID",
    "createdTimestamp",
    "createdUserId"
})
public class CommentType {

    @XmlElement(name = "CommentType")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String commentType;
    @XmlElement(name = "CommentText")
    protected String commentText;
    @XmlElement(name = "CommentID", required = true)
    protected BigInteger commentID;
    @XmlElement(name = "CreatedTimestamp")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String createdTimestamp;
    @XmlElement(name = "CreatedUserId", required = true)
    protected BigInteger createdUserId;

    /**
     * Gets the value of the commentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommentType() {
        return commentType;
    }

    /**
     * Sets the value of the commentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommentType(String value) {
        this.commentType = value;
    }

    /**
     * Gets the value of the commentText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommentText() {
        return commentText;
    }

    /**
     * Sets the value of the commentText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommentText(String value) {
        this.commentText = value;
    }

    /**
     * Gets the value of the commentID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCommentID() {
        return commentID;
    }

    /**
     * Sets the value of the commentID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCommentID(BigInteger value) {
        this.commentID = value;
    }

    /**
     * Gets the value of the createdTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    /**
     * Sets the value of the createdTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreatedTimestamp(String value) {
        this.createdTimestamp = value;
    }

    /**
     * Gets the value of the createdUserId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCreatedUserId() {
        return createdUserId;
    }

    /**
     * Sets the value of the createdUserId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCreatedUserId(BigInteger value) {
        this.createdUserId = value;
    }

}
