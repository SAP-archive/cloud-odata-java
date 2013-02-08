package com.sap.core.odata.processor.api.jpa.factory;

import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.access.JPAProcessor;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmModelView;

/**
 * This factory provides EDM Provider and OData JPA Processor.
 * @author AG
 *
 */
public interface JPAAccessFactory {
	public JPAEdmModelView getJPAEdmModelView(ODataJPAContext oDataJPAContext);
	public JPAProcessor getJPAProcessor(ODataJPAContext oDataJPAContext);
}
