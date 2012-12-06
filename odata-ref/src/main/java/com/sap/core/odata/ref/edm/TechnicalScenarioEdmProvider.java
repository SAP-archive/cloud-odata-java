package com.sap.core.odata.ref.edm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EdmProviderDefault;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Facets;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.edm.provider.Key;
import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.PropertyRef;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * Provider for the entity data model used in the reference scenario
 * @author SAP AG
 */
public class TechnicalScenarioEdmProvider extends EdmProviderDefault {

  public static final String NAMESPACE_1 = "TecRefScenario";

  private static final FullQualifiedName ET_KEY_IS_STRING = new FullQualifiedName(NAMESPACE_1, "EtKeyTypeString");
  private static final FullQualifiedName ET_KEY_IS_INTEGER = new FullQualifiedName(NAMESPACE_1, "EtKeyTypeInteger");
  private static final FullQualifiedName ET_COMPLEX_KEY = new FullQualifiedName(NAMESPACE_1, "EtComplexKey");
  private static final FullQualifiedName ET_ALL_TYPES = new FullQualifiedName(NAMESPACE_1, "EtAllTypes");
  private static final FullQualifiedName ENTITY_TYPE_1_BASE = new FullQualifiedName(NAMESPACE_1, "Base");
  private static final FullQualifiedName ENTITY_TYPE_1_2 = new FullQualifiedName(NAMESPACE_1, "Team");
  private static final FullQualifiedName ENTITY_TYPE_1_3 = new FullQualifiedName(NAMESPACE_1, "Room");
  private static final FullQualifiedName ENTITY_TYPE_1_4 = new FullQualifiedName(NAMESPACE_1, "Manager");
  private static final FullQualifiedName ENTITY_TYPE_1_5 = new FullQualifiedName(NAMESPACE_1, "Building");

  private static final String ENTITY_CONTAINER_1 = "Container1";
  
  private static final String ES_KEY_IS_STRING = "KeyTypeString";
  private static final String ES_KEY_IS_INTEGER = "KeyTypeInteger";
  private static final String ES_COMPLEX_KEY = "ComplexKey";
  private static final String ES_ALL_TYPES = "AllTypes";

  @Override
  public Collection<Schema> getSchemas() throws ODataMessageException {
    Collection<Schema> schemas = new ArrayList<Schema>();

    Schema schema = new Schema();
    schema.setNamespace(NAMESPACE_1);

    List<EntityType> entityTypes = new ArrayList<EntityType>();
    entityTypes.add(getEntityType(ET_KEY_IS_STRING));
    entityTypes.add(getEntityType(ET_KEY_IS_INTEGER));
    entityTypes.add(getEntityType(ET_COMPLEX_KEY));
    entityTypes.add(getEntityType(ET_ALL_TYPES));
    entityTypes.add(getEntityType(ENTITY_TYPE_1_BASE));
    entityTypes.add(getEntityType(ENTITY_TYPE_1_2));
    entityTypes.add(getEntityType(ENTITY_TYPE_1_3));
    entityTypes.add(getEntityType(ENTITY_TYPE_1_4));
    entityTypes.add(getEntityType(ENTITY_TYPE_1_5));
    schema.setEntityTypes(entityTypes);

    
    List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
    EntityContainer entityContainer = new EntityContainer();
    entityContainer.setName(ENTITY_CONTAINER_1).setDefaultEntityContainer(true);

    List<EntitySet> entitySets = new ArrayList<EntitySet>();
    entitySets.add(getEntitySet(ENTITY_CONTAINER_1, ES_KEY_IS_STRING));
    entitySets.add(getEntitySet(ENTITY_CONTAINER_1, ES_KEY_IS_INTEGER));
    entitySets.add(getEntitySet(ENTITY_CONTAINER_1, ES_COMPLEX_KEY));
    entitySets.add(getEntitySet(ENTITY_CONTAINER_1, ES_ALL_TYPES));
    entityContainer.setEntitySets(entitySets);

    entityContainers.add(entityContainer);
    schema.setEntityContainers(entityContainers);

    schemas.add(schema);

    return schemas;
  }

