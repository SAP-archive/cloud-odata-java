package com.sap.core.odata.core.servicedocument;

import java.util.List;

import com.sap.core.odata.api.servicedocument.Accept;
import com.sap.core.odata.api.servicedocument.Categories;
import com.sap.core.odata.api.servicedocument.Collection;
import com.sap.core.odata.api.servicedocument.CommonAttributes;
import com.sap.core.odata.api.servicedocument.ExtensionElement;
import com.sap.core.odata.api.servicedocument.Title;

/**
 * @author SAP AG
 */
public class CollectionImpl implements Collection {
  private Title title;
  private String href;
  private List<Accept> acceptElements;
  private List<Categories> categories;
  private CommonAttributes attributes;
  private List<ExtensionElement> extensionElements;

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
    return acceptElements;
  }

  @Override
  public List<Categories> getCategories() {
    return categories;
  }

  @Override
  public List<ExtensionElement> getExtesionElements() {
    return extensionElements;
  }

  public CollectionImpl setTitle(final Title title) {
    this.title = title;
    return this;
  }

  public CollectionImpl setHref(final String href) {
    this.href = href;
    return this;
  }

  public CollectionImpl setAcceptElements(final List<Accept> acceptElements) {
    this.acceptElements = acceptElements;
    return this;
  }

  public CollectionImpl setCategories(final List<Categories> categories) {
    this.categories = categories;
    return this;
  }

  public CollectionImpl setCommonAttributes(final CommonAttributes attributes) {
    this.attributes = attributes;
    return this;
  }

  public CollectionImpl setExtesionElements(final List<ExtensionElement> elements) {
    extensionElements = elements;
    return this;
  }

}
