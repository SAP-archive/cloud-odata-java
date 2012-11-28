package com.sap.core.odata.ref.util;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectHelper {

  private static final Logger LOG = LoggerFactory.getLogger(ObjectHelper.class);
  
  private final Object object;
  private final Map<String, Field> fieldsToIds;
  private Map<String, Object> fieldId2Value;

  private ObjectHelper(Object object) {
    this.object = object;

    fieldsToIds = mapFieldsToIds(object.getClass());
    fieldId2Value = new HashMap<String, Object>();
  }

  public Class<?> getObjectClass() {
    return this.object.getClass();
  }

  private ObjectHelper init() {

    return this;
  }

  public static ObjectHelper init(Object object) {
    return new ObjectHelper(object).init();
  }

  public Map<String, Object> getFieldValues() throws IllegalAccessException {
    fieldId2Value = mapFieldValueToId(object, fieldsToIds);

    return Collections.unmodifiableMap(fieldId2Value);
  }

  public Map<String, Object> getFlatFieldValues() throws IllegalAccessException {
    Mapper<String, Object> mapper = new Mapper<String, Object>() {
      @Override
      public String createKey(String key) {
        return key.substring(0, 1).toUpperCase() + key.substring(1);
      }
      @Override
      public Object createValue(Object value) {
        if(value != null) {
          if(value.getClass().isPrimitive()) {
            return value;
          } else if(value instanceof Date) {
            return value;
          } else if(!ObjectHelper.isComplexType(value.getClass())) {
            return value;
          }
        }
        return String.valueOf(value);
      }
    };

    return mapToId(object, fieldsToIds, mapper);
  }
  
  private <T, K> Map<T, K> mapToId(Object object, Map<String, Field> fieldsToIds, Mapper<T, K> mapper) throws IllegalAccessException {

    Map<T, K> fieldId2FieldValue = new HashMap<T, K>();
    Set<Entry<String, Field>> entries = fieldsToIds.entrySet();

    for (Entry<String, Field> entry : entries) {
      Object value = getFieldValue(object, entry.getValue());
      T mappedKey = mapper.createKey(entry.getKey());
      K mappedValue = mapper.createValue(value);

      fieldId2FieldValue.put(mappedKey, mappedValue);
    }

    return fieldId2FieldValue;
  }

  
  private Object getFieldValue(final Object inInstance, final Field field) throws IllegalAccessException {
    final boolean accessible = field.isAccessible();
    if (!accessible) {
      field.setAccessible(true);
    }
    final Object value = field.get(inInstance);
    if (!accessible) {
      field.setAccessible(false);
    }
    return value;
  }

  private Map<String, Field> mapFieldsToIds(final Class<?> clazz) {
    return mapFieldsToIds(clazz, true);
  }

  private Map<String, Field> mapFieldsToIds(final Class<?> clazz, boolean inherited) {
    final Map<String, Field> id2Field = new HashMap<String, Field>();

    final Field[] allFields = clazz.getDeclaredFields();

    for (final Field field : allFields) {
      final String id = field.getName();
      id2Field.put(id, field);
    }

    Class<?> superClazz = clazz.getSuperclass();
    if (inherited && superClazz != Object.class) {
      Map<String, Field> inheritedFields = mapFieldsToIds(superClazz, inherited);
      id2Field.putAll(inheritedFields);
    }

    return id2Field;
  }

  private Map<String, Object> mapFieldValueToId(Object object, Map<String, Field> fieldsToIds) throws IllegalAccessException {

    Map<String, Object> fieldId2FieldValue = new HashMap<String, Object>();
    Set<Entry<String, Field>> entries = fieldsToIds.entrySet();

    for (Entry<String, Field> entry : entries) {
      Object value = getFieldValue(object, entry.getValue());
      if (value != null && isComplexType(entry.getValue().getType())) {
        value = ObjectHelper.init(value);
      }
      // else {
      // value = getFieldValue(object, entry.getValue());
      // }
      fieldId2FieldValue.put(entry.getKey(), value);
    }

    return fieldId2FieldValue;
  }

  private static boolean isComplexType(Class<?> type) {
    boolean isComplex = true;
    if (type == String.class || type == Integer.class || type == Boolean.class || type == Date.class || type == Float.class || type == Object.class
    // || Collections.class.isAssignableFrom(type)
    // || type.isArray()
        || type.isPrimitive()) {
      isComplex = false;
    }
//    LOG.debug("Class {} is complex? -> {}", type.getSimpleName(), isComplex);
    return isComplex;
  }

  @Override
  public String toString() {
    return this.object == null ? "NULL" : object.toString();
  }
}
