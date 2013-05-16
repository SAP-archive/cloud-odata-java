package com.sap.core.odata.core.doc;

import com.sap.core.odata.api.doc.Accept;
import com.sap.core.odata.api.doc.CommonAttributes;

/**
 * AcceptImpl
 * The implementation of the interface Accept
 * @author SAP AG
 */
public class AcceptImpl implements Accept {
  private String value;
  private CommonAttributes commonAttributes;

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public CommonAttributes getCommonAttributes() {
    return commonAttributes;
  }

  public AcceptImpl setText(final String text) {
    value = text;
    return this;
  }

  public AcceptImpl setCommonAttributes(final CommonAttributes commonAttributes) {
    this.commonAttributes = commonAttributes;
    return this;
  }
}
