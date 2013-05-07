package com.sap.core.odata.core.svc.parser;

import java.util.List;

public interface ServiceDocument {
  public List<Workspace> getWorkspaces();

  public CommonAttributes getCommonAttributes();

  public List<ExtensionElement> getExtesionElements();
}
