package com.sap.core.odata.core.debug;

import java.io.IOException;

import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * @author SAP AG
 */
public interface DebugInfo {

  public String getName();

  public void appendJson(JsonStreamWriter jsonStreamWriter) throws IOException;
}
