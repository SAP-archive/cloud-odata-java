package com.sap.core.odata.api.doc;

import java.util.List;

/**
 * A Categories element
 * <p>Categories element provides a list of the categories that can be applied to the members of a Collection
 * <p>If the "href" attribute is provided, the Categories element MUST be empty and MUST NOT have the "fixed" or "scheme" attributes
 * @author SAP AG
 */

public interface Categories {

  /**
   * Get the IRI reference identifying a Category Document
   * 
   * @return href as String
   */
  public String getHref();

  /**
   * Get the attribute fixed that indicates whether the list of categories is a fixed or an open set
   * 
   * @return {@link Fixed}
   */
  public Fixed getFixed();

  /**
   * Get the scheme
   * 
   * @return scheme as String
   */
  public String getScheme();

  /**
   * Get the list of categories
   * 
   * @return list of {@link Category}
   */
  public List<Category> getCategoryList();

}
