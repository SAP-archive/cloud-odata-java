package com.sap.core.odata.processor.ref;

import com.sap.core.odata.processor.api.jpa.model.JPAEdmExtension;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView;

public class SalesOrderProcessingExtension implements JPAEdmExtension {
	
	public void extend(JPAEdmSchemaView view){
		view.registerOperations(SalesOrderHeaderProcessor.class,null);

	}
	
}
