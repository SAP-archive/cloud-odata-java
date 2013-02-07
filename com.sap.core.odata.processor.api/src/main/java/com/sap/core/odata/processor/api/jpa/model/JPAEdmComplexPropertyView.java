package com.sap.core.odata.processor.api.jpa.model;

import com.sap.core.odata.api.edm.provider.ComplexProperty;

public interface JPAEdmComplexPropertyView extends JPAEdmBaseView {
	
	ComplexProperty getEdmComplexProperty( );
}
