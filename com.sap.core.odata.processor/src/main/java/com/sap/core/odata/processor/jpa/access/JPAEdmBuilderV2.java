package com.sap.core.odata.processor.jpa.access;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.FullQualifiedName;
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
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

/**
 * This class creates parses a JPA Model using EntityManagerFactory instance and
 * creates an OData model using JPAEntityManagerFactory APIs
 * 
 * @author SAP AG
 * 
 */
public class JPAEdmBuilderV2 implements JPAEdmBuilder {

	private String pUnitName;
	private String namespace;
	private EntityManagerFactory emf = null;
	private Metamodel metaModel = null;
	private List<ComplexType> complexTypes = null;

	private static boolean flag = false;

	public JPAEdmBuilderV2(String pUnitName, EntityManagerFactory emf) {
		this.pUnitName = pUnitName;
		this.emf = emf;
		metaModel = this.emf.getMetamodel();
	}

	@Override
	/**
	 * This method returns a list of schema objects.Each schema object contains a list of entitytypes, entitycontainer, complextypes and associations
	 * 
	 * @return List of schema objects
	 * @throws ODataJPAModelException
	 */
	public List<Schema> getSchemas() throws ODataJPAModelException {

		// Create a Schema with Namespace.
		// The Namespace equals JPA Persistence Unit
		List<Schema> schemas = new ArrayList<Schema>();
		Schema schema = new Schema();
		schema.setNamespace(pUnitName);
		this.namespace = pUnitName;

		// Set Entity Types
		List<EntityType> entityTypes = getEntityTypes();
		if (entityTypes != null && entityTypes.isEmpty() == false)
			schema.setEntityTypes(entityTypes);

		// Set Complex Types
		if (this.complexTypes != null && complexTypes.isEmpty() == false)
			schema.setComplexTypes(complexTypes);

		// Set Entity Container
		List<EntityContainer> containers = getEntityContainers();
		if (containers != null && containers.isEmpty() == false)
			schema.setEntityContainers(containers);

		schemas.add(schema);

		return schemas;
	}

	private List<EntityType> getEntityTypes() throws ODataJPAModelException {

		Set<javax.persistence.metamodel.EntityType<?>> jpaEntityTypes = metaModel
				.getEntities();

		if (jpaEntityTypes == null || jpaEntityTypes.isEmpty() == true)
			return null;

		List<EntityType> entityTypes = new ArrayList<EntityType>();
		for (javax.persistence.metamodel.EntityType<?> jpaEntityType : jpaEntityTypes) {
			FullQualifiedName fqName = new FullQualifiedName(this.namespace,
					jpaEntityType.getName());
			EntityType entityType = getEntityType(fqName);
			if (entityType != null)
				entityTypes.add(entityType);
		}

		return entityTypes;

	}

	private EntityType getEntityType(FullQualifiedName fqName)
			throws ODataJPAModelException {

		String entityName = fqName.getName();
		Key entityKey = new Key();

		for (javax.persistence.metamodel.EntityType<?> jpaEntityType : metaModel
				.getEntities()) {
			if (jpaEntityType.getName().equals(entityName)) {
				List<Property> properties = getEntityProperties(jpaEntityType,
						entityKey);
				if (properties != null && properties.isEmpty() == false) {
					EntityType entityType = new EntityType();

					entityType.setProperties(properties);
					entityType.setKey(entityKey);
					entityType.setName(entityName);
					return entityType;
				}
			}
		}

		return null;
	}

	private EntitySet getEntitySet(FullQualifiedName fqName)
			throws ODataJPAModelException {
		EntitySet entitySet = new EntitySet();
		entitySet.setName(fqName.getName() + "s");
		entitySet.setEntityType(fqName);
		return entitySet;
	}

