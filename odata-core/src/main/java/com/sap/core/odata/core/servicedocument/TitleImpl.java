package com.sap.core.odata.core.servicedocument;

import com.sap.core.odata.api.servicedocument.Title;

/**
 * TitleImpl
 * <p>The implementiation of the interface Title
 * @author SAP AG
 */
public class TitleImpl implements Title {
  private String text;

  @Override
  /**
   * {@inherit}
   */
  public String getText() {
    return text;
  }

  public TitleImpl setText(final String text) {
    this.text = text;
    return this;
  }
}
