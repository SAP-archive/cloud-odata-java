package com.sap.core.odata.processor.jpa.access;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.Metamodel;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Facets;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.processor.jpa.access.api.JPAEdmBuilder;

public class JPAEdmBuilderV2 implements JPAEdmBuilder {

	private String pUnitName;
	
	private EntityManagerFactory emf = null;
	private Metamodel metaModel = null;

	public JPAEdmBuilderV2(String pUnitName) {
		this.pUnitName = pUnitName;
		emf = Persistence.createEntityManagerFactory(this.pUnitName);
		metaModel = emf.getMetamodel();
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
		return schemas;
	}

	@Override
	public List<EntityType> getEntityTypes() {
		
		
		FullQualifiedName fullQualifiedName = null;
		Set<javax.persistence.metamodel.EntityType<?>> jpaEntityTypes = metaModel
				.getEntities();
		List<EntityType> entityTypes = new ArrayList<EntityType>();
		for (javax.persistence.metamodel.EntityType<?> jpaEntityType : jpaEntityTypes) {
			EntityType entityType = new EntityType();
			fullQualifiedName = new FullQualifiedName(pUnitName,jpaEntityType.getName());
			entityType.setName(fullQualifiedName.toString());
			entityTypes.add(entityType);
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
			}
		}

		return entityType;
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
		FullQualifiedName fullQualifiedName = new FullQualifiedName(pUnitName, jpaAttribute.getName());
		simpleProperty.setName(fullQualifiedName.toString());
		
		//Edm Type
		EdmSimpleTypeKind simpleTypeKind = JPATypeConvertor.convertToEdmSimpleType(jpaAttribute.getJavaType());
		simpleProperty.setType(simpleTypeKind);
		
		Facets facets = new Facets( );
		
		return simpleProperty;
	}
	
	private ComplexProperty createComplexProperty(Attribute<?, ?> jpaAttribute){
		return null;
	}

}