	private List<EntityContainer> getEntityContainers()
			throws ODataJPAModelException {

		List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
		EntityContainer entityContainer = new EntityContainer();
		entityContainer.setName(this.namespace + "Container")
				.setDefaultEntityContainer(true);

		List<EntitySet> entitySets = getEntitySets();
		if (entitySets != null && entitySets.isEmpty() == false)
			entityContainer.setEntitySets(entitySets);

		entityContainers.add(entityContainer);

		return entityContainers;

	}

	private List<EntitySet> getEntitySets() throws ODataJPAModelException {

		Set<javax.persistence.metamodel.EntityType<?>> jpaEntityTypes = metaModel
				.getEntities();

		if (jpaEntityTypes == null || jpaEntityTypes.isEmpty() == true)
			return null;

		List<EntitySet> entitySets = new ArrayList<EntitySet>();
		for (javax.persistence.metamodel.EntityType<?> jpaEntityType : jpaEntityTypes) {
			entitySets.add(getEntitySet(new FullQualifiedName(this.namespace,
					jpaEntityType.getName())));
		}
		return entitySets;
	}

	private ComplexType getComplexType(FullQualifiedName fullQualifiedName)
			throws ODataJPAModelException {

		flag = false;
		if (this.complexTypes == null)
			this.complexTypes = new ArrayList<ComplexType>();
		// Get from buffer
		else {
			for (ComplexType complexType : complexTypes) {
				if (complexType.getBaseType().toString()
						.equals(fullQualifiedName.toString()))
					return complexType;
			}
		}

		// Buffer
		String complexTypeName = fullQualifiedName.getName();
		List<Property> properties = new ArrayList<Property>();

		for (javax.persistence.metamodel.EmbeddableType<?> jpaEntityType : metaModel
				.getEmbeddables()) {
			if (jpaEntityType.getJavaType().getName().equals(complexTypeName)) {
				for (Attribute<?, ?> attribute : jpaEntityType.getAttributes()) {
					/*
					 * if (attribute.isCollection()) {
					 * 
					 * }
					 */if (PersistentAttributeType.EMBEDDED.toString().equals(
							attribute.getPersistentAttributeType().toString())) {
						properties.add(createComplexProperty(attribute));
					} else if (PersistentAttributeType.BASIC.toString().equals(
							attribute.getPersistentAttributeType().toString())) {
						properties.add(createSimpleProperty(attribute));
					}
				}

				ComplexType complexType = new ComplexType();
				complexType
						.setName(jpaEntityType.getJavaType().getSimpleName());
				complexType.setProperties(properties);

				if (flag == false) {
					this.complexTypes.add(complexType);
					flag = true;
				}
				return complexType;
			}
		}

		return null;
	}

	private List<Property> getEntityProperties(
			javax.persistence.metamodel.EntityType<?> jpaEntityType,
			Key entityKey) throws ODataJPAModelException {

		List<Property> properties = new ArrayList<Property>();
		ArrayList<String> keyNames = new ArrayList<String>();
		for (Attribute<?, ?> jpaAttribute : jpaEntityType.getAttributes()) {
			String attributeType = jpaAttribute.getPersistentAttributeType()
					.toString();
			// If the property is an complex type
			if (PersistentAttributeType.EMBEDDED.toString().equals(
					attributeType)) {
				// If the complex property is a key attribute
				if (jpaEntityType.getIdType().getJavaType()
						.equals(jpaAttribute.getJavaType())) {

					for (SimpleProperty property : normalizeKeyAttribute(jpaAttribute)) {
						properties.add(property);
						keyNames.add(property.getName());
					}
					

				}
				// If the complex property is a non-key attribute
				else {
					properties.add(createComplexProperty(jpaAttribute));
				}

			}
			// If the property is an simple type
			else if (PersistentAttributeType.BASIC.toString().equals(
					attributeType)) {
				SimpleProperty property = createSimpleProperty(jpaAttribute);
				properties.add(property);
				
				// If the simple property is a key attribute
				SingularAttribute<?, ?> attribute = (SingularAttribute<?, ?>) jpaAttribute;
				if (attribute.isId()) {
					
					keyNames.add(attribute.getName());
					
				}

			}
		}
		if(!keyNames.isEmpty())
		{
			formKey(entityKey,keyNames);
		}
		return properties;
	}

