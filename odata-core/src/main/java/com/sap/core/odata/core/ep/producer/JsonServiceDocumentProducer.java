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

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.core.edm.provider.EdmImplProv;
import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * Writes the  OData service document in JSON.
 * @author SAP AG
 */
public class JsonServiceDocumentProducer {

  public static void writeServiceDocument(final Writer writer, final Edm edm) throws EntityProviderException {
    final EdmProvider edmProvider = ((EdmImplProv) edm).getEdmProvider();

    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
    try {
      jsonStreamWriter.beginObject();
      jsonStreamWriter.name(FormatJson.D);
      jsonStreamWriter.beginObject();
      jsonStreamWriter.name(FormatJson.ENTITY_SETS);
      jsonStreamWriter.beginArray();

      boolean first = true;
      if (edmProvider.getSchemas() != null) {
        for (final Schema schema : edmProvider.getSchemas()) {
          if (schema.getEntityContainers() != null) {
            for (final EntityContainer entityContainer : schema.getEntityContainers()) {
              for (final EntitySet entitySet : entityContainer.getEntitySets()) {
                if (first) {
                  first = false;
                } else {
                  jsonStreamWriter.separator();
                }
                jsonStreamWriter.stringValue((entityContainer.isDefaultEntityContainer() ?
                    "" : (entityContainer.getName() + Edm.DELIMITER)) + entitySet.getName());
              }
            }
          }
        }
      }
      jsonStreamWriter.endArray();
      jsonStreamWriter.endObject();
      jsonStreamWriter.endObject();
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (final ODataException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }

  }
}
