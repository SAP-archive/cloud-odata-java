package com.sap.core.odata.core.ep.consumer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.stream.JsonReader;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySetInfo;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.core.edm.provider.EdmEntitySetInfoImplProv;
import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.servicedocument.ServiceDocumentImpl;

/**
 * Reads the OData service document (JSON).
 * @author SAP AG
 */
public class JsonServiceDocumentConsumer {
  private static final String DEFAULT_CHARSET = "UTF-8";
  List<EdmEntitySetInfo> entitySets = new ArrayList<EdmEntitySetInfo>();
  private String currentHandledObjectName;

  public ServiceDocumentImpl parseJson(final InputStream in) throws EntityProviderException {
    return readServiceDocument(createJsonReader(in));
  }

  private ServiceDocumentImpl readServiceDocument(final JsonReader reader) throws EntityProviderException {
    try {
      reader.beginObject();
      currentHandledObjectName = reader.nextName();
      if (FormatJson.D.equals(currentHandledObjectName)) {
        reader.beginObject();
        readContent(reader);
        reader.endObject();
      }
      reader.endObject();
      reader.peek();
      reader.close();
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (final IllegalStateException e) {
      throw new EntityProviderException(EntityProviderException.COMMON.addContent("The structure of the service document is not valid"), e);
    } catch (final EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (final ODataException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
    return new ServiceDocumentImpl().setEntitySetsInfo(entitySets);
  }

  private void readContent(final JsonReader reader) throws IOException, EdmException, EntityProviderException {
    currentHandledObjectName = reader.nextName();
    if (FormatJson.ENTITY_SETS.equals(currentHandledObjectName)) {
      reader.beginArray();
      readEntitySets(reader);
      reader.endArray();
    }
  }

  private void readEntitySets(final JsonReader reader) throws IOException, EntityProviderException, EdmException {
    while (reader.hasNext()) {
      currentHandledObjectName = reader.nextString();
      if (currentHandledObjectName != null) {
        // Looking for the last dot: "\\.(?=[^.]+$)"
        String[] names = currentHandledObjectName.split("\\" + Edm.DELIMITER + "(?=[^" + Edm.DELIMITER + "]+$)");
        if (names.length == 1) {
          EntitySet entitySet = new EntitySet().setName(names[0]);
          EntityContainerInfo container = new EntityContainerInfo().setDefaultEntityContainer(true);
          EdmEntitySetInfo entitySetInfo = new EdmEntitySetInfoImplProv(entitySet, container);
          entitySets.add(entitySetInfo);
        } else {
          EntitySet entitySet = new EntitySet().setName(names[1]);
          EntityContainerInfo container = new EntityContainerInfo().setName(names[0]).setDefaultEntityContainer(false);
          EdmEntitySetInfo entitySetInfo = new EdmEntitySetInfoImplProv(entitySet, container);
          entitySets.add(entitySetInfo);
        }
      }
    }
  }

  private JsonReader createJsonReader(final InputStream in) throws EntityProviderException {
    if (in == null) {
      throw new EntityProviderException(EntityProviderException.COMMON.addContent(("Got not supported NULL object as content to de-serialize.")));
    }
    InputStreamReader isReader;
    try {
      isReader = new InputStreamReader(in, DEFAULT_CHARSET);
    } catch (UnsupportedEncodingException e) {
      throw new EntityProviderException(EntityProviderException.COMMON.addContent(e));
    }
    return new JsonReader(isReader);
  }
}
