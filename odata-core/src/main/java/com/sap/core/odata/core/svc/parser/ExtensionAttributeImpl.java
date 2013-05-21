package com.sap.core.odata.core.svc.parser;

/**
* ExtensionAttributeImpl
* <p>The implementiation of the interface ExtensionAttribute
* @author SAP AG
*/
public class ExtensionAttributeImpl {
  private String namespace;
  private String prefix;
  private String name;
  private String text;

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
