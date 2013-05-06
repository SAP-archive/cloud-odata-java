package com.sap.core.odata.processor.core.jpa.cud;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;

import org.easymock.EasyMock;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;
import com.sap.core.odata.core.edm.EdmInt32;
import com.sap.core.odata.core.edm.EdmString;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmMappingImpl;

public class JPATestUtil {
	
	public static EdmStructuralType getEdmStructuralType()
	{
		EdmStructuralType edmType = EasyMock.createMock(EdmStructuralType.class);
		try {
			List<String> propertyNames = new ArrayList<String>();
			propertyNames.add("id");
			propertyNames.add("description");
			EdmProperty edmProperty1 = mockEdmProperty1();
			EdmProperty edmProperty2 = mockEdmProperty2();
			EdmMapping edmMapping = mockEdmMappingForEntityType();
			EasyMock.expect(edmType.getName()).andStubReturn("SalesOrderHeader");
			EasyMock.expect(edmType.getPropertyNames()).andStubReturn(propertyNames);
			EasyMock.expect(edmType.getProperty("id")).andStubReturn(edmProperty1);
			EasyMock.expect(edmType.getProperty("description")).andStubReturn(edmProperty2);
			EasyMock.expect(edmType.getMapping()).andStubReturn(edmMapping);
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		EasyMock.replay(edmType);
		return edmType;
	}
	
	
	public static  Map<String, Object> getPropertyValueMap()
	{
		Map<String,Object> propertyValueMap = new HashMap<String, Object>();
		propertyValueMap.put("id", 1);
		return propertyValueMap;
	}
	public static PostUriInfo getPostUriInfo() {
		PostUriInfo postUriInfo = EasyMock.createMock(PostUriInfo.class);
		EdmEntitySet targetEntitySet = mockSourceEdmEntitySet();
		EasyMock.expect(postUriInfo.getTargetEntitySet()).andStubReturn(targetEntitySet);
		EasyMock.replay(postUriInfo);
		return postUriInfo;
	}
	public static PutMergePatchUriInfo getPutMergePatchUriInfo() {
		PutMergePatchUriInfo putUriInfo = EasyMock.createMock(PutMergePatchUriInfo.class);
		EdmEntitySet targetEntitySet = mockSourceEdmEntitySet();
		EasyMock.expect(putUriInfo.getTargetEntitySet()).andStubReturn(targetEntitySet);
		EasyMock.replay(putUriInfo);
		return putUriInfo;
	}

	public static EdmEntitySet mockSourceEdmEntitySet() {
		EdmEntitySet entitySet = EasyMock.createMock(EdmEntitySet.class);
		try {
			final EdmNavigationProperty navigationProperty = mockNavigationProperty();
			EasyMock.expect(entitySet.getEntityType()).andStubReturn(mockEdmEntityType(navigationProperty));
			EasyMock.expect(entitySet.getRelatedEntitySet(navigationProperty)).andStubReturn(getSIEntitySet());
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		EasyMock.replay(entitySet);
		return entitySet;
	}
	public static Object getJPAEntity()
	{
		SalesOrderHeader sHead = new SalesOrderHeader(1,"laptop");
		SalesOrderLineItem sItem = new SalesOrderLineItem(23);
		List<SalesOrderLineItem> sItems = new ArrayList<SalesOrderLineItem>();
		sItems.add(sItem);
		sHead.setSalesOrderLineItems(sItems);
		return sHead;
		
	}

	public static EdmEntityType mockEdmEntityType(EdmNavigationProperty navigationProperty) {
		
		EdmEntityType entityType = EasyMock.createMock(EdmEntityType.class);
		EdmMapping mapping = EasyMock.createMock(EdmMapping.class);
		List<String> navigationPropertyNames = new ArrayList<String>();
		List<String> propertyNames = new ArrayList<String>();
		List<EdmProperty> keyProperties = new ArrayList<EdmProperty>();
		propertyNames.add("id");
		propertyNames.add("description");
		navigationPropertyNames.add("SalesOrderLineItemDetails");
		try {
			EasyMock.expect(mapping.getInternalName()).andStubReturn("SalesOrderHeader");
			EasyMock.replay(mapping);
			EasyMock.expect(entityType.getName()).andStubReturn("SalesOrderHeader");
			EasyMock.expect(entityType.getMapping()).andStubReturn(mapping);
			EasyMock.expect(entityType.getNavigationPropertyNames()).andStubReturn(navigationPropertyNames);
			EasyMock.expect(entityType.getProperty("SalesOrderLineItemDetails")).andStubReturn(/*mockNavigationProperty()*/navigationProperty);
			EdmProperty property1 = mockEdmProperty1();
			keyProperties.add(property1);
			EasyMock.expect(entityType.getProperty("id")).andStubReturn(property1);
			EasyMock.expect(entityType.getProperty("description")).andStubReturn(mockEdmProperty2());
			EasyMock.expect(entityType.getKeyProperties()).andReturn(keyProperties);
			EasyMock.expect(entityType.getPropertyNames()).andStubReturn(propertyNames);
			
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		EasyMock.replay(entityType);
		return entityType;
	}
	private static EdmEntitySet getSIEntitySet() {
		EdmEntitySet entitySet = EasyMock.createMock(EdmEntitySet.class);
        try {
               EasyMock.expect(entitySet.getEntityType()).andStubReturn(
                            mockTargetEdmEntityType());
        } catch (EdmException e) {
               fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
                            + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
        }
        EasyMock.replay(entitySet);
        return entitySet;

	}
	
	public static EdmEntityType mockTargetEdmEntityType() {
        EdmEntityType entityType = EasyMock.createMock(EdmEntityType.class);
        EdmMapping mapping = EasyMock.createMock(EdmMapping.class);

        List<String> propertyNames = new ArrayList<String>();
        propertyNames.add("price");
        try {
               EasyMock.expect(mapping.getInternalName()).andStubReturn(
                            "SalesOrderLineItem");
               EasyMock.replay(mapping);
               EasyMock.expect(entityType.getName()).andStubReturn(
                            "SalesOrderLineItem");
               EasyMock.expect(entityType.getMapping()).andStubReturn(mapping);
               EdmProperty property = mockEdmPropertyOfTarget();
               EasyMock.expect(entityType.getProperty("price")).andStubReturn(
                            property);
               EasyMock.expect(entityType.getPropertyNames()).andStubReturn(
                            propertyNames);

        } catch (EdmException e) {
               fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
                            + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
        }
        EasyMock.replay(entityType);
        return entityType;
  }

	private static EdmProperty mockEdmPropertyOfTarget() {
        EdmProperty edmProperty = EasyMock.createMock(EdmProperty.class);

        EdmType type = EasyMock.createMock(EdmType.class);
        EasyMock.expect(type.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
        EasyMock.replay(type);
        EdmMapping mapping = EasyMock.createMock(EdmMapping.class);
        EasyMock.expect(mapping.getInternalName()).andStubReturn("price");
        EasyMock.replay(mapping);
        try {
               EasyMock.expect(edmProperty.getName()).andStubReturn("price");
               EasyMock.expect(edmProperty.getType()).andStubReturn(type);
               EasyMock.expect(edmProperty.getMapping()).andStubReturn(mapping);
        } catch (EdmException e) {
               fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
                            + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
        }
        EasyMock.replay(edmProperty);
        return edmProperty;
  }



	public static EdmNavigationProperty mockNavigationProperty() {
		EdmNavigationProperty navigationProperty = EasyMock.createMock(EdmNavigationProperty.class);
		EdmMapping mapping = EasyMock.createMock(EdmMapping.class);
		EasyMock.expect(mapping.getInternalName()).andStubReturn("salesOrderLineItems");
		EasyMock.replay(mapping);
		try {
			EasyMock.expect(navigationProperty.getMultiplicity()).andStubReturn(EdmMultiplicity.MANY);
			EasyMock.expect(navigationProperty.getMapping()).andStubReturn(mapping);
			
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		EasyMock.replay(navigationProperty);
		return navigationProperty;
		
		//EdmNavigationPropertyImplProv navigationPropertyImplProv = new EdmNavigationPropertyImplProv(edm, property)
	}


	public static Metamodel mockMetaModel()
	{
		Set<EntityType<?>> jpaEntities = new HashSet<EntityType<?>>();
		jpaEntities.add(new JPATestUtil.DemoEntityType());
		Metamodel metaModel = EasyMock.createMock(Metamodel.class);
		EasyMock.expect(metaModel.getEntities()).andStubReturn(jpaEntities);
		EasyMock.replay(metaModel);
		return metaModel;
	}
	private static EdmMapping mockEdmMappingForEntityType() {
		EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
		EasyMock.expect(edmMapping.getInternalName()).andStubReturn("SalesOrderHeader");
		EasyMock.replay(edmMapping);
		return edmMapping;
	}

	private static EdmProperty mockEdmProperty1() {
		EdmProperty edmProperty = EasyMock.createMock(EdmProperty.class);
		EdmType edmType = mockEdmType1();
		EdmMapping edmMapping = mockEdmMappingForProperty1();
		try {
			EasyMock.expect(edmProperty.getName()).andStubReturn("id");
			EasyMock.expect(edmProperty.getType()).andStubReturn(edmType);
			EasyMock.expect(edmProperty.getMapping()).andStubReturn(edmMapping);
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		EasyMock.replay(edmProperty);
		return edmProperty;
		
	}
	private static EdmProperty mockEdmProperty2() {
		EdmProperty edmProperty = EasyMock.createMock(EdmProperty.class);
		EdmType edmType = mockEdmType2();
		EdmMapping edmMapping = mockEdmMappingForProperty2();
		try {
			EasyMock.expect(edmProperty.getName()).andStubReturn("description");
			EasyMock.expect(edmProperty.getType()).andStubReturn(edmType);
			EasyMock.expect(edmProperty.getMapping()).andStubReturn(edmMapping);
		} catch (EdmException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()
					+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		EasyMock.replay(edmProperty);
		return edmProperty;
		
	}

	private static EdmMapping mockEdmMappingForProperty1() {
		JPAEdmMappingImpl  mapping = new JPAEdmMappingImpl();
		mapping.setJPAType(null);
		mapping.setInternalName("id");
		return (EdmMapping)mapping;
	}
	private static EdmMapping mockEdmMappingForProperty2() {
		JPAEdmMappingImpl  mapping = new JPAEdmMappingImpl();
		mapping.setJPAType(null);
		mapping.setInternalName("description");
		return (EdmMapping)mapping;
	}

	private static EdmType mockEdmType1() {
		EdmInt32 edmType = new EdmInt32();
		return edmType;
		
	}
	private static EdmType mockEdmType2() {
		EdmString edmType = new EdmString();
		return edmType;
		
	}
	
	
	static class DemoEntityType implements EntityType<SalesOrderHeader>
	{

		@Override
		public <Y> SingularAttribute<? super com.sap.core.odata.processor.core.jpa.cud.SalesOrderHeader, Y> getId(
				Class<Y> type) {
			return null;
		}

		@Override
		public <Y> SingularAttribute<com.sap.core.odata.processor.core.jpa.cud.SalesOrderHeader, Y> getDeclaredId(
				Class<Y> type) {
			return null;
		}

		@Override
		public <Y> SingularAttribute<? super SalesOrderHeader, Y> getVersion(
				Class<Y> type) {
			return null;
		}

		@Override
		public <Y> SingularAttribute<SalesOrderHeader, Y> getDeclaredVersion(
				Class<Y> type) {
			return null;
		}

		@Override
		public IdentifiableType<? super SalesOrderHeader> getSupertype() {
			return null;
		}

		@Override
		public boolean hasSingleIdAttribute() {
			return false;
		}

		@Override
		public boolean hasVersionAttribute() {
			return false;
		}

		@Override
		public Set<SingularAttribute<? super SalesOrderHeader, ?>> getIdClassAttributes() {
			return null;
		}

		@Override
		public Type<?> getIdType() {
			return null;
		}

		@Override
		public Set<Attribute<? super SalesOrderHeader, ?>> getAttributes() {
			return null;
		}

		@Override
		public Set<Attribute<SalesOrderHeader, ?>> getDeclaredAttributes() {
			return null;
		}

		@Override
		public <Y> SingularAttribute<? super SalesOrderHeader, Y> getSingularAttribute(
				String name, Class<Y> type) {
			return null;
		}

		@Override
		public <Y> SingularAttribute<SalesOrderHeader, Y> getDeclaredSingularAttribute(
				String name, Class<Y> type) {
			return null;
		}

		@Override
		public Set<SingularAttribute<? super SalesOrderHeader, ?>> getSingularAttributes() {
			return null;
		}

		@Override
		public Set<SingularAttribute<SalesOrderHeader, ?>> getDeclaredSingularAttributes() {
			return null;
		}

		@Override
		public <E> CollectionAttribute<? super SalesOrderHeader, E> getCollection(
				String name, Class<E> elementType) {
			return null;
		}

		@Override
		public <E> CollectionAttribute<SalesOrderHeader, E> getDeclaredCollection(
				String name, Class<E> elementType) {
			return null;
		}

		@Override
		public <E> SetAttribute<? super SalesOrderHeader, E> getSet(
				String name, Class<E> elementType) {
			return null;
		}

		@Override
		public <E> SetAttribute<SalesOrderHeader, E> getDeclaredSet(
				String name, Class<E> elementType) {
			return null;
		}

		@Override
		public <E> ListAttribute<? super SalesOrderHeader, E> getList(
				String name, Class<E> elementType) {
			return null;
		}

		@Override
		public <E> ListAttribute<SalesOrderHeader, E> getDeclaredList(
				String name, Class<E> elementType) {
			return null;
		}

		@Override
		public <K, V> MapAttribute<? super SalesOrderHeader, K, V> getMap(
				String name, Class<K> keyType, Class<V> valueType) {
			return null;
		}

		@Override
		public <K, V> MapAttribute<SalesOrderHeader, K, V> getDeclaredMap(
				String name, Class<K> keyType, Class<V> valueType) {
			return null;
		}

		@Override
		public Set<PluralAttribute<? super SalesOrderHeader, ?, ?>> getPluralAttributes() {
			return null;
		}

		@Override
		public Set<PluralAttribute<SalesOrderHeader, ?, ?>> getDeclaredPluralAttributes() {
			return null;
		}

		@Override
		public Attribute<? super SalesOrderHeader, ?> getAttribute(String name) {
			return null;
		}

		@Override
		public Attribute<SalesOrderHeader, ?> getDeclaredAttribute(String name) {
			return null;
		}

		@Override
		public SingularAttribute<? super SalesOrderHeader, ?> getSingularAttribute(
				String name) {
			return null;
		}

		@Override
		public SingularAttribute<SalesOrderHeader, ?> getDeclaredSingularAttribute(
				String name) {
			return null;
		}

		@Override
		public CollectionAttribute<? super SalesOrderHeader, ?> getCollection(
				String name) {
			return null;
		}

		@Override
		public CollectionAttribute<SalesOrderHeader, ?> getDeclaredCollection(
				String name) {
			return null;
		}

		@Override
		public SetAttribute<? super SalesOrderHeader, ?> getSet(String name) {
			return null;
		}

		@Override
		public SetAttribute<SalesOrderHeader, ?> getDeclaredSet(String name) {
			return null;
		}

		@Override
		public ListAttribute<? super SalesOrderHeader, ?> getList(String name) {
			return null;
		}

		@Override
		public ListAttribute<SalesOrderHeader, ?> getDeclaredList(String name) {
			return null;
		}

		@Override
		public MapAttribute<? super SalesOrderHeader, ?, ?> getMap(String name) {
			return null;
		}

		@Override
		public MapAttribute<SalesOrderHeader, ?, ?> getDeclaredMap(String name) {
			return null;
		}

		@Override
		public javax.persistence.metamodel.Type.PersistenceType getPersistenceType() {
			return null;
		}

		@Override
		public Class<SalesOrderHeader> getJavaType() {
			return com.sap.core.odata.processor.core.jpa.cud.SalesOrderHeader.class;
		}

		@Override
		public javax.persistence.metamodel.Bindable.BindableType getBindableType() {
			return null;
		}

		@Override
		public Class<SalesOrderHeader> getBindableJavaType() {
			return null;
		}

		@Override
		public String getName() {
			return "SalesOrderHeader";
		}
		
	}


	
	

	

}
