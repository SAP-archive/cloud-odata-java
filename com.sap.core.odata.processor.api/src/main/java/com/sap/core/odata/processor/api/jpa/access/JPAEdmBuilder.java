package com.sap.core.odata.processor.api.jpa.access;

import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityContainerView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView;

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
	 * <li> {@link JPAEdmAssociationSetView }</li>
	 * <li> {@link JPAEdmAssociationView }</li>
	 * <li> {@link JPAEdmBaseView }</li>
	 * <li> {@link JPAEdmComplexPropertyView }</li>
	 * <li> {@link JPAEdmComplexTypeView }</li>
	 * <li> {@link JPAEdmEntityContainerView }</li>
	 * <li> {@link JPAEdmEntitySetView }</li>
	 * <li> {@link JPAEdmEntityTypeView }</li>
	 * <li> {@link JPAEdmKeyView }</li>
	 * <li> {@link JPAEdmModelView }</li>
	 * <li> {@link JPAEdmNavigationPropertyView }</li>
	 * <li> {@link JPAEdmPropertyView }</li>
	 * <li> {@link JPAEdmReferentialConstraintRoleView }</li>
	 * <li> {@link JPAEdmReferentialConstraintView }</li>
	 * <li> {@link JPAEdmSchemaView }</li>
	 * </ul>
	 * @throws ODataJPARuntimeException 
	 **/
	public void build() throws ODataJPAModelException, ODataJPARuntimeException;
}
