package com.sap.core.odata.core.servicedocument;

import com.sap.core.odata.api.servicedocument.Category;
import com.sap.core.odata.api.servicedocument.CommonAttributes;

/**
 * CategoryImpl
 * <p>The implementiation of the interface Category
 * @author SAP AG
 */
public class CategoryImpl implements Category {
  private String scheme;
  private String term;
  private CommonAttributes commonAttributes;
  private String label;

  @Override
  /**
   * {@inherit}
   */
  public String getScheme() {
    return scheme;
  }

  @Override
  /**
   * {@inherit}
   */
  public String getTerm() {
    return term;
  }

  @Override
  /**
   * {@inherit}
   */
  public CommonAttributes getCommonAttributes() {
    return commonAttributes;
  }

  @Override
  /**
   * {@inherit}
   */
  public String getLabel() {
    return label;
  }

  public CategoryImpl setScheme(final String scheme) {
    this.scheme = scheme;
    return this;
  }

  public CategoryImpl setTerm(final String term) {
    this.term = term;
    return this;
  }

  public CategoryImpl setCommonAttributes(final CommonAttributes commonAttribute) {
    commonAttributes = commonAttribute;
    return this;
  }

  public CategoryImpl setLabel(final String label) {
    this.label = label;
    return this;
  }
}
