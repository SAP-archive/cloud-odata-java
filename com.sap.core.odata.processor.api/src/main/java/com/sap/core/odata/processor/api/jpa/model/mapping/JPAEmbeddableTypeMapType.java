package com.sap.core.odata.processor.api.jpa.model.mapping;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * 				The default name for EDM
 * 				complex type is derived from JPA Embeddable type name. This can be
 * 				overriden using JPAEmbeddableTypeMapType.
 * 			
 * 
 * <p>Java class for JPAEmbeddableTypeMapType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="JPAEmbeddableTypeMapType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EDMComplexType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="JPAAttributes" type="{http://www.sap.com/core/odata/processor/api/jpa/model/mapping}JPAAttributeMapType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="exclude" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JPAEmbeddableTypeMapType", propOrder = {
    "edmComplexType",
    "jpaAttributes"
})
public class JPAEmbeddableTypeMapType {

    @XmlElement(name = "EDMComplexType")
    protected String edmComplexType;
    @XmlElement(name = "JPAAttributes", required = true)
    protected JPAAttributeMapType jpaAttributes;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "exclude")
    protected Boolean exclude;

    /**
     * Gets the value of the edmComplexType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEDMComplexType() {
        return edmComplexType;
    }

    /**
     * Sets the value of the edmComplexType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEDMComplexType(String value) {
        this.edmComplexType = value;
    }

    /**
     * Gets the value of the jpaAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link JPAAttributeMapType }
     *     
     */
    public JPAAttributeMapType getJPAAttributes() {
        return jpaAttributes;
    }

    /**
     * Sets the value of the jpaAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link JPAAttributeMapType }
     *     
     */
    public void setJPAAttributes(JPAAttributeMapType value) {
        this.jpaAttributes = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the exclude property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isExclude() {
        if (exclude == null) {
            return false;
        } else {
            return exclude;
        }
    }

    /**
     * Sets the value of the exclude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setExclude(Boolean value) {
        this.exclude = value;
    }

}
