package com.sap.core.odata.processor.core.jpa.model;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationSetView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityContainerView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmFunctionImportView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.core.jpa.access.model.JPAEdmNameBuilder;

public class JPAEdmEntityContainer extends JPAEdmBaseViewImpl implements
		JPAEdmEntityContainerView {

	private JPAEdmEntitySetView entitySetView;
	private JPAEdmSchemaView schemaView;
	private JPAEdmAssociationSetView associationSetView;

	private EntityContainer currentEntityContainer;
	private List<EntityContainer> consistentEntityContainerList;

	public JPAEdmEntityContainer(JPAEdmSchemaView view) {
		super(view);
		this.schemaView = view;
	}

	@Override
	public JPAEdmBuilder getBuilder() {
		if (this.builder == null)
			this.builder = new JPAEdmEntityContainerBuilder();

		return builder;
	}

	@Override
	public EntityContainer getEdmEntityContainer() {
		return currentEntityContainer;
	}

	@Override
	public List<EntityContainer> getConsistentEdmEntityContainerList() {
		return consistentEntityContainerList;
	}

	@Override
	public JPAEdmEntitySetView getJPAEdmEntitySetView() {
		return entitySetView;
	}

	@Override
	public JPAEdmAssociationSetView getEdmAssociationSetView() {
		return associationSetView;
	}

	@Override
	public void clean() {
		super.clean();
		this.entitySetView = null;
		this.associationSetView = null;
		this.currentEntityContainer = null;
		this.consistentEntityContainerList = null;
	}

	private class JPAEdmEntityContainerBuilder implements JPAEdmBuilder {
		/*
		 * 
		 * Each call to build method creates a new Entity Container and builds
		 * the entity container with Association Sets and Entity Sets. The newly
		 * created and built entity container is added to the exiting Entity
		 * Container List.
		 * 
		 * ************************************************************ Build
		 * EDM Entity Container - STEPS
		 * ************************************************************ 1)
		 * Instantiate New EDM Entity Container 2) Build Name for EDM Entity
		 * Container 2) Create Entity Container List (if does not exists) 3)
		 * Build EDM Entity Set 4) Add EDM Entity Set to EDM Entity Container 6)
		 * Build EDM Association Set 7) Add EDM Association Set to EDM Entity
		 * Container 8) Add EDM Entity Container to the Container List
		 * ************************************************************ Build
		 * EDM Entity Container - STEPS
		 * ************************************************************
		 */
		@Override
		public void build() throws ODataJPAModelException,
				ODataJPARuntimeException {

			currentEntityContainer = new EntityContainer();

			if (consistentEntityContainerList == null) {
				currentEntityContainer.setDefaultEntityContainer(true);
				consistentEntityContainerList = new ArrayList<EntityContainer>();
			}

			entitySetView = new JPAEdmEntitySet(schemaView);
			entitySetView.getBuilder().build();
			if (entitySetView.isConsistent())
				currentEntityContainer.setEntitySets(entitySetView
						.getConsistentEdmEntitySetList());
			else {
				isConsistent = false;
				return;
			}

			if (!schemaView.getJPAEdmAssociationView().isConsistent())
				schemaView.getJPAEdmAssociationView().getBuilder().build();

			associationSetView = new JPAEdmAssociationSet(schemaView);
			associationSetView.getBuilder().build();
			if (associationSetView.isConsistent())
				currentEntityContainer.setAssociationSets(associationSetView
						.getConsistentEdmAssociationSetList());
			else {
				isConsistent = false;
				return;
			}

			JPAEdmNameBuilder.build(JPAEdmEntityContainer.this);
			if (schemaView.getJPAEdmExtension() != null) {
				JPAEdmFunctionImportView functionImportView = new JPAEdmFunctionImport(
						schemaView);
				functionImportView.getBuilder().build();
				if (functionImportView.getConsistentFunctionImportList() != null) {
					currentEntityContainer
							.setFunctionImports(functionImportView
									.getConsistentFunctionImportList());
				}
			}
			
			consistentEntityContainerList.add(currentEntityContainer);
			isConsistent = true;

		}

	}
}
