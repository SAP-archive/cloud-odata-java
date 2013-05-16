package com.sap.core.odata.api.doc;

import com.sap.core.odata.api.rt.RuntimeDelegate;

public class ServiceDocumentParserFactory {

  public static ServiceDocumentParser create() {
    return RuntimeDelegate.createServiceDocumentParser();
  }
}
