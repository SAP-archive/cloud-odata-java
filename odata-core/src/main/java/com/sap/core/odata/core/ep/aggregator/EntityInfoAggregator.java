package com.sap.core.odata.core.ep.aggregator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmConcurrencyMode;
import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmTargetPath;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.ep.EntityProviderException;

/**
 * Aggregator to get easy and fast access to all for a serializer necessary {@link EdmEntitySet} informations.
 * 
 * @author SAP AG
 */
public class EntityInfoAggregator {

  private static final Set<String> SYN_TARGET_PATHS = new HashSet<String>(Arrays.asList(
      EdmTargetPath.SYNDICATION_AUTHOREMAIL,
      EdmTargetPath.SYNDICATION_AUTHOREMAIL,
      EdmTargetPath.SYNDICATION_AUTHORURI,
      EdmTargetPath.SYNDICATION_PUBLISHED,
      EdmTargetPath.SYNDICATION_RIGHTS,
      EdmTargetPath.SYNDICATION_TITLE,
      EdmTargetPath.SYNDICATION_UPDATED,
      EdmTargetPath.SYNDICATION_CONTRIBUTORNAME,
      EdmTargetPath.SYNDICATION_CONTRIBUTOREMAIL,
      EdmTargetPath.SYNDICATION_CONTRIBUTORURI,
      EdmTargetPath.SYNDICATION_SOURCE,
      EdmTargetPath.SYNDICATION_SUMMARY));

  private Map<String, EntityPropertyInfo> name2EntityPropertyInfo = new HashMap<String, EntityPropertyInfo>();
  private Map<String, EntityPropertyInfo> targetPath2EntityPropertyInfo = new HashMap<String, EntityPropertyInfo>();
  private List<NavigationPropertyInfo> navigationPropertyInfos = new ArrayList<NavigationPropertyInfo>();
  private List<String> keyPropertyNames = new ArrayList<String>();
  /**
   * list with all property names in the order based on order in {@link EdmProperty} (normally [key, entity,
   * navigation])
   */
  private List<String> allPropertyNames = new ArrayList<String>();
  private List<String> etagPropertyNames = new ArrayList<String>();
  private List<String> noneSyndicationTargetPaths = new ArrayList<String>();
  private boolean isDefaultEntityContainer;
  private String entitySetName;
  private String entityTypeNamespace;
  private String entityContainerName;
  private String entityTypeName;
  private boolean entityTypeHasStream;

  /**
   * Constructor is private to force creation over {@link #create(EdmEntitySet)} method.
   */
  private EntityInfoAggregator() {}

  /**
   * Create an {@link EntityInfoAggregator} based on given {@link EdmEntitySet}
   * 
   * @param entitySet
   *          with which the {@link EntityInfoAggregator} is initialized.
   * @return created and initialized {@link EntityInfoAggregator}
   * @throws EntityProviderException
   *           if during initialization of {@link EntityInfoAggregator} something goes wrong (e.g. exceptions during
   *           access
   *           of {@link EdmEntitySet}).
   */
  public static EntityInfoAggregator create(final EdmEntitySet entitySet) throws EntityProviderException {
    EntityInfoAggregator eia = new EntityInfoAggregator();
    eia.initialize(entitySet);
    return eia;
  }

