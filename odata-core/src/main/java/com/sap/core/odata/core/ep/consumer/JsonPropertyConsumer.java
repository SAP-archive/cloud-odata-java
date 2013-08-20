package com.sap.core.odata.core.ep.consumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.sap.core.odata.api.edm.Edm;
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

      if (reader.peek() != JsonToken.END_DOCUMENT) {
        throw new EntityProviderException(EntityProviderException.END_DOCUMENT_EXPECTED.addContent(reader.peek().toString()));
      }

      return result;
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.EXCEPTION_OCCURRED.addContent(e.getClass().getSimpleName()), e);
    } catch (final IllegalStateException e) {
      throw new EntityProviderException(EntityProviderException.EXCEPTION_OCCURRED.addContent(e.getClass().getSimpleName()), e);
    }
  }

  private void handleName(final JsonReader reader, final Map<String, Object> typeMappings, final EntityPropertyInfo entityPropertyInfo, final Map<String, Object> result, final String nextName) throws EntityProviderException {
    if (!entityPropertyInfo.getName().equals(nextName)) {
      throw new EntityProviderException(EntityProviderException.ILLEGAL_ARGUMENT.addContent(nextName));
    }
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
      throw new EntityProviderException(EntityProviderException.EXCEPTION_OCCURRED.addContent(e.getClass().getSimpleName()), e);
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.EXCEPTION_OCCURRED.addContent(e.getClass().getSimpleName()), e);
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
          throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY_VALUE.addContent(entityPropertyInfo.getName()));
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
          throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY_VALUE.addContent(entityPropertyInfo.getName()));
        }
        break;
      default:
        if (tokenType == JsonToken.STRING) {
          value = reader.nextString();
        } else {
          throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY_VALUE.addContent(entityPropertyInfo.getName()));
        }
        break;
      }
    }

    final Class<?> typeMappingClass = typeMapping == null ? type.getDefaultType() : (Class<?>) typeMapping;
    return type.valueOfString((String) value, EdmLiteralKind.JSON, entityPropertyInfo.getFacets(), typeMappingClass);
  }

  @SuppressWarnings("unchecked")
  private Object readComplexProperty(final JsonReader reader, final EntityComplexPropertyInfo complexPropertyInfo, final Object typeMapping) throws EdmException, EntityProviderException, IOException {
    if (reader.peek().equals(JsonToken.NULL)) {
      reader.nextNull();
      if (complexPropertyInfo.isMandatory()) {
        throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY_VALUE.addContent(complexPropertyInfo.getName()));
      }
      return null;
    }

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
        String actualTypeName = reader.nextString();
        String expectedTypeName = complexPropertyInfo.getType().getNamespace() + Edm.DELIMITER + complexPropertyInfo.getType().getName();
        if (!expectedTypeName.equals(actualTypeName)) {
          throw new EntityProviderException(EntityProviderException.INVALID_ENTITYTYPE.addContent(expectedTypeName).addContent(actualTypeName));
        }
        reader.endObject();
      } else {
        EntityPropertyInfo childPropertyInfo = complexPropertyInfo.getPropertyInfo(childName);
        if (childPropertyInfo == null) {
          throw new EntityProviderException(EntityProviderException.INVALID_PROPERTY.addContent(childName));
        }
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
