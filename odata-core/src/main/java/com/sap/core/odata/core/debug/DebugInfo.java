package com.sap.core.odata.core.debug;

import java.io.IOException;

import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * @author SAP AG
 */
public interface DebugInfo {

  /**
   * Gets the name of this debug information part, useful as title.
   * @return the name
   */
  public String getName();

  /**
   * Appends the content of this debug information part
   * to the given JSON stream writer.
   * @param jsonStreamWriter a JSON stream writer
   */
  public void appendJson(JsonStreamWriter jsonStreamWriter) throws IOException;
}
