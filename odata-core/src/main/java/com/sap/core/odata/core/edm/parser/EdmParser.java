package com.sap.core.odata.core.edm.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmAction;
import com.sap.core.odata.api.edm.EdmConcurrencyMode;
import com.sap.core.odata.api.edm.EdmContentKind;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.AnnotationAttribute;
import com.sap.core.odata.api.edm.provider.AnnotationElement;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;
import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.CustomizableFeedMappings;
import com.sap.core.odata.api.edm.provider.DataServices;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Facets;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.edm.provider.FunctionImportParameter;
import com.sap.core.odata.api.edm.provider.Key;
import com.sap.core.odata.api.edm.provider.NavigationProperty;
import com.sap.core.odata.api.edm.provider.OnDelete;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.PropertyRef;
import com.sap.core.odata.api.edm.provider.ReferentialConstraint;
import com.sap.core.odata.api.edm.provider.ReferentialConstraintRole;
import com.sap.core.odata.api.edm.provider.ReturnType;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.api.ep.EntityProviderException;

public class EdmParser {

  private Map<String, String> aliasNamespaceMap = new HashMap<String, String>();
  private Map<String, String> namespaceMap;
  private Map<String, String> mandatoryNamespaces;
  private Map<FullQualifiedName, EntityType> entityTypesMap = new HashMap<FullQualifiedName, EntityType>();
  private Map<FullQualifiedName, Association> associationsMap = new HashMap<FullQualifiedName, Association>();
  private Map<FullQualifiedName, EntityContainer> containerMap = new HashMap<FullQualifiedName, EntityContainer>();
  private List<NavigationProperty> navProperties = new ArrayList<NavigationProperty>();
  private String currentHandledStartTagName;
  private String currentNamespace;
  private final String DEFAULT_NAMESPACE = "";

  public DataServices readMetadata(final XMLStreamReader reader, final boolean validate)
      throws EntityProviderException {
    try {
      initialize();
      DataServices dataServices = new DataServices();
      List<Schema> schemas = new ArrayList<Schema>();

      while (reader.hasNext()
          && !(reader.isEndElement() && Edm.NAMESPACE_EDMX_2007_06.equals(reader.getNamespaceURI()) && EdmParserConstants.EDM_DATA_SERVICES.equals(reader.getLocalName()))) {
        reader.next();
        if (reader.isStartElement()) {
          extractNamespaces(reader);
          if (EdmParserConstants.EDM_SCHEMA.equals(reader.getLocalName())) {
            schemas.add(readSchema(reader));
          } else if (EdmParserConstants.EDM_DATA_SERVICES.equals(reader
              .getLocalName())) {
            dataServices.setDataServiceVersion(reader.getAttributeValue(Edm.NAMESPACE_M_2007_08, "DataServiceVersion"));
          }
        }
      }
      if (validate) {
        validate();
      }
      dataServices.setSchemas(schemas);
      reader.close();
      return dataServices;
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }

  }

  private Schema readSchema(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_SCHEMA);

    Schema schema = new Schema();
    List<ComplexType> complexTypes = new ArrayList<ComplexType>();
    List<EntityType> entityTypes = new ArrayList<EntityType>();
    List<Association> associations = new ArrayList<Association>();
    List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();

