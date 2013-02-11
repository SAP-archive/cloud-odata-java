package com.sap.core.odata.processor.api.jpa.access;

import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;

/**
 * Interface provides methods for building elements of an Entity Data Model from
 * a Java Persistence Model.
 * 
 * @author SAP AG
 * 
 */
public interface JPAEdmBuilder {
	/**
	 * The Method builds EDM Elements by transforming JPA MetaModel. The method
	 * processes EDM JPA Containers which could be accessed using the following
	 * views,
	 * <ul>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationSetView}
	 * </li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationView}</li>
	 * <li> {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmBaseView}</li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexPropertyView}
	 * </li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexTypeView}</li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityContainerView}
	 * </li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmEntitySetView}</li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView}</li>
	 * <li> {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmKeyView}</li>
	 * <li> {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmModelView}</li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmNavigationPropertyView}
	 * </li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmPropertyView}</li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmReferentialConstraintRoleView}
	 * </li>
	 * <li>
	 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmReferentialConstraintView}
	 * </li>
	 * <li> {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView}</li>
	 * </ul>
	 * 
	 * @throws ODataJPARuntimeException
	 **/
	public void build() throws ODataJPAModelException, ODataJPARuntimeException;
}
