package com.sap.core.odata.processor.core.jpa.mock.model;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.edm.provider.Key;
import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.PropertyRef;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmMapping;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmMappingImpl;

public class EdmSchemaMock {
	
	private static final String ASSOCIATION_ROLE_NAME_ONE = "SalesOrderHeader";
	private static final String ASSOCIATION_NAME = "SalesOrderHeader_SalesOrderItem";
	private static final String ASSOCIATION_SET_NAME = "SalesOrderHeader_SalesOrderItemSet";
	private static final String ASSOCIATION_ROLE_NAME_TWO = "SalesOrderItem";
	private static final String NAMESPACE = "salesorderprocessing";
	private static final String ENTITY_CONTAINER_NAME = "salesorderprocessingContainer";
	private static final String ENTITY_NAME_ONE = "SalesOrderHeader";
	private static final String ENTITY_NAME_TWO = "SalesOrderItem";
	private static final String ENTITY_SET_NAME_ONE = "SalesOrderHeaders";
	private static final String FUNCTION_IMPORT_NAME_ONE = "SalesOrder_FunctionImport1";
	private static final String FUNCTION_IMPORT_NAME_TWO = "SalesOrder_FunctionImport2";
	private static final String ENTITY_SET_NAME_TWO = "SalesOrderItems";
	private static final String COMPLEX_TYPE_NAME_ONE = "Address";
	private static final String COMPLEX_TYPE_NAME_TWO = "SalesOrderItemKey";
	
