package com.sap.core.odata.processor.core.jpa.model;

import javax.persistence.metamodel.Metamodel;

import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmModelView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView;

public class JPAEdmModel extends JPAEdmBaseViewImpl implements JPAEdmModelView {

	protected JPAEdmSchemaView schemaView;
	public JPAEdmModel(Metamodel metaModel, String pUnitName) {
		super(metaModel,pUnitName);
	}
	
	public JPAEdmModel(ODataJPAContext ctx){
		super(ctx);
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
