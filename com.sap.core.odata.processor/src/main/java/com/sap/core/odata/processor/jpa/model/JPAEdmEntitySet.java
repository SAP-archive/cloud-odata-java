package com.sap.core.odata.processor.jpa.model;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.processor.jpa.access.model.JPAEdmNameBuilder;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmSchemaView;

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
		return new JPAEdmEntitySetBuilder();
	}

	@Override
	public EntitySet getEdmEntitySet() {
		return currentEntitySet;
	}

	@Override
	public List<EntitySet> getConsistentEntitySetList() {
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
	}

	private class JPAEdmEntitySetBuilder implements JPAEdmBuilder {

		@Override
		public void build() throws ODataJPAModelException {

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
