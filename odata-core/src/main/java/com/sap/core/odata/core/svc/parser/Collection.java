package com.sap.core.odata.core.svc.parser;

import java.util.List;

public interface Collection {
  public Title getTitle();

  public String getHref();

  public CommonAttributes getCommonAttributes();

  public List<Categories> getCategories();

  public List<ExtensionElement> getExtesionElements();

}
