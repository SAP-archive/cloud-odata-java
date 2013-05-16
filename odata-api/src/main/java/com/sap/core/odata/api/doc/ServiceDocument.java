package com.sap.core.odata.api.doc;

import java.util.List;

/**
 * A Service element
 * <p>Service element is the container for service information associated with one or more Workspace. 
 * @author SAP AG
 */
public interface ServiceDocument {

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
