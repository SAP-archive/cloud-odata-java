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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * 
 * 				The default name for EDM
 * 				property is derived from JPA attribute name. This can be overriden
 * 				using
 * 				JPAAttributeMapType.
 * 			
 * 
 * <p>Java class for JPAAttributeMapType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="JPAAttributeMapType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="JPAAttribute" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="exclude" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JPAAttributeMapType", propOrder = {
    "jpaAttribute"
})
public class JPAAttributeMapType {

  @XmlElement(name = "JPAAttribute")
  protected List<JPAAttributeMapType.JPAAttribute> jpaAttribute;

  /**
   * Gets the value of the jpaAttribute property.
   * 
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the jpaAttribute property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getJPAAttribute().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link JPAAttributeMapType.JPAAttribute }
   * 
   * 
   */
  public List<JPAAttributeMapType.JPAAttribute> getJPAAttribute() {
    if (jpaAttribute == null) {
      jpaAttribute = new ArrayList<JPAAttributeMapType.JPAAttribute>();
    }
    return jpaAttribute;
  }

  /**
   * <p>Java class for anonymous complex type.
   * 
   * <p>The following schema fragment specifies the expected content contained within this class.
   * 
   * <pre>
   * &lt;complexType>
   *   &lt;simpleContent>
   *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
   *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
   *       &lt;attribute name="exclude" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
   *     &lt;/extension>
   *   &lt;/simpleContent>
   * &lt;/complexType>
   * </pre>
   * 
   * 
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {
      "value"
  })
  public static class JPAAttribute {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "exclude")
    protected Boolean exclude;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
      return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(final String value) {
      this.value = value;
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

}
