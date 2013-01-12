package com.sap.core.odata.processor.jpa.api.factory;

import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.access.JPAProcessor;

public interface JPAAccessFactory {
	public JPAEdmBuilder getJPAEdmBuilder(ODataJPAContext oDataJPAContext);

	public JPAProcessor getJPAProcessor(ODataJPAContext oDataJPAContext);
}
