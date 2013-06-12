package com.sap.core.odata.core.servicedocument;

import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.servicedocument.CommonAttributes;
import com.sap.core.odata.api.servicedocument.ExtensionAttribute;

/**
 * @author SAP AG
 */
public class CommonAttributesImpl implements CommonAttributes {
  private String base;
  private String lang;
  private List<ExtensionAttribute> attributes;

  @Override
  public String getBase() {
    return base;
  }

  @Override
  public String getLang() {
    return lang;
  }

  @Override
  public List<ExtensionAttribute> getAttributes() {
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

  public CommonAttributesImpl setAttributes(final List<ExtensionAttribute> attributes) {
    this.attributes = attributes;
    return this;
  }

}