  @Override
  public EntityType getEntityType(final FullQualifiedName edmFQName) throws ODataMessageException {
    if (NAMESPACE_1.equals(edmFQName.getNamespace()))
    {
      if (ET_KEY_IS_STRING.getName().equals(edmFQName.getName()))
      {
        Collection<Property> properties = new ArrayList<Property>();
        properties.add(new SimpleProperty().setName("KeyString")
            .setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setNullable(false)));
        return new EntityType().setName(ET_KEY_IS_STRING.getName()).setKey(createKey("KeyString"));
      }
      else if (ET_KEY_IS_INTEGER.getName().equals(edmFQName.getName()))
      {
        Collection<Property> properties = new ArrayList<Property>();
        properties.add(new SimpleProperty().setName("KeyInteger")
            .setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setNullable(false)));
        return new EntityType().setName(ET_KEY_IS_INTEGER.getName()).setKey(createKey("KeyInteger"));
      }
      else if (ET_COMPLEX_KEY.getName().equals(edmFQName.getName()))
      {
        Collection<Property> properties = new ArrayList<Property>();
        properties.add(new SimpleProperty().setName("KeyString")
            .setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setNullable(false)));
        properties.add(new SimpleProperty().setName("KeyInteger")
            .setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setNullable(false)));
        return new EntityType().setName(ET_COMPLEX_KEY.getName()).setKey(createKey("KeyInteger", "KeyString"));
      }
      else if (ET_ALL_TYPES.getName().equals(edmFQName.getName()))
      {
        Collection<Property> properties = new ArrayList<Property>();
        properties.add(new SimpleProperty().setName("Boolean").setType(EdmSimpleTypeKind.Boolean));
        properties.add(new SimpleProperty().setName("Binary").setType(EdmSimpleTypeKind.Binary));
        properties.add(new SimpleProperty().setName("Byte").setType(EdmSimpleTypeKind.Byte));
        properties.add(new SimpleProperty().setName("DateTime").setType(EdmSimpleTypeKind.DateTime));
        properties.add(new SimpleProperty().setName("DateTimeOffset").setType(EdmSimpleTypeKind.DateTimeOffset));
        properties.add(new SimpleProperty().setName("Decimal").setType(EdmSimpleTypeKind.Decimal));
        properties.add(new SimpleProperty().setName("Double").setType(EdmSimpleTypeKind.Double));
        properties.add(new SimpleProperty().setName("Guid").setType(EdmSimpleTypeKind.Guid));
        properties.add(new SimpleProperty().setName("Int16").setType(EdmSimpleTypeKind.Int16));
        properties.add(new SimpleProperty().setName("Int32").setType(EdmSimpleTypeKind.Int32));
        properties.add(new SimpleProperty().setName("Int64").setType(EdmSimpleTypeKind.Int64));
        properties.add(new SimpleProperty().setName("SByte").setType(EdmSimpleTypeKind.SByte));
        properties.add(new SimpleProperty().setName("Single").setType(EdmSimpleTypeKind.Single));
        properties.add(new SimpleProperty().setName("String").setType(EdmSimpleTypeKind.String));
        properties.add(new SimpleProperty().setName("Time").setType(EdmSimpleTypeKind.Time));

        return new EntityType().setName(ET_ALL_TYPES.getName());
      }
    }

    return null;
  }

  @Override
  public ComplexType getComplexType(final FullQualifiedName edmFQName) throws ODataMessageException {
    
    return null;
  }

  @Override
  public Association getAssociation(final FullQualifiedName edmFQName) throws ODataMessageException {
    
    
    return null;
  }

  @Override
  public EntityContainerInfo getEntityContainerInfo(final String name) throws ODataMessageException {
    if (name == null || ENTITY_CONTAINER_1.equals(name))
      return new EntityContainerInfo().setName(ENTITY_CONTAINER_1).setDefaultEntityContainer(true);

    return null;
  }

  @Override
  public EntitySet getEntitySet(final String entityContainer, final String name) throws ODataMessageException {
    if (ENTITY_CONTAINER_1.equals(entityContainer)) 
    {
      if (ES_KEY_IS_STRING.equals(name))
        return new EntitySet().setName(name).setEntityType(ET_KEY_IS_STRING);
      else if (ES_KEY_IS_INTEGER.equals(name))
        return new EntitySet().setName(name).setEntityType(ET_KEY_IS_INTEGER);
      else if (ES_COMPLEX_KEY.equals(name))
        return new EntitySet().setName(name).setEntityType(ET_COMPLEX_KEY);
      else if (ES_ALL_TYPES.equals(name))
        return new EntitySet().setName(name).setEntityType(ET_ALL_TYPES);
    }

    return null;
  }

  @Override
  public FunctionImport getFunctionImport(final String entityContainer, final String name) throws ODataMessageException {
    return null;
  }

  @Override
  public AssociationSet getAssociationSet(final String entityContainer, final FullQualifiedName association, final String sourceEntitySetName, final String sourceEntitySetRole) throws ODataMessageException {
    return null;
  }

  private Key createKey(final String... keyNames) {
    Collection<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
    for (final String keyName : keyNames)
      keyProperties.add(new PropertyRef().setName(keyName));
    return new Key().setKeys(keyProperties);
  }
}
