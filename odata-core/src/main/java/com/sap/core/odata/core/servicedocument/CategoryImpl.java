package com.sap.core.odata.core.servicedocument;

import com.sap.core.odata.api.servicedocument.Category;
import com.sap.core.odata.api.servicedocument.CommonAttributes;

/**
 * @author SAP AG
 */
public class CategoryImpl implements Category {
  private String scheme;
  private String term;
  private CommonAttributes commonAttributes;
  private String label;

  @Override
  public String getScheme() {
    return scheme;
  }

  @Override
  public String getTerm() {
    return term;
  }

  @Override
  public CommonAttributes getCommonAttributes() {
    return commonAttributes;
  }

  @Override
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
