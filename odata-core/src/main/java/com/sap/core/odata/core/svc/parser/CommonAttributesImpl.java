package com.sap.core.odata.core.svc.parser;

/**
 * CommonAttributesImpl
 * <p>The implementiation of the interface CommonAttributes
 * @author SAP AG
 */
import java.util.Collections;
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
    return Collections.unmodifiableList(attributes);
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
