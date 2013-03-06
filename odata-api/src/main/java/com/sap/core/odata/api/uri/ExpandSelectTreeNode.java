package com.sap.core.odata.api.uri;

import java.util.HashMap;
import java.util.List;

import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;

public interface ExpandSelectTreeNode {

  public boolean isAll();
  
  public List<EdmProperty> getProperties();
  
  public HashMap<EdmNavigationProperty,ExpandSelectTreeNode> getLinks();
}
