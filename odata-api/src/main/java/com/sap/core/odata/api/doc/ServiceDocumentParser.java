package com.sap.core.odata.api.doc;

import java.io.InputStream;

public interface ServiceDocumentParser {
  public ServiceDocument parseXml(InputStream in) throws ServiceDocumentParserException;
}
