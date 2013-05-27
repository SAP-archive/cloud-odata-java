package com.sap.core.odata.core.servicedocument;

import java.util.List;

import com.sap.core.odata.api.servicedocument.Categories;
import com.sap.core.odata.api.servicedocument.Category;
import com.sap.core.odata.api.servicedocument.Fixed;

public class CategoriesImpl implements Categories {
  private String href;
  private Fixed fixed;
  private String scheme;
  private List<Category> categoryList;

  @Override
  public String getHref() {
    return href;
  }

  @Override
  public Fixed getFixed() {
    return fixed;
  }

  @Override
  public String getScheme() {
    return scheme;
  }

  @Override
  public List<Category> getCategoryList() {
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

  public CategoriesImpl setCategoryList(final List<Category> categoryList) {
    this.categoryList = categoryList;
    return this;
  }

}
