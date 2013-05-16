package com.sap.core.odata.core.doc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.stream.JsonReader;
import com.sap.core.odata.api.doc.ServiceDocumentParserException;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.core.ep.util.FormatJson;

public class JsonServiceDocumentParser {
  private static final String DEFAULT_CHARSET = "UTF-8";
  List<EntitySet> entitySets = new ArrayList<EntitySet>();
  private String currentHandledObjectName;

  public List<EntitySet> parseJson(final InputStream in) throws ServiceDocumentParserException, UnsupportedEncodingException {
    return readServiceDocument(createJsonReader(in));
  }

  private List<EntitySet> readServiceDocument(final JsonReader reader) throws ServiceDocumentParserException {
    try {
      reader.beginObject();
      currentHandledObjectName = reader.nextName();
      if (FormatJson.D.equals(currentHandledObjectName)) {
        reader.beginObject();
        readContent(reader);
        reader.endObject();
      }
      reader.endObject();

    } catch (IOException e) {
      throw new ServiceDocumentParserException("");
    } catch (IllegalStateException e) {
      throw new ServiceDocumentParserException("The structure of the service document is not valid");
    }
    return entitySets;
  }

  private void readContent(final JsonReader reader) throws IOException, ServiceDocumentParserException {
    currentHandledObjectName = reader.nextName();
    if ("EntitySets".equals(currentHandledObjectName)) {
      reader.beginArray();
      readEntitySets(reader);
      reader.endArray();
    }
  }

  private void readEntitySets(final JsonReader reader) throws IOException {
    while (reader.hasNext()) {
      currentHandledObjectName = reader.nextString();
      if (currentHandledObjectName != null) {
        entitySets.add(new EntitySet().setName(currentHandledObjectName));
      }
    }
  }

  private JsonReader createJsonReader(final InputStream in) throws ServiceDocumentParserException, UnsupportedEncodingException {
    if (in == null) {
      throw new ServiceDocumentParserException("Got not supported NULL object as content to de-serialize.");
    }
    return new JsonReader(new InputStreamReader(in, DEFAULT_CHARSET));
  }
}
