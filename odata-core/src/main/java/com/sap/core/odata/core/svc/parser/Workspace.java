package com.sap.core.odata.core.svc.parser;

import java.util.List;

public interface Workspace {
  public Title getTitle();

  public List<Collection> getCollections();

  public CommonAttributes getCommonAttributes();

  public List<ExtensionElement> getExtesionElements();

}
