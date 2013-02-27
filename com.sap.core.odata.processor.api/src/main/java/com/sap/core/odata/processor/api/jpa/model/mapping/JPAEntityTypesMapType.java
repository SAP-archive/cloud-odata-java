package com.sap.core.odata.processor.api.jpa.model.mapping;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for JPAEntityTypesMapType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="JPAEntityTypesMapType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="JPAEntityType" type="{http://www.sap.com/core/odata/processor/api/jpa/model/mapping}JPAEntityTypeMapType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JPAEntityTypesMapType", propOrder = { "jpaEntityType" })
public class JPAEntityTypesMapType {

	@XmlElement(name = "JPAEntityType")
	protected List<JPAEntityTypeMapType> jpaEntityType;

	/**
	 * Gets the value of the jpaEntityType property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the jpaEntityType property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getJPAEntityType().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link JPAEntityTypeMapType }
	 * 
	 * 
	 */
	public List<JPAEntityTypeMapType> getJPAEntityType() {
		if (jpaEntityType == null) {
			jpaEntityType = new ArrayList<JPAEntityTypeMapType>();
		}
		return this.jpaEntityType;
	}

}
