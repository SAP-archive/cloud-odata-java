package com.sap.core.odata.processor.core.jpa.cud;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.ODataFeed;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;

public class JPACreateRequest extends JPAWriteRequest {

  private Metamodel metamodel;

  public JPACreateRequest() {
    super();
    jpaEmbeddableKeyMap = new HashMap<String, HashMap<String, String>>();
    jpaEmbeddableKeyObjectMap = new HashMap<String, Class<?>>();
  }

  public JPACreateRequest(final Metamodel metamodel) {
    this();
    this.metamodel = metamodel;
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> process(final PostUriInfo postUriInfo, final InputStream content, final String requestContentType) throws ODataJPARuntimeException {
    final EdmEntitySet entitySet = postUriInfo.getTargetEntitySet();
    EdmEntityType entityType = null;
    try {
      entityType = entitySet.getEntityType();
    } catch (EdmException e3) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e3.getMessage()), e3);
    }
    String entityName = null;
    try {
      if (entityType.getMapping() != null && entityType.getMapping().getInternalName() != null)
      {
        entityName = entityType.getMapping().getInternalName();
      } else {
        entityName = entityType.getName();
      }
    } catch (EdmException e1) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e1.getMessage()), e1);
    }
    Object jpaEntity = null;
    Set<EntityType<?>> entityTypeSet = metamodel.getEntities();
    String currentEntityName = null;
    for (EntityType<?> entityTypeTemp : entityTypeSet) {
      if (entityTypeTemp.getJavaType().getName().endsWith("." + entityName)) {
        currentEntityName = entityTypeTemp.getName();
        try {
          jpaEntity = entityTypeTemp.getJavaType().newInstance();
          break;
        } catch (InstantiationException e) {
          throw ODataJPARuntimeException
              .throwException(ODataJPARuntimeException.GENERAL
                  .addContent(e.getMessage()), e);
        } catch (IllegalAccessException e) {
          throw ODataJPARuntimeException
              .throwException(ODataJPARuntimeException.GENERAL
                  .addContent(e.getMessage()), e);
        }
      }
    }

    ODataEntry entryValues = null;
    try {
      entryValues = parseEntry(entitySet, content, requestContentType, true);
    } catch (ODataBadRequestException e1) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e1.getMessage()), e1);
    }

    Map<String, Object> propertyValueMap = entryValues.getProperties();
    parse2JPAEntityValueMap(jpaEntity, entityType, propertyValueMap, currentEntityName);

    Map<EdmNavigationProperty, EdmEntitySet> navPropEntitySetMap = null;
    try {
      navPropEntitySetMap = createInlinedEntities(jpaEntity, entitySet, entryValues, currentEntityName);
    } catch (ODataException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    }
    List<T> objectList = new ArrayList<T>();
    objectList.add((T) jpaEntity);
    objectList.add((T) navPropEntitySetMap);
    return objectList;
  }

  @SuppressWarnings("unchecked")
  public final Object parse2JPAEntityValueMap(
      final Object jpaEntity, final EdmStructuralType edmEntityType, final Map<String, Object> propertyValueMap, final String entityName)
      throws ODataJPARuntimeException {

    if (jpaEntity == null || edmEntityType == null || propertyValueMap == null || propertyValueMap.size() == 0) {
      return null;
    }

    String jpaEntityAccessKey = jpaEntity.getClass().getName();

    if (!jpaEntityAccessMap.containsKey(jpaEntityAccessKey)) {
      jpaEntityAccessMap.put(jpaEntityAccessKey,
          getSetters(jpaEntity, edmEntityType, true));
    }

    HashMap<String, Method> setters = jpaEntityAccessMap
        .get(jpaEntityAccessKey);
    HashMap<String, String> embeddableKeys = jpaEmbeddableKeyMap
        .get(jpaEntityAccessKey);
    String propertyName = null;
    try {
      for (String key : setters.keySet()) {

        EdmProperty property = (EdmProperty) edmEntityType
            .getProperty(key);
        if (property.getMapping() != null && property.getMapping().getInternalName() != null) {
          propertyName = property.getMapping().getInternalName();
        } else {
          propertyName = property.getName();
        }
        Method method = setters.get(key);
        Object propertyValue = propertyValueMap.get(key);
        if (propertyValue == null) {
          continue;
        }
        /*        if (propertyValue instanceof java.util.GregorianCalendar) {
                  propertyValue = ((java.util.GregorianCalendar) propertyValue).getTime();
                }*/

        if (method != null) {
          if (property.getType().getKind().equals(EdmTypeKind.COMPLEX)) {
            Object complexObject = jpaComplexObjectMap.get(propertyName);
            parse2JPAEntityValueMap(complexObject, ((EdmComplexType) property.getType()),
                (Map<String, Object>) propertyValue, propertyName);
            setters.get(key).invoke(jpaEntity, complexObject);
          } else {
            setters.get(key).invoke(jpaEntity, propertyValue);
          }
        }
      }

      if (embeddableKeys != null) {
        Object embeddableKeyObj = null;
        Method method = null;
        for (String embeddableKey : embeddableKeys.keySet()) {
          String name = embeddableKeys.get(embeddableKey);
          String[] nameParts = name.split("\\.");//$NON-NLS-1$
          Object propertyValue = jpaEntity;
          Class<?> propertyClass = null;

          try {
            for (EntityType<?> entity : metamodel.getEntities())
            {
              if (entity.getName().equals(entityName))
              {
                Attribute<?, ?> attribute = entity.getAttribute(nameParts[0].substring(3, 4).toLowerCase() + nameParts[0].substring(4));
                propertyClass = attribute.getJavaType();
                if (embeddableKeyObj == null) {
                  try {
                    embeddableKeyObj = propertyClass.newInstance();
                  } catch (InstantiationException e) {
                    throw ODataJPARuntimeException
                        .throwException(ODataJPARuntimeException.GENERAL
                            .addContent(e.getMessage()), e);
                  }
                }
                break;
              }
            }

            method = propertyValue.getClass().getMethod(
                nameParts[0], propertyClass);
            populateEmbeddableKey(embeddableKeyObj, embeddableKey, nameParts[1], propertyValueMap);
          } catch (NoSuchMethodException e) {
            throw ODataJPARuntimeException
                .throwException(ODataJPARuntimeException.GENERAL
                    .addContent(e.getMessage()), e);
          }
        }
        propertyName = "Embeddable Key";//$NON-NLS-1$
        method.invoke(jpaEntity, embeddableKeyObj);
      }
    } catch (SecurityException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (EdmException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (IllegalAccessException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (IllegalArgumentException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.ERROR_JPQL_PARAM_VALUE
              .addContent(propertyName), e);
    } catch (InvocationTargetException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    }
    return jpaEntity;
  }

  private void populateEmbeddableKey(final Object embeddableKeyObject, final String key, final String setterName, final Map<String, Object> propertyValueMap) throws ODataJPARuntimeException {
    Class<?> propertyClass = jpaEmbeddableKeyObjectMap.get(key);
    Method method = null;
    try {
      method = embeddableKeyObject.getClass().getMethod(setterName, propertyClass);
    } catch (NoSuchMethodException e1) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e1.getMessage()), e1);
    } catch (SecurityException e1) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e1.getMessage()), e1);
    }
    try {
      method.invoke(embeddableKeyObject, propertyValueMap.get(key));
    } catch (IllegalAccessException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (IllegalArgumentException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.ERROR_JPQL_KEY_VALUE
              .addContent(key), e);
    } catch (InvocationTargetException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    }
  }

  private <T> Map<EdmNavigationProperty, EdmEntitySet> createInlinedEntities(final T jpaEntity, final EdmEntitySet entitySet, final ODataEntry entryValues, final String jpaEntityName) throws ODataException {
    if (jpaEntity == null) {
      return null;
    }
    Map<String, Object> relatedPropertyValueMap = new HashMap<String, Object>();
    Map<String, Class<?>> relatedClassMap = new HashMap<String, Class<?>>();
    Map<EdmNavigationProperty, EdmEntitySet> navPropEntitySetMap = new HashMap<EdmNavigationProperty, EdmEntitySet>();
    final EdmEntityType entityType = entitySet.getEntityType();
    for (final String navigationPropertyName : entityType.getNavigationPropertyNames()) {
      final EdmNavigationProperty navigationProperty = (EdmNavigationProperty) entityType.getProperty(navigationPropertyName);
      List<ODataEntry> relatedValueList = null;
      if (entryValues.getProperties().get(navigationPropertyName) != null) {
        relatedValueList = ((ODataFeed) entryValues.getProperties().get(navigationPropertyName)).getEntries();
      }
      List<Object> relatedDataList = null;
      if (relatedValueList != null) {
        relatedDataList = new ArrayList<Object>();
        final EdmEntitySet relatedEntitySet = entitySet.getRelatedEntitySet(navigationProperty);

        for (final ODataEntry relatedValues : relatedValueList) {

          String entityName = null;
          EdmEntityType relatedEntityType = relatedEntitySet.getEntityType();
          try {
            if (relatedEntityType.getMapping() != null && relatedEntityType.getMapping().getInternalName() != null)
            {
              entityName = relatedEntityType.getMapping().getInternalName();
            } else {
              entityName = relatedEntityType.getName();
            }
          } catch (EdmException e1) {
            throw ODataJPARuntimeException
                .throwException(ODataJPARuntimeException.GENERAL
                    .addContent(e1.getMessage()), e1);
          }

          Object relatedData = null;
          Set<EntityType<?>> entityTypeSet = metamodel.getEntities();
          String currentEntityName = null;
          for (EntityType<?> entityTypeTemp : entityTypeSet) {
            if (entityTypeTemp.getJavaType().getName().endsWith("." + entityName)) {
              currentEntityName = entityTypeTemp.getName();
              try {
                relatedClassMap.put(navigationProperty.getMapping().getInternalName(), entityTypeTemp.getJavaType());
                relatedData = entityTypeTemp.getJavaType().newInstance();
                break;
              } catch (InstantiationException e) {
                throw ODataJPARuntimeException
                    .throwException(ODataJPARuntimeException.GENERAL
                        .addContent(e.getMessage()), e);
              } catch (IllegalAccessException e) {
                throw ODataJPARuntimeException
                    .throwException(ODataJPARuntimeException.GENERAL
                        .addContent(e.getMessage()), e);
              }
            }
          }
          if (relatedValues != null && relatedEntitySet != null) {
            relatedDataList.add(relatedData);
            if (navPropEntitySetMap.get(navigationProperty) == null) {
              navPropEntitySetMap.put(navigationProperty, relatedEntitySet);
            }
            parse2JPAEntityValueMap(relatedData, relatedEntitySet.getEntityType(), relatedValues.getProperties(), currentEntityName);
          } else {
            continue;
          }
          createInlinedEntities(relatedData, relatedEntitySet, relatedValues, currentEntityName);
        }
      }
      relatedPropertyValueMap.put(navigationProperty.getMapping().getInternalName(), relatedDataList);
    }
    setNavigationProperties(jpaEntity, entitySet, relatedPropertyValueMap, jpaEntityName, relatedClassMap);
    return navPropEntitySetMap;
  }

  @SuppressWarnings("unchecked")
  private void setNavigationProperties(
      final Object jpaEntity, final EdmEntitySet entitySet, final Map<String, Object> propertyValueMap, final String entityName, final Map<String, Class<?>> relatedClassMap) throws ODataJPARuntimeException {
    if (jpaEntity == null || entitySet == null || propertyValueMap == null || propertyValueMap.size() == 0) {
      return;
    }
    List<HashMap<?, ?>> mapList = getSettersForNavigationProperties(jpaEntity, entitySet, relatedClassMap);
    HashMap<String, Method> setters = (HashMap<String, Method>) mapList.get(0);
    HashMap<String, EdmMultiplicity> multiplicityMap = (HashMap<String, EdmMultiplicity>) mapList.get(1);
    for (String key : setters.keySet()) {
      Method method = setters.get(key);
      List<Object> propertyValue = (List<Object>) propertyValueMap.get(key);
      if (propertyValue == null || propertyValue.size() == 0) {
        continue;
      }
      try {
        if (multiplicityMap.get(key) == EdmMultiplicity.MANY) {
          method.invoke(jpaEntity, propertyValue);
        } else {
          method.invoke(jpaEntity, propertyValue.get(0));
        }
      } catch (IllegalAccessException e) {
        throw ODataJPARuntimeException
            .throwException(ODataJPARuntimeException.GENERAL
                .addContent(e.getMessage()), e);
      } catch (IllegalArgumentException e) {
        throw ODataJPARuntimeException
            .throwException(ODataJPARuntimeException.GENERAL
                .addContent(e.getMessage()), e);
      } catch (InvocationTargetException e) {
        throw ODataJPARuntimeException
            .throwException(ODataJPARuntimeException.GENERAL
                .addContent(e.getMessage()), e);
      }
    }

  }

  private List<HashMap<?, ?>> getSettersForNavigationProperties(final Object jpaEntity, final EdmEntitySet edmEntitySet, final Map<String, Class<?>> relatedClassMap) throws ODataJPARuntimeException {
    List<HashMap<?, ?>> mapList = new ArrayList<HashMap<?, ?>>();
    HashMap<String, Method> setters = new HashMap<String, Method>();
    HashMap<String, EdmMultiplicity> multiplicityMap = new HashMap<String, EdmMultiplicity>();
    EdmEntityType edmEntityType = null;
    try {
      edmEntityType = edmEntitySet.getEntityType();
    } catch (EdmException e2) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e2.getMessage()), e2);
    }

    try {
      for (final String navigationPropertyName : edmEntityType.getNavigationPropertyNames()) {
        final EdmNavigationProperty navigationProperty = (EdmNavigationProperty) edmEntityType.getProperty(navigationPropertyName);
        String entityName = null;
        try {
          if (navigationProperty.getMapping() != null && navigationProperty.getMapping().getInternalName() != null)
          {
            entityName = navigationProperty.getMapping().getInternalName();
          } else {
            entityName = navigationProperty.getName();
          }
        } catch (EdmException e1) {
          throw ODataJPARuntimeException
              .throwException(ODataJPARuntimeException.GENERAL
                  .addContent(e1.getMessage()), e1);
        }
        String name = getSetterName(entityName);

        Class<?> propertyClass = null;
        if (navigationProperty.getMultiplicity() == EdmMultiplicity.MANY) {
          propertyClass = List.class;
          multiplicityMap.put(entityName, EdmMultiplicity.MANY);
        } else {
          propertyClass = relatedClassMap.get(entityName);
          if (propertyClass == null) {
            continue;
          }
          multiplicityMap.put(entityName, EdmMultiplicity.ONE);
        }
        try {
          setters.put(
              entityName,
              jpaEntity.getClass().getDeclaredMethod(name, propertyClass));
        } catch (NoSuchMethodException e) {
          throw ODataJPARuntimeException
              .throwException(ODataJPARuntimeException.GENERAL
                  .addContent(e.getMessage()), e);
        } catch (SecurityException e) {
          throw ODataJPARuntimeException
              .throwException(ODataJPARuntimeException.GENERAL
                  .addContent(e.getMessage()), e);
        }
      }
    } catch (EdmException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    }
    mapList.add(0, setters);
    mapList.add(1, multiplicityMap);
    return mapList;
  }

  private String getSetterName(final String navigationPropertyName)
      throws ODataJPARuntimeException {
    StringBuilder builder = new StringBuilder();
    char c = Character.toUpperCase(navigationPropertyName.charAt(0));

    builder.append("set").append(c).append(navigationPropertyName.substring(1)) //$NON-NLS-1$
        .toString();
    if (builder.length() > 0) {
      return builder.toString();
    } else {
      return null;
    }

  }
}
