package com.sap.core.odata.core.svc.parser;

import java.util.List;

/**
 * A Collection
 * <p>Collection element describes a Collection
 * @author SAP AG
 */
public interface Collection {
  /**
   * Get the human-readable title for the Collection
   * 
   * @return {@link Title}
   */
  public Title getTitle();

  /**
   * Get the "href" attribute, whose value gives IRI of the Collection
   * 
   * @return href as String
   */
  public String getHref();

  /**
   * Get common attributes
   * 
   * @return {@link CommonAttributes}
   */
  public CommonAttributes getCommonAttributes();

  /**
   * Get the media range that specifies a type of representation 
   * that can be POSTed to a Collection
   * @return a list of {@link Accept}
   */
  public List<Accept> getAcceptElements();

  /**
   *
   * 
   * @return a list of {@link Categories}
   */
  public List<Categories> getCategories();

  /**
   * Get the list of extension elements
   * 
   * @return a list of {@link ExtensionElement}
   */
  public List<ExtensionElement> getExtesionElements();

}
