package com.sap.core.odata.core.ep.producer;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
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

  public void append(Writer writer, final EntityPropertyInfo propertyInfo, final Object value) throws EntityProviderException {
    try {
      JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);

      jsonStreamWriter.beginObject();
      jsonStreamWriter.name(FormatJson.D);
      jsonStreamWriter.beginObject();

      appendProperty(writer, propertyInfo.isComplex() ? (EntityComplexPropertyInfo) propertyInfo : propertyInfo, value);

      jsonStreamWriter.endObject();
      jsonStreamWriter.endObject();
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (final EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  protected static void appendProperty(final Writer writer, final EntityPropertyInfo propertyInfo, final Object value) throws IOException, EdmException {
    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);

    if (propertyInfo.isComplex()) {
      jsonStreamWriter.name(propertyInfo.getName());
      jsonStreamWriter.beginObject();
      jsonStreamWriter.name(FormatJson.METADATA);
      jsonStreamWriter.beginObject();
      jsonStreamWriter.namedStringValueRaw(FormatJson.TYPE,
          propertyInfo.getType().getNamespace() + Edm.DELIMITER + propertyInfo.getType().getName());
      jsonStreamWriter.endObject();
      for (final EntityPropertyInfo childPropertyInfo : ((EntityComplexPropertyInfo) propertyInfo).getPropertyInfos()) {
        jsonStreamWriter.separator();
        appendProperty(writer, childPropertyInfo,
            value instanceof Map ? ((Map<?, ?>) value).get(childPropertyInfo.getName()) : value);
      }
      jsonStreamWriter.endObject();
    } else {
      final EdmSimpleType type = (EdmSimpleType) propertyInfo.getType();
      final String valueAsString = type.valueToString(value, EdmLiteralKind.JSON, propertyInfo.getFacets());
      if (type == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()) {
        jsonStreamWriter.namedStringValue(propertyInfo.getName(), valueAsString);
      } else if (type == EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance()
          || type == EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance()
          || type == EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance()
          || type == EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance()
          || type == EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance()) {
        jsonStreamWriter.namedValue(propertyInfo.getName(), valueAsString);
      } else {
        jsonStreamWriter.namedStringValueRaw(propertyInfo.getName(), valueAsString);
      }
    }
  }
}
