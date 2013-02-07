package com.sap.core.odata.processor.api.jpa.model;

import java.util.List;

import javax.persistence.metamodel.Attribute;

import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.SimpleProperty;

/**
 * A view on Java Persistence Entity Attributes and EDM properties. Java
 * Persistence Attributes of type
 * <ol>
 * <li>embedded id - are converted into EDM keys</li>
 * <li>id - are converted into EDM keys</li>
 * <li>attributes - are converted into EDM properties</li>
 * <li>emeddable type - are converted into EDM complex properties</li>
 * <li>relationships - are converted into Associations/Navigation properties</li>
 * </ol>
 * <p>
 * The implementation of the view provides access to EDM properties. The view
 * acts as a container for consistent list of EDM properties of an EDM entity
 * type. EDM property is consistent only if there exists at least one property
 * in the entity type and there is at least on key property.
 * 
 * @author SAP AG
 *         <p>
 * @DoNotImplement
 * @see {@link JPAEdmKeyView},{@link JPAEdmNavigationPropertyView}
 * 
 */
public interface JPAEdmPropertyView extends JPAEdmBaseView {
	/**
	 * The method returns a simple EDM property.
	 * 
	 * @return an instance of type {@link SimpleProperty}
	 */
	SimpleProperty getEdmSimpleProperty();

	/**
	 * The method returns a JPA EDM key view.
	 * 
	 * @return an instance of type {@link JPAEdmKeyView}
	 */
	JPAEdmKeyView getJPAEdmKeyView();

	/**
	 * The method returns a list of Properties for the given Entity Type.
	 * 
	 * @return a list of {@link Property}
	 */
	List<Property> getEdmPropertyList();

	/**
	 * The method returns a JPA Attribute for the given JPA entity type.
	 * 
	 * @return an instance of type @ Attribute}
	 */
	Attribute<?, ?> getJPAAttribute();

	/**
	 * The method returns a JPA EDM navigation property view.
	 * 
	 * @return an instance of type {@link JPAEdmNavigationPropertyView}
	 */
	JPAEdmNavigationPropertyView getJPAEdmNavigationPropertyView();

}
