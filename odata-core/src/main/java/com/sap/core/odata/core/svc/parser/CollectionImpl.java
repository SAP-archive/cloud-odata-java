package com.sap.core.odata.core.svc.parser;

import java.util.List;

/**
 * CollectionImpl
 * <p>The implementiation of the interface Collection
 * @author SAP AG
 */
public class CollectionImpl {
  private TitleImpl title;
  private String href;
  private List<AcceptImpl> acceptElements;
  private List<CategoriesImpl> categories;
  private CommonAttributesImpl attributes;
  private List<ExtensionElementImpl> extensionElements;

  public CommonAttributesImpl getCommonAttributes() {
    return attributes;
  }

  public TitleImpl getTitle() {
    return title;
  }

  public String getHref() {
    return href;
  }

  public List<AcceptImpl> getAcceptElements() {
    return acceptElements;
  }

  public List<CategoriesImpl> getCategories() {
    return categories;
  }

  public List<ExtensionElementImpl> getExtesionElements() {
    return extensionElements;
  }

  public CollectionImpl setTitle(final TitleImpl title) {
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

  public CollectionImpl setCommonAttributes(final CommonAttributesImpl attributes) {
    this.attributes = attributes;
    return this;
  }

  public CollectionImpl setExtesionElements(final List<ExtensionElementImpl> elements) {
    extensionElements = elements;
    return this;
  }

}
