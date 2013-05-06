/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
