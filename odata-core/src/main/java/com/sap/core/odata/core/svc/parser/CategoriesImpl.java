package com.sap.core.odata.core.svc.parser;

import java.util.List;

/**
 * CategoriesImpl
 * <p>The implementiation of the interface Categories
 * @author SAP AG
 */
public class CategoriesImpl {
  private String href;
  private Fixed fixed;
  private String scheme;
  private List<CategoryImpl> categoryList;

  public String getHref() {
    return href;
  }

  public Fixed getFixed() {
    return fixed;
  }

  public String getScheme() {
    return scheme;
  }

  public List<CategoryImpl> getCategoryList() {
    return categoryList;
  }

  public CategoriesImpl setHref(final String href) {
    this.href = href;
    return this;
  }

  public CategoriesImpl setFixed(final Fixed fixed) {
    this.fixed = fixed;
    return this;
  }

  public CategoriesImpl setScheme(final String scheme) {
    this.scheme = scheme;
    return this;
  }

  public CategoriesImpl setCategoryList(final List<CategoryImpl> categoryList) {
    this.categoryList = categoryList;
    return this;
  }

}
