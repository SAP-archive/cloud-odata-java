package com.sap.core.odata.core.ep.producer;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.core.ep.aggregator.EntityComplexPropertyInfo;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * Producer for writing a single simple or complex property in JSON, also usable
 * for function imports returning a single instance of a simple or complex type.
 * @author SAP AG
 */
public class JsonPropertyEntityProducer {

  public void append(final Writer writer, final EntityPropertyInfo propertyInfo, final Object value) throws EntityProviderException {
    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);

    try {
      jsonStreamWriter.beginObject();
      jsonStreamWriter.name(FormatJson.D);
      jsonStreamWriter.beginObject();

      jsonStreamWriter.name(propertyInfo.getName());
      appendPropertyValue(jsonStreamWriter, propertyInfo.isComplex() ? (EntityComplexPropertyInfo) propertyInfo : propertyInfo, value);

      jsonStreamWriter.endObject();
      jsonStreamWriter.endObject();
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (final EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  protected static void appendPropertyValue(final JsonStreamWriter jsonStreamWriter, final EntityPropertyInfo propertyInfo, final Object value) throws IOException, EdmException {
    if (propertyInfo.isComplex()) {
      jsonStreamWriter.beginObject();
      appendPropertyMetadata(jsonStreamWriter, propertyInfo.getType());
      for (final EntityPropertyInfo childPropertyInfo : ((EntityComplexPropertyInfo) propertyInfo).getPropertyInfos()) {
        jsonStreamWriter.separator();
        final String name = childPropertyInfo.getName();
        jsonStreamWriter.name(name);
        appendPropertyValue(jsonStreamWriter, childPropertyInfo,
            value instanceof Map ? ((Map<?, ?>) value).get(name) : value);
      }
      jsonStreamWriter.endObject();
    } else {
      final EdmSimpleType type = (EdmSimpleType) propertyInfo.getType();
      final String valueAsString = type.valueToString(value, EdmLiteralKind.JSON, propertyInfo.getFacets());
      if (type == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()) {
        jsonStreamWriter.stringValue(valueAsString);
      } else if (type == EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance()
          || type == EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance()
          || type == EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance()
          || type == EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance()
          || type == EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance()) {
        jsonStreamWriter.unquotedValue(valueAsString);
      } else {
        jsonStreamWriter.stringValueRaw(valueAsString);
      }
    }
  }

  protected static void appendPropertyMetadata(final JsonStreamWriter jsonStreamWriter, final EdmType type) throws IOException, EdmException {
    jsonStreamWriter.name(FormatJson.METADATA);
    jsonStreamWriter.beginObject();
    jsonStreamWriter.namedStringValueRaw(FormatJson.TYPE, type.getNamespace() + Edm.DELIMITER + type.getName());
    jsonStreamWriter.endObject();
  }
}
