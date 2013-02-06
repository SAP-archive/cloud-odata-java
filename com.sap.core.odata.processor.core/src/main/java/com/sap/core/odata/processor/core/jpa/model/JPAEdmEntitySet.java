package com.sap.core.odata.processor.core.jpa.model;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.processor.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.api.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.api.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.core.jpa.access.model.JPAEdmNameBuilder;

public class JPAEdmEntitySet extends JPAEdmBaseViewImpl implements
		JPAEdmEntitySetView {

	private EntitySet currentEntitySet = null;
	private List<EntitySet> consistentEntitySetList = null;
	private JPAEdmEntityTypeView entityTypeView = null;
	private JPAEdmSchemaView schemaView;

	public JPAEdmEntitySet(JPAEdmSchemaView view) {
		super(view);
		this.schemaView = view;
	}

	@Override
	public JPAEdmBuilder getBuilder() {
		if (this.builder == null)
			this.builder = new JPAEdmEntitySetBuilder();
		
		return builder;
	}

	@Override
	public EntitySet getEdmEntitySet() {
		return currentEntitySet;
	}

	@Override
	public List<EntitySet> getConsistentEdmEntitySetList() {
		return consistentEntitySetList;
	}

	@Override
	public JPAEdmEntityTypeView getJPAEdmEntityTypeView() {
		return entityTypeView;
	}

	@Override
	public void clean() {
		this.currentEntitySet = null;
		this.consistentEntitySetList = null;
		this.entityTypeView = null;
		this.isConsistent = false;
	}

	private class JPAEdmEntitySetBuilder implements JPAEdmBuilder {

		@Override
		public void build() throws ODataJPAModelException, ODataJPARuntimeException {

			if (consistentEntitySetList == null)
				consistentEntitySetList = new ArrayList<EntitySet>();

			entityTypeView = new JPAEdmEntityType(schemaView);
			entityTypeView.getBuilder().build();

			if (entityTypeView.isConsistent() && entityTypeView.getConsistentEdmEntityTypes() != null ) {

				String nameSpace = schemaView.getEdmSchema().getNamespace();
				for (EntityType entityType : entityTypeView
						.getConsistentEdmEntityTypes()) {

					currentEntitySet = new EntitySet();
					currentEntitySet.setEntityType(new FullQualifiedName(
							nameSpace, entityType.getName()));
					JPAEdmNameBuilder.build(JPAEdmEntitySet.this);
					consistentEntitySetList.add(currentEntitySet);

				}
				isConsistent = true;
			} else {
				isConsistent = false;
				return;
			}

		}

	}

}
