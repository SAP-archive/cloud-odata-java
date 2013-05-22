package com.sap.core.odata.core.servicedocument;

import java.util.List;

import com.sap.core.odata.api.servicedocument.ExtensionAttribute;
import com.sap.core.odata.api.servicedocument.ExtensionElement;

/**
* ExtensionElementImpl
* <p>The implementiation of the interface ExtensionElement
* @author SAP AG
*/
public class ExtensionElementImpl implements ExtensionElement {
  private String namespace;
  private String prefix;
  private String name;
  private String text;
  private List<ExtensionElementImpl> anyElements;
  private List<ExtensionAttributeImpl> attributes;

  @Override
  /**
   * {@inherit}
   */
  public String getNamespace() {
    return namespace;
  }

  @Override
  /**
   * {@inherit}
   */
  public String getPrefix() {
    return prefix;
  }

  @Override
  /**
   * {@inherit}
   */
  public String getName() {
    return name;
  }

  @Override
  /**
   * {@inherit}
   */
  public String getText() {
    return text;
  }

  @Override
  /**
   * {@inherit}
   */
  public List<ExtensionElement> getElements() {
    return (List<ExtensionElement>) (List<? extends ExtensionElement>) anyElements;
  }

  @Override
  /**
   * {@inherit}
   */
  public List<ExtensionAttribute> getAttributes() {
    return (List<ExtensionAttribute>) (List<? extends ExtensionAttribute>) attributes;
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
