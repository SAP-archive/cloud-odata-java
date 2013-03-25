package com.sap.core.odata.api.ep.callback;

import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;

public abstract class WrtiteCallbackContext {
  private EdmEntitySet sourceEntitySet;

  private EdmNavigationProperty navigationProperty;

  private Map<String, Object> entryData;

  private ExpandSelectTreeNode currentNode;

  public ExpandSelectTreeNode getCurrentExpandSelectTreeNode() {
    return currentNode;
  }

  public void setCurrentExpandSelectTreeNode(ExpandSelectTreeNode currentNode) {
    this.currentNode = currentNode;
  }

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

  public Map<String, Object> getEntryData() {
    return entryData;
  }

  public void setEntryData(Map<String, Object> entryData) {
    this.entryData = entryData;
  }

  public Map<String, Object> extractKeyFromEntryData() throws EntityProviderException {
    HashMap<String, Object> key = new HashMap<String, Object>();
    try {
      for (String keyPropertyName : sourceEntitySet.getEntityType().getKeyPropertyNames()) {
        key.put(keyPropertyName, entryData.get(keyPropertyName));
      }
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
    return entryData;
  }
}
