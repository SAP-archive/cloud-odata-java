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
package com.sap.core.odata.processor.core.jpa.access.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import com.sap.core.odata.api.edm.EdmAssociationEnd;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmMapping;

public final class JPAEntityParser {

  /*
   * List of buffers used by the Parser
   */
  private static short MAX_SIZE = 10;
  public static final String ACCESS_MODIFIER_GET = "get";
  public static final String ACCESS_MODIFIER_SET = "set";

  private HashMap<String, HashMap<String, Method>> jpaEntityAccessMap = null;
  private HashMap<String, HashMap<String, String>> jpaEmbeddableKeyMap = null;

  private static JPAEntityParser jpaEntityParser;

  private JPAEntityParser() {
    jpaEntityAccessMap = new HashMap<String, HashMap<String, Method>>(
        MAX_SIZE);
    jpaEmbeddableKeyMap = new HashMap<String, HashMap<String, String>>();
  };

  public static final JPAEntityParser create() {
    if (jpaEntityParser == null) {
      jpaEntityParser = new JPAEntityParser();
    }
    return jpaEntityParser;
  }

  /**
   * The method returns a Hash Map of Properties and values for selected
   * properties of an EdmEntity Type
   * 
   * @param jpaEntity
   * @param selectedItems
   * @return a Hash Map of Properties and values for given selected properties
   *         of an EdmEntity Type
   * @throws ODataJPARuntimeException
   */

  public final HashMap<String, Object> parse2EdmPropertyValueMap(
      final Object jpaEntity, final List<EdmProperty> selectPropertyList)
      throws ODataJPARuntimeException {
    HashMap<String, Object> edmEntity = new HashMap<String, Object>();
    String methodName = null;
    Method method = null;
    for (int i = 0; i < selectPropertyList.size(); i++) {
      String key = null;
      Object propertyValue = null;
      EdmProperty property = null;
      property = selectPropertyList.get(i);

      try {
        methodName = getAccessModifierName(property.getName(),
            property.getMapping(), ACCESS_MODIFIER_GET);
        String[] nameParts = methodName.split("\\.");
        if (nameParts.length > 1) {
          Object propertyVal = new Object();
          propertyVal = jpaEntity;
          for (String namePart : nameParts) {
            method = propertyVal.getClass().getMethod(
                namePart, (Class<?>[]) null);
            method.setAccessible(true);
            propertyVal = method.invoke(propertyVal);
          }
          edmEntity.put(property.getName(), propertyVal);
        } else {
          method = jpaEntity.getClass().getMethod(methodName,
              (Class<?>[]) null);
          method.setAccessible(true);
          propertyValue = method.invoke(jpaEntity);
          key = property.getName();
          if (property.getType().getKind()
              .equals(EdmTypeKind.COMPLEX)) {
            try {
              propertyValue = parse2EdmPropertyValueMap(
                  propertyValue,
                  (EdmStructuralType) property.getType());
            } catch (ODataJPARuntimeException e) {
              throw e;
            }
          }
          edmEntity.put(key, propertyValue);
        }
      } catch (EdmException e) {
        throw ODataJPARuntimeException.throwException(
            ODataJPARuntimeException.GENERAL.addContent(e
                .getMessage()), e);
      } catch (SecurityException e) {
        throw ODataJPARuntimeException.throwException(
            ODataJPARuntimeException.GENERAL.addContent(e
                .getMessage()), e);
      } catch (NoSuchMethodException e) {
        throw ODataJPARuntimeException.throwException(
            ODataJPARuntimeException.GENERAL.addContent(e
                .getMessage()), e);
      } catch (IllegalArgumentException e) {
        throw ODataJPARuntimeException.throwException(
            ODataJPARuntimeException.GENERAL.addContent(e
                .getMessage()), e);
      } catch (IllegalAccessException e) {
        throw ODataJPARuntimeException.throwException(
            ODataJPARuntimeException.GENERAL.addContent(e
                .getMessage()), e);
      } catch (InvocationTargetException e) {
        throw ODataJPARuntimeException.throwException(
            ODataJPARuntimeException.GENERAL.addContent(e
                .getMessage()), e);
      }
    }

    return edmEntity;
  }

