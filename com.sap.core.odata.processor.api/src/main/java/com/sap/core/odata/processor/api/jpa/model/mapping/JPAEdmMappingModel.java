package com.sap.core.odata.processor.api.jpa.model.mapping;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PersistenceUnit" type="{http://www.sap.com/core/odata/processor/api/jpa/model/mapping}JPAPersistenceUnitMapType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "persistenceUnit" })
@XmlRootElement(name = "JPAEDMMappingModel")
public class JPAEdmMappingModel {

  @XmlElement(name = "PersistenceUnit", required = true)
  protected JPAPersistenceUnitMapType persistenceUnit;

  /**
   * Gets the value of the persistenceUnit property.
   * 
   * @return possible object is {@link JPAPersistenceUnitMapType }
   * 
   */
  public JPAPersistenceUnitMapType getPersistenceUnit() {
    return persistenceUnit;
  }

  /**
   * Sets the value of the persistenceUnit property.
   * 
   * @param value
   *            allowed object is {@link JPAPersistenceUnitMapType }
   * 
   */
  public void setPersistenceUnit(final JPAPersistenceUnitMapType value) {
    persistenceUnit = value;
  }

}
