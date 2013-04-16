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
import java.util.List;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * Provider for writing a collection of simple-type or complex-type instances in JSON.
 * @author SAP AG
 */
public class JsonCollectionEntityProducer {

  public void append(final Writer writer, final EntityPropertyInfo propertyInfo, final List<?> data) throws EntityProviderException {
    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);

    try {
      jsonStreamWriter.beginObject();
      jsonStreamWriter.name(FormatJson.D);
      jsonStreamWriter.beginObject();

      jsonStreamWriter.name(FormatJson.METADATA);
      jsonStreamWriter.beginObject();
      jsonStreamWriter.namedStringValueRaw(FormatJson.TYPE,
          "Collection(" + propertyInfo.getType().getNamespace() + Edm.DELIMITER + propertyInfo.getType().getName() + ")");
      jsonStreamWriter.endObject();
      jsonStreamWriter.separator();

      jsonStreamWriter.name(FormatJson.RESULTS);
      jsonStreamWriter.beginArray();
      boolean first = true;
      for (final Object item : data) {
        if (first) {
          first = false;
        } else {
          jsonStreamWriter.separator();
        }
        JsonPropertyEntityProducer.appendPropertyValue(jsonStreamWriter, propertyInfo, item);
      }
      jsonStreamWriter.endArray();

      jsonStreamWriter.endObject();
      jsonStreamWriter.endObject();
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (final EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }
}
