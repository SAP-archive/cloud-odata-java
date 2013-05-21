package com.sap.core.odata.core.svc.parser;

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
