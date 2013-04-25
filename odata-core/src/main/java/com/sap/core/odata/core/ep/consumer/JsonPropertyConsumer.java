package com.sap.core.odata.core.ep.consumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.core.ep.aggregator.EntityComplexPropertyInfo;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.ep.util.JsonUtils;

/**
 * @author SAP AG
 */
public class JsonPropertyConsumer {

  private int openJsonObjects;

  public Map<String, Object> readPropertyStandalone(JsonReader reader, EdmProperty edmProperty) throws EntityProviderException {
    return readPropertyStandalone(reader, edmProperty, null);
  }

  public Map<String, Object> readPropertyStandalone(JsonReader reader, EdmProperty edmProperty, Map<String, Object> typeMappings) throws EntityProviderException {
    try {

      openJsonObjects = JsonUtils.startJson(reader);

      EntityPropertyInfo entityPropertyInfo = EntityInfoAggregator.create(edmProperty);

      String propertyName = reader.nextName();

      Object mapping = null;
      if (typeMappings != null) {
        mapping = typeMappings.get(propertyName);
      }
      Object propertyValue = readProperty(reader, entityPropertyInfo, mapping);

      JsonUtils.endJson(reader, openJsonObjects);

      Map<String, Object> result = new HashMap<String, Object>();
      result.put(propertyName, propertyValue);
      return result;
    } catch (IOException e) {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(e.getMessage()), e);
    }
  }

  public Object readProperty(JsonReader reader, EntityPropertyInfo entityPropertyInfo, Object typeMapping) throws EntityProviderException {
    try {
      Object value = null;
      if (entityPropertyInfo.isComplex()) {
        value = readComplexProperty(reader, (EntityComplexPropertyInfo) entityPropertyInfo, typeMapping);
      } else {
        value = readSimpleProperty(reader, entityPropertyInfo, typeMapping);
      }
      return value;
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (IOException e) {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(e.getMessage()), e);
    }
  }

  private Object readSimpleProperty(JsonReader reader, EntityPropertyInfo entityPropertyInfo, Object typeMapping) throws EdmException, EntityProviderException, IOException {
    final EdmSimpleType type = (EdmSimpleType) entityPropertyInfo.getType();
    Object value = null;
    final JsonToken tokenType = reader.peek();
    if (tokenType == JsonToken.NULL)
      reader.nextNull();
    else
      switch (EdmSimpleTypeKind.valueOf(type.getName())) {
      case Boolean:
        if (tokenType == JsonToken.BOOLEAN) {
          value = reader.nextBoolean();
          value = value.toString();
        } else {
          throw new EntityProviderException(EntityProviderException.COMMON);
        }
        break;
      case Byte:
      case SByte:
      case Int16:
      case Int32:
        if (tokenType == JsonToken.NUMBER) {
          value = reader.nextInt();
          value = value.toString();
        } else {
          throw new EntityProviderException(EntityProviderException.COMMON);
        }
        break;
      default:
        if (tokenType == JsonToken.STRING)
          value = reader.nextString();
        else
          throw new EntityProviderException(EntityProviderException.COMMON);
        break;
      }

    final Class<?> typeMappingClass = typeMapping == null ? type.getDefaultType() : (Class<?>) typeMapping;

    return type.valueOfString((String) value, EdmLiteralKind.JSON, entityPropertyInfo.getFacets(), typeMappingClass);
  }

  @SuppressWarnings("unchecked")
  private Object readComplexProperty(JsonReader reader, EntityComplexPropertyInfo complexPropertyInfo, Object typeMapping) throws EdmException, EntityProviderException, IOException {
    reader.beginObject();
    Map<String, Object> data = new HashMap<String, Object>();

    Map<String, Object> mapping;
    if (typeMapping != null) {
      if (typeMapping instanceof Map) {
        mapping = (Map<String, Object>) typeMapping;
      } else {
        throw new EntityProviderException(EntityProviderException.INVALID_MAPPING.addContent(complexPropertyInfo.getName()));
      }
    } else {
      mapping = new HashMap<String, Object>();
    }

    while (reader.hasNext()) {
      String childName = reader.nextName();
      if (FormatJson.METADATA.equals(childName)) {
        reader.beginObject();
        childName = reader.nextName();
        if (!FormatJson.TYPE.equals(childName)) {
          throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE.addContent(FormatJson.TYPE).addContent(FormatJson.METADATA));
        }
        reader.nextString();
        reader.endObject();
      } else {
        EntityPropertyInfo childPropertyInfo = complexPropertyInfo.getPropertyInfo(childName);
        if (childPropertyInfo.isComplex()) {
          Object childData = readComplexProperty(reader, (EntityComplexPropertyInfo) childPropertyInfo, mapping.get(childName));
          data.put(childName, childData);
        } else {
          Object childData = readSimpleProperty(reader, childPropertyInfo, mapping.get(childName));
          data.put(childName, childData);
        }
      }
    }
    reader.endObject();
    return data;
  }
}
