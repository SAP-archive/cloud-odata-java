package com.sap.core.odata.api.uri.resultviews;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.enums.InlineCount;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.NavigationSegment;

public interface Uri7BResultView {
  /**
   * @return {@link EdmEntityContainer} the target entity container
   */
  public EdmEntityContainer getEntityContainer();

  /**
   * @return {@link EdmEntitySet}
   */
  public EdmEntitySet getStartEntitySet();

  /**
   * @return {@link EdmEntitySet} target entity set
   */
  public EdmEntitySet getTargetEntitySet();

  /**
   * @return {@link EdmType} the target type of the entity set
   */
  public EdmType getTargetType();

  /**
   * @return list of {@link KeyPredicate}
   */
  public List<KeyPredicate> getKeyPredicates();

  /**
   * @return list of {@link NavigationSegment}
   */
  public List<NavigationSegment> getNavigationSegments();

  /**
   * @return boolean
   */
  public boolean isLinks();

  /**
   * @return {@link Format} the format
   */
  public Format getFormat();

  /**
   * @return String the customer format
   */
  public String getCustomFormat();

  /**
   * @return String the filter
   */
  public String getFilter();

  /**
   * @return {@link InlineCount} the inline count
   */
  public InlineCount getInlineCount();

  /**
   * @return String skip token
   */
  public String getSkipToken();

  /**
   * @return String skip
   */
  public int getSkip();

  /**
   * @return int top
   */
  public Integer getTop();

  /**
   * @return Map of {@literal<String, String>} custom query options
   */
  public Map<String, String> getCustomQueryOptions();
}
