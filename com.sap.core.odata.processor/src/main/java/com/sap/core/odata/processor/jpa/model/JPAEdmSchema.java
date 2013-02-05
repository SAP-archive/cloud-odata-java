package com.sap.core.odata.processor.jpa.model;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.NavigationProperty;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.processor.jpa.access.model.JPAEdmNameBuilder;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmComplexTypeView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityContainerView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmModelView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmSchemaView;

public class JPAEdmSchema extends JPAEdmBaseViewImpl implements
		JPAEdmSchemaView {

	private Schema schema;
	private JPAEdmComplexTypeView complexTypeView;
	private JPAEdmEntityContainerView entityContainerView;
	private JPAEdmAssociationView associationView = null;
	private List<String> nonKeyComplexList = null;

	public JPAEdmSchema(JPAEdmModelView modelView) {
		super(modelView);
		if (nonKeyComplexList == null) {
			nonKeyComplexList = new ArrayList<String>();
		}
	}

	@Override
	public List<String> getNonKeyComplexTypeList() {
		return nonKeyComplexList;
	}

	@Override
	public void addNonKeyComplexName(String complexTypeName) {
		nonKeyComplexList.add(complexTypeName);
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
		if (this.builder == null)
			this.builder = new JPAEdmSchemaBuilder();
		
		return builder;
	}

	@Override
	public void clean() {
		super.clean();
		schema = null;
	}

	private class JPAEdmSchemaBuilder implements JPAEdmBuilder {
		/*
		 * 
		 * Each call to build method creates a new EDM Schema. The newly created
		 * schema is built with Entity Containers, associations, Complex Types
		 * and Entity Types.
		 * 
		 * ************************************************************ Build
		 * EDM Schema - STEPS
		 * ************************************************************ 1) Build
		 * Name for EDM Schema 2) Build EDM Complex Types from JPA Embeddable
		 * Types 3) Add EDM Complex Types to EDM Schema 4) Build EDM Entity
		 * Container 5) Add EDM Entity Container to EDM Schema 6) Fetch Built
		 * EDM Entity Types from EDM Entity Container 7) Add EDM Entity Types to
		 * EDM Schema 8) Fetch Built EDM Association Sets from EDM Entity
		 * Container 9) Fetch Built EDM Associations from EDM Association Set
		 * 10) Add EDM Association to EDM Schema
		 * ************************************************************ Build
		 * EDM Schema - STEPS
		 * ************************************************************
		 */
		@Override
		public void build() throws ODataJPAModelException {

			schema = new Schema();
			JPAEdmNameBuilder.build(JPAEdmSchema.this);

			associationView = new JPAEdmAssociation(JPAEdmSchema.this);

			complexTypeView = new JPAEdmComplexType(JPAEdmSchema.this);
			complexTypeView.getBuilder().build();

			entityContainerView = new JPAEdmEntityContainer(JPAEdmSchema.this);
			entityContainerView.getBuilder().build();
			schema.setEntityContainers(entityContainerView
					.getConsistentEdmEntityContainerList());

			JPAEdmEntitySetView entitySetView = entityContainerView
					.getJPAEdmEntitySetView();
			if (entitySetView.isConsistent()
					&& entitySetView.getJPAEdmEntityTypeView() != null) {
				JPAEdmEntityTypeView entityTypeView = entitySetView
						.getJPAEdmEntityTypeView();
				if (entityTypeView.isConsistent()
						&& !entityTypeView.getConsistentEdmEntityTypes()
								.isEmpty())
					schema.setEntityTypes(entityTypeView
							.getConsistentEdmEntityTypes());
			}
			if (complexTypeView.isConsistent()) {
				List<ComplexType> complexTypes = complexTypeView
						.getConsistentEdmComplexTypes();
				List<ComplexType> existingComplexTypes = new ArrayList<ComplexType>();
				for (ComplexType complexType : complexTypes) {
					if (nonKeyComplexList.contains(complexType.getName())) {
						existingComplexTypes.add(complexType);
					}
				}
				if (!existingComplexTypes.isEmpty())
					schema.setComplexTypes(existingComplexTypes);
			}

			List<String> existingAssociationList = new ArrayList<String>();
			if (associationView.isConsistent()
					&& !associationView.getConsistentEdmAssociationList()
							.isEmpty()) {

				List<Association> consistentAssociationList = associationView
						.getConsistentEdmAssociationList();
				schema.setAssociations(consistentAssociationList);
				for (Association association : consistentAssociationList)
					existingAssociationList.add(association.getName());

			}
			List<EntityType> entityTypes = entityContainerView
					.getJPAEdmEntitySetView().getJPAEdmEntityTypeView()
					.getConsistentEdmEntityTypes();
			List<NavigationProperty> navigationProperties;
			if (entityTypes != null && !entityTypes.isEmpty()) {
				for (EntityType entityType : entityTypes) {

					List<NavigationProperty> consistentNavigationProperties = null;
					navigationProperties = entityType.getNavigationProperties();
					if (navigationProperties != null) {
						consistentNavigationProperties = new ArrayList<NavigationProperty>();
						for (NavigationProperty navigationProperty : navigationProperties) {
							if (existingAssociationList
									.contains(navigationProperty
											.getRelationship().getName())) {
								consistentNavigationProperties
										.add(navigationProperty);
							}
						}
						if (consistentNavigationProperties.isEmpty())
							entityType.setNavigationProperties(null);
						else
							entityType
									.setNavigationProperties(consistentNavigationProperties);
					}

				}
			}
		}

	}

	@Override
	public final JPAEdmAssociationView getJPAEdmAssociationView() {
		return this.associationView;
	}
}
