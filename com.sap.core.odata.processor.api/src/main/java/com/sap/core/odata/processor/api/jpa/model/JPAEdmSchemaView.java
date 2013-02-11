package com.sap.core.odata.processor.api.jpa.model;

import java.util.List;

import com.sap.core.odata.api.edm.provider.Schema;

/**
 * <p>
 * A view on Java Persistence Model and Entity Data Model Schema. Each java
 * persistence unit corresponds to a one EDM schema.
 * </p>
 * <p>
 * The implementation of the view provides access to EDM schema created from
 * Java Persistence unit. The implementation acts as a container for schema. The
 * schema is consistent only if following elements are consistent
 * <ol>
 * <li>{@link com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationView}</li>
 * <li>
 * {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityContainerView}</li>
 * <li>{@link com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexTypeView}</li>
 * </ol>
 * </p>
 * 
 * @author SAP AG
 *         <p>
 * @DoNotImplement
 * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationView
 * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityContainerView
 * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexTypeView
 * 
 */
public interface JPAEdmSchemaView extends JPAEdmBaseView {
	/**
	 * The method returns the EDM schema present in the container.
	 * 
	 * @return an instance EDM schema of type
	 *         {@link com.sap.core.odata.api.edm.provider.Schema}
	 */
	public Schema getEdmSchema();

	/**
	 * The method returns JPA EDM container view. The JPA EDM container view can
	 * be used to access EDM Entity Container elements.
	 * 
	 * @return an instance of type
	 *         {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityContainerView}
	 */
	public JPAEdmEntityContainerView getJPAEdmEntityContainerView();

	/**
	 * The method returns JPA EDM complex view. The JPA EDM complex view can be
	 * used to access EDM complex types and JPA Embeddable Types.
	 * 
	 * @return an instance of type
	 *         {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexTypeView}
	 */
	public JPAEdmComplexTypeView getJPAEdmComplexTypeView();

	/**
	 * The method returns JPA EDM association view. The JPA EDM association view
	 * can be used to access EDM associations and JPA Relationships.
	 * 
	 * @return an instance of type
	 *         {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationView}
	 */
	public JPAEdmAssociationView getJPAEdmAssociationView();

	public List<String> getNonKeyComplexTypeList();

	public void addNonKeyComplexName(String complexTypeName);

}
