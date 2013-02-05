package com.sap.core.odata.processor.core.jpa.model;

import javax.persistence.metamodel.Metamodel;

import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmModelView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmSchemaView;

public class JPAEdmModel extends JPAEdmBaseViewImpl implements JPAEdmModelView {

	protected JPAEdmSchemaView schemaView;
	public JPAEdmModel(Metamodel metaModel, String pUnitName) {
		super(metaModel,pUnitName);
	}

	@Override
	public JPAEdmSchemaView getEdmSchemaView( ){
		return schemaView;
	}
	
	@Override
	public JPAEdmBuilder getBuilder( ){
		if (this.builder == null)
			this.builder = new JPAEdmModelBuilder();
		
		return builder;
	}
	

	private class JPAEdmModelBuilder implements JPAEdmBuilder{

		@Override
		public void build() throws ODataJPAModelException, ODataJPARuntimeException {
			schemaView = new JPAEdmSchema(JPAEdmModel.this);
			schemaView.getBuilder().build();
		}
		
	}
}
