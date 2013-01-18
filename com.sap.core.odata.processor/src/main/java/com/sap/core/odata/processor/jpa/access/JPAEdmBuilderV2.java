package com.sap.core.odata.processor.jpa.access;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EntityManagerFactory;
import javax.persistence.JoinColumn;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;
import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Facets;
import com.sap.core.odata.api.edm.provider.Key;
import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.api.edm.provider.NavigationProperty;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.PropertyRef;
import com.sap.core.odata.api.edm.provider.ReferentialConstraint;
import com.sap.core.odata.api.edm.provider.ReferentialConstraintRole;
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

	private final String FROM_NAVIGATION = "from";
	private final String TO_NAVIGATION = "to";
	private final String FROM_ASSOCIATION_ROLE = "FromRole_";
	private final String TO_ASSOCIATION_ROLE = "ToRole_";
	private final String ASSOCIATION_PREFIX = "ASSOC_";
	private final String PREFIX_NAVIGATION_NAME = "To_";
	private String pUnitName;
	private String namespace;
	private EntityManagerFactory emf = null;
	private Metamodel metaModel = null;
	private List<ComplexType> complexTypes = null;
	private List<Association> associationList = null;
	private List<AssociationSet> associationSetList = null;
	private Map<String,HashMap<String, SimpleProperty>> propertyColumnNameCache = null;

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

		// Set Association in schema
		if(this.associationList != null && !this.associationList.isEmpty())
		{
			schema.setAssociations(associationList);
		}
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
		if(propertyColumnNameCache == null)
		{
			propertyColumnNameCache = new HashMap<String, HashMap<String,SimpleProperty>>();
		}
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

	private EntityType getEntityType(FullQualifiedName fqName) throws ODataJPAModelException {
		String entityName = fqName.getName();
		Key entityKey = new Key();
		List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
		propertyColumnNameCache.put(entityName, new HashMap<String, SimpleProperty>());
		for (javax.persistence.metamodel.EntityType<?> jpaEntityType : metaModel.getEntities()) {
			if (jpaEntityType.getName().equals(entityName)) {
				List<Property> properties = getEntityProperties(jpaEntityType,
						entityKey, navigationProperties);
				EntityType entityType = new EntityType();
				if (!navigationProperties.isEmpty()) {
					entityType.setNavigationProperties(navigationProperties);
				}
				if (properties != null && properties.isEmpty() == false) {

					entityType.setProperties(properties);
					entityType.setKey(entityKey);
					entityType.setName(entityName);
					return entityType;
				}
			}
		}

		return null;
	}

	private EntitySet getEntitySet(FullQualifiedName fqName) throws ODataJPAModelException {
		EntitySet entitySet = new EntitySet();
		entitySet.setName(fqName.getName() + "s");
		entitySet.setEntityType(fqName);
		return entitySet;
	}

	private List<EntityContainer> getEntityContainers() throws ODataJPAModelException {

		List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
		EntityContainer entityContainer = new EntityContainer();
		entityContainer.setName(this.namespace + "Container")
				.setDefaultEntityContainer(true);

		List<EntitySet> entitySets = getEntitySets();
		if (entitySets != null && entitySets.isEmpty() == false)
			entityContainer.setEntitySets(entitySets);

		if(this.associationSetList != null && !this.associationSetList.isEmpty())
		{
			entityContainer.setAssociationSets(associationSetList);
		}
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

	private ComplexType getComplexType(FullQualifiedName fullQualifiedName) throws ODataJPAModelException {

		flag = false;
		if (this.complexTypes == null)
			this.complexTypes = new ArrayList<ComplexType>();
		// Get from buffer
		else {
			for (ComplexType complexType : complexTypes) {
				if (complexType.getBaseType().toString().equals(fullQualifiedName.toString()))
					return complexType;
			}
		}

		// Buffer
		String complexTypeName = fullQualifiedName.getName();
		List<Property> properties = new ArrayList<Property>();

		for (javax.persistence.metamodel.EmbeddableType<?> jpaEntityType : metaModel.getEmbeddables()) {
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

	private List<Property> getEntityProperties(javax.persistence.metamodel.EntityType<?> jpaEntityType,Key entityKey, List<NavigationProperty> navigationProperties)throws ODataJPAModelException {

		List<Property> properties = new ArrayList<Property>();
		ArrayList<String> keyNames = new ArrayList<String>();
		HashMap<String, SimpleProperty> propertyColumn = propertyColumnNameCache.get(jpaEntityType.getJavaType().getSimpleName());
		for (Attribute<?, ?> jpaAttribute : jpaEntityType.getAttributes()) {
			String attributeType = jpaAttribute.getPersistentAttributeType().toString();
			// If the property is an complex type
			if (PersistentAttributeType.EMBEDDED.toString().equals(attributeType)) {
				// If the complex property is a key attribute
				if (jpaEntityType.getIdType().getJavaType().equals(jpaAttribute.getJavaType())) {

					for (SimpleProperty property : normalizeKeyAttribute(jpaAttribute,propertyColumn)) {
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
			else if (PersistentAttributeType.BASIC.toString().equals(attributeType)) {
				SimpleProperty property = createSimpleProperty(jpaAttribute);
				properties.add(property);

				// If the simple property is a key attribute
				SingularAttribute<?, ?> attribute = (SingularAttribute<?, ?>) jpaAttribute;
				if (attribute.isId()) {
					keyNames.add(attribute.getName());

				}
				Column column = ((AnnotatedElement) attribute.getJavaMember()).getAnnotation(Column.class);
				if(column != null && column.name() != null)
				{
					propertyColumn.put(column.name(),property);
				}
				else
				{
					propertyColumn.put(attribute.getName(), property);
				}

			}
			// If the property is an association.It is assumed it is a non-key
			// attribute
			else {
				Association association;
				
				if (associationList == null) {
					associationList = new ArrayList<Association>();
				}
				if (associationSetList == null) {
					associationSetList = new ArrayList<AssociationSet>();
				}
				// Try to get the association from the buffer
				association = getAssociation(jpaAttribute,jpaEntityType);
				// If the association has already not been created
				if (association == null) {
					association = createAssociation(jpaAttribute, jpaEntityType);
				}
				// If the association has already been created the update the referential constraint
				else if (association != null) {
					updateAssociation(association, jpaAttribute);
				}
				if (navigationProperties == null) {
					navigationProperties = new ArrayList<NavigationProperty>();
				}
				NavigationProperty navigationProperty = new NavigationProperty();
				navigationProperty.setName(PREFIX_NAVIGATION_NAME.concat(jpaAttribute.getName()));
				navigationProperty.setMapping(new Mapping().setInternalName(jpaAttribute.getName()));
				navigationProperty.setFromRole(FROM_NAVIGATION.concat(jpaEntityType.getName()));
				navigationProperty.setToRole(TO_NAVIGATION.concat(jpaAttribute.getJavaType().getSimpleName()));
				if (association != null) {
					navigationProperty.setRelationship(new FullQualifiedName(pUnitName, association.getName()));
					navigationProperties.add(navigationProperty);
				}
			}
		}
		if (!keyNames.isEmpty()) {
			formKey(entityKey, keyNames);
		}
		return properties;
	}

	private void updateAssociation(Association association,Attribute<?, ?> jpaAttribute) {
		if(association.getReferentialConstraint() != null)
		{
			return;
		}
		else
		{
			JoinColumn joinColumn = ((AnnotatedElement) jpaAttribute.getJavaMember()).getAnnotation(JoinColumn.class);
			if(joinColumn != null)
			{
				ReferentialConstraint referentialConstraint = createReferencialConstraint(jpaAttribute, joinColumn, association);
				association.setReferentialConstraint(referentialConstraint);
			}
		}
			
	}

	/*private SimpleProperty createForeignKeyProperty(Attribute<?, ?> jpaAttribute) throws ODataJPAModelException {
		SimpleProperty simpleProperty = new SimpleProperty();
		JoinColumn joinColumn = ((AnnotatedElement) jpaAttribute.getJavaMember()).getAnnotation(JoinColumn.class);
		SimpleProperty referencedProperty = searchCache(jpaAttribute.getJavaType().getSimpleName(),joinColumn.referencedColumnName());
		if(referencedProperty != null)
		{
			simpleProperty.setName(joinColumn.name());
			simpleProperty.setType(referencedProperty.getType());
			simpleProperty.setFacets(referencedProperty.getFacets());
			
		}
		else
		{
			Attribute<?, ?> referencedAttribute = searchMetaModel(jpaAttribute.getJavaType(),joinColumn.referencedColumnName());
			simpleProperty.setName(joinColumn.name());
			EdmSimpleTypeKind simpleTypeKind = JPATypeConvertor.convertToEdmSimpleType(referencedAttribute.getJavaType());
			simpleProperty.setType(simpleTypeKind);
			
		}
		
		
		
		return simpleProperty;
	}
*/
	private Attribute<?, ?> searchMetaModel(Class<?> entityJavaType,String referencedColumnName) {
		for (javax.persistence.metamodel.EntityType<?> jpaEntityType : metaModel.getEntities()) {
			if(jpaEntityType.getJavaType().getName().equals(entityJavaType.getName()))
			{
				for (Attribute<?, ?> jpaAttribute : jpaEntityType.getAttributes())
				{
					if(jpaAttribute.getPersistentAttributeType().toString().equals(PersistentAttributeType.BASIC.toString()))
					{
						Column column = ((AnnotatedElement) jpaAttribute.getJavaMember()).getAnnotation(Column.class);
						if(column != null)
						{
							if(column.name().equalsIgnoreCase(referencedColumnName))
							{
								return jpaAttribute;
							}
							
						}
						
					}
					else if(jpaAttribute.getPersistentAttributeType().toString().equals(PersistentAttributeType.EMBEDDED.toString()))
					{
						for(javax.persistence.metamodel.EmbeddableType<?> jpaComplexType : metaModel.getEmbeddables())
						{
							if(jpaEntityType.getIdType().getJavaType().equals(jpaAttribute.getJavaType()))
							{
								for(Attribute<?, ?> complexKeyAttribute : jpaComplexType.getAttributes())
								{
									Column column = ((AnnotatedElement) complexKeyAttribute.getJavaMember()).getAnnotation(Column.class);
									if(column != null)
									{
										if(column.name().equalsIgnoreCase(referencedColumnName))
										{
											return complexKeyAttribute;
										}
									
									}
								}
							}
						}
					}
			}
			
		}
	}
	return null;
}

	private SimpleProperty searchCache(String entityName,String referencedColumnName) {
		if(propertyColumnNameCache.containsKey(entityName))
		{
			HashMap<String,SimpleProperty> keyProperties = propertyColumnNameCache.get(entityName);
			if(keyProperties != null && !keyProperties.isEmpty())
			{
				if(keyProperties.containsKey(referencedColumnName))
				{
					return keyProperties.get(referencedColumnName);
				}
			}
		}
		return null;
	}

	private Association createAssociation(Attribute<?, ?> jpaAttribute,javax.persistence.metamodel.EntityType<?> jpaEntityType) throws ODataJPAModelException {
		if(associationList == null)
		{
			associationList = new ArrayList<Association>();
		}
		Association association = null;
		String sourceName = jpaEntityType.getName();
		String targetName = jpaAttribute.getJavaType().getSimpleName();
		String jpaAttributeType = jpaAttribute.getPersistentAttributeType().toString();
		JoinColumn joinColumn = ((AnnotatedElement) jpaAttribute.getJavaMember()).getAnnotation(JoinColumn.class);
		if (jpaAttributeType.equals(PersistentAttributeType.ONE_TO_MANY.toString())) {
			
			association = createAssociationObject(sourceName,targetName,EdmMultiplicity.ONE,EdmMultiplicity.MANY);
			// Create association set
			createAssociationSet(association,sourceName,targetName);
		} 
		else if (jpaAttributeType.equals(PersistentAttributeType.MANY_TO_ONE.toString())) {
			 
			association = createAssociationObject(targetName, sourceName, EdmMultiplicity.ONE,EdmMultiplicity.MANY);
			createAssociationSet(association, targetName, sourceName);
			if(joinColumn != null)
			{
				if(joinColumn.referencedColumnName() == "")
				{
					throw ODataJPAModelException.throwException(ODataJPAModelException.INVALID_ASSOCIATION, null);
				}
				else
				{
					association.setReferentialConstraint(createReferencialConstraint(jpaAttribute,joinColumn,association));
				}
			}
			
			
		}
		else if (jpaAttributeType.equals(PersistentAttributeType.ONE_TO_ONE.toString())) {
			
			if(joinColumn != null)
			{
				association = createAssociationObject(targetName, sourceName, EdmMultiplicity.ONE, EdmMultiplicity.ONE);
				createAssociationSet(association, targetName, sourceName);
				if(joinColumn.referencedColumnName() == "")
				{
					throw ODataJPAModelException.throwException(ODataJPAModelException.INVALID_ASSOCIATION, null);
				}
				else
				{
					association.setReferentialConstraint(createReferencialConstraint(jpaAttribute,joinColumn,association));
				}
				
			}
			else
			{
				association = createAssociationObject(sourceName, targetName, EdmMultiplicity.ONE, EdmMultiplicity.ONE);
				createAssociationSet(association, sourceName, targetName);
			}

		} 
		else if (jpaAttributeType.equals(PersistentAttributeType.MANY_TO_MANY.toString())) {
			
			if(joinColumn != null)
			{
				association = createAssociationObject(targetName, sourceName, EdmMultiplicity.MANY, EdmMultiplicity.MANY);
				createAssociationSet(association, targetName, sourceName);
				if(joinColumn.referencedColumnName() == "")
				{
					throw ODataJPAModelException.throwException(ODataJPAModelException.INVALID_ASSOCIATION, null);
				}
				else
				{
					association.setReferentialConstraint(createReferencialConstraint(jpaAttribute,joinColumn,association));
				}
				
			}
			else
			{
				association = createAssociationObject(sourceName, targetName, EdmMultiplicity.MANY, EdmMultiplicity.MANY);
				createAssociationSet(association, sourceName, targetName);
			}

		}
		if(association != null)
		{
			associationList.add(association);
			return association;
		}
		return null;
	}

	private ReferentialConstraint createReferencialConstraint(Attribute<?, ?> jpaAttribute, JoinColumn joinColumn,Association association) {
		if(joinColumn != null)
		{
			ReferentialConstraint referencialConstraint = new ReferentialConstraint();
			List<PropertyRef> propertyRefSourceList = new ArrayList<PropertyRef>();
			List<PropertyRef> propertyRefTargetList = new ArrayList<PropertyRef>();
			PropertyRef propertyRefSource = new PropertyRef();
			PropertyRef propertyRefTarget = new PropertyRef();
			if(searchCache(jpaAttribute.getJavaType().getSimpleName(), joinColumn.referencedColumnName()) != null)
			{
				SimpleProperty property = searchCache(jpaAttribute.getJavaType().getSimpleName(), joinColumn.referencedColumnName());
				propertyRefSource.setName(property.getName());
			}
			else
			{
				Attribute<?, ?> attr = searchMetaModel(jpaAttribute.getJavaType(), joinColumn.referencedColumnName());
				propertyRefSource.setName(attr.getName());
			}
			if(searchCache(jpaAttribute.getDeclaringType().getJavaType().getSimpleName(), joinColumn.name()) != null)
			{
				SimpleProperty property = searchCache(jpaAttribute.getDeclaringType().getJavaType().getSimpleName(), joinColumn.name());
				propertyRefTarget.setName(property.getName());
			}
			else
			{
				Attribute<?, ?> attr = searchMetaModel(jpaAttribute.getDeclaringType().getJavaType(), joinColumn.name());
				propertyRefTarget.setName(attr.getName());
			}
			propertyRefSourceList.add(propertyRefSource);
			propertyRefTargetList.add(propertyRefTarget);
			referencialConstraint.setPrincipal(new ReferentialConstraintRole().setPropertyRefs(propertyRefSourceList).setRole(association.getEnd1().getRole()));
			referencialConstraint.setDependent(new ReferentialConstraintRole().setPropertyRefs(propertyRefTargetList).setRole(association.getEnd2().getRole()));
			return referencialConstraint;
		}
		return null;
	}

	private Association createAssociationObject(String sourceEntityName,String targetEntityName, EdmMultiplicity sourceCardinality, EdmMultiplicity targetCardinality) {
		Association association = new Association();
		association.setName(ASSOCIATION_PREFIX.concat(sourceEntityName.concat(targetEntityName)));
		association.setEnd1(new AssociationEnd().setType(new FullQualifiedName(pUnitName, sourceEntityName)).setRole(FROM_ASSOCIATION_ROLE.concat(sourceEntityName)).setMultiplicity(sourceCardinality))
					.setEnd2(new AssociationEnd().setType(new FullQualifiedName(pUnitName, targetEntityName)).setRole(TO_ASSOCIATION_ROLE.concat(targetEntityName)).setMultiplicity(targetCardinality));
		return association;
		
	}

	private void createAssociationSet(Association association,String sourceEntityName, String targetEntityName) {
		if(associationSetList == null)
		{
			associationSetList = new ArrayList<AssociationSet>();
		}
		AssociationSet associationSet = new AssociationSet();
		associationSet.setName(association.getName().concat("s"));
		associationSet.setAssociation(new FullQualifiedName(pUnitName, association.getName()));
		associationSet.setEnd1(new AssociationSetEnd().setRole(association.getEnd1().getRole()).setEntitySet(sourceEntityName.concat("s")));
		associationSet.setEnd2(new AssociationSetEnd().setRole(association.getEnd2().getRole()).setEntitySet(targetEntityName.concat("s")));
		associationSetList.add(associationSet);
	}

	private Association getAssociation(Attribute<?, ?> jpaAttribute,javax.persistence.metamodel.EntityType<?> jpaEntityType) {
		String sourceEntityName = jpaEntityType.getName();
		String targetEntityName = jpaAttribute.getJavaType().getSimpleName();
		// Check if the association has already been created by some other entity
		if (associationList.isEmpty() == false) {
			for (Association assc : associationList) {
				if (assc.getEnd1().getType().getName().equals(targetEntityName)
						|| assc.getEnd2().getType().getName().equals(targetEntityName)) {
					if (assc.getEnd1().getType().getName().equals(sourceEntityName)
							|| assc.getEnd2().getType().getName().equals(sourceEntityName)) {
						return assc;
					}
				}
			}
		}
		return null;
	}

	private List<SimpleProperty> normalizeKeyAttribute(Attribute<?, ?> jpaAttribute, HashMap<String, SimpleProperty> idPropertyColumn) throws ODataJPAModelException {
		List<SimpleProperty> properties = new ArrayList<SimpleProperty>();
		((AnnotatedElement)jpaAttribute.getJavaMember()).getAnnotations();
		for (javax.persistence.metamodel.EmbeddableType<?> jpaComplexEntityType : metaModel.getEmbeddables()) {
			if (jpaComplexEntityType.getJavaType().getName().equals(jpaAttribute.getJavaType().getName())) {
				for (Attribute<?, ?> attribute : jpaComplexEntityType.getDeclaredAttributes()) {
					// If the property in the embedded key class is a simple type
					if (attribute.getPersistentAttributeType().equals(PersistentAttributeType.BASIC)) {
						
						SimpleProperty simpleProperty = new SimpleProperty();
						// Set the internal name as EmbeddedClassName.memberName
						simpleProperty.setMapping(new Mapping().setInternalName(jpaAttribute.getName() + "."+ attribute.getName()));
						simpleProperty.setName(attribute.getName());
						EdmSimpleTypeKind simpleTypeKind = JPATypeConvertor.convertToEdmSimpleType(attribute.getJavaType());
						simpleProperty.setType(simpleTypeKind);
						simpleProperty.setFacets(setFacets(attribute));
						properties.add(simpleProperty);
						Column column = ((AnnotatedElement) attribute.getJavaMember()).getAnnotation(Column.class);
						if(column != null)
						{
							idPropertyColumn.put(column.name(),simpleProperty);
						}
						else
						{
							idPropertyColumn.put(attribute.getName(), simpleProperty);
						}
						
					} 
					// If the property in the embedded key class is again a complex type
					else if ((attribute.getPersistentAttributeType().equals(PersistentAttributeType.EMBEDDED)))
						properties.addAll(normalizeKeyAttribute(attribute,idPropertyColumn));
				}
				return properties;
			}
		}
		return null;
	}

	// Method to populate the key of an entity.This method populates the import parameter entityKey.
	private void formKey(Key entityKey, ArrayList<String> keyNames) throws ODataJPAModelException {
		List<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
		for (String keyName : keyNames) {
			keyProperties.add(new PropertyRef().setName(keyName));
		}

		entityKey.setKeys(keyProperties);

	}

	private SimpleProperty createSimpleProperty(Attribute<?, ?> jpaAttribute) throws ODataJPAModelException {
		SimpleProperty simpleProperty = new SimpleProperty();
		// Property Name
		simpleProperty.setName(jpaAttribute.getName());

		// Edm Type
		EdmSimpleTypeKind simpleTypeKind = JPATypeConvertor.convertToEdmSimpleType(jpaAttribute.getJavaType());
		simpleProperty.setType(simpleTypeKind);
		simpleProperty.setFacets(setFacets(jpaAttribute));

		return simpleProperty;
	}

	private EdmFacets setFacets(Attribute<?, ?> jpaAttribute) throws ODataJPAModelException {
		Facets facets = new Facets();
		if (jpaAttribute.getJavaMember() instanceof AnnotatedElement) {
			Column column = ((AnnotatedElement) jpaAttribute.getJavaMember()).getAnnotation(Column.class);
			if (column != null) {
				EdmSimpleTypeKind attrEmdType = JPATypeConvertor.convertToEdmSimpleType(jpaAttribute.getJavaType());
				if (column.nullable()) {
					facets.setNullable(true);
				}
				if (column.length() != 0 && attrEmdType.equals(EdmSimpleTypeKind.String)) {
					facets.setMaxLength(column.length());
				}
				if (column.precision() != 0 && attrEmdType.equals(EdmSimpleTypeKind.Double)) {
					facets.setPrecision(column.precision());
				}
			}
			return facets;
		}
		return facets;
	}

	private ComplexProperty createComplexProperty(Attribute<?, ?> jpaAttribute) throws ODataJPAModelException {
		
		FullQualifiedName fqName = new FullQualifiedName(this.namespace,jpaAttribute.getJavaType().getName());
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

}
