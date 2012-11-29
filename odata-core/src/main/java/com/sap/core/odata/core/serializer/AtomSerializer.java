package com.sap.core.odata.core.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.serialization.ODataSerializationException;
import com.sap.core.odata.api.serialization.ODataSerializer;
import com.sap.core.odata.api.serialization.ODataSerializerProperties;

public class AtomSerializer extends ODataSerializer {

  private static final Logger LOG = LoggerFactory.getLogger(AtomSerializer.class);

  AtomSerializer(ODataSerializerProperties properties) throws ODataSerializationException {
    super(properties);
  }

  @Override
  public InputStream serialize() throws ODataSerializationException {
    try {
      LOG.debug("Start serialization...");
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      this.serializeInto(out);

      out.flush();
      out.close();
      LOG.debug("...finished serialization.");
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON);
    }
  }

  @Override
  public void serializeInto(OutputStream stream) throws ODataSerializationException {
    try {
      EdmEntitySet ees = getEdmEntitySet();
      EdmEntityType eet = ees.getEntityType();

      ODataSerializer serializer = createSerializerFor(eet);
      LOG.debug("Use serializer class {}", serializer.getClass());
      serializer.serializeInto(stream);
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON);
    }
  }

  private ODataSerializer createSerializerFor(EdmEntityType entityType) throws ODataSerializationException, EdmException {

    if (isMultiValueMap(getData())) {
      return new AtomFeedSerializer(this.properties);
    }
    return new AtomEntrySerializer(properties);
  }

  private boolean isMultiValueMap(Map<String, Object> data) {
    if (data == null || data.isEmpty()) {
      return false;
    }
    Object value = data.values().iterator().next();
    return (value instanceof Map<?, ?>);
  }
}
