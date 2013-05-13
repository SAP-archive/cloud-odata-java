package com.sap.core.odata.processor.core.jpa.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmMappingModelAccess;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmKeyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmNavigationPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.core.jpa.access.model.JPAEdmNameBuilder;

public class JPAEdmEntityType extends JPAEdmBaseViewImpl implements
		JPAEdmEntityTypeView {

	private JPAEdmSchemaView schemaView = null;
	private EntityType currentEdmEntityType = null;
	private javax.persistence.metamodel.EntityType<?> currentJPAEntityType = null;
	private EntityTypeList<EntityType> consistentEntityTypes = null;

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
		public void build() throws ODataJPAModelException,
				ODataJPARuntimeException {

			Set<javax.persistence.metamodel.EntityType<?>> jpaEntityTypes = metaModel
					.getEntities();

			if (jpaEntityTypes == null || jpaEntityTypes.isEmpty() == true)
				return;
			else if (consistentEntityTypes == null) {
				consistentEntityTypes = new EntityTypeList<EntityType>();

			}

			for (javax.persistence.metamodel.EntityType<?> jpaEntityType : jpaEntityTypes) {
				currentEdmEntityType = new EntityType();
				currentJPAEntityType = jpaEntityType;
				
				// Check for need to Exclude
				if(isExcluded(JPAEdmEntityType.this))
					continue;
				
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

		private boolean isExcluded(JPAEdmEntityType jpaEdmEntityType) {
			JPAEdmMappingModelAccess mappingModelAccess = jpaEdmEntityType
					.getJPAEdmMappingModelAccess();
			if (mappingModelAccess != null
					&& mappingModelAccess.isMappingModelExists()
					&& mappingModelAccess.checkExclusionOfJPAEntityType(jpaEdmEntityType.getJPAEntityType().getName())){
				return true;
			}
			return false;
		}

	}

	private class EntityTypeList<X> extends ArrayList<EntityType> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 719079109608251592L;

		@Override
		public Iterator<EntityType> iterator() {
			return new EntityTypeListIterator<X>(size());
		}

	}

	private class EntityTypeListIterator<E> implements ListIterator<EntityType> {

		private int size = 0;
		private int pos = 0;

		public EntityTypeListIterator(int listSize) {
			this.size = listSize;
		}

		@Override
		public void add(EntityType e) {
			consistentEntityTypes.add(e);
			size++;
		}

		@Override
		public boolean hasNext() {
			if (pos < size)
				return true;

			return false;
		}

		@Override
		public boolean hasPrevious() {
			if (pos > 0)
				return true;
			return false;
		}

		@Override
		public EntityType next() {
			if (pos < size) {
				currentEdmEntityType = consistentEntityTypes.get(pos++);
				return currentEdmEntityType;
			}

			return null;
		}

		@Override
		public int nextIndex() {
			return pos;
		}

		@Override
		public EntityType previous() {
			if (pos > 0 && pos < size) {
				currentEdmEntityType = consistentEntityTypes.get(--pos);
				return currentEdmEntityType;
			}
			return null;
		}

		@Override
		public int previousIndex() {
			if (pos > 0)
				return pos - 1;

			return 0;
		}

		@Override
		public void remove() {
			consistentEntityTypes.remove(pos);
		}

		@Override
		public void set(EntityType e) {
			consistentEntityTypes.set(pos, e);
		}

	}
}
