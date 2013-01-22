package com.sap.core.odata.processor.jpa.model;

import javax.persistence.metamodel.Metamodel;

import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmModelView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

public class JPAEdmModel extends JPAEdmBaseViewImpl implements JPAEdmModelView {

	protected JPAEdmSchemaView schemaView;
	public JPAEdmModel(Metamodel metaModel, String pUnitName) {
		super(metaModel,pUnitName);
	}

	@Override
	public JPAEdmSchemaView getSchemaView( ){
		return schemaView;
	}
	
	@Override
	public JPAEdmBuilder getBuilder( ){
		return new JPAEdmModelBuilder( );
	}
	

	private class JPAEdmModelBuilder implements JPAEdmBuilder{

		@Override
		public void build() throws ODataJPAModelException {
			schemaView = new JPAEdmSchema(JPAEdmModel.this);
			schemaView.getBuilder().build();
		}
		
	}
}
