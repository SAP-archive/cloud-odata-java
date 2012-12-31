package com.sap.core.odata.processor.jpa.access;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.ComplexType;
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

		// Set Entity Types
		schema.setEntityTypes(getEntityTypes());
		// Set Complex Types

		/*To be uncommented later		
		schema.setComplexTypes(getComplexTypes());*/
		
		// Set Entity Container
		schema.setEntityContainers(getEntityContainers());
		schemas.add(schema);
		return schemas;
	}

	private List<EntityContainer> getEntityContainers() {
		List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
		EntityContainer entityContainer = new EntityContainer();
	    entityContainer.setName(pUnitName+"Container").setDefaultEntityContainer(true);
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

		Set<javax.persistence.metamodel.EntityType<?>> jpaEntityTypes = metaModel
				.getEntities();
		List<EntityType> entityTypes = new ArrayList<EntityType>();
		for (javax.persistence.metamodel.EntityType<?> jpaEntityType : jpaEntityTypes) {
			entityTypes.add(getEntityType(new FullQualifiedName(pUnitName,jpaEntityType.getName())));
		}

		return entityTypes;

	}

	@Override
	public EntityType getEntityType(FullQualifiedName fqName) {
		String entityName = fqName.getName();
		EntityType entityType = null;
		Key entityKey = new Key();
		for (javax.persistence.metamodel.EntityType<?> jpaEntityType : metaModel
				.getEntities()) {
			if ( jpaEntityType.getName().equals(entityName) )
			{
				entityType = new EntityType();
				entityType.setProperties(getEntityProperties(jpaEntityType,entityKey));
				entityType.setKey(entityKey);
				entityType.setName(entityName);
			}
		}

		return entityType;
	}
	@Override
	public List<ComplexType> getComplexTypes()
	{
		Set<javax.persistence.metamodel.EmbeddableType<?>> jpaComplexTypes = metaModel.getEmbeddables();
		List<ComplexType> complexTypes = new ArrayList<ComplexType>();
		for (javax.persistence.metamodel.EmbeddableType<?> jpaComplexType : jpaComplexTypes) {
			complexTypes.add(getComplexType(new FullQualifiedName(pUnitName,jpaComplexType.getJavaType().getName())));
		}

		return complexTypes;
	}


@Override
	public ComplexType getComplexType(FullQualifiedName fullQualifiedName) {
		String complexTypeName = fullQualifiedName.getName();
		List<Property> properties = new ArrayList<Property>();
		for (javax.persistence.metamodel.EmbeddableType<?> jpaEntityType : metaModel.getEmbeddables()) {
			if ( jpaEntityType.getJavaType().getName().equals(complexTypeName) )
			{
				 for (Attribute<?, ?> attribute : jpaEntityType.getAttributes()) {
					 if(attribute.isCollection())
					 {}
					 else if(PersistentAttributeType.EMBEDDED.toString().equals(attribute.getPersistentAttributeType().toString()))
					 {
						/*To be uncommented later
						properties.add(createComplexProperty(attribute));*/
					 }
					 else if(PersistentAttributeType.BASIC.toString().equals(attribute.getPersistentAttributeType().toString()))
					 {
						 properties.add(createSimpleProperty(attribute));
					 }
				 }
				 return new ComplexType().setName(fullQualifiedName.getName()).setProperties(properties);
			}
		}
		return null;
	}

	/*private Key getKey(javax.persistence.metamodel.EntityType<?> jpaEntityType) {
		if(jpaEntityType.hasSingleIdAttribute())
		{
			Class<?> type = jpaEntityType.getIdType().getJavaType();
			type.getDeclaringClass();
			
			SingularAttribute<?,?> idAttribute = jpaEntityType.getId(JPAIdTypeGenerator.getJPAIdClass(jpaEntityType.getIdType().getJavaType()));
			//SingularAttribute<?,?> idAttribute = jpaEntityType.getId(java.lang.Long.class);
			System.out.println(idAttribute.getName());
			
		}
			
		List<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
		keyProperties.add(new PropertyRef().setName(KEY_NAME));
		return new Key().setKeys(keyProperties);
	}
*/
	private List<Property> getEntityProperties(javax.persistence.metamodel.EntityType<?> jpaEntityType,Key entityKey) {

		List<Property> properties = new ArrayList<Property>();
		for(Attribute<?, ?> jpaAttribute : jpaEntityType.getAttributes()){
			if ( PersistentAttributeType.EMBEDDED.toString().equals(jpaAttribute.getPersistentAttributeType().toString()) )
			{
				if(jpaEntityType.getIdType().getJavaType().equals(jpaAttribute.getJavaType()))
				{
					formKey(entityKey,jpaAttribute);
				}
				//To be implemented later
				properties.add(createComplexProperty(jpaAttribute));
			}
			else if(PersistentAttributeType.BASIC.toString().equals(jpaAttribute.getPersistentAttributeType().toString()))
			{
				properties.add(createSimpleProperty(jpaAttribute));
				SingularAttribute<?,?> attribute = (SingularAttribute<?, ?>) jpaAttribute;
				if(attribute.isId())
				{
					formKey(entityKey,jpaAttribute);
				}
			}
		}
		return properties;
	}

	void formKey(Key entityKey, Attribute<?, ?> jpaAttribute) {
		if ( PersistentAttributeType.EMBEDDED.toString().equals(jpaAttribute.getPersistentAttributeType().toString()) )
		{
			
			//To be implementated
		}
		else
		{
			List<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
			keyProperties.add(new PropertyRef().setName(jpaAttribute.getName()));
			entityKey.setKeys(keyProperties);
		}
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
		ComplexProperty complexProperty = new ComplexProperty();
		complexProperty.setName(jpaAttribute.getName());
		complexProperty.setType(new FullQualifiedName(pUnitName, jpaAttribute.getName()));
		return complexProperty;
	}
	public List<Association> getAssociations()
	{
		for(javax.persistence.metamodel.EntityType<?> jpaEntityType : metaModel.getEntities())
		{
			for(javax.persistence.metamodel.Attribute<?,?> attribute:jpaEntityType.getAttributes())
			{
	//			attribute.getJavaType().get
			}
		}
		return null;
	}

}
