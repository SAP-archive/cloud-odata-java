package com.sap.core.odata.core.svc.parser;

/**
 * TitleImpl
 * <p>The implementiation of the interface Title
 * @author SAP AG
 */
public class TitleImpl {
  private String text;

  public String getText() {
    return text;
  }

  public TitleImpl setText(final String text) {
    this.text = text;
    return this;
  }
}
