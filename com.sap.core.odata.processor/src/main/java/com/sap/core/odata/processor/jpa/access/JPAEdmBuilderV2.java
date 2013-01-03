package com.sap.core.odata.processor.jpa.access;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;

import com.sap.core.odata.api.edm.EdmFacets;
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
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

public class JPAEdmBuilderV2 implements JPAEdmBuilder {

	private String pUnitName;
	private EntityManagerFactory emf = null;
	private Metamodel metaModel = null;
	private List<ComplexType> complexTypes;
	public JPAEdmBuilderV2(String pUnitName, EntityManagerFactory emf) {
		this.pUnitName = pUnitName;
		this.emf = emf;
		metaModel = this.emf.getMetamodel();
		complexTypes = new ArrayList<ComplexType>();
	}

	@Override
	public List<Schema> getSchemas() throws ODataJPAModelException {

		// Create a Schema with Namespace.
		// The Namespace equals JPA Persistence Unit
		List<Schema> schemas = new ArrayList<Schema>();
		Schema schema = new Schema();
		schema.setNamespace(pUnitName);

		// Set Entity Types
		schema.setEntityTypes(getEntityTypes());

		
		/*// Set Complex Types
		if(!complexTypes.isEmpty())
		{
			schema.setComplexTypes(complexTypes);
		}*/

		// Set Entity Container
		schema.setEntityContainers(getEntityContainers());
		schemas.add(schema);
		return schemas;
	}

	@Override
	public List<EntityType> getEntityTypes() throws ODataJPAModelException {

		Set<javax.persistence.metamodel.EntityType<?>> jpaEntityTypes = metaModel
				.getEntities();
		List<EntityType> entityTypes = new ArrayList<EntityType>();
		for (javax.persistence.metamodel.EntityType<?> jpaEntityType : jpaEntityTypes) {
			entityTypes.add(getEntityType(new FullQualifiedName(pUnitName,
					jpaEntityType.getName())));
		}

		return entityTypes;

	}

	@Override
	public EntityType getEntityType(FullQualifiedName fqName)
			throws ODataJPAModelException {
		String entityName = fqName.getName();
		EntityType entityType = null;
		Key entityKey = new Key();
		for (javax.persistence.metamodel.EntityType<?> jpaEntityType : metaModel
				.getEntities()) {
			if (jpaEntityType.getName().equals(entityName)) {
				entityType = new EntityType();
				entityType.setProperties(getEntityProperties(jpaEntityType,
						entityKey));
				entityType.setKey(entityKey);
				entityType.setName(entityName);
			}
		}

		return entityType;
	}

	
	private ComplexType getComplexType(Attribute<?, ?> attribute)
			throws ODataJPAModelException {
		List<Property> properties = new ArrayList<Property>();
		String complexTypeName = pUnitName+"."+attribute.getName();
		EmbeddableType<?> embdType = metaModel.embeddable(attribute.getJavaType());
				for (Attribute<?, ?> attr : embdType.getAttributes()) {
					if (attr.isCollection()) {
					} else if (PersistentAttributeType.EMBEDDED.toString()
							.equals(attr.getPersistentAttributeType()
									.toString())) {
						properties.add(createComplexProperty(attr));
					} else if (PersistentAttributeType.BASIC.toString().equals(
							attr.getPersistentAttributeType().toString())) {
						properties.add(createSimpleProperty(attr));
					}
				}
				return new ComplexType().setName(complexTypeName)
						.setProperties(properties);
		
		
	}
	
	@Override
	public EntitySet getEntitySet(FullQualifiedName fqName) throws ODataJPAModelException {
		EntitySet entitySet = new EntitySet();
		entitySet.setName(fqName.getName() + "s");
		entitySet.setEntityType(fqName);
		return entitySet;
	}
	
	private List<EntityContainer> getEntityContainers() throws ODataJPAModelException {
		List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
		EntityContainer entityContainer = new EntityContainer();
		entityContainer.setName(pUnitName + "Container")
				.setDefaultEntityContainer(true);
		entityContainer.setEntitySets(getEntitySets());
		entityContainers.add(entityContainer);
		return entityContainers;
	}

	private List<EntitySet> getEntitySets() throws ODataJPAModelException {
		List<EntitySet> entitySets = new ArrayList<EntitySet>();
		Set<javax.persistence.metamodel.EntityType<?>> jpaEntityTypes = metaModel
				.getEntities();
		for (javax.persistence.metamodel.EntityType<?> jpaEntityType : jpaEntityTypes) {
			entitySets.add(getEntitySet(new FullQualifiedName(pUnitName,
					jpaEntityType.getName())));
		}
		return entitySets;
	}

	