  /**
   * Create an {@link EntityPropertyInfo} based on given {@link EdmProperty}
   * 
   * @param property
   *          for which the {@link EntityPropertyInfo} is created.
   * @return created {@link EntityPropertyInfo}
   * @throws EntityProviderException
   *           if create of {@link EntityPropertyInfo} something goes wrong (e.g. exceptions during
   *           access of {@link EdmProperty}).
   */
  public static EntityPropertyInfo create(final EdmProperty property) throws EntityProviderException {
    try {
      EntityInfoAggregator eia = new EntityInfoAggregator();
      return eia.createEntityPropertyInfo(property);
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  /**
   * Create an map of <code>complex type property name</code> to {@link EntityPropertyInfo} based on given {@link EdmComplexType}
   * 
   * @param complexType
   *          for which the {@link EntityPropertyInfo} is created.
   * @return created map of <code>complex type property name</code> to {@link EntityPropertyInfo}
   * @throws EntityProviderException
   *           if create of {@link EntityPropertyInfo} something goes wrong (e.g. exceptions during
   *           access of {@link EntityPropertyInfo}).
   */
  public static Map<String, EntityPropertyInfo> create(final EdmComplexType complexType) throws EntityProviderException {
    try {
      EntityInfoAggregator entityInfo = new EntityInfoAggregator();
      return entityInfo.createInfoObjects(complexType, complexType.getPropertyNames());
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  /**
   * Create an {@link EntityPropertyInfo} based on given {@link EdmFunctionImport}
   * 
   * @param functionImport
   *          for which the {@link EntityPropertyInfo} is created.
   * @return created {@link EntityPropertyInfo}
   * @throws EntityProviderException
   *           if create of {@link EntityPropertyInfo} something goes wrong (e.g. exceptions during
   *           access of {@link EdmFunctionImport}).
   */
  public static EntityPropertyInfo create(final EdmFunctionImport functionImport) throws EntityProviderException {
    try {
      EntityInfoAggregator eia = new EntityInfoAggregator();
      return eia.createEntityPropertyInfo(functionImport, functionImport.getReturnType().getType());
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  public boolean isEntityTypeHasStream() {
    return entityTypeHasStream;
  }

  /**
   * @return entity set name.
   */
  public String getEntitySetName() {
    return entitySetName;
  }

  /**
   * @return <code>true</code> if the entity container of {@link EdmEntitySet} is the default container,
   *         otherwise <code>false</code>.
   */
  public boolean isDefaultEntityContainer() {
    return isDefaultEntityContainer;
  }

  public EntityPropertyInfo getTargetPathInfo(final String targetPath) {
    return targetPath2EntityPropertyInfo.get(targetPath);
  }

  public String getEntityTypeNamespace() {
    return entityTypeNamespace;
  }

  public String getEntityTypeName() {
    return entityTypeName;
  }

  public String getEntityContainerName() {
    return entityContainerName;
  }

  /**
   * @return unmodifiable set of all found target path names.
   */
  public Collection<String> getTargetPathNames() {
    return Collections.unmodifiableCollection(targetPath2EntityPropertyInfo.keySet());
  }

  /**
   * @return unmodifiable set of found <code>none syndication target path names</code> (all target path names which are
   *         not pre-defined).
   */
  public List<String> getNoneSyndicationTargetPathNames() {
    return Collections.unmodifiableList(noneSyndicationTargetPaths);
  }

  /**
   * @return unmodifiable set of all found navigation property names.
   */
  public List<String> getNavigationPropertyNames() {
    List<String> navPropNames = new ArrayList<String>();
    for (NavigationPropertyInfo info : navigationPropertyInfos) {
      navPropNames.add(info.getName());
    }
    return Collections.unmodifiableList(navPropNames);
  }

  /**
   * @return unmodifiable set of all property names.
   */
  public List<String> getPropertyNames() {
    return Collections.unmodifiableList(allPropertyNames);
  }

  public Collection<EntityPropertyInfo> getPropertyInfos() {
    return Collections.unmodifiableCollection(name2EntityPropertyInfo.values());
  }

  public EntityPropertyInfo getPropertyInfo(final String name) {
    return name2EntityPropertyInfo.get(name);
  }

  public Collection<EntityPropertyInfo> getETagPropertyInfos() {
    List<EntityPropertyInfo> keyProperties = new ArrayList<EntityPropertyInfo>();
    for (String etagPropertyName : etagPropertyNames) {
      EntityPropertyInfo e = name2EntityPropertyInfo.get(etagPropertyName);
      keyProperties.add(e);
    }
    return keyProperties;
  }

  /**
   * @return list of all key property infos.
   */
  public List<EntityPropertyInfo> getKeyPropertyInfos() {
    List<EntityPropertyInfo> keyProperties = new ArrayList<EntityPropertyInfo>();
    for (String keyPropertyName : keyPropertyNames) {
      keyProperties.add(name2EntityPropertyInfo.get(keyPropertyName));
    }
    return keyProperties;
  }

  /**
   * @return unmodifiable collection of all navigation properties.
   */
  public List<NavigationPropertyInfo> getNavigationPropertyInfos() {
    return Collections.unmodifiableList(navigationPropertyInfos);
  }

  // #########################################
  // #
  // # Start with private methods
  // #
  // #########################################

  private void initialize(final EdmEntitySet entitySet) throws EntityProviderException {
    try {
      EdmEntityType type = entitySet.getEntityType();
      entitySetName = entitySet.getName();
      entityTypeName = type.getName();
      entityTypeNamespace = type.getNamespace();
      entityTypeHasStream = type.hasStream();
      isDefaultEntityContainer = entitySet.getEntityContainer().isDefaultEntityContainer();
      entityContainerName = entitySet.getEntityContainer().getName();
      //
      List<String> propertyNames = new ArrayList<String>();
      propertyNames.addAll(type.getPropertyNames());
      propertyNames.addAll(type.getNavigationPropertyNames());
      //
      name2EntityPropertyInfo = createInfoObjects(type, propertyNames);
      //
      keyPropertyNames.addAll(type.getKeyPropertyNames());
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private Map<String, EntityPropertyInfo> createInfoObjects(final EdmStructuralType type, final List<String> propertyNames) throws EntityProviderException {
    try {
      Map<String, EntityPropertyInfo> infos = new HashMap<String, EntityPropertyInfo>();

      for (String propertyName : propertyNames) {
        EdmTyped typed = type.getProperty(propertyName);
        allPropertyNames.add(typed.getName());

        if (typed instanceof EdmProperty) {
          EdmProperty property = (EdmProperty) typed;

          checkETagRelevant(property);

          EntityPropertyInfo info = createEntityPropertyInfo(property);
          infos.put(info.getName(), info);

          checkTargetPathInfo(property, info);
        } else if (typed instanceof EdmNavigationProperty) {
          EdmNavigationProperty navProperty = (EdmNavigationProperty) typed;
          NavigationPropertyInfo info = NavigationPropertyInfo.create(navProperty);
          navigationPropertyInfos.add(info);
        }
      }

      return infos;
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private EntityPropertyInfo createEntityPropertyInfo(final EdmProperty property) throws EdmException, EntityProviderException {
    EdmType type = property.getType();
    if (type instanceof EdmSimpleType) {
      return EntityPropertyInfo.create(property);
    } else if (type instanceof EdmComplexType) {
      EdmComplexType complex = (EdmComplexType) type;
      Map<String, EntityPropertyInfo> recursiveInfos = createInfoObjects(complex, complex.getPropertyNames());
      return EntityComplexPropertyInfo.create(property, complex.getPropertyNames(), recursiveInfos);
    } else {
      throw new EntityProviderException(EntityProviderException.UNSUPPORTED_PROPERTY_TYPE);
    }
  }

  private EntityPropertyInfo createEntityPropertyInfo(final EdmFunctionImport functionImport, final EdmType type) throws EdmException, EntityProviderException {
    EntityPropertyInfo epi;

    if (type.getKind() == EdmTypeKind.COMPLEX) {
      EdmComplexType complex = (EdmComplexType) type;
      Map<String, EntityPropertyInfo> eia = EntityInfoAggregator.create(complex);

      List<EntityPropertyInfo> childEntityInfoList = new ArrayList<EntityPropertyInfo>();
      for (String propertyName : complex.getPropertyNames()) {
        childEntityInfoList.add(eia.get(propertyName));
      }
      epi = new EntityComplexPropertyInfo(functionImport.getName(), type, null, null, childEntityInfoList);

    } else if (type.getKind() == EdmTypeKind.SIMPLE) {

      epi = new EntityPropertyInfo(functionImport.getName(), type, null, null, null, null);
    } else {
      throw new EntityProviderException(EntityProviderException.UNSUPPORTED_PROPERTY_TYPE.addContent(type.getKind()));
    }

    return epi;
  }

  private void checkETagRelevant(final EdmProperty edmProperty) throws EntityProviderException {
    try {
      if (edmProperty.getFacets() != null && edmProperty.getFacets().getConcurrencyMode() == EdmConcurrencyMode.Fixed) {
        etagPropertyNames.add(edmProperty.getName());
      }
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private void checkTargetPathInfo(final EdmProperty property, final EntityPropertyInfo propertyInfo) throws EntityProviderException {
    try {
      EdmCustomizableFeedMappings customizableFeedMappings = property.getCustomizableFeedMappings();
      if (customizableFeedMappings != null) {
        String targetPath = customizableFeedMappings.getFcTargetPath();
        targetPath2EntityPropertyInfo.put(targetPath, propertyInfo);
        if (!SYN_TARGET_PATHS.contains(targetPath)) {
          noneSyndicationTargetPaths.add(targetPath);
        }
      }
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }
}
