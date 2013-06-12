package com.sap.core.odata.api.servicedocument;

import java.io.InputStream;

/**
 * @author SAP AG
 */
public interface ServiceDocumentParser {
  public ServiceDocument parseXml(InputStream in) throws ServiceDocumentParserException;
}
