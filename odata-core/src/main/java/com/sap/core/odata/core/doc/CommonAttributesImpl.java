package com.sap.core.odata.core.doc;

/**
 * CommonAttributesImpl
 * <p>The implementiation of the interface CommonAttributes
 * @author SAP AG
 */
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.doc.CommonAttributes;
import com.sap.core.odata.api.doc.ExtensionAttribute;

public class CommonAttributesImpl implements CommonAttributes {
  private String base;
  private String lang;
  private List<ExtensionAttributeImpl> attributes;

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
    return Collections.unmodifiableList((List<ExtensionAttribute>) (List<? extends ExtensionAttribute>) attributes);
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
