package com.sap.core.odata.testutils.mocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;
import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Facets;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.edm.provider.Key;
import com.sap.core.odata.api.edm.provider.NavigationProperty;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.PropertyRef;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * Provider for the entity data model used as technical reference scenario
 * @author SAP AG
 */
public class TechnicalScenarioEdmProvider extends EdmProvider {

  public static final String NAMESPACE_1 = "TecRefScenario";

  public static final FullQualifiedName ET_KEY_IS_STRING = new FullQualifiedName(NAMESPACE_1, "EtKeyTypeString");
  public static final FullQualifiedName ET_KEY_IS_INTEGER = new FullQualifiedName(NAMESPACE_1, "EtKeyTypeInteger");
  public static final FullQualifiedName ET_COMPLEX_KEY = new FullQualifiedName(NAMESPACE_1, "EtComplexKey");
  public static final FullQualifiedName ET_ALL_TYPES = new FullQualifiedName(NAMESPACE_1, "EtAllTypes");
  public static final FullQualifiedName ET_STRING_FACETS = new FullQualifiedName(NAMESPACE_1, "EtStringFacets");

  public static final FullQualifiedName CT_ADDRESS = new FullQualifiedName(NAMESPACE_1, "CtAdress");
  public static final FullQualifiedName CT_ALL_TYPES = new FullQualifiedName(NAMESPACE_1, "CtAllTypes");

  public static final FullQualifiedName ASSOCIATION_ET1_ET2 = new FullQualifiedName(NAMESPACE_1, "Association");
  public static final String ROLE_1 = "Role1";
  public static final String ROLE_2 = "Role2";

  public static final String ENTITY_CONTAINER_1 = "Container1";

  public static final String ES_KEY_IS_STRING = "KeyTypeString";
  public static final String ES_KEY_IS_INTEGER = "KeyTypeInteger";
  public static final String ES_COMPLEX_KEY = "ComplexKey";
  public static final String ES_ALL_TYPES = "AllTypes";
  public static final String ES_STRING_FACETS = "StringFacets";

  @Override
  public List<Schema> getSchemas() throws ODataMessageException {
    Schema schema = new Schema();
    schema.setNamespace(NAMESPACE_1);

    schema.setEntityTypes(Arrays.asList(
        getEntityType(ET_KEY_IS_STRING),
        getEntityType(ET_KEY_IS_INTEGER),
        getEntityType(ET_COMPLEX_KEY),
        getEntityType(ET_ALL_TYPES)));

    schema.setComplexTypes(Arrays.asList(getComplexType(CT_ALL_TYPES)));

    EntityContainer entityContainer = new EntityContainer();
    entityContainer.setName(ENTITY_CONTAINER_1).setDefaultEntityContainer(true);
    entityContainer.setEntitySets(Arrays.asList(
        getEntitySet(ENTITY_CONTAINER_1, ES_KEY_IS_STRING),
        getEntitySet(ENTITY_CONTAINER_1, ES_KEY_IS_INTEGER),
        getEntitySet(ENTITY_CONTAINER_1, ES_COMPLEX_KEY),
        getEntitySet(ENTITY_CONTAINER_1, ES_ALL_TYPES),
        getEntitySet(ENTITY_CONTAINER_1, ES_STRING_FACETS)));

    schema.setEntityContainers(Arrays.asList(entityContainer));

    return Arrays.asList(schema);
  }

