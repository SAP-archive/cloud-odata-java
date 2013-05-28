package com.sap.core.odata.core.servicedocument;

import com.sap.core.odata.api.servicedocument.Title;

public class TitleImpl implements Title {
  private String text;

  @Override
  public String getText() {
    return text;
  }

  public TitleImpl setText(final String text) {
    this.text = text;
    return this;
  }
}
