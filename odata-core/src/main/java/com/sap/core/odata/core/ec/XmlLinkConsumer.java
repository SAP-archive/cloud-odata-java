package com.sap.core.odata.core.ec;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.ec.EntityConsumerException;
import com.sap.core.odata.api.edm.EdmEntitySet;

public class XmlLinkConsumer {

  public Map<String, Object> readLink(XMLStreamReader reader, EdmEntitySet entitySet) throws EntityConsumerException {
    return Collections.emptyMap();
  }

  public List<Map<String, Object>> readLinks(XMLStreamReader reader, EdmEntitySet entitySet) throws EntityConsumerException {
    return Collections.emptyList();
  }
}
