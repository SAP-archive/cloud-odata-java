package com.sap.core.odata.core.servicedocument;

import java.util.List;

import com.sap.core.odata.api.servicedocument.Categories;
import com.sap.core.odata.api.servicedocument.Category;
import com.sap.core.odata.api.servicedocument.Fixed;

/**
 * CategoriesImpl
 * <p>The implementiation of the interface Categories
 * @author SAP AG
 */
public class CategoriesImpl implements Categories {
  private String href;
  private Fixed fixed;
  private String scheme;
  private List<CategoryImpl> categoryList;

  @Override
  /**
   * {@inherit}
   */
  public String getHref() {
    return href;
  }

  @Override
  /**
   * {@inherit}
   */
  public Fixed getFixed() {
    return fixed;
  }

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
  public List<Category> getCategoryList() {
    return (List<Category>) (List<? extends Category>) categoryList;
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
