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
      jsonStreamWriter.beginObject()
          .name(FormatJson.D)
          .beginObject();

      jsonStreamWriter.name(propertyInfo.getName());
      appendPropertyValue(jsonStreamWriter, propertyInfo.isComplex() ? (EntityComplexPropertyInfo) propertyInfo : propertyInfo, value);

      jsonStreamWriter.endObject()
          .endObject();
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (final EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  protected static void appendPropertyValue(final JsonStreamWriter jsonStreamWriter, final EntityPropertyInfo propertyInfo, final Object value) throws IOException, EdmException, EntityProviderException {
    if (propertyInfo.isComplex()) {
      if (value == null || value instanceof Map<?, ?>) {
        jsonStreamWriter.beginObject();
        appendPropertyMetadata(jsonStreamWriter, propertyInfo.getType());
        for (final EntityPropertyInfo childPropertyInfo : ((EntityComplexPropertyInfo) propertyInfo).getPropertyInfos()) {
          jsonStreamWriter.separator();
          final String name = childPropertyInfo.getName();
          jsonStreamWriter.name(name);
          appendPropertyValue(jsonStreamWriter, childPropertyInfo, value == null ? null : ((Map<?, ?>) value).get(name));
        }
        jsonStreamWriter.endObject();
      } else {
        throw new EntityProviderException(EntityProviderException.ILLEGAL_ARGUMENT.addContent("A complex property must have a Map as data"));
      }
    } else {
      final EdmSimpleType type = (EdmSimpleType) propertyInfo.getType();
      final Object contentValue = value instanceof Map ? ((Map<?, ?>) value).get(propertyInfo.getName()) : value;
      final String valueAsString = type.valueToString(contentValue, EdmLiteralKind.JSON, propertyInfo.getFacets());
      switch (EdmSimpleTypeKind.valueOf(type.getName())) {
      case String:
        jsonStreamWriter.stringValue(valueAsString);
        break;
      case Boolean:
      case Byte:
      case SByte:
      case Int16:
      case Int32:
        jsonStreamWriter.unquotedValue(valueAsString);
        break;
      case DateTime:
      case DateTimeOffset:
        // Although JSON escaping is (and should be) done in the JSON
        // serializer, we backslash-escape the forward slash here explicitly
        // because it is not required to escape it in JSON but in OData.
        jsonStreamWriter.stringValueRaw(valueAsString == null ? null : valueAsString.replace("/", "\\/"));
        break;
      default:
        jsonStreamWriter.stringValueRaw(valueAsString);
        break;
      }
    }
  }

  protected static void appendPropertyMetadata(final JsonStreamWriter jsonStreamWriter, final EdmType type) throws IOException, EdmException {
    jsonStreamWriter.name(FormatJson.METADATA)
        .beginObject()
        .namedStringValueRaw(FormatJson.TYPE, type.getNamespace() + Edm.DELIMITER + type.getName())
        .endObject();
  }
}