	public static Schema createMockEdmSchema()
	{
		Schema schema = new Schema();
		schema.setNamespace(NAMESPACE);
		schema.setComplexTypes(createComplexTypes());
		schema.setEntityContainers(createEntityContainer());
		schema.setEntityTypes(createEntityTypes());
		schema.setAssociations(createAssociations());
		return schema;
	}
	private static List<EntityContainer> createEntityContainer() {
		List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
		EntityContainer entityContainer = new EntityContainer();
		entityContainer.setName(ENTITY_CONTAINER_NAME);
		entityContainer.setEntitySets(createEntitySets());
		entityContainer.setAssociationSets(createAssociationSets());
		entityContainer.setFunctionImports(createFunctionImports());
		entityContainers.add(entityContainer);
		return entityContainers;
	}
	private static List<AssociationSet> createAssociationSets() {
		List<AssociationSet> associationSets = new ArrayList<AssociationSet>();
		AssociationSet associationSet = new AssociationSet();
		associationSet.setName(ASSOCIATION_SET_NAME);
		associationSet.setAssociation(new FullQualifiedName(NAMESPACE, ASSOCIATION_NAME));
		associationSet.setEnd1(new AssociationSetEnd().setEntitySet(ENTITY_SET_NAME_ONE)
				.setRole(ASSOCIATION_ROLE_NAME_ONE));
		associationSet.setEnd2(new AssociationSetEnd().setEntitySet(ENTITY_SET_NAME_TWO)
				.setRole(ASSOCIATION_ROLE_NAME_TWO));
		associationSets.add(associationSet);
		return associationSets;
	}
	private static List<EntitySet> createEntitySets() {
		List<EntitySet> entitySets = new ArrayList<EntitySet>();
		EntitySet entitySet = new EntitySet();
		entitySet.setName(ENTITY_SET_NAME_ONE);
		entitySet.setEntityType(new FullQualifiedName(NAMESPACE, ENTITY_NAME_ONE));
		entitySets.add(entitySet);
		entitySet = new EntitySet();
		entitySet.setName(ENTITY_SET_NAME_TWO);
		entitySet.setEntityType(new FullQualifiedName(NAMESPACE, ENTITY_NAME_TWO));
		entitySets.add(entitySet);
		return entitySets;
	}
	private static List<FunctionImport> createFunctionImports() {
		List<FunctionImport> functionImports = new ArrayList<FunctionImport>();
		FunctionImport functionImport = new FunctionImport();
		functionImport.setName(FUNCTION_IMPORT_NAME_ONE);
		functionImports.add(functionImport);
		functionImport = new FunctionImport();
		functionImport.setName(FUNCTION_IMPORT_NAME_TWO);
		functionImports.add(functionImport);
		return functionImports;
	}
	private static List<Association> createAssociations() {
		List<Association> associations = new ArrayList<Association>();
		Association association = new Association();
		association.setName(ASSOCIATION_NAME);
		association.setEnd1(new AssociationEnd().setMultiplicity(EdmMultiplicity.ONE)
				.setRole(ASSOCIATION_ROLE_NAME_ONE)
				.setType(new FullQualifiedName(NAMESPACE, ENTITY_NAME_ONE)));
		association.setEnd2(new AssociationEnd().setMultiplicity(EdmMultiplicity.MANY)
				.setRole(ASSOCIATION_ROLE_NAME_TWO)
				.setType(new FullQualifiedName(NAMESPACE, ENTITY_NAME_TWO)));
		associations.add(association);
		return associations;
	}
	private static List<EntityType> createEntityTypes() {
		List<EntityType> entityTypes = new ArrayList<EntityType>();
		EntityType entityType = new EntityType();
		entityType.setName(ENTITY_NAME_ONE);
		String[] keyNamesOne = {"SoId"};
		entityType.setKey(createKey(keyNamesOne));
		entityTypes.add(entityType);
		
		entityType = new EntityType();
		entityType.setName(ENTITY_NAME_TWO);
		String[] keyNamesTwo = {"SoId","LiId"};
		entityType.setKey(createKey(keyNamesTwo));
		entityTypes.add(entityType);
		return entityTypes;
		
	}
	private static Key createKey(String[] keyNames) {
		Key key = new Key();
		List<PropertyRef> keys = new ArrayList<PropertyRef>();
		for(String keyName:keyNames)
			keys.add(new PropertyRef().setName(keyName));
		key.setKeys(keys);
		return null;
	}
	private static List<ComplexType> createComplexTypes() {
		List<ComplexType> complexTypes = new ArrayList<ComplexType>();
		ComplexType complexTypeOne = new ComplexType();
		complexTypeOne.setName(COMPLEX_TYPE_NAME_ONE);
		complexTypeOne.setProperties(createComplexTypePropertiesOne());
		complexTypes.add(complexTypeOne);
		ComplexType complexTypeTwo = new ComplexType();
		complexTypeTwo.setName(COMPLEX_TYPE_NAME_TWO);
		complexTypeTwo.setProperties(createComplexTypePropertiesTwo());
		complexTypes.add(complexTypeTwo);
		return complexTypes;
	}
	private static List<Property> createComplexTypePropertiesTwo() {
		List<Property> properties = new ArrayList<Property>();
		SimpleProperty property = new SimpleProperty();
		property.setName("SoId");
		property.setType(EdmSimpleTypeKind.Int64);
		JPAEdmMapping mapping = new JPAEdmMappingImpl();
		mapping.setJPAColumnName("Sales_Order_Id");
		((Mapping)mapping).setInternalName("SalesOrderItemKey.SoId");
		property.setMapping((Mapping) mapping);
		properties.add(property);
		property = new SimpleProperty();
		property.setName("LiId");
		property.setType(EdmSimpleTypeKind.Int64);
		mapping = new JPAEdmMappingImpl();
		mapping.setJPAColumnName("Sales_Order_Item_Id");
		property.setMapping((Mapping) mapping);
		properties.add(property);
		return properties;
		
	}
	private static List<Property> createComplexTypePropertiesOne() {
		List<Property> properties = new ArrayList<Property>();
		SimpleProperty property = new SimpleProperty();
		property.setName("StreetName");
		property.setType(EdmSimpleTypeKind.String);
		JPAEdmMapping mapping = new JPAEdmMappingImpl();
		mapping.setJPAColumnName("STREET_NAME");
		property.setMapping((Mapping) mapping);
		properties.add(property);
		property = new SimpleProperty();
		property.setName("City");
		property.setType(EdmSimpleTypeKind.String);
		mapping = new JPAEdmMappingImpl();
		mapping.setJPAColumnName("CITY");
		property.setMapping((Mapping) mapping);
		properties.add(property);
		return properties;
	}

}
