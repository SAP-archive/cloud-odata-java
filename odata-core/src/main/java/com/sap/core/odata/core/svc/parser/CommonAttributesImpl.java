package com.sap.core.odata.core.svc.parser;

import java.util.List;

public class CommonAttributesImpl {
  private String base;
  private String lang;
  private List<ExtensionAttributeImpl> attributes;

  public String getBase() {
    return base;
  }

  public String getLang() {
    return lang;
  }

  public List<ExtensionAttributeImpl> getAttributes() {
    return attributes;
  }

  public CommonAttributesImpl setBase(final String base) {
    this.base = base;
    return this;
  }

  public CommonAttributesImpl setLang(final String lang) {
    this.lang = lang;
    return this;
  }

  public CommonAttributesImpl setAttributes(final List<ExtensionAttributeImpl> attributes) {
    this.attributes = attributes;
    return this;
  }

}
