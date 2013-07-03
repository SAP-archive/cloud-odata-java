package com.sap.core.odata.api.ep.callback;

import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;

/**
 * Wrapper for {@link WriteEntryCallbackContext} and {@link WriteFeedCallbackContext}.
 * @com.sap.core.odata.DoNotImplement 
 * @author SAP AG
 */
public abstract class WriteCallbackContext {
  private EdmEntitySet sourceEntitySet;
  private EdmNavigationProperty navigationProperty;
  private Map<String, Object> entryData;
  private ExpandSelectTreeNode currentNode;

  /**
   * Current means the node pointing to the target entity set
   * @return the current node of the expand select tree
   */
  public ExpandSelectTreeNode getCurrentExpandSelectTreeNode() {
    return currentNode;
  }

  /**
   * Do Not Call This Method!
   * @param currentNode
   */
  public void setCurrentExpandSelectTreeNode(final ExpandSelectTreeNode currentNode) {
    this.currentNode = currentNode;
  }

  /**
   * Returns entity set which contains an entry that should be expanded
   * @return source entity set
   */
  public EdmEntitySet getSourceEntitySet() {
    return sourceEntitySet;
  }

  /**
   * Do Not Call This Method!
   * @param entitySet
   */
  public void setSourceEntitySet(final EdmEntitySet entitySet) {
    sourceEntitySet = entitySet;
  }

  /**
   * Navigation property which is contained in the expand clause.
   * @return navigation property pointing to the entity which has to be expanded.
   */
  public EdmNavigationProperty getNavigationProperty() {
    return navigationProperty;
  }

  /**
   * Do Not Call This Method!
   * @param navigationProperty
   */
  public void setNavigationProperty(final EdmNavigationProperty navigationProperty) {
    this.navigationProperty = navigationProperty;
  }

  /**
   * Source entry data which was just serialized.
   * @return data of the source entry
   */
  public Map<String, Object> getEntryData() {
    return entryData;
  }

  /**
   * Do Not Call This Method!
   * @param entryData
   */
  public void setEntryData(final Map<String, Object> entryData) {
    this.entryData = entryData;
  }

  /**
   * @return the key of the current entry as a Map<String,Object>
   * @throws EntityProviderException in case of an {@link EdmException}
   */
  public Map<String, Object> extractKeyFromEntryData() throws EntityProviderException {
    HashMap<String, Object> key = new HashMap<String, Object>();
    try {
      for (String keyPropertyName : sourceEntitySet.getEntityType().getKeyPropertyNames()) {
        key.put(keyPropertyName, entryData.get(keyPropertyName));
      }
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.EXCEPTION_OCCURRED.addContent(e.getClass().getSimpleName()), e);
    }
    return key;
  }
}