	private List<SimpleProperty> normalizeKeyAttribute(
			Attribute<?, ?> jpaAttribute) throws ODataJPAModelException {
		List<SimpleProperty> properties = new ArrayList<SimpleProperty>();
		for (javax.persistence.metamodel.EmbeddableType<?> jpaComplexEntityType : metaModel
				.getEmbeddables()) {
			if (jpaComplexEntityType.getJavaType().getName()
					.equals(jpaAttribute.getJavaType().getName())) {
				for (Attribute<?, ?> attribute : jpaComplexEntityType
						.getAttributes()) {
					if (attribute.getPersistentAttributeType().equals(
							PersistentAttributeType.BASIC)) {
						SimpleProperty simpleProperty = new SimpleProperty();
						simpleProperty.setName(jpaAttribute.getName() + "."
								+ attribute.getName());
						EdmSimpleTypeKind simpleTypeKind = JPATypeConvertor
								.convertToEdmSimpleType(attribute.getJavaType());
						simpleProperty.setType(simpleTypeKind);
						simpleProperty.setFacets(setFacets(attribute));
						properties.add(simpleProperty);
					} else if ((attribute.getPersistentAttributeType()
							.equals(PersistentAttributeType.EMBEDDED)))
						properties.addAll(normalizeKeyAttribute(attribute));
				}
				return properties;
			}
		}
		return null;
	}

	// Method to populate the key of an entity.This method populates the import
	// parameter entityKey.
	private void formKey(Key entityKey, ArrayList<String> keyNames)
			throws ODataJPAModelException {
		List<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
		for (String keyName : keyNames) {
			keyProperties.add(new PropertyRef().setName(keyName));
		}

		entityKey.setKeys(keyProperties);

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
		simpleProperty.setFacets(setFacets(jpaAttribute));

		return simpleProperty;
	}

	private EdmFacets setFacets(Attribute<?, ?> jpaAttribute)
			throws ODataJPAModelException {
		Facets facets = new Facets();
		if (jpaAttribute.getJavaMember() instanceof AnnotatedElement) {
			Column column = ((AnnotatedElement) jpaAttribute.getJavaMember())
					.getAnnotation(Column.class);
			if (column != null) {
				EdmSimpleTypeKind attrEmdType = JPATypeConvertor
						.convertToEdmSimpleType(jpaAttribute.getJavaType());
				if (column.nullable()) {
					facets.setNullable(true);
				}
				if (column.length() != 0
						&& attrEmdType.equals(EdmSimpleTypeKind.String)) {
					facets.setMaxLength(column.length());
				}
				if (column.precision() != 0
						&& attrEmdType.equals(EdmSimpleTypeKind.Double)) {
					facets.setPrecision(column.precision());
				}
			}
			return facets;
		}
		return facets;
	}

	private ComplexProperty createComplexProperty(Attribute<?, ?> jpaAttribute)
			throws ODataJPAModelException {

		FullQualifiedName fqName = new FullQualifiedName(this.namespace,
				jpaAttribute.getJavaType().getName());
		ComplexType complexType = getComplexType(fqName);

		if (complexType != null) {
			ComplexProperty complexProperty = new ComplexProperty();
			complexProperty.setName(jpaAttribute.getName());
			complexProperty.setType(new FullQualifiedName(namespace,
					complexType.getName()));
			return complexProperty;
		}

		return null;
	}

	/*
	 * public List<Association> getAssociations() { for
	 * (javax.persistence.metamodel.EntityType<?> jpaEntityType : metaModel
	 * .getEntities()) { for (javax.persistence.metamodel.Attribute<?, ?>
	 * attribute : jpaEntityType .getAttributes()) { //
	 * attribute.getJavaType().get } } return null; }
	 */

}