  /**
   * The method returns a Hash Map of Properties and values for an EdmEntity
   * Type The method uses reflection on object jpaEntity to get the list of
   * accessModifier method. Then uses the accessModifier method to extract the value from
   * JPAEntity.
   * 
   * @param jpaEntity
   * @param structuralType
   * @return a Hash Map of Properties and values for given EdmEntity Type
   * @throws ODataJPARuntimeException
   */
  public final HashMap<String, Object> parse2EdmPropertyValueMap(
      final Object jpaEntity, final EdmStructuralType structuralType)
      throws ODataJPARuntimeException {

    if (jpaEntity == null || structuralType == null) {
      return null;
    }

    String jpaEntityAccessKey = jpaEntity.getClass().getName();

    if (!jpaEntityAccessMap.containsKey(jpaEntityAccessKey)) {
      jpaEntityAccessMap.put(jpaEntityAccessKey,
          getAccessModifier(jpaEntity, structuralType, ACCESS_MODIFIER_GET));
    }

    HashMap<String, Object> edmEntity = new HashMap<String, Object>();
    HashMap<String, Method> getters = jpaEntityAccessMap
        .get(jpaEntityAccessKey);
    HashMap<String, String> embeddableKeys = jpaEmbeddableKeyMap
        .get(jpaEntityAccessKey);

    try {
      for (String key : getters.keySet()) {

        EdmProperty property = (EdmProperty) structuralType
            .getProperty(key);

        Method method = getters.get(key);
        Object propertyValue = null;

        if (method != null) {
          getters.get(key).setAccessible(true);
          propertyValue = getters.get(key).invoke(jpaEntity);
        }

        if (property.getType().getKind().equals(EdmTypeKind.COMPLEX)) {
          propertyValue = parse2EdmPropertyValueMap(propertyValue,
              (EdmStructuralType) property.getType());
        }

        edmEntity.put(key, propertyValue);

      }

      if (embeddableKeys != null) {
        for (String key : embeddableKeys.keySet()) {
          String name = embeddableKeys.get(key);
          String[] nameParts = name.split("\\.");
          Object propertyValue = jpaEntity;
          Method method = null;
          for (String namePart : nameParts) {
            method = propertyValue.getClass().getMethod(
                namePart, (Class<?>[]) null);
            method.setAccessible(true);
            propertyValue = method.invoke(propertyValue);
          }
          edmEntity.put(key, propertyValue);
        }
      }
    } catch (EdmException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (SecurityException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (NoSuchMethodException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (IllegalArgumentException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (IllegalAccessException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (InvocationTargetException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    }
    return edmEntity;
  }

  // This method appends the associated entities as a java list to an expanded
  // map of a source entity
  public final HashMap<String, Object> parse2EdmNavigationValueMap(
      final Object jpaEntity, final List<EdmNavigationProperty> navigationPropertyList)
      throws ODataJPARuntimeException {
    Object result = null;
    String methodName = null;
    HashMap<String, Object> navigationMap = new HashMap<String, Object>();
    if (navigationPropertyList != null
        && navigationPropertyList.size() != 0) {

      try {
        for (EdmNavigationProperty navigationProperty : navigationPropertyList) {
          methodName = getAccessModifierName(navigationProperty.getName(),
              navigationProperty.getMapping(), ACCESS_MODIFIER_GET);
          Method getterMethod = jpaEntity.getClass()
              .getDeclaredMethod(methodName, (Class<?>[]) null);
          getterMethod.setAccessible(true);
          result = getterMethod.invoke(jpaEntity);
          navigationMap.put(navigationProperty.getName(), result);
        }
      } catch (IllegalArgumentException e) {
        throw ODataJPARuntimeException.throwException(
            ODataJPARuntimeException.GENERAL.addContent(e
                .getMessage()), e);
      } catch (IllegalAccessException e) {
        throw ODataJPARuntimeException.throwException(
            ODataJPARuntimeException.GENERAL.addContent(e
                .getMessage()), e);
      } catch (InvocationTargetException e) {
        throw ODataJPARuntimeException.throwException(
            ODataJPARuntimeException.GENERAL.addContent(e
                .getMessage()), e);
      } catch (EdmException e) {
        throw ODataJPARuntimeException.throwException(
            ODataJPARuntimeException.GENERAL.addContent(e
                .getMessage()), e);
      } catch (SecurityException e) {
        throw ODataJPARuntimeException.throwException(
            ODataJPARuntimeException.GENERAL.addContent(e
                .getMessage()), e);
      } catch (NoSuchMethodException e) {
        throw ODataJPARuntimeException.throwException(
            ODataJPARuntimeException.GENERAL.addContent(e
                .getMessage()), e);
      }
    }
    return navigationMap;
  }

  public HashMap<String, Method> getAccessModifier(final Object jpaEntity,
      final EdmStructuralType structuralType, final String accessModifier) throws ODataJPARuntimeException {

    HashMap<String, Method> accessModifierMap = new HashMap<String, Method>();
    HashMap<String, String> embeddableKey = new HashMap<String, String>();
    try {
      for (String propertyName : structuralType.getPropertyNames()) {

        EdmProperty property = (EdmProperty) structuralType
            .getProperty(propertyName);

        String name = getAccessModifierName(property.getName(),
            property.getMapping(), accessModifier);
        String[] nameParts = name.split("\\.");
        if (nameParts.length > 1) {
          embeddableKey.put(propertyName, name);
        } else {
          accessModifierMap.put(
              propertyName,
              jpaEntity.getClass().getMethod(name,
                  (Class<?>[]) null));
        }
      }
    } catch (NoSuchMethodException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (SecurityException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (EdmException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    }

    if (!embeddableKey.isEmpty()) {
      jpaEmbeddableKeyMap.put(jpaEntity.getClass().getName(),
          embeddableKey);
    }
    return accessModifierMap;
  }

  private static String getAccessModifierName(final String propertyName, final EdmMapping mapping, final String accessModifier)
      throws ODataJPARuntimeException {
    String name = null;
    StringBuilder builder = new StringBuilder();
    String[] nameParts = {};
    if (mapping == null || mapping.getInternalName() == null) {
      name = propertyName;
    } else {
      name = mapping.getInternalName();
    }
    if (name != null) {
      nameParts = name.split("\\.");
    }
    if (nameParts.length == 1) {
      if (name != null) {
        char c = Character.toUpperCase(name.charAt(0));

        builder.append(accessModifier).append(c).append(name.substring(1))
            .toString();
      }
    } else if (nameParts.length > 1) {

      for (int i = 0; i < nameParts.length; i++) {
        name = nameParts[i];
        char c = Character.toUpperCase(name.charAt(0));
        if (i == 0) {
          builder.append(accessModifier).append(c).append(name.substring(1));
        } else {
          builder.append(".").append(accessModifier).append(c)
              .append(name.substring(1));
        }
      }
    } else {
      return null;
    }

    if (builder.length() > 0) {
      return builder.toString();
    } else {
      return null;
    }

  }

  public Method getAccessModifier(final Object jpaEntity, final EdmNavigationProperty navigationProperty, final String accessModifier)
      throws ODataJPARuntimeException {

    try {

      String name = getAccessModifierName(navigationProperty.getName(),
          navigationProperty.getMapping(), accessModifier);

      Class<?>[] params = null;
      if (accessModifier.equals(ACCESS_MODIFIER_SET)) {
        EdmAssociationEnd end = navigationProperty.getRelationship().getEnd(navigationProperty.getToRole());
        switch (end.getMultiplicity()) {
        case MANY:
          params = new Class<?>[] { List.class };
          break;
        case ONE:
          params = new Class<?>[] { ((JPAEdmMapping) end.getEntityType().getMapping()).getJPAType() };
        default:
          break;
        }
      }
      return jpaEntity.getClass().getMethod(name,
          params);

    } catch (NoSuchMethodException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (SecurityException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (EdmException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    }

  }
}
