/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.processor.api.jpa.model.mapping;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * The default name for EDM complex type is derived from JPA Embeddable type
 * name. This can be overriden using JPAEmbeddableTypeMapType.
 * 
 * 
 * <p>
 * Java class for JPAEmbeddableTypeMapType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
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
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JPAEmbeddableTypeMapType", propOrder = { "edmComplexType",
    "jpaAttributes" })
public class JPAEmbeddableTypeMapType {

  @XmlElement(name = "EDMComplexType")
  protected String edmComplexType;
  @XmlElement(name = "JPAAttributes", required = true)
  protected JPAAttributeMapType jpaAttributes;
  @XmlAttribute(name = "name", required = true)
  protected String name;

  /**
   * Gets the value of the edmComplexType property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getEDMComplexType() {
    return edmComplexType;
  }

  /**
   * Sets the value of the edmComplexType property.
   * 
   * @param value
   *            allowed object is {@link String }
   * 
   */
  public void setEDMComplexType(final String value) {
    edmComplexType = value;
  }

  /**
   * Gets the value of the jpaAttributes property.
   * 
   * @return possible object is {@link JPAAttributeMapType }
   * 
   */
  public JPAAttributeMapType getJPAAttributes() {
    return jpaAttributes;
  }

  /**
   * Sets the value of the jpaAttributes property.
   * 
   * @param value
   *            allowed object is {@link JPAAttributeMapType }
   * 
   */
  public void setJPAAttributes(final JPAAttributeMapType value) {
    jpaAttributes = value;
  }

  /**
   * Gets the value of the name property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the value of the name property.
   * 
   * @param value
   *            allowed object is {@link String }
   * 
   */
  public void setName(final String value) {
    name = value;
  }

}
