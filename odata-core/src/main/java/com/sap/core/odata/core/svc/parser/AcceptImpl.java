package com.sap.core.odata.core.svc.parser;

/**
 * AcceptImpl
 * The implementation of the interface Accept
 * @author SAP AG
 */
public class AcceptImpl {
  private String value;
  private CommonAttributesImpl commonAttributes;

  public String getText() {
    return value;
  }

  public CommonAttributesImpl getCommonAttributes() {
    return commonAttributes;
  }

  public AcceptImpl setText(final String text) {
    value = text;
    return this;
  }

  public AcceptImpl setCommonAttributes(final CommonAttributesImpl commonAttributes) {
    this.commonAttributes = commonAttributes;
    return this;
  }
}
