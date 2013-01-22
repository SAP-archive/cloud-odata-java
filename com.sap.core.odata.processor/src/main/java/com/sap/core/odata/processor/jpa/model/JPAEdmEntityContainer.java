package com.sap.core.odata.processor.jpa.model;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.processor.jpa.access.model.JPAEdmNameBuilder;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmAssociationSetView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityContainerView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

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
		return new JPAEdmEntityContainerBuilder();
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
		 *  Each call to build method creates a new Entity Container
		 *  and builds the entity container with Association Sets and
		 *  Entity Sets. The newly created and built entity container
		 *  is added to the exiting Entity Container List.
		 *   
		 * ************************************************************
		 * 				Build EDM Entity Container - STEPS 
		 * ************************************************************
		 * 1) Instantiate New EDM Entity Container
		 * 2) Build Name for EDM Entity Container
		 * 2) Create Entity Container List (if does not exists)
		 * 3) Build EDM Entity Set
		 * 4) Add EDM Entity Set to EDM Entity Container
		 * 6) Build EDM Association Set
		 * 7) Add EDM Association Set to EDM Entity Container
		 * 8) Add EDM Entity Container to the Container List
		 * ************************************************************
		 * 				Build EDM Entity Container - STEPS 
		 * ************************************************************ 
		 *  
		 */
		@Override
		public void build() throws ODataJPAModelException {

			currentEntityContainer = new EntityContainer();

			if (consistentEntityContainerList == null) {
				currentEntityContainer.setDefaultEntityContainer(true);
				consistentEntityContainerList = new ArrayList<EntityContainer>();
			}

			entitySetView = new JPAEdmEntitySet(schemaView);
			entitySetView.getBuilder().build();
			if (entitySetView.isConsistent())
				currentEntityContainer.setEntitySets(entitySetView
						.getConsistentEntitySetList());
			else {
				isConsistent = false;
				return;
			}
			
			associationSetView = new JPAEdmAssociationSet(
					JPAEdmEntityContainer.this);
			associationSetView.getBuilder().build();
			if (associationSetView.isConsistent())
				currentEntityContainer.setAssociationSets(associationSetView
						.getConsistentEdmAssociationSetList());
			else {
				isConsistent = false;
				//return;
			}
			
			
			JPAEdmNameBuilder.build(JPAEdmEntityContainer.this);
			consistentEntityContainerList.add(currentEntityContainer);
			isConsistent = true;

		}

	}
}
