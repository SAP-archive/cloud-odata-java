/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.ep.aggregator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.sap.core.odata.api.ep.EntityProviderException;

/**
 * 
 * 
 * @author SAP AG
 */
public class EntityTypeMapping {
  private static final EntityTypeMapping ENTITY_TYPE_MAPPING = new EntityTypeMapping();
  final String propertyName;
  final Class<?> mapping;
  final List<EntityTypeMapping> mappings;

  private EntityTypeMapping() {
    this(null, Object.class);
  }

  private EntityTypeMapping(final String name, final Class<?> mappingClass) {
    propertyName = name;
    mapping = mappingClass;
    mappings = Collections.emptyList();
  }

  private EntityTypeMapping(final String name, final List<EntityTypeMapping> typeMappings) {
    propertyName = name;
    mapping = EntityTypeMapping.class;
    List<EntityTypeMapping> tmp = new ArrayList<EntityTypeMapping>();
    for (EntityTypeMapping typeMapping : typeMappings) {
      tmp.add(typeMapping);
    }
    mappings = Collections.unmodifiableList(tmp);
  }

  public static EntityTypeMapping create(final Map<String, Object> mappings) throws EntityProviderException {
    return create(null, mappings);
  }

  @SuppressWarnings("unchecked")
  public static EntityTypeMapping create(final String name, final Map<String, Object> mappings) throws EntityProviderException {
    if (mappings == null) {
      return ENTITY_TYPE_MAPPING;
    }
    List<EntityTypeMapping> typeMappings = new ArrayList<EntityTypeMapping>();
    Set<Entry<String, Object>> entries = mappings.entrySet();
    for (Entry<String, Object> entry : entries) {
      EntityTypeMapping typeMapping;
      Object value = entry.getValue();
      if (value instanceof Map) {
        typeMapping = create(entry.getKey(), (Map<String, Object>) value);
      } else if (value instanceof Class) {
        typeMapping = new EntityTypeMapping(entry.getKey(), (Class<?>) value);
      } else {
        throw new EntityProviderException(EntityProviderException.ILLEGAL_ARGUMENT.addContent("Got invalid mapping value."));
      }
      typeMappings.add(typeMapping);
    }

    return new EntityTypeMapping(name, typeMappings);
  }

  boolean isComplex() {
    return mappings != null && mapping == EntityTypeMapping.class;
  }

  /**
   * If this {@link EntityTypeMapping} is complex ({@link #isComplex()} the mapping for the property
   * with given <code>name</code> is returned.
   * If it is not complex an empty {@link EntityTypeMapping} is returned.
   * 
   * @param name
   * @return
   */
  public EntityTypeMapping getEntityTypeMapping(final String name) {
    if (isComplex()) {
      for (EntityTypeMapping mapping : mappings) {
        if (mapping.propertyName.equals(name)) {
          return mapping;
        }
      }
    }
    return ENTITY_TYPE_MAPPING;
  }

  /**
   * If this {@link EntityTypeMapping} is complex ({@link #isComplex()} the mapping {@link Class} for the property
   * with given <code>name</code> is returned.
   * If it is not complex <code>NULL</code> is returned.
   * 
   * @param name
   * @return mapping {@link Class} for the property with given <code>name</code> or <code>NULL</code>.
   */
  public Class<?> getMappingClass(final String name) {
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
      return "{'" + propertyName + "'->" + mappings.toString() + "}";
    }
    return "{'" + propertyName + "' as " + (mapping == null ? "NULL" : mapping.getSimpleName()) + "}";
  }

}
