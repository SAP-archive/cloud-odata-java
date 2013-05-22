package com.sap.core.odata.api.servicedocument;

import java.util.List;

/**
 * A AtomInfo
 * <p>AtomInfo represents the structure of Service Document according RFC 5023 (for ATOM format) 
 * @author SAP AG
 */
public interface AtomInfo {

  /**
   * Get the list of workspaces
   * 
   * @return a list of {@link Workspace}
   */
  public List<Workspace> getWorkspaces();

  /**
   * Get common attributes
   * 
   * @return {@link CommonAttributes}
   */
  public CommonAttributes getCommonAttributes();

  /**
   * Get the list of extension elements
   * 
   * @return a list of {@link ExtensionElement}
   */
  public List<ExtensionElement> getExtesionElements();
}