  @Override
  public EntityType getEntityType(final FullQualifiedName edmFQName) throws ODataMessageException {
    if (NAMESPACE_1.equals(edmFQName.getNamespace()))
    {
      if (ET_KEY_IS_STRING.getName().equals(edmFQName.getName()))
      {
        List<Property> properties = new ArrayList<Property>();
        properties.add(new SimpleProperty().setName("KeyString")
            .setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setNullable(false)));

        List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();

        navigationProperties.add(new NavigationProperty().setName("navProperty").setFromRole(ROLE_1).setToRole(ROLE_2).setRelationship(ASSOCIATION_ET1_ET2));

        return new EntityType().setName(ET_KEY_IS_STRING.getName()).setProperties(properties).setNavigationProperties(navigationProperties).setKey(createKey("KeyString"));
      }
      else if (ET_KEY_IS_INTEGER.getName().equals(edmFQName.getName()))
      {
        List<Property> properties = new ArrayList<Property>();
        properties.add(new SimpleProperty().setName("KeyInteger")
            .setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setNullable(false)));

        List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
        navigationProperties.add(new NavigationProperty().setName("navProperty").setFromRole(ROLE_2).setToRole(ROLE_1).setRelationship(ASSOCIATION_ET1_ET2));

        return new EntityType().setName(ET_KEY_IS_INTEGER.getName()).setProperties(properties).setNavigationProperties(navigationProperties).setKey(createKey("KeyInteger"));

      }
      else if (ET_COMPLEX_KEY.getName().equals(edmFQName.getName()))
      {
        List<Property> properties = new ArrayList<Property>();
        properties.add(new SimpleProperty().setName("KeyString")
            .setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setNullable(false)));
        properties.add(new SimpleProperty().setName("KeyInteger")
            .setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setNullable(false)));

        return new EntityType().setName(ET_COMPLEX_KEY.getName()).setProperties(properties).setKey(createKey("KeyInteger", "KeyString"));
      }
      else if (ET_ALL_TYPES.getName().equals(edmFQName.getName()))
      {
        List<Property> properties = new ArrayList<Property>();
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
        properties.add(new ComplexProperty().setName("Complex").setType(CT_ALL_TYPES));
        return new EntityType().setName(ET_ALL_TYPES.getName()).setProperties(properties);
      }
      else if (ET_STRING_FACETS.getName().equals(edmFQName.getName()))
      {
        List<Property> properties = new ArrayList<Property>();
        //TODO: What here?
        //        properties.add(new SimpleProperty().setName("StringCollation").setType(EdmSimpleTypeKind.String)
        //            .setFacets(new Facets().setCollation(collation)));

        properties.add(new SimpleProperty().setName("StringDefaultValue").setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setDefaultValue("defaultValue")));

        properties.add(new SimpleProperty().setName("StringFixedLength").setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setFixedLength(true)));

        properties.add(new SimpleProperty().setName("StringMaxLength").setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setMaxLength(15)));

        properties.add(new SimpleProperty().setName("StringLength").setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setMaxLength(15).setFixedLength(true)));

        properties.add(new SimpleProperty().setName("StringNullable").setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setNullable(false)));

        properties.add(new SimpleProperty().setName("StringUnicode").setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setUnicode(false)));

        return new EntityType().setName(ET_STRING_FACETS.getName()).setProperties(properties);
      }
    }

    return null;
  }

  @Override
  public ComplexType getComplexType(final FullQualifiedName edmFQName) throws ODataMessageException
  {
    if (NAMESPACE_1.equals(edmFQName.getNamespace()))
      if (CT_ADDRESS.getName().equals(edmFQName.getName())) {
        List<Property> properties = new ArrayList<Property>();
        properties.add(new SimpleProperty().setName("Street").setType(EdmSimpleTypeKind.String));
        properties.add(new SimpleProperty().setName("City").setType(EdmSimpleTypeKind.String));
        return new ComplexType().setName(CT_ADDRESS.getName()).setAbstract(false).setProperties(properties);
      }
      else if (CT_ALL_TYPES.getName().equals(edmFQName.getName())) {
        List<Property> properties = new ArrayList<Property>();
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
        properties.add(new ComplexProperty().setName("Address").setType(CT_ADDRESS));
        return new ComplexType().setName(CT_ALL_TYPES.getName()).setAbstract(false).setProperties(properties);
      }

    return null;
  }

  @Override
  public Association getAssociation(final FullQualifiedName edmFQName) throws ODataMessageException {
    if (NAMESPACE_1.equals(edmFQName.getNamespace())) {
      if (ASSOCIATION_ET1_ET2.getName().equals(edmFQName.getName())) {
        AssociationEnd end1 = new AssociationEnd().setMultiplicity(EdmMultiplicity.ONE).setRole(ROLE_1).setType(ET_KEY_IS_STRING);
        AssociationEnd end2 = new AssociationEnd().setMultiplicity(EdmMultiplicity.ONE).setRole(ROLE_2).setType(ET_KEY_IS_INTEGER);
        return new Association().setName("Association").setEnd1(end1).setEnd2(end2);
      }
    }

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
      else if (ES_STRING_FACETS.equals(name))
        return new EntitySet().setName(name).setEntityType(ET_STRING_FACETS);
    }

    return null;
  }

  @Override
  public FunctionImport getFunctionImport(final String entityContainer, final String name) throws ODataMessageException {
    return null;
  }

  @Override
  public AssociationSet getAssociationSet(final String entityContainer, final FullQualifiedName association, final String sourceEntitySetName, final String sourceEntitySetRole) throws ODataMessageException {
    if (ENTITY_CONTAINER_1.equals(entityContainer)) {
      if (ASSOCIATION_ET1_ET2.equals(association)) {
        AssociationSetEnd end1 = new AssociationSetEnd().setRole(ROLE_1).setEntitySet(ES_KEY_IS_STRING);
        AssociationSetEnd end2 = new AssociationSetEnd().setRole(ROLE_2).setEntitySet(ES_KEY_IS_INTEGER);

        return new AssociationSet().setName("AssociationSet").setEnd1(end1).setEnd2(end2);
      }
    }
    return null;
  }

  private Key createKey(final String... keyNames) {
    List<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
    for (final String keyName : keyNames)
      keyProperties.add(new PropertyRef().setName(keyName));
    return new Key().setKeys(keyProperties);
  }

}
