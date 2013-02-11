package com.sap.core.odata.processor.api.jpa.factory;

import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.access.JPAProcessor;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmModelView;

/**
 * Factory interface for creating
 * <ol>
 * <li>JPA EDM Model view</li>
 * <li>JPA Processor</li>
 * </ol>
 * 
 * @author SAP AG
 * 
 */
public interface JPAAccessFactory {
	/**
	 * The method returns an instance of JPA EDM model view based on OData JPA
	 * Context. The JPA EDM model view thus returned can be used for building
	 * EDM models from Java persistence models.
	 * 
	 * @param oDataJPAContext
	 *            a non null instance of
	 *            {@link com.sap.core.odata.processor.api.jpa.ODataJPAContext}
	 * @return an instance of type
	 *         {@link com.sap.core.odata.processor.api.jpa.model.JPAEdmModelView}
	 */
	public JPAEdmModelView getJPAEdmModelView(ODataJPAContext oDataJPAContext);

	/**
	 * The method returns an instance of JPA processor based on OData JPA
	 * Context. The JPA Processor thus returned can be used for building and
	 * processing JPQL statements.
	 * 
	 * @param oDataJPAContext
	 *            a non null instance of
	 *            {@link com.sap.core.odata.processor.api.jpa.ODataJPAContext}
	 * @return an instance of type
	 *         {@link com.sap.core.odata.processor.api.jpa.access.JPAProcessor}
	 */
	public JPAProcessor getJPAProcessor(ODataJPAContext oDataJPAContext);
}
