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
      JsonStreamWriter.beginObject(writer);
      JsonStreamWriter.name(writer, FormatJson.D);
      JsonStreamWriter.beginObject(writer);

      appendProperty(writer, propertyInfo.isComplex() ? (EntityComplexPropertyInfo) propertyInfo : propertyInfo, value);

      JsonStreamWriter.endObject(writer);
      JsonStreamWriter.endObject(writer);
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  protected static void appendProperty(Writer writer, final EntityPropertyInfo propertyInfo, final Object value) throws IOException, EdmException {
    if (propertyInfo.isComplex()) {
      JsonStreamWriter.name(writer, propertyInfo.getName());
      JsonStreamWriter.beginObject(writer);
      JsonStreamWriter.name(writer, FormatJson.METADATA);
      JsonStreamWriter.beginObject(writer);
      JsonStreamWriter.namedStringValueRaw(writer, FormatJson.TYPE,
          propertyInfo.getType().getNamespace() + Edm.DELIMITER + propertyInfo.getType().getName());
      JsonStreamWriter.endObject(writer);
      for (final EntityPropertyInfo childPropertyInfo : ((EntityComplexPropertyInfo) propertyInfo).getPropertyInfos()) {
        JsonStreamWriter.separator(writer);
        appendProperty(writer, childPropertyInfo,
            value instanceof Map ? ((Map<?, ?>) value).get(childPropertyInfo.getName()) : value);
      }
      JsonStreamWriter.endObject(writer);
    } else {
      final EdmSimpleType type = (EdmSimpleType) propertyInfo.getType();
      final String valueAsString = type.valueToString(value, EdmLiteralKind.JSON, propertyInfo.getFacets());
      if (type == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance())
        JsonStreamWriter.namedStringValue(writer, propertyInfo.getName(), valueAsString);
      else if (type == EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance()
          || type == EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance()
          || type == EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance()
          || type == EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance()
          || type == EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance())
        JsonStreamWriter.namedValue(writer, propertyInfo.getName(), valueAsString);
      else
        JsonStreamWriter.namedStringValueRaw(writer, propertyInfo.getName(), valueAsString);
    }
  }
}
