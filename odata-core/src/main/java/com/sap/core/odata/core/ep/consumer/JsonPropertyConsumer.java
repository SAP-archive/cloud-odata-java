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
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.core.ep.aggregator.EntityComplexPropertyInfo;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.util.FormatJson;

/**
 * @author SAP AG
 */
public class JsonPropertyConsumer {

  public Map<String, Object> readPropertyStandalone(final JsonReader reader, final EdmProperty property, final EntityProviderReadProperties readProperties) throws EntityProviderException {
    try {
      EntityPropertyInfo entityPropertyInfo = EntityInfoAggregator.create(property);
      Map<String, Object> typeMappings = readProperties == null ? null : readProperties.getTypeMappings();
      Map<String, Object> result = new HashMap<String, Object>();

      reader.beginObject();
      String nextName = reader.nextName();
      if (FormatJson.D.equals(nextName)) {
        reader.beginObject();
        nextName = reader.nextName();
        handleName(reader, typeMappings, entityPropertyInfo, result, nextName);
        reader.endObject();
      } else {
        handleName(reader, typeMappings, entityPropertyInfo, result, nextName);
      }
      reader.endObject();
      
      if(reader.peek() != JsonToken.END_DOCUMENT){
        //TODO: CA Messagetext
        throw new EntityProviderException(EntityProviderException.COMMON);
      }

      return result;
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(e.getMessage()), e);
    } catch (final IllegalStateException e) {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(e.getMessage()), e);
    }
  }

  private void handleName(final JsonReader reader, final Map<String, Object> typeMappings, final EntityPropertyInfo entityPropertyInfo, final Map<String, Object> result, final String nextName) throws EntityProviderException {
    Object mapping = null;
    if (typeMappings != null) {
      mapping = typeMappings.get(nextName);
    }
    Object propertyValue = readPropertyValue(reader, entityPropertyInfo, mapping);
    result.put(nextName, propertyValue);
  }

  protected Object readPropertyValue(final JsonReader reader, final EntityPropertyInfo entityPropertyInfo, final Object typeMapping) throws EntityProviderException {
    try {
      return entityPropertyInfo.isComplex() ?
          readComplexProperty(reader, (EntityComplexPropertyInfo) entityPropertyInfo, typeMapping) :
          readSimpleProperty(reader, entityPropertyInfo, typeMapping);
    } catch (final EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(e.getMessage()), e);
    }
  }

  private Object readSimpleProperty(final JsonReader reader, final EntityPropertyInfo entityPropertyInfo, final Object typeMapping) throws EdmException, EntityProviderException, IOException {
    final EdmSimpleType type = (EdmSimpleType) entityPropertyInfo.getType();
    Object value = null;
    final JsonToken tokenType = reader.peek();
    if (tokenType == JsonToken.NULL) {
      reader.nextNull();
    } else {
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
        if (tokenType == JsonToken.STRING) {
          value = reader.nextString();
        } else {
          throw new EntityProviderException(EntityProviderException.COMMON);
        }
        break;
      }
    }

    final Class<?> typeMappingClass = typeMapping == null ? type.getDefaultType() : (Class<?>) typeMapping;
    return type.valueOfString((String) value, EdmLiteralKind.JSON, entityPropertyInfo.getFacets(), typeMappingClass);
  }

  @SuppressWarnings("unchecked")
  private Object readComplexProperty(final JsonReader reader, final EntityComplexPropertyInfo complexPropertyInfo, final Object typeMapping) throws EdmException, EntityProviderException, IOException {
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

        Object childData = readPropertyValue(reader, childPropertyInfo, mapping.get(childName));
        if (data.containsKey(childName)) {
          throw new EntityProviderException(EntityProviderException.DOUBLE_PROPERTY.addContent(childName));
        }
        data.put(childName, childData);
      }
    }
    reader.endObject();
    return data;
  }
}
