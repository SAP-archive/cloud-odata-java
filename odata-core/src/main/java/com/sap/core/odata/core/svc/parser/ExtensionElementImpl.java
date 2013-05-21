package com.sap.core.odata.core.svc.parser;

import java.util.List;

/**
* ExtensionElementImpl
* <p>The implementiation of the interface ExtensionElement
* @author SAP AG
*/
public class ExtensionElementImpl {
  private String namespace;
  private String prefix;
  private String name;
  private String text;
  private List<ExtensionElementImpl> anyElements;
  private List<ExtensionAttributeImpl> attributes;

  public String getNamespace() {
    return namespace;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getName() {
    return name;
  }

  public String getText() {
    return text;
  }

  public List<ExtensionElementImpl> getElements() {
    return anyElements;
  }

  public List<ExtensionAttributeImpl> getAttributes() {
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

  public ExtensionElementImpl setElements(final List<ExtensionElementImpl> anyElements) {
    this.anyElements = anyElements;
    return this;
  }

  public ExtensionElementImpl setAttributes(final List<ExtensionAttributeImpl> attributes) {
    this.attributes = attributes;
    return this;
  }

}
