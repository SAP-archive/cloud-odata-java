package com.sap.core.odata.processor.api.factory;

import com.sap.core.odata.processor.api.ODataJPAContext;
import com.sap.core.odata.processor.api.access.JPAProcessor;
import com.sap.core.odata.processor.api.model.JPAEdmModelView;

/**
 * The factory int
 * @author AG
 *
 */
public interface JPAAccessFactory {
	public JPAEdmModelView getJPAEdmModelView(ODataJPAContext oDataJPAContext);
	public JPAProcessor getJPAProcessor(ODataJPAContext oDataJPAContext);
}
