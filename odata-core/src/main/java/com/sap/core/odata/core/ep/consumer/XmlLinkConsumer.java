package com.sap.core.odata.core.ep.consumer;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.ep.EntityProviderException;

public class XmlLinkConsumer {

  public Map<String, Object> readLink(XMLStreamReader reader, EdmEntitySet entitySet) throws EntityProviderException {
    return Collections.emptyMap();
  }

  public List<Map<String, Object>> readLinks(XMLStreamReader reader, EdmEntitySet entitySet) throws EntityProviderException {
    return Collections.emptyList();
  }
}
