package com.sap.core.odata.core.svc.parser;

/**
 * CategoryImpl
 * <p>The implementiation of the interface Category
 * @author SAP AG
 */
public class CategoryImpl {
  private String scheme;
  private String term;
  private CommonAttributesImpl commonAttribute;
  private String label;

  public String getScheme() {
    return scheme;
  }

  public String getTerm() {
    return term;
  }

  public CommonAttributesImpl getCommonAttribute() {
    return commonAttribute;
  }

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

  public CategoryImpl setCommonAttribute(final CommonAttributesImpl commonAttribute) {
    this.commonAttribute = commonAttribute;
    return this;
  }

  public CategoryImpl setLabel(final String label) {
    this.label = label;
    return this;
  }
}
