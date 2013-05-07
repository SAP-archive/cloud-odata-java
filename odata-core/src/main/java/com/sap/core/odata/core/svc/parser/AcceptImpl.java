package com.sap.core.odata.core.svc.parser;

public class AcceptImpl {
  private String text;
  private CommonAttributesImpl commonAttributes;

  public String getText() {
    return text;
  }

  public CommonAttributesImpl getCommonAttributes() {
    return commonAttributes;
  }

  public AcceptImpl setText(final String text) {
    this.text = text;
    return this;
  }

  public AcceptImpl setCommonAttributes(final CommonAttributesImpl commonAttributes) {
    this.commonAttributes = commonAttributes;
    return this;
  }
}
