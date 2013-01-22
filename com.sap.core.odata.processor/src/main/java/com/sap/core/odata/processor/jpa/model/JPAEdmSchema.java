package com.sap.core.odata.processor.jpa.model;

import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.processor.jpa.access.model.JPAEdmNameBuilder;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmComplexTypeView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityContainerView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmModelView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

public class JPAEdmSchema extends JPAEdmBaseViewImpl implements
JPAEdmSchemaView {

	private Schema schema;
	private JPAEdmComplexTypeView complexTypeView;
	private JPAEdmEntityContainerView entityContainerView;
	private JPAEdmAssociationView associationView = null;


	public JPAEdmSchema(JPAEdmModelView modelView) {
		super(modelView);
	}

	@Override
	public Schema getEdmSchema() {
		return this.schema;
	}
	
	@Override
	public JPAEdmEntityContainerView getJPAEdmEntityContainerView() {
		return entityContainerView;
	}

	@Override
	public JPAEdmComplexTypeView getJPAEdmComplexTypeView() {
		return complexTypeView;
	}

	@Override
	public JPAEdmBuilder getBuilder() {
		return new JPAEdmSchemaBuilder();
	}
	
	@Override
	public void clean() {
		super.clean();
		schema = null;
	}
	
	private class JPAEdmSchemaBuilder implements JPAEdmBuilder {
		/*
		 * 
		 * Each call to build method creates a new EDM Schema. The
		 * newly created schema is built with Entity Containers,
		 * associations, Complex Types and Entity Types.
		 *  
		 * ************************************************************
		 * 					Build EDM Schema - STEPS
		 * ************************************************************
		 * 1) Build Name for EDM Schema 
		 * 2) Build EDM Complex Types from JPA Embeddable Types 
		 * 3) Add EDM Complex Types to EDM Schema
		 * 4) Build EDM Entity Container 
		 * 5) Add EDM Entity Container to EDM Schema
		 * 6) Fetch Built EDM Entity Types from EDM Entity Container 
		 * 7) Add EDM Entity Types to EDM Schema 
		 * 8) Fetch Built EDM Association Sets from EDM Entity Container 
		 * 9) Fetch Built EDM Associations from EDM Association Set 
		 * 10) Add EDM Association to EDM Schema
		 * ************************************************************
		 * 					Build EDM Schema - STEPS
		 * ************************************************************
		 *
		 */
		@Override
		public void build() throws ODataJPAModelException {

			schema = new Schema();
			JPAEdmNameBuilder.build(JPAEdmSchema.this);

			complexTypeView = new JPAEdmComplexType(
					JPAEdmSchema.this);
			complexTypeView.getBuilder().build();

			if (complexTypeView.isConsistent())
				schema.setComplexTypes(complexTypeView
						.getConsistentEdmComplexTypes());

			entityContainerView = new JPAEdmEntityContainer(
					JPAEdmSchema.this);
			entityContainerView.getBuilder().build();
			schema.setEntityContainers(entityContainerView
					.getConsistentEdmEntityContainerList());

			JPAEdmEntitySetView entitySetView = entityContainerView
					.getJPAEdmEntitySetView();
			if (entitySetView.isConsistent()) {
				JPAEdmEntityTypeView entityTypeView = entitySetView
						.getJPAEdmEntityTypeView();
				if (entityTypeView.isConsistent())
					schema.setEntityTypes(entityTypeView
							.getConsistentEdmEntityTypes());
			}

			if (associationView.isConsistent())
				schema.setAssociations(associationView
							.getConsistentEdmAssociationList());
		}

	}

	@Override
	public final JPAEdmAssociationView getJPAEdmAssociationView() {
		return this.associationView;
	}





}
