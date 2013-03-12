package com.sap.core.odata.core.ep.aggregator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.sap.core.odata.api.ep.EntityProviderException;

public class EntityTypeMapping {
  public static EntityTypeMapping create(Map<String, Object> mappings) throws EntityProviderException {
    return create(null, mappings);
  }

  public static EntityTypeMapping create(String name, Map<String, Object> mappings) throws EntityProviderException {
    if (mappings == null) {
      return new EntityTypeMapping();
    }
    List<EntityTypeMapping> typeMappings = new ArrayList<EntityTypeMapping>();
    Set<Entry<String, Object>> entries = mappings.entrySet();
    for (Entry<String, Object> entry : entries) {
      EntityTypeMapping typeMapping;
      Object value = entry.getValue();
      if (value instanceof Map) {
        typeMapping = create(entry.getKey(), (Map) value);
      } else if (value instanceof Class) {
        typeMapping = new EntityTypeMapping(entry.getKey(), (Class<?>) value);
      } else {
        throw new EntityProviderException(EntityProviderException.COMMON);
      }
      typeMappings.add(typeMapping);
    }

    return new EntityTypeMapping(name, typeMappings);
  }

  final String propertyName;
  final Class<?> mapping;
  final List<EntityTypeMapping> mappings;

  private EntityTypeMapping() {
    this(null, Object.class);
  }

  private EntityTypeMapping(String name, Class<?> mappingClass) {
    propertyName = name;
    mapping = mappingClass;
    mappings = Collections.emptyList();
  }

  private EntityTypeMapping(String name, List<EntityTypeMapping> typeMappings) {
    propertyName = name;
    mapping = EntityTypeMapping.class;
    List<EntityTypeMapping> tmp = new ArrayList<EntityTypeMapping>();
    for (EntityTypeMapping typeMapping : typeMappings) {
      tmp.add(typeMapping);
    }
    mappings = Collections.unmodifiableList(tmp);
  }

  boolean isComplex() {
    return mappings != null && mapping == EntityTypeMapping.class;
  }

  public EntityTypeMapping getEntityTypeMapping(String name) {
    if (isComplex()) {
      for (EntityTypeMapping mapping : mappings) {
        if (mapping.propertyName.equals(name)) {
          return mapping;
        }
      }
    }
    return new EntityTypeMapping();
  }

  public Class<?> getMappingClass(String name) {
    if (isComplex()) {
      for (EntityTypeMapping mapping : mappings) {
        if (mapping.propertyName.equals(name)) {
          return mapping.mapping;
        }
      }
    }
    return null;
  }

  @Override
  public String toString() {
    if (isComplex()) {
      return "Name: " + propertyName + " " + mappings.toString();
    }
    return "Name: " + propertyName + " MappedClass: " + mapping;
  }

}