    schema.setNamespace(reader.getAttributeValue(null, EdmParserConstants.EDM_SCHEMA_NAMESPACE));
    schema.setAlias(reader.getAttributeValue(null, EdmParserConstants.EDM_SCHEMA_ALIAS));
    schema.setAnnotationAttributes(readAnnotationAttribute(reader));
    currentNamespace = schema.getNamespace();
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_EDM_2008_09.equals(reader.getNamespaceURI()) && EdmParserConstants.EDM_SCHEMA.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        extractNamespaces(reader);
        currentHandledStartTagName = reader.getLocalName();
        if (EdmParserConstants.EDM_ENTITY_TYPE.equals(currentHandledStartTagName)) {
          entityTypes.add(readEntityType(reader));
        } else if (EdmParserConstants.EDM_COMPLEX_TYPE.equals(currentHandledStartTagName)) {
          complexTypes.add(readComplexType(reader));
        } else if (EdmParserConstants.EDM_ASSOCIATION.equals(currentHandledStartTagName)) {
          associations.add(readAssociation(reader));
        } else if (EdmParserConstants.EDM_ENTITY_CONTAINER.equals(currentHandledStartTagName)) {
          entityContainers.add(readEntityContainer(reader));
        } else {
          annotationElements.add(readAnnotationElement(reader));
        }
      }
    }
    if (schema.getAlias() != null) {
      aliasNamespaceMap.put(schema.getAlias(), schema.getNamespace());
    }
    schema.setEntityTypes(entityTypes).setComplexTypes(complexTypes).setAssociations(associations).setEntityContainers(entityContainers).setAnnotationElements(annotationElements);
    return schema;
  }

  private EntityContainer readEntityContainer(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_ENTITY_CONTAINER);
    EntityContainer container = new EntityContainer();
    List<EntitySet> entitySets = new ArrayList<EntitySet>();
    List<AssociationSet> associationSets = new ArrayList<AssociationSet>();
    List<FunctionImport> functionImports = new ArrayList<FunctionImport>();
    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();

    container.setName(reader.getAttributeValue(null, EdmParserConstants.EDM_NAME));
    if (reader.getAttributeValue(Edm.NAMESPACE_M_2007_08, "IsDefaultEntityContainer") != null) {
      container.setDefaultEntityContainer("true".equalsIgnoreCase(reader.getAttributeValue(Edm.NAMESPACE_M_2007_08, "IsDefaultEntityContainer")));
    }
    container.setExtendz(reader.getAttributeValue(null, EdmParserConstants.EDM_CONTAINER_EXTENDZ));
    container.setAnnotationAttributes(readAnnotationAttribute(reader));

    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_EDM_2008_09.equals(reader.getNamespaceURI()) && EdmParserConstants.EDM_ENTITY_CONTAINER.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        extractNamespaces(reader);
        currentHandledStartTagName = reader.getLocalName();
        if (EdmParserConstants.EDM_ENTITY_SET.equals(currentHandledStartTagName)) {
          entitySets.add(readEntitySet(reader));
        } else if (EdmParserConstants.EDM_ASSOCIATION_SET.equals(currentHandledStartTagName)) {
          associationSets.add(readAssociationSet(reader));
        } else if (EdmParserConstants.EDM_FUNCTION_IMPORT.equals(currentHandledStartTagName)) {
          functionImports.add(readFunctionImport(reader));
        } else {
          annotationElements.add(readAnnotationElement(reader));
        }
      }
    }
    container.setEntitySets(entitySets).setAssociationSets(associationSets).setFunctionImports(functionImports).setAnnotationElements(annotationElements);

    containerMap.put(new FullQualifiedName(currentNamespace, container.getName()), container);
    return container;
  }

  private FunctionImport readFunctionImport(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_FUNCTION_IMPORT);
    FunctionImport function = new FunctionImport();
    List<FunctionImportParameter> functionParameters = new ArrayList<FunctionImportParameter>();
    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();

    function.setName(reader.getAttributeValue(null, EdmParserConstants.EDM_NAME));
    function.setHttpMethod(reader.getAttributeValue(Edm.NAMESPACE_M_2007_08, EdmParserConstants.EDM_FUNCTION_IMPORT_HTTP_METHOD));
    function.setEntitySet(reader.getAttributeValue(null, EdmParserConstants.EDM_ENTITY_SET));
    ReturnType returnType = new ReturnType();
    String returnTypeString = reader.getAttributeValue(null, EdmParserConstants.EDM_FUNCTION_IMPORT_RETURN);
    if (returnTypeString != null) {
      if (returnTypeString.startsWith("Collection") || returnTypeString.startsWith("collection")) {
        returnType.setMultiplicity(EdmMultiplicity.MANY);
        returnTypeString = returnTypeString.substring(returnTypeString.indexOf("(") + 1, returnTypeString.length() - 1);
        if (function.getEntitySet() == null) {
          //				throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE.addContent("EntitySet");
        }
      }
      FullQualifiedName fqName = extractFQName(returnTypeString);
      returnType.setTypeName(fqName);
      function.setReturnType(returnType);
    }
    function.setAnnotationAttributes(readAnnotationAttribute(reader));
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_EDM_2008_09.equals(reader.getNamespaceURI()) && EdmParserConstants.EDM_FUNCTION_IMPORT.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        extractNamespaces(reader);
        currentHandledStartTagName = reader.getLocalName();
        if (EdmParserConstants.EDM_FUNCTION_PARAMETER.equals(currentHandledStartTagName)) {
          functionParameters.add(readFunctionImportParameter(reader));
        } else {
          annotationElements.add(readAnnotationElement(reader));
        }
      }
    }
    function.setParameters(functionParameters).setAnnotationElements(annotationElements);
    return function;
  }

  private FunctionImportParameter readFunctionImportParameter(final XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_FUNCTION_PARAMETER);
    FunctionImportParameter functionParameter = new FunctionImportParameter();
    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();

    functionParameter.setName(reader.getAttributeValue(null, EdmParserConstants.EDM_NAME));
    String type = reader.getAttributeValue(null, EdmParserConstants.EDM_TYPE);
    if (type == null) {
      throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE
          .addContent(EdmParserConstants.EDM_TYPE).addContent(EdmParserConstants.EDM_FUNCTION_PARAMETER));
    }
    functionParameter.setType(EdmSimpleTypeKind.valueOf(extractFQName(type).getName()));
    functionParameter.setFacets(readFacets(reader));
    functionParameter.setAnnotationAttributes(readAnnotationAttribute(reader));
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_EDM_2008_09.equals(reader.getNamespaceURI()) && EdmParserConstants.EDM_FUNCTION_IMPORT.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        extractNamespaces(reader);
        annotationElements.add(readAnnotationElement(reader));
      }
    }
    functionParameter.setAnnotationElements(annotationElements);
    return functionParameter;
  }

  private AssociationSet readAssociationSet(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_ASSOCIATION_SET);
    AssociationSet associationSet = new AssociationSet();
    List<AssociationSetEnd> ends = new ArrayList<AssociationSetEnd>();
    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();

    associationSet.setName(reader.getAttributeValue(null, EdmParserConstants.EDM_NAME));
    String association = reader.getAttributeValue(null, EdmParserConstants.EDM_ASSOCIATION);
    if (association != null) {
      associationSet.setAssociation(extractFQName(association));
    } else {
      throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE
          .addContent(EdmParserConstants.EDM_ASSOCIATION).addContent(EdmParserConstants.EDM_ASSOCIATION_SET));
    }
    associationSet.setAnnotationAttributes(readAnnotationAttribute(reader));
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_EDM_2008_09.equals(reader.getNamespaceURI()) && EdmParserConstants.EDM_ASSOCIATION_SET.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        extractNamespaces(reader);
        currentHandledStartTagName = reader.getLocalName();
        if (EdmParserConstants.EDM_ASSOCIATION_END.equals(currentHandledStartTagName)) {
          AssociationSetEnd associationSetEnd = new AssociationSetEnd();
          associationSetEnd.setEntitySet(reader.getAttributeValue(null, EdmParserConstants.EDM_ENTITY_SET));
          associationSetEnd.setRole(reader.getAttributeValue(null, EdmParserConstants.EDM_ROLE));
          ends.add(associationSetEnd);
        } else {
          annotationElements.add(readAnnotationElement(reader));
        }
      }
    }
    if (ends.size() != 2) {
      throw new EntityProviderException(EntityProviderException.ILLEGAL_ARGUMENT.addContent("Count of AssociationSet ends should be 2"));
    } else {
      associationSet.setEnd1(ends.get(0)).setEnd2(ends.get(1));
    }
    associationSet.setAnnotationElements(annotationElements);
    return associationSet;
  }

  private EntitySet readEntitySet(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_ENTITY_SET);
    EntitySet entitySet = new EntitySet();
    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();
    entitySet.setName(reader.getAttributeValue(null, EdmParserConstants.EDM_NAME));
    String entityType = reader.getAttributeValue(null, EdmParserConstants.EDM_ENTITY_TYPE);
    if (entityType != null) {
      FullQualifiedName fqName = extractFQName(entityType);
      entitySet.setEntityType(fqName);
    } else {
      throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE
          .addContent(EdmParserConstants.EDM_ENTITY_TYPE).addContent(EdmParserConstants.EDM_ENTITY_SET));
    }
    entitySet.setAnnotationAttributes(readAnnotationAttribute(reader));
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_EDM_2008_09.equals(reader.getNamespaceURI()) && EdmParserConstants.EDM_ENTITY_SET.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        extractNamespaces(reader);
        annotationElements.add(readAnnotationElement(reader));
      }
    }
    entitySet.setAnnotationElements(annotationElements);
    return entitySet;
  }

  private Association readAssociation(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_ASSOCIATION);

    Association association = new Association();
    association.setName(reader.getAttributeValue(null, EdmParserConstants.EDM_NAME));
    List<AssociationEnd> associationEnds = new ArrayList<AssociationEnd>();
    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();
    association.setAnnotationAttributes(readAnnotationAttribute(reader));
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_EDM_2008_09.equals(reader.getNamespaceURI()) && EdmParserConstants.EDM_ASSOCIATION.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        extractNamespaces(reader);
        currentHandledStartTagName = reader.getLocalName();
        if (EdmParserConstants.EDM_ASSOCIATION_END.equals(currentHandledStartTagName)) {
          associationEnds.add(readAssociationEnd(reader));
        } else if (EdmParserConstants.EDM_ASSOCIATION_CONSTRAINT.equals(currentHandledStartTagName)) {
          association.setReferentialConstraint(readReferentialConstraint(reader));
        } else {
          annotationElements.add(readAnnotationElement(reader));
        }
      }
    }
    if (associationEnds.size() < 2 && associationEnds.size() > 2) {
      throw new EntityProviderException(EntityProviderException.ILLEGAL_ARGUMENT.addContent("Count of association ends should be 2"));
    }

    association.setEnd1(associationEnds.get(0)).setEnd2(associationEnds.get(1)).setAnnotationElements(annotationElements);
    associationsMap.put(new FullQualifiedName(currentNamespace, association.getName()), association);
    return association;
  }

  private ReferentialConstraint readReferentialConstraint(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_ASSOCIATION_CONSTRAINT);
    ReferentialConstraint refConstraint = new ReferentialConstraint();
    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();
    refConstraint.setAnnotationAttributes(readAnnotationAttribute(reader));
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_EDM_2008_09.equals(reader.getNamespaceURI()) && EdmParserConstants.EDM_ASSOCIATION_CONSTRAINT.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        extractNamespaces(reader);
        currentHandledStartTagName = reader.getLocalName();
        if (EdmParserConstants.EDM_ASSOCIATION_PRINCIPAL.equals(currentHandledStartTagName)) {
          reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_ASSOCIATION_PRINCIPAL);
          refConstraint.setPrincipal(readReferentialConstraintRole(reader));
        } else if (EdmParserConstants.EDM_ASSOCIATION_DEPENDENT.equals(currentHandledStartTagName)) {
          reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_ASSOCIATION_DEPENDENT);
          refConstraint.setDependent(readReferentialConstraintRole(reader));
        } else {
          annotationElements.add(readAnnotationElement(reader));
        }
      }
    }
    refConstraint.setAnnotationElements(annotationElements);
    return refConstraint;
  }

  private ReferentialConstraintRole readReferentialConstraintRole(final XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    ReferentialConstraintRole rcRole = new ReferentialConstraintRole();
    rcRole.setRole(reader.getAttributeValue(null, EdmParserConstants.EDM_ROLE));
    List<PropertyRef> propertyRefs = new ArrayList<PropertyRef>();
    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();
    rcRole.setAnnotationAttributes(readAnnotationAttribute(reader));
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_EDM_2008_09.equals(reader.getNamespaceURI())
        && (EdmParserConstants.EDM_ASSOCIATION_PRINCIPAL.equals(reader.getLocalName()) || EdmParserConstants.EDM_ASSOCIATION_DEPENDENT.equals(reader.getLocalName())))) {
      reader.next();
      if (reader.isStartElement()) {
        extractNamespaces(reader);
        currentHandledStartTagName = reader.getLocalName();
        if (EdmParserConstants.EDM_PROPERTY_REF.equals(currentHandledStartTagName)) {
          propertyRefs.add(readPropertyRef(reader));
        } else {
          annotationElements.add(readAnnotationElement(reader));
        }
      }
    }
    rcRole.setPropertyRefs(propertyRefs).setAnnotationElements(annotationElements);
    return rcRole;
  }

  private ComplexType readComplexType(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_COMPLEX_TYPE);

    ComplexType complexType = new ComplexType();
    List<Property> properties = new ArrayList<Property>();
    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();
    complexType.setName(reader.getAttributeValue(null, EdmParserConstants.EDM_NAME));
    complexType.setAnnotationAttributes(readAnnotationAttribute(reader));
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_EDM_2008_09.equals(reader.getNamespaceURI()) && EdmParserConstants.EDM_COMPLEX_TYPE.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        extractNamespaces(reader);
        currentHandledStartTagName = reader.getLocalName();
        if (EdmParserConstants.EDM_PROPERTY.equals(currentHandledStartTagName)) {
          properties.add(readProperty(reader));
        } else {
          annotationElements.add(readAnnotationElement(reader));
        }
      }
    }

    complexType.setProperties(properties).setAnnotationElements(annotationElements);
    return complexType;

  }

  private EntityType readEntityType(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_ENTITY_TYPE);
    EntityType entityType = new EntityType();
    List<Property> properties = new ArrayList<Property>();
    List<NavigationProperty> navProperties = new ArrayList<NavigationProperty>();
    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();
    Key key = null;

    entityType.setName(reader.getAttributeValue(null, EdmParserConstants.EDM_NAME));
    String hasStream = reader.getAttributeValue(Edm.NAMESPACE_M_2007_08, EdmParserConstants.M_ENTITY_TYPE_HAS_STREAM);
    if (hasStream != null) {
      entityType.setHasStream("true".equalsIgnoreCase(hasStream));
    }

    if (reader.getAttributeValue(null, EdmParserConstants.EDM_ENTITY_TYPE_ABSTRACT) != null) {
      entityType.setAbstract("true".equalsIgnoreCase(reader.getAttributeValue(null, EdmParserConstants.EDM_ENTITY_TYPE_ABSTRACT)));
    }
    String baseType = reader.getAttributeValue(null, EdmParserConstants.EDM_BASE_TYPE);
    if (baseType != null) {
      entityType.setBaseType(extractFQName(baseType));
    }
    entityType.setCustomizableFeedMappings(readCustomizableFeedMappings(reader));
    entityType.setAnnotationAttributes(readAnnotationAttribute(reader));
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_EDM_2008_09.equals(reader.getNamespaceURI()) && EdmParserConstants.EDM_ENTITY_TYPE.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        currentHandledStartTagName = reader.getLocalName();
        if (EdmParserConstants.EDM_ENTITY_TYPE_KEY.equals(currentHandledStartTagName)) {
          key = readEntityTypeKey(reader);
        } else if (EdmParserConstants.EDM_PROPERTY.equals(currentHandledStartTagName)) {
          properties.add(readProperty(reader));
        } else if (EdmParserConstants.EDM_NAVIGATION_PROPERTY.equals(currentHandledStartTagName)) {
          navProperties.add(readNavigationProperty(reader));
        } else {
          annotationElements.add(readAnnotationElement(reader));
        }
        extractNamespaces(reader);
      }
    }

    if (entityType.getName() != null) {
      FullQualifiedName fqName = new FullQualifiedName(currentNamespace, entityType.getName());
      entityTypesMap.put(fqName, entityType);
    }
    entityType.setKey(key).setProperties(properties).setNavigationProperties(navProperties).setAnnotationElements(annotationElements);
    return entityType;
  }

  private Key readEntityTypeKey(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_ENTITY_TYPE_KEY);
    List<PropertyRef> keys = new ArrayList<PropertyRef>();
    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();
    List<AnnotationAttribute> annotationAttributes = readAnnotationAttribute(reader);
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_EDM_2008_09.equals(reader.getNamespaceURI()) && EdmParserConstants.EDM_ENTITY_TYPE_KEY.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        extractNamespaces(reader);
        currentHandledStartTagName = reader.getLocalName();
        if (EdmParserConstants.EDM_PROPERTY_REF.equals(currentHandledStartTagName)) {
          reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_PROPERTY_REF);
          keys.add(readPropertyRef(reader));
        } else {
          annotationElements.add(readAnnotationElement(reader));
        }
      }
    }
    return new Key().setKeys(keys).setAnnotationElements(annotationElements).setAnnotationAttributes(annotationAttributes);
  }

  private PropertyRef readPropertyRef(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_PROPERTY_REF);
    PropertyRef propertyRef = new PropertyRef();
    propertyRef.setName(reader.getAttributeValue(null, EdmParserConstants.EDM_NAME));
    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();
    propertyRef.setAnnotationAttributes(readAnnotationAttribute(reader));
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_EDM_2008_09.equals(reader.getNamespaceURI()) && EdmParserConstants.EDM_PROPERTY_REF.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        extractNamespaces(reader);
        annotationElements.add(readAnnotationElement(reader));
      }
    }
    return propertyRef.setAnnotationElements(annotationElements);
  }

  private NavigationProperty readNavigationProperty(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_NAVIGATION_PROPERTY);

    NavigationProperty navProperty = new NavigationProperty();
    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();
    navProperty.setName(reader.getAttributeValue(null, EdmParserConstants.EDM_NAME));
    String relationship = reader.getAttributeValue(null, EdmParserConstants.EDM_NAVIGATION_RELATIONSHIP);
    if (relationship != null) {
      FullQualifiedName fqName = extractFQName(relationship);
      navProperty.setRelationship(fqName);

    } else {
      throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE
          .addContent(EdmParserConstants.EDM_NAVIGATION_RELATIONSHIP).addContent(EdmParserConstants.EDM_NAVIGATION_PROPERTY));
    }

    navProperty.setFromRole(reader.getAttributeValue(null, EdmParserConstants.EDM_NAVIGATION_FROM_ROLE));
    navProperty.setToRole(reader.getAttributeValue(null, EdmParserConstants.EDM_NAVIGATION_TO_ROLE));
    navProperty.setAnnotationAttributes(readAnnotationAttribute(reader));
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_EDM_2008_09.equals(reader.getNamespaceURI()) && EdmParserConstants.EDM_NAVIGATION_PROPERTY.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        extractNamespaces(reader);
        annotationElements.add(readAnnotationElement(reader));
      }
    }
    navProperty.setAnnotationElements(annotationElements);
    navProperties.add(navProperty);
    return navProperty;
  }

  private Property readProperty(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_PROPERTY);
    Property property;
    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();
    String type = reader.getAttributeValue(null, EdmParserConstants.EDM_TYPE);
    if (type == null) {
      throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE
          .addContent(EdmParserConstants.EDM_TYPE).addContent(EdmParserConstants.EDM_PROPERTY));
    }
    FullQualifiedName fqName = extractFQName(type);

    if ("Edm".equals(fqName.getNamespace())) {
      property = readSimpleProperty(reader, fqName);
    } else {
      property = readComplexProperty(reader, fqName);
    }
    property.setFacets(readFacets(reader));
    property.setCustomizableFeedMappings(readCustomizableFeedMappings(reader));
    property.setMimeType(reader.getAttributeValue(Edm.NAMESPACE_M_2007_08, EdmParserConstants.M_MIMETYPE));
    property.setAnnotationAttributes(readAnnotationAttribute(reader));
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_EDM_2008_09.equals(reader.getNamespaceURI()) && EdmParserConstants.EDM_PROPERTY.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        extractNamespaces(reader);
        annotationElements.add(readAnnotationElement(reader));
      }
    }
    property.setAnnotationElements(annotationElements);
    return property;
  }

  private Property readComplexProperty(final XMLStreamReader reader, final FullQualifiedName fqName) throws XMLStreamException {
    ComplexProperty property = new ComplexProperty();
    property.setName(reader.getAttributeValue(null, EdmParserConstants.EDM_NAME));
    property.setType(fqName);
    return property;
  }

  private Property readSimpleProperty(final XMLStreamReader reader, final FullQualifiedName fqName) throws XMLStreamException {
    SimpleProperty property = new SimpleProperty();
    property.setName(reader.getAttributeValue(null, EdmParserConstants.EDM_NAME));
    property.setType(EdmSimpleTypeKind.valueOf(fqName.getName()));
    return property;
  }

  private Facets readFacets(final XMLStreamReader reader) throws XMLStreamException {
    String isNullable = reader.getAttributeValue(null, EdmParserConstants.EDM_PROPERTY_NULLABLE);
    String maxLength = reader.getAttributeValue(null, EdmParserConstants.EDM_PROPERTY_MAX_LENGTH);
    String precision = reader.getAttributeValue(null, EdmParserConstants.EDM_PROPERTY_PRECISION);
    String scale = reader.getAttributeValue(null, EdmParserConstants.EDM_PROPERTY_SCALE);
    String isFixedLength = reader.getAttributeValue(null, EdmParserConstants.EDM_PROPERTY_FIXED_LENGTH);
    String isUnicode = reader.getAttributeValue(null, EdmParserConstants.EDM_PROPERTY_UNICODE);
    String concurrencyMode = reader.getAttributeValue(null, EdmParserConstants.EDM_PROPERTY_CONCURRENCY_MODE);
    String defaultValue = reader.getAttributeValue(null, EdmParserConstants.EDM_PROPERTY_DEFAULT_VALUE);
    String collation = reader.getAttributeValue(null, EdmParserConstants.EDM_PROPERTY_COLLATION);
    if (isNullable != null || maxLength != null || precision != null || scale != null || isFixedLength != null || isUnicode != null
        || concurrencyMode != null || defaultValue != null || collation != null) {
      Facets facets = new Facets();
      if (isNullable != null) {
        facets.setNullable("true".equalsIgnoreCase(isNullable));
      }
      if (maxLength != null) {
        if (EdmParserConstants.EDM_PROPERTY_MAX_LENGTH_MAX_VALUE.equals(maxLength)) {
          facets.setMaxLength(Integer.MAX_VALUE);
        } else {
          facets.setMaxLength(Integer.parseInt(maxLength));
        }
      }
      if (precision != null) {
        facets.setPrecision(Integer.parseInt(precision));
      }
      if (scale != null) {
        facets.setScale(Integer.parseInt(scale));
      }
      if (isFixedLength != null) {
        facets.setFixedLength("true".equalsIgnoreCase(isFixedLength));
      }
      if (isUnicode != null) {
        facets.setUnicode("true".equalsIgnoreCase(isUnicode));
      }
      for (int i = 0; i < EdmConcurrencyMode.values().length; i++) {
        if (EdmConcurrencyMode.values()[i].name().equalsIgnoreCase(concurrencyMode)) {
          facets.setConcurrencyMode(EdmConcurrencyMode.values()[i]);
        }
      }
      facets.setDefaultValue(defaultValue);
      facets.setCollation(collation);
      return facets;
    } else {
      return null;
    }
  }

  private CustomizableFeedMappings readCustomizableFeedMappings(final XMLStreamReader reader) {
    String targetPath = reader.getAttributeValue(Edm.NAMESPACE_M_2007_08, EdmParserConstants.M_FC_TARGET_PATH);
    String sourcePath = reader.getAttributeValue(Edm.NAMESPACE_M_2007_08, EdmParserConstants.M_FC_SOURCE_PATH);
    String nsUri = reader.getAttributeValue(Edm.NAMESPACE_M_2007_08, EdmParserConstants.M_FC_NS_URI);
    String nsPrefix = reader.getAttributeValue(Edm.NAMESPACE_M_2007_08, EdmParserConstants.M_FC_PREFIX);
    String keepInContent = reader.getAttributeValue(Edm.NAMESPACE_M_2007_08, EdmParserConstants.M_FC_KEEP_IN_CONTENT);
    String contentKind = reader.getAttributeValue(Edm.NAMESPACE_M_2007_08, EdmParserConstants.M_FC_CONTENT_KIND);

    if (targetPath != null || sourcePath != null || nsUri != null || nsPrefix != null || keepInContent != null || contentKind != null) {
      CustomizableFeedMappings feedMapping = new CustomizableFeedMappings();
      if (keepInContent != null) {
        feedMapping.setFcKeepInContent("true".equals(keepInContent));
      }
      for (int i = 0; i < EdmContentKind.values().length; i++) {
        if (EdmContentKind.values()[i].name().equalsIgnoreCase(contentKind)) {
          feedMapping.setFcContentKind(EdmContentKind.values()[i]);
        }
      }
      return feedMapping.setFcTargetPath(targetPath).setFcSourcePath(sourcePath).setFcNsUri(nsUri).setFcNsPrefix(nsPrefix);
    } else {
      return null;
    }

  }

  private AssociationEnd readAssociationEnd(final XMLStreamReader reader) throws EntityProviderException, XMLStreamException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_EDM_2008_09, EdmParserConstants.EDM_ASSOCIATION_END);

    AssociationEnd associationEnd = new AssociationEnd();
    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();
    associationEnd.setRole(reader.getAttributeValue(null, EdmParserConstants.EDM_ROLE));
    associationEnd.setMultiplicity(EdmMultiplicity.fromLiteral(reader.getAttributeValue(null, EdmParserConstants.EDM_ASSOCIATION_MULTIPLICITY)));
    String type = reader.getAttributeValue(null, EdmParserConstants.EDM_TYPE);
    if (type == null) {
      throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE
          .addContent(EdmParserConstants.EDM_TYPE).addContent(EdmParserConstants.EDM_ASSOCIATION_END));
    }
    associationEnd.setType(extractFQName(type));
    associationEnd.setAnnotationAttributes(readAnnotationAttribute(reader));
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_EDM_2008_09.equals(reader.getNamespaceURI()) && EdmParserConstants.EDM_ASSOCIATION_END.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        extractNamespaces(reader);
        currentHandledStartTagName = reader.getLocalName();
        if (EdmParserConstants.EDM_ASSOCIATION_ONDELETE.equals(currentHandledStartTagName)) {
          OnDelete onDelete = new OnDelete();
          for (int i = 0; i < EdmAction.values().length; i++) {
            if (EdmAction.values()[i].name().equalsIgnoreCase(reader.getAttributeValue(null, EdmParserConstants.EDM_ONDELETE_ACTION))) {
              onDelete.setAction(EdmAction.values()[i]);
            }
          }
          associationEnd.setOnDelete(onDelete);
        } else {
          annotationElements.add(readAnnotationElement(reader));
        }
      }
    }
    associationEnd.setAnnotationElements(annotationElements);
    return associationEnd;
  }

  private AnnotationElement readAnnotationElement(final XMLStreamReader reader) throws XMLStreamException {
    AnnotationElement aElement = new AnnotationElement();
    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();
    List<AnnotationAttribute> annotationAttributes = new ArrayList<AnnotationAttribute>();
    aElement.setName(reader.getLocalName());
    String elementNamespace = reader.getNamespaceURI();
    if (!Edm.NAMESPACE_EDM_2008_09.equals(elementNamespace)) {
      aElement.setPrefix(reader.getPrefix());
      aElement.setNamespace(elementNamespace);
    }
    for (int i = 0; i < reader.getAttributeCount(); i++) {
      AnnotationAttribute annotationAttribute = new AnnotationAttribute();
      annotationAttribute.setText(reader.getAttributeValue(i));
      annotationAttribute.setName(reader.getAttributeLocalName(i));
      annotationAttribute.setPrefix(reader.getAttributePrefix(i));
      String namespace = reader.getAttributeNamespace(i);
      if (!DEFAULT_NAMESPACE.equals(namespace)) {
        annotationAttribute.setNamespace(namespace);
      }
      annotationAttributes.add(annotationAttribute);
    }
    aElement.setAttributes(annotationAttributes);
    while (reader.hasNext() && !(reader.isEndElement() && aElement.getName() != null && aElement.getName().equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        annotationElements.add(readAnnotationElement(reader));
      } else if (reader.isCharacters()) {
        aElement.setText(reader.getText());
      }
    }
    if (!annotationElements.isEmpty()) {
      aElement.setChildElements(annotationElements);
    }
    return aElement;
  }

  private List<AnnotationAttribute> readAnnotationAttribute(final XMLStreamReader reader) {
    List<AnnotationAttribute> annotationAttributes = new ArrayList<AnnotationAttribute>();
    for (int i = 0; i < reader.getAttributeCount(); i++) {
      if (!mandatoryNamespaces.containsValue(reader.getAttributeNamespace(i)) && !DEFAULT_NAMESPACE.equals(reader.getAttributeNamespace(i))) {
        annotationAttributes.add(new AnnotationAttribute().setName(reader.getAttributeLocalName(i)).
            setPrefix(reader.getAttributePrefix(i)).setNamespace(reader.getAttributeNamespace(i)).setText(reader.getAttributeValue(i)));
      }
    }
    if (annotationAttributes.isEmpty()) {
      return null;
    }
    return annotationAttributes;
  }

  private void checkAllMandatoryNamespacesAvailable() throws EntityProviderException {
    if (!namespaceMap.containsValue(Edm.NAMESPACE_EDMX_2007_06)) {
      throw new EntityProviderException(EntityProviderException.INVALID_NAMESPACE.addContent(Edm.NAMESPACE_EDMX_2007_06));
    } else if (!namespaceMap.containsValue(Edm.NAMESPACE_M_2007_08)) {
      throw new EntityProviderException(EntityProviderException.INVALID_NAMESPACE.addContent(Edm.NAMESPACE_EDMX_2007_06));
    } else if (!namespaceMap.containsValue(Edm.NAMESPACE_EDM_2008_09)) {
      throw new EntityProviderException(EntityProviderException.INVALID_NAMESPACE.addContent(Edm.NAMESPACE_EDMX_2007_06));
    }
  }

  private void extractNamespaces(final XMLStreamReader reader) throws EntityProviderException {
    int namespaceCount = reader.getNamespaceCount();
    for (int i = 0; i < namespaceCount; i++) {
      String namespacePrefix = reader.getNamespacePrefix(i);
      String namespaceUri = reader.getNamespaceURI(i);
      if (namespacePrefix == null || DEFAULT_NAMESPACE.equals(namespacePrefix)) {
        namespacePrefix = Edm.PREFIX_EDM;
      }
      namespaceMap.put(namespacePrefix, namespaceUri);
    }
  }

  private FullQualifiedName extractFQName(final String name)
      throws EntityProviderException {
    String[] names = name.split("\\.");
    if (names.length != 2) {
      throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid type"));
    } else {
      return new FullQualifiedName(names[0], names[1]);
    }

  }

  private FullQualifiedName validateEntityTypeWithAlias(final FullQualifiedName aliasName) throws EntityProviderException {
    String namespace = aliasNamespaceMap.get(aliasName.getNamespace());
    FullQualifiedName fqName = new FullQualifiedName(namespace, aliasName.getName());
    if (!entityTypesMap.containsKey(fqName)) {
      throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid Type"));
    }
    return fqName;
  }

  private void validateEntityTypes() throws EntityProviderException {
    for (Map.Entry<FullQualifiedName, EntityType> entityTypes : entityTypesMap.entrySet()) {
      if (entityTypes.getValue() != null && entityTypes.getKey() != null) {
        EntityType entityType = entityTypes.getValue();
        if (entityType.getBaseType() != null) {
          FullQualifiedName baseTypeFQName = entityType.getBaseType();
          EntityType baseEntityType;
          if (!entityTypesMap.containsKey(baseTypeFQName)) {
            FullQualifiedName fqName = validateEntityTypeWithAlias(baseTypeFQName);
            baseEntityType = entityTypesMap.get(fqName);
          } else {
            baseEntityType = entityTypesMap.get(baseTypeFQName);
          }
          if (baseEntityType.getKey() == null) {
            throw new EntityProviderException(EntityProviderException.COMMON.addContent("Missing key for EntityType " + baseEntityType.getName()));
          }
        } else if (entityType.getKey() == null) {
          throw new EntityProviderException(EntityProviderException.COMMON.addContent("Missing key for EntityType " + entityType.getName()));
        }
      }
    }
  }

  private void validateRelationship() throws EntityProviderException {
    for (NavigationProperty navProperty : navProperties) {
      if (associationsMap.containsKey(navProperty.getRelationship())) {
        Association assoc = associationsMap.get(navProperty.getRelationship());
        if (!(assoc.getEnd1().getRole().equals(navProperty.getFromRole()) ^ assoc.getEnd1().getRole().equals(navProperty.getToRole())
        && (assoc.getEnd2().getRole().equals(navProperty.getFromRole()) ^ assoc.getEnd2().getRole().equals(navProperty.getToRole())))) {
          throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid end of association"));
        }
        if (!entityTypesMap.containsKey(assoc.getEnd1().getType())) {
          validateEntityTypeWithAlias(assoc.getEnd1().getType());
        }
        if (!entityTypesMap.containsKey(assoc.getEnd2().getType())) {
          validateEntityTypeWithAlias(assoc.getEnd2().getType());
        }
      } else {
        throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid Relationship"));
      }
    }

  }

  private void validateAssociation() throws EntityProviderException {
    for (Map.Entry<FullQualifiedName, EntityContainer> container : containerMap.entrySet()) {
      for (AssociationSet associationSet : container.getValue().getAssociationSets()) {
        FullQualifiedName association = associationSet.getAssociation();
        if (associationsMap.containsKey(association) && container.getKey().getNamespace().equals(association.getNamespace())) {
          validateAssociationEnd(associationSet.getEnd1(), associationsMap.get(association));
          validateAssociationEnd(associationSet.getEnd2(), associationsMap.get(association));
          boolean end1 = false;
          boolean end2 = false;
          for (EntitySet entitySet : container.getValue().getEntitySets()) {
            if (entitySet.getName().equals(associationSet.getEnd1().getEntitySet())) {
              end1 = true;
            }
            if (entitySet.getName().equals(associationSet.getEnd2().getEntitySet())) {
              end2 = true;
            }
          }
          if (!(end1 && end2)) {
            throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid AssociationSet"));
          }
        } else {
          throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid AssociationSet"));
        }
      }
    }

  }

  private void validateAssociationEnd(final AssociationSetEnd end, final Association association) throws EntityProviderException {
    if (!(association.getEnd1().getRole().equals(end.getRole()) ^ association.getEnd2().getRole().equals(end.getRole()))) {
      throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid Association"));
    }
  }

  private void validateEntitySet() throws EntityProviderException {
    for (Map.Entry<FullQualifiedName, EntityContainer> container : containerMap.entrySet()) {
      for (EntitySet entitySet : container.getValue().getEntitySets()) {
        FullQualifiedName entityType = entitySet.getEntityType();
        if (!(entityTypesMap.containsKey(entityType)
        && container.getKey().getNamespace().equals(entityType.getNamespace()))) {
          validateEntityTypeWithAlias(entityType);
        }
      }
    }
  }

  private void validate() throws EntityProviderException {
    checkAllMandatoryNamespacesAvailable();
    validateEntityTypes();
    validateRelationship();
    validateAssociation();
    validateEntitySet();
  }

  private void initialize() {
    namespaceMap = new HashMap<String, String>();
    mandatoryNamespaces = new HashMap<String, String>();
    mandatoryNamespaces.put(Edm.PREFIX_EDMX, Edm.NAMESPACE_EDMX_2007_06);
    mandatoryNamespaces.put(Edm.PREFIX_EDM, Edm.NAMESPACE_EDM_2008_09);
    mandatoryNamespaces.put(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08);
  }
}
