package com.sap.core.odata.core.svc.parser;

import java.util.List;

public class CategoriesImpl {
  private String href;
  private String fixed;
  private String scheme;
  private List<CategoryImpl> categoryList;

  public String getHref() {
    return href;
  }

  public String getFixed() {
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

  public CategoriesImpl setFixed(final String fixed) {
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
