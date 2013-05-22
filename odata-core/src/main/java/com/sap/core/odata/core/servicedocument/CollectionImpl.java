package com.sap.core.odata.core.servicedocument;

import java.util.List;

import com.sap.core.odata.api.servicedocument.Accept;
import com.sap.core.odata.api.servicedocument.Categories;
import com.sap.core.odata.api.servicedocument.Collection;
import com.sap.core.odata.api.servicedocument.CommonAttributes;
import com.sap.core.odata.api.servicedocument.ExtensionElement;
import com.sap.core.odata.api.servicedocument.Title;

/**
 * CollectionImpl
 * <p>The implementiation of the interface Collection
 * @author SAP AG
 */
public class CollectionImpl implements Collection {
  private Title title;
  private String href;
  private List<AcceptImpl> acceptElements;
  private List<CategoriesImpl> categories;
  private CommonAttributes attributes;
  private List<ExtensionElementImpl> extensionElements;

  @Override
  /**
   * {@inherit}
   */
  public CommonAttributes getCommonAttributes() {
    return attributes;
  }

  @Override
  /**
   * {@inherit}
   */
  public Title getTitle() {
    return title;
  }

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
  public List<Accept> getAcceptElements() {
    return (List<Accept>) (List<? extends Accept>) acceptElements;
  }

  @Override
  /**
   * {@inherit}
   */
  public List<Categories> getCategories() {
    return (List<Categories>) (List<? extends Categories>) categories;
  }

  @Override
  /**
   * {@inherit}
   */
  public List<ExtensionElement> getExtesionElements() {
    return (List<ExtensionElement>) (List<? extends ExtensionElement>) extensionElements;
  }

  public CollectionImpl setTitle(final Title title) {
    this.title = title;
    return this;
  }

  public CollectionImpl setHref(final String href) {
    this.href = href;
    return this;
  }

  public CollectionImpl setAcceptElements(final List<AcceptImpl> acceptElements) {
    this.acceptElements = acceptElements;
    return this;
  }

  public CollectionImpl setCategories(final List<CategoriesImpl> categories) {
    this.categories = categories;
    return this;
  }

  public CollectionImpl setCommonAttributes(final CommonAttributes attributes) {
    this.attributes = attributes;
    return this;
  }

  public CollectionImpl setExtesionElements(final List<ExtensionElementImpl> elements) {
    extensionElements = elements;
    return this;
  }

}
