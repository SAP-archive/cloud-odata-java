package com.sap.core.odata.core.svc.parser;

import java.util.List;

/**
 * A Workspace element
 * <p>Workspaces are server-defined groups of Collections. 
 * @author SAP AG
 */
public interface Workspace {
  /**
   * Get the human-readable title for the Workspace
   * 
   * @return {@link Title}
   */
  public Title getTitle();

  /**
   * Get the list of the Collections
   * 
   * @return a list of {@link Collection}
   */
  public List<Collection> getCollections();

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
