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
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.serialization.ODataSerializationException;
import com.sap.core.odata.api.serialization.ODataSerializer;

public class AtomSerializer extends ODataSerializer {

  private static final Logger LOG = LoggerFactory.getLogger(AtomSerializer.class);

  AtomSerializer(ODataContext ctx) throws ODataSerializationException {
    super(ctx);
  }

  @Override
  public InputStream serializeEntry(EdmEntitySet entitySet, Map<String, Object> data) throws ODataSerializationException {
    try {
      LOG.debug("Start serialization...");
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      this.serializeInto(out, entitySet, data);

      out.flush();
      out.close();
      LOG.debug("...finished serialization.");
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON);
    }
  }

  public void serializeInto(OutputStream stream, EdmEntitySet entitySet, Map<String, Object> data) throws ODataSerializationException {
    try {
      AtomEntrySerializer serializer = new AtomEntrySerializer(this.getContext());
      LOG.debug("Use serializer class {}", serializer.getClass());
      serializer.serializeInto(stream, entitySet, data);
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON);
    }
  }

  private boolean isMultiValueMap(Map<String, Object> data) {
    if (data == null || data.isEmpty()) {
      return false;
    }
    Object value = data.values().iterator().next();
    return (value instanceof Map<?, ?>);
  }
}
