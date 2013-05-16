package com.sap.core.odata.core.doc;

import java.util.List;

import com.sap.core.odata.api.doc.Accept;
import com.sap.core.odata.api.doc.Categories;
import com.sap.core.odata.api.doc.Collection;
import com.sap.core.odata.api.doc.CommonAttributes;
import com.sap.core.odata.api.doc.ExtensionElement;
import com.sap.core.odata.api.doc.Title;

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
  public CommonAttributes getCommonAttributes() {
    return attributes;
  }

  @Override
  public Title getTitle() {
    return title;
  }

  @Override
  public String getHref() {
    return href;
  }

  @Override
  public List<Accept> getAcceptElements() {
    return (List<Accept>) (List<? extends Accept>) acceptElements;
  }

  @Override
  public List<Categories> getCategories() {
    return (List<Categories>) (List<? extends Categories>) categories;
  }

  @Override
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