	/*
	 * private Key getKey(javax.persistence.metamodel.EntityType<?>
	 * jpaEntityType) { if(jpaEntityType.hasSingleIdAttribute()) { Class<?> type
	 * = jpaEntityType.getIdType().getJavaType(); type.getDeclaringClass();
	 * 
	 * SingularAttribute<?,?> idAttribute =
	 * jpaEntityType.getId(JPAIdTypeGenerator
	 * .getJPAIdClass(jpaEntityType.getIdType().getJavaType()));
	 * //SingularAttribute<?,?> idAttribute =
	 * jpaEntityType.getId(java.lang.Long.class);
	 * System.out.println(idAttribute.getName());
	 * 
	 * }
	 * 
	 * List<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
	 * keyProperties.add(new PropertyRef().setName(KEY_NAME)); return new
	 * Key().setKeys(keyProperties); }
	 */
	private List<Property> getEntityProperties(
			javax.persistence.metamodel.EntityType<?> jpaEntityType,
			Key entityKey) throws ODataJPAModelException {

		List<Property> properties = new ArrayList<Property>();
		SimpleProperty simpleProperty;
		for (Attribute<?, ?> jpaAttribute : jpaEntityType.getAttributes()) {
			if (PersistentAttributeType.EMBEDDED.toString().equals(
					jpaAttribute.getPersistentAttributeType().toString())) {
				if (jpaEntityType.getIdType().getJavaType()
						.equals(jpaAttribute.getJavaType())) {
					formKey(entityKey, jpaAttribute);
				}
				/*// To be implemented later
				complexTypes.add(getComplexType(jpaAttribute));
				//properties.add(createComplexProperty(jpaAttribute));
*/			} else if (PersistentAttributeType.BASIC.toString().equals(
					jpaAttribute.getPersistentAttributeType().toString())) {
				simpleProperty = createSimpleProperty(jpaAttribute);
				SingularAttribute<?, ?> attribute = (SingularAttribute<?, ?>) jpaAttribute;
				if (attribute.isId()) {
					Facets facet = (Facets) simpleProperty.getFacets();
					facet.setNullable(false);
					simpleProperty.setFacets(facet);
					formKey(entityKey, jpaAttribute);
					
				}
				properties.add(simpleProperty);
			}
		}
		return properties;
	}

	private void formKey(Key entityKey, Attribute<?, ?> jpaAttribute) {
		if (PersistentAttributeType.EMBEDDED.toString().equals(
				jpaAttribute.getPersistentAttributeType().toString())) {

			// TODO be implementated
		} else {
			List<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
			keyProperties
					.add(new PropertyRef().setName(jpaAttribute.getName()));
			entityKey.setKeys(keyProperties);
		}
	}

	private SimpleProperty createSimpleProperty(Attribute<?, ?> jpaAttribute)
			throws ODataJPAModelException {
		SimpleProperty simpleProperty = new SimpleProperty();
		// Property Name
		simpleProperty.setName(jpaAttribute.getName());

		// Edm Type
		EdmSimpleTypeKind simpleTypeKind = JPATypeConvertor
				.convertToEdmSimpleType(jpaAttribute.getJavaType());
		simpleProperty.setType(simpleTypeKind);

		// Facets
		
		simpleProperty.setFacets(setFacets(jpaAttribute));

		return simpleProperty;
	}

	private EdmFacets setFacets(Attribute<?, ?> jpaAttribute) throws ODataJPAModelException {
		Facets facets = new Facets();
		if(jpaAttribute.getJavaMember() instanceof AnnotatedElement)
		{
			Column column = ((AnnotatedElement) jpaAttribute.getJavaMember()).getAnnotation(Column.class);
			if(column != null)
			{
				EdmSimpleTypeKind attrEmdType = JPATypeConvertor.convertToEdmSimpleType(jpaAttribute.getJavaType());
				if(column.nullable())
				{
					facets.setNullable(true);
				}
				if(column.length()!=0 && attrEmdType.equals(EdmSimpleTypeKind.String))
				{
					facets.setMaxLength(column.length());
				}
				if(column.precision()!=0 && attrEmdType.equals(EdmSimpleTypeKind.Double))
				{
					facets.setPrecision(column.precision());
				}
			}
			return facets;
		}
		return facets;
	}

	private ComplexProperty createComplexProperty(Attribute<?, ?> jpaAttribute) {
		ComplexProperty complexProperty = new ComplexProperty();
		complexProperty.setName(jpaAttribute.getName());
		complexProperty.setType(new FullQualifiedName(pUnitName, jpaAttribute
				.getName()));
		return complexProperty;
	}

	public List<Association> getAssociations() {
		for (javax.persistence.metamodel.EntityType<?> jpaEntityType : metaModel
				.getEntities()) {
			for (javax.persistence.metamodel.Attribute<?, ?> attribute : jpaEntityType
					.getAttributes()) {
				// attribute.getJavaType().get
			}
		}
		return null;
	}

}
