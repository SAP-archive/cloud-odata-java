package com.sap.core.odata.core.ep.consumer;

import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.ep.EntityProviderException;

public class XmlLinkConsumer {

  public String readLink(XMLStreamReader reader, EdmEntitySet entitySet) throws EntityProviderException {
    return null;
  }

  public List<String> readLinks(XMLStreamReader reader, EdmEntitySet entitySet) throws EntityProviderException {
    return Collections.emptyList();
  }
}
