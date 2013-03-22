package com.sap.core.odata.api.ep.callback;

import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmNavigationProperty;

public class WriteFeedCallbackContext {
 private EdmEntitySet sourceEntitySet;
  
  private EdmNavigationProperty navigationProperty;
  
  private Map<String, Object> key;

  public EdmEntitySet getSourceEntitySet() {
    return sourceEntitySet;
  }

  public void setSourceEntitySet(EdmEntitySet entitySet) {
    this.sourceEntitySet = entitySet;
  }

  public EdmNavigationProperty getNavigationProperty() {
    return navigationProperty;
  }

  public void setNavigationProperty(EdmNavigationProperty navigationProperty) {
    this.navigationProperty = navigationProperty;
  }

  public Map<String, Object> getKey() {
    return key;
  }

  public void setKey(Map<String, Object> key) {
    this.key = key;
  }
}
