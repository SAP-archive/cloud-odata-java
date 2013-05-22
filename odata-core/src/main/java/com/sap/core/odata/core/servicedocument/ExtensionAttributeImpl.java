package com.sap.core.odata.core.servicedocument;

import com.sap.core.odata.api.servicedocument.ExtensionAttribute;

/**
* ExtensionAttributeImpl
* <p>The implementiation of the interface ExtensionAttribute
* @author SAP AG
*/
public class ExtensionAttributeImpl implements ExtensionAttribute {
  private String namespace;
  private String prefix;
  private String name;
  private String text;

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

  public ExtensionAttributeImpl setNamespace(final String namespace) {
    this.namespace = namespace;
    return this;
  }

  public ExtensionAttributeImpl setPrefix(final String prefix) {
    this.prefix = prefix;
    return this;
  }

  public ExtensionAttributeImpl setName(final String name) {
    this.name = name;
    return this;
  }

  public ExtensionAttributeImpl setText(final String text) {
    this.text = text;
    return this;
  }

}
