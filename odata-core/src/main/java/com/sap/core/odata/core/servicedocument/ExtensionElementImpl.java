package com.sap.core.odata.core.servicedocument;

import java.util.List;

import com.sap.core.odata.api.servicedocument.ExtensionAttribute;
import com.sap.core.odata.api.servicedocument.ExtensionElement;

/**
 * @author SAP AG
 */
public class ExtensionElementImpl implements ExtensionElement {
  private String namespace;
  private String prefix;
  private String name;
  private String text;
  private List<ExtensionElement> anyElements;
  private List<ExtensionAttribute> attributes;

  @Override
  public String getNamespace() {
    return namespace;
  }

  @Override
  public String getPrefix() {
    return prefix;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public List<ExtensionElement> getElements() {
    return anyElements;
  }

  @Override
  public List<ExtensionAttribute> getAttributes() {
    return attributes;
  }

  public ExtensionElementImpl setNamespace(final String namespace) {
    this.namespace = namespace;
    return this;
  }

  public ExtensionElementImpl setPrefix(final String prefix) {
    this.prefix = prefix;
    return this;
  }

  public ExtensionElementImpl setName(final String name) {
    this.name = name;
    return this;
  }

  public ExtensionElementImpl setText(final String text) {
    this.text = text;
    return this;

  }

  public ExtensionElementImpl setElements(final List<ExtensionElement> anyElements) {
    this.anyElements = anyElements;
    return this;
  }

  public ExtensionElementImpl setAttributes(final List<ExtensionAttribute> attributes) {
    this.attributes = attributes;
    return this;
  }

}
