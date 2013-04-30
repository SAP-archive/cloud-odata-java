/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;

/**
 * Aggregator to get easy and fast access to all for serialization and de-serialization necessary {@link EdmEntitySet} informations.
 * 
 * @author SAP AG
 */
public class EntityInfoAggregator {

  private static final Set<String> SYN_TARGET_PATHS = new HashSet<String>(Arrays.asList(
      EdmTargetPath.SYNDICATION_AUTHORNAME,
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

  private Map<String, EntityPropertyInfo> propertyInfo = new HashMap<String, EntityPropertyInfo>();
  private Map<String, NavigationPropertyInfo> navigationPropertyInfos = new HashMap<String, NavigationPropertyInfo>();
  private List<EntityPropertyInfo> keyPropertyInfos;

  /*
   * list with all property names in the order based on order in {@link EdmProperty} (normally [key, entity,
   * navigation])
   */
  private List<String> etagPropertyNames = new ArrayList<String>();
  private List<String> propertyNames;
  private List<String> navigationPropertyNames;
  private List<String> selectedPropertyNames;
  private List<String> selectedNavigationPropertyNames;
  private List<String> expandedNavigationPropertyNames;

  private Map<String, EntityPropertyInfo> targetPath2EntityPropertyInfo = new HashMap<String, EntityPropertyInfo>();
  private List<String> noneSyndicationTargetPaths = new ArrayList<String>();

  private boolean isDefaultEntityContainer;
  private String entitySetName;
  private String entityContainerName;

  private EdmEntityType entityType;
  private EdmEntitySet entitySet;

  /**
   * Constructor is private to force creation over {@link #create(EdmEntitySet)} method.
   */
  private EntityInfoAggregator() {}

  /**
   * Create an {@link EntityInfoAggregator} based on given {@link EdmEntitySet}
   * 
   * @param entitySet
   *          with which the {@link EntityInfoAggregator} is initialized.
   * @param expandSelectTree 
   * @return created and initialized {@link EntityInfoAggregator}
   * @throws EntityProviderException
   *           if during initialization of {@link EntityInfoAggregator} something goes wrong (e.g. exceptions during
   *           access
   *           of {@link EdmEntitySet}).
   */
  public static EntityInfoAggregator create(final EdmEntitySet entitySet, final ExpandSelectTreeNode expandSelectTree) throws EntityProviderException {
    EntityInfoAggregator eia = new EntityInfoAggregator();
    eia.initialize(entitySet, expandSelectTree);
    return eia;
  }

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
    eia.initialize(entitySet, null);
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
      return entityInfo.createPropertyInfoObjects(complexType, complexType.getPropertyNames());
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

  /**
   * @return the edm entity set which was used to build this entity info aggregator object
   */
  public EdmEntitySet getEntitySet() {
    return entitySet;
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

  public EdmEntityType getEntityType() {
    return entityType;
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
  public List<String> getNavigationPropertyNames() throws EntityProviderException {
    return Collections.unmodifiableList(navigationPropertyNames);
  }

  /**
   * @return unmodifiable set of all property names.
    */
  public List<String> getPropertyNames() throws EntityProviderException {
    return Collections.unmodifiableList(propertyNames);
  }

  /**
   * @return unmodifiable set of selected property names.
   */
  public List<String> getSelectedPropertyNames() throws EntityProviderException {
    return Collections.unmodifiableList(selectedPropertyNames);
  }

  /**
   * @return unmodifiable set of selected property names.
   */
  public List<String> getSelectedNavigationPropertyNames() throws EntityProviderException {
    return Collections.unmodifiableList(selectedNavigationPropertyNames);
  }

  public Collection<EntityPropertyInfo> getPropertyInfos() {
    return Collections.unmodifiableCollection(propertyInfo.values());
  }

  public EntityPropertyInfo getPropertyInfo(final String name) {
    return propertyInfo.get(name);
  }

  public Collection<EntityPropertyInfo> getETagPropertyInfos() {
    List<EntityPropertyInfo> keyProperties = new ArrayList<EntityPropertyInfo>();
    for (String etagPropertyName : etagPropertyNames) {
      EntityPropertyInfo e = propertyInfo.get(etagPropertyName);
      keyProperties.add(e);
    }
    return keyProperties;
  }

  /**
   * @return list of all key property infos
   * @throws EntityProviderException 
   */
  public List<EntityPropertyInfo> getKeyPropertyInfos() throws EntityProviderException {

    if (keyPropertyInfos == null) {
      try {
        keyPropertyInfos = new ArrayList<EntityPropertyInfo>();
        for (String keyPropertyName : entityType.getKeyPropertyNames()) {
          keyPropertyInfos.add(propertyInfo.get(keyPropertyName));
        }
      } catch (EdmException e) {
        throw new EntityProviderException(EntityProviderException.COMMON, e);
      }
    }
    return keyPropertyInfos;
  }

  public NavigationPropertyInfo getNavigationPropertyInfo(final String name) {
    return navigationPropertyInfos.get(name);
  }

  private void initialize(final EdmEntitySet entitySet, final ExpandSelectTreeNode expandSelectTree) throws EntityProviderException {
    try {
      this.entitySet = entitySet;
      entityType = entitySet.getEntityType();
      entitySetName = entitySet.getName();
      isDefaultEntityContainer = entitySet.getEntityContainer().isDefaultEntityContainer();
      entityContainerName = entitySet.getEntityContainer().getName();

      propertyNames = entityType.getPropertyNames();
      navigationPropertyNames = entityType.getNavigationPropertyNames();

      propertyInfo = createPropertyInfoObjects(entityType, propertyNames);
      navigationPropertyInfos = createNavigationInfoObjects(entityType, navigationPropertyNames);

      selectedPropertyNames = propertyNames;
      selectedNavigationPropertyNames = navigationPropertyNames;
      expandedNavigationPropertyNames = new ArrayList<String>();

      if (expandSelectTree != null && !expandSelectTree.isAll()) {
        selectedPropertyNames = new ArrayList<String>();
        selectedNavigationPropertyNames = new ArrayList<String>();
        for (EdmProperty property : expandSelectTree.getProperties()) {
          selectedPropertyNames.add(property.getName());
        }
        for (String property : expandSelectTree.getLinks().keySet()) {
          selectedNavigationPropertyNames.add(property);
          if (expandSelectTree.getLinks().get(property) != null) {
            expandedNavigationPropertyNames.add(property);
          }
        }
      } else if (expandSelectTree != null) {
        for (String property : expandSelectTree.getLinks().keySet()) {
          if (expandSelectTree.getLinks().get(property) != null) {
            expandedNavigationPropertyNames.add(property);
          }
        }
      }

    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private Map<String, EntityPropertyInfo> createPropertyInfoObjects(final EdmStructuralType type, final List<String> propertyNames) throws EntityProviderException {
    try {
      Map<String, EntityPropertyInfo> infos = new HashMap<String, EntityPropertyInfo>();

      for (String propertyName : propertyNames) {
        EdmProperty property = (EdmProperty) type.getProperty(propertyName);

        checkETagRelevant(property);

        EntityPropertyInfo info = createEntityPropertyInfo(property);
        infos.put(info.getName(), info);
        checkTargetPathInfo(property, info);
      }

      return infos;
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private Map<String, NavigationPropertyInfo> createNavigationInfoObjects(final EdmStructuralType type, final List<String> propertyNames) throws EntityProviderException {
    try {
      Map<String, NavigationPropertyInfo> infos = new HashMap<String, NavigationPropertyInfo>();

      for (String propertyName : propertyNames) {
        EdmNavigationProperty navProperty = (EdmNavigationProperty) type.getProperty(propertyName);
        NavigationPropertyInfo info = NavigationPropertyInfo.create(navProperty);
        infos.put(propertyName, info);
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
      Map<String, EntityPropertyInfo> recursiveInfos = createPropertyInfoObjects(complex, complex.getPropertyNames());
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

  public List<String> getExpandedNavigationPropertyNames() {
    return expandedNavigationPropertyNames;
  }
}
