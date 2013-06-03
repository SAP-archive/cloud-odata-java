package com.sap.core.odata.processor.api.jpa.model.mapping;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * 				The default name for EDM
 * 				entity type is derived from JPA entity type name. This can be
 * 				overriden using JPAEntityTypeMapType.
 * 			
 * 
 * <p>Java class for JPAEntityTypeMapType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="JPAEntityTypeMapType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EDMEntityType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EDMEntitySet" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="JPAAttributes" type="{http://www.sap.com/core/odata/processor/api/jpa/model/mapping}JPAAttributeMapType"/>
 *         &lt;element name="JPARelationships" type="{http://www.sap.com/core/odata/processor/api/jpa/model/mapping}JPARelationshipMapType"/>
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
@XmlType(name = "JPAEntityTypeMapType", propOrder = {
    "edmEntityType",
    "edmEntitySet",
    "jpaAttributes",
    "jpaRelationships"
})
public class JPAEntityTypeMapType {

  @XmlElement(name = "EDMEntityType")
  protected String edmEntityType;
  @XmlElement(name = "EDMEntitySet")
  protected String edmEntitySet;
  @XmlElement(name = "JPAAttributes", required = true)
  protected JPAAttributeMapType jpaAttributes;
  @XmlElement(name = "JPARelationships", required = true)
  protected JPARelationshipMapType jpaRelationships;
  @XmlAttribute(name = "name", required = true)
  protected String name;
  @XmlAttribute(name = "exclude")
  protected Boolean exclude;

  /**
   * Gets the value of the edmEntityType property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public String getEDMEntityType() {
    return edmEntityType;
  }

  /**
   * Sets the value of the edmEntityType property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public void setEDMEntityType(final String value) {
    edmEntityType = value;
  }

  /**
   * Gets the value of the edmEntitySet property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public String getEDMEntitySet() {
    return edmEntitySet;
  }

  /**
   * Sets the value of the edmEntitySet property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public void setEDMEntitySet(final String value) {
    edmEntitySet = value;
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
  public void setJPAAttributes(final JPAAttributeMapType value) {
    jpaAttributes = value;
  }

  /**
   * Gets the value of the jpaRelationships property.
   * 
   * @return
   *     possible object is
   *     {@link JPARelationshipMapType }
   *     
   */
  public JPARelationshipMapType getJPARelationships() {
    return jpaRelationships;
  }

  /**
   * Sets the value of the jpaRelationships property.
   * 
   * @param value
   *     allowed object is
   *     {@link JPARelationshipMapType }
   *     
   */
  public void setJPARelationships(final JPARelationshipMapType value) {
    jpaRelationships = value;
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
  public void setName(final String value) {
    name = value;
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
  public void setExclude(final Boolean value) {
    exclude = value;
  }

}
