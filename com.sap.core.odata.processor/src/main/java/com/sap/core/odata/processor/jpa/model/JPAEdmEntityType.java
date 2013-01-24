package com.sap.core.odata.processor.jpa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.processor.jpa.access.model.JPAEdmNameBuilder;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmKeyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmNavigationPropertyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

public class JPAEdmEntityType extends JPAEdmBaseViewImpl implements
		JPAEdmEntityTypeView {

	private JPAEdmSchemaView schemaView = null;
	private EntityType currentEdmEntityType = null;
	private javax.persistence.metamodel.EntityType<?> currentJPAEntityType = null;
	private List<EntityType> consistentEntityTypes = null;

	public JPAEdmEntityType(JPAEdmSchemaView view) {
		super(view);
		this.schemaView = view;
	}

	@Override
	public JPAEdmBuilder getBuilder() {
		return new JPAEdmEntityTypeBuilder();
	}

	@Override
	public EntityType getEdmEntityType() {
		return this.currentEdmEntityType;
	}

	@Override
	public javax.persistence.metamodel.EntityType<?> getJPAEntityType() {
		return this.currentJPAEntityType;
	}

	@Override
	public List<EntityType> getConsistentEdmEntityTypes() {
		return consistentEntityTypes;
	}

	private class JPAEdmEntityTypeBuilder implements JPAEdmBuilder {

		@Override
		public void build() throws ODataJPAModelException {

			Set<javax.persistence.metamodel.EntityType<?>> jpaEntityTypes = metaModel
					.getEntities();

			if (jpaEntityTypes == null || jpaEntityTypes.isEmpty() == true)
				return;
			else if (consistentEntityTypes == null) {
				consistentEntityTypes = new ArrayList<EntityType>();
			}

			for (javax.persistence.metamodel.EntityType<?> jpaEntityType : jpaEntityTypes) {
				currentEdmEntityType = new EntityType();
				currentJPAEntityType = jpaEntityType;
				JPAEdmNameBuilder.build(JPAEdmEntityType.this);

				JPAEdmPropertyView propertyView = new JPAEdmProperty(schemaView);
				propertyView.getBuilder().build();

				currentEdmEntityType.setProperties(propertyView
						.getPropertyList());
				if(propertyView.getJPAEdmNavigationPropertyView() != null)
				{
					JPAEdmNavigationPropertyView navPropView = propertyView.getJPAEdmNavigationPropertyView();
					if(navPropView.getConsistentEdmNavigationProperties() != null && !navPropView.getConsistentEdmNavigationProperties().isEmpty())
					{
						currentEdmEntityType.setNavigationProperties(navPropView.getConsistentEdmNavigationProperties());
					}
				}
				JPAEdmKeyView keyView = propertyView.getJPAEdmKeyView();
				currentEdmEntityType.setKey(keyView.getEdmKey());

				consistentEntityTypes.add(currentEdmEntityType);
			}

		}

	}
}
