package com.sap.core.odata.processor.core.jpa.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.processor.core.jpa.access.model.JPAEdmNameBuilder;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmKeyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmNavigationPropertyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmSchemaView;

public class JPAEdmEntityType extends JPAEdmBaseViewImpl implements
		JPAEdmEntityTypeView {

	private JPAEdmSchemaView schemaView = null;
	private EntityType currentEdmEntityType = null;
	private javax.persistence.metamodel.EntityType<?> currentJPAEntityType = null;
	private List<EntityType> consistentEntityTypes = null;

	private HashMap<String, EntityType> consistentEntityTypeMap;

	public JPAEdmEntityType(JPAEdmSchemaView view) {
		super(view);
		this.schemaView = view;
		consistentEntityTypeMap = new HashMap<String, EntityType>();
	}

	@Override
	public JPAEdmBuilder getBuilder() {
		if (this.builder == null)
			this.builder = new JPAEdmEntityTypeBuilder();
		
		return builder;
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

	@Override
	public EntityType searchEdmEntityType(String jpaEntityTypeName) {
		return consistentEntityTypeMap.get(jpaEntityTypeName);
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
						.getEdmPropertyList());
				if (propertyView.getJPAEdmNavigationPropertyView() != null) {
					JPAEdmNavigationPropertyView navPropView = propertyView
							.getJPAEdmNavigationPropertyView();
					if (navPropView.getConsistentEdmNavigationProperties() != null
							&& !navPropView
									.getConsistentEdmNavigationProperties()
									.isEmpty()) {
						currentEdmEntityType
								.setNavigationProperties(navPropView
										.getConsistentEdmNavigationProperties());
					}
				}
				JPAEdmKeyView keyView = propertyView.getJPAEdmKeyView();
				currentEdmEntityType.setKey(keyView.getEdmKey());

				consistentEntityTypes.add(currentEdmEntityType);
				consistentEntityTypeMap.put(currentJPAEntityType.getName(),
						currentEdmEntityType);
			}

		}

	}

}
