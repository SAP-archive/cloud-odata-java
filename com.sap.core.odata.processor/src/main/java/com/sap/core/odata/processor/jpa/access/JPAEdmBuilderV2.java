package com.sap.core.odata.processor.jpa.access;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.Metamodel;

import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Facets;
import com.sap.core.odata.api.edm.provider.Key;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.PropertyRef;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.processor.jpa.access.api.JPAEdmBuilder;

public class JPAEdmBuilderV2 implements JPAEdmBuilder {

	private static final String ENTITY_CONTAINER = "SalesOrderProcessingContainer";
	private static final String KEY_NAME = "id";

	private String pUnitName;

	private EntityManagerFactory emf = null;
	private Metamodel metaModel = null;

	public JPAEdmBuilderV2(String pUnitName,EntityManagerFactory emf) {
		this.pUnitName = pUnitName;
		this.emf = emf;
		metaModel = this.emf.getMetamodel();
	}

	@Override
	public List<Schema> getSchemas() {

		// Create a Schema with Namespace.
		// The Namespace equals JPA Persistence Unit
		List<Schema> schemas = new ArrayList<Schema>();
		Schema schema = new Schema();
		schema.setNamespace(pUnitName);

		// Create Entity Types
		schema.setEntityTypes(getEntityTypes());
		schema.setEntityContainers(getEntityContainers());
		return schemas;
	}

	private List<EntityContainer> getEntityContainers() {
		List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
		EntityContainer entityContainer = new EntityContainer();
	    entityContainer.setName(ENTITY_CONTAINER).setDefaultEntityContainer(true);
	    entityContainer.setEntitySets(getEntitySets());
	    entityContainers.add(entityContainer);
	    return entityContainers;
	}

	@Override
	public EntitySet getEntitySet(FullQualifiedName fqName) {
		EntitySet entitySet = new EntitySet();
		entitySet.setName(fqName.getName()+"s");
		entitySet.setEntityType(fqName);
		return entitySet;
	}

	private List<EntitySet> getEntitySets() {
		List<EntitySet> entitySets = new ArrayList<EntitySet>();
		Set<javax.persistence.metamodel.EntityType<?>> jpaEntityTypes = metaModel.getEntities();
		for (javax.persistence.metamodel.EntityType<?> jpaEntityType : jpaEntityTypes) {
			entitySets.add(getEntitySet(new FullQualifiedName(pUnitName, jpaEntityType.getName())));
		}
		return entitySets;
	}

	@Override
	public List<EntityType> getEntityTypes() {


		//FullQualifiedName fullQualifiedName = null;
		Set<javax.persistence.metamodel.EntityType<?>> jpaEntityTypes = metaModel
				.getEntities();
		List<EntityType> entityTypes = new ArrayList<EntityType>();
		for (javax.persistence.metamodel.EntityType<?> jpaEntityType : jpaEntityTypes) {
			/*EntityType entityType = new EntityType();
			fullQualifiedName = new FullQualifiedName(pUnitName,jpaEntityType.getName());
			entityType.setName(fullQualifiedName.toString());*/

			entityTypes.add(getEntityType(new FullQualifiedName(pUnitName,jpaEntityType.getName())));
		}

		return entityTypes;

	}

	@Override
	public EntityType getEntityType(FullQualifiedName fqName) {
		String entityName = fqName.getName();
		EntityType entityType = null;
		for (javax.persistence.metamodel.EntityType<?> jpaEntityType : metaModel
				.getEntities()) {
			if ( jpaEntityType.getName().equals(entityName) )
			{
				entityType = new EntityType();
				entityType.setProperties(getEntityProperties(jpaEntityType));
				entityType.setKey(getKey(jpaEntityType));
			}
		}

		return entityType;
	}



	private Key getKey(javax.persistence.metamodel.EntityType<?> jpaEntityType) {
		List<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
		keyProperties.add(new PropertyRef().setName(KEY_NAME));
		return new Key().setKeys(keyProperties);
	}

	private List<Property> getEntityProperties(javax.persistence.metamodel.EntityType<?> jpaEntityType) {

		List<Property> properties = new ArrayList<Property>();
		for(Attribute<?, ?> jpaAttribute : jpaEntityType.getAttributes()){
			if ( PersistentAttributeType.EMBEDDED.toString().equals(jpaAttribute.getPersistentAttributeType().toString()) )
				properties.add(createComplexProperty(jpaAttribute));
			else
				properties.add(createSimpleProperty(jpaAttribute));
		}
		return properties;
	}

	private SimpleProperty createSimpleProperty(Attribute<?, ?> jpaAttribute ){
		SimpleProperty simpleProperty = new SimpleProperty();

		//Property Name
		simpleProperty.setName(jpaAttribute.getName());

		//Edm Type
		EdmSimpleTypeKind simpleTypeKind = JPATypeConvertor.convertToEdmSimpleType(jpaAttribute.getJavaType());
		simpleProperty.setType(simpleTypeKind);

		//Facets
		Facets facets = new Facets( );

		simpleProperty.setFacets(facets);


		return simpleProperty;
	}

	private ComplexProperty createComplexProperty(Attribute<?, ?> jpaAttribute){
		return null;
	}

}
