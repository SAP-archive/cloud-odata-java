package com.sap.core.odata.processor.jpa.api.factory;

import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.api.access.JPAProcessor;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmModelView;

public interface JPAAccessFactory {
	public JPAEdmModelView getJPAEdmModelView(ODataJPAContext oDataJPAContext);
	public JPAProcessor getJPAProcessor(ODataJPAContext oDataJPAContext);
}
