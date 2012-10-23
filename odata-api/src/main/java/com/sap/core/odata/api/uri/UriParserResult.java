package com.sap.core.odata.api.uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.enums.InlineCount;
import com.sap.core.odata.api.enums.UriType;

public interface UriParserResult {

  public UriType getUriType();

  public EdmEntityContainer getEntityContainer();

  public EdmEntitySet getEntitySet();

  public EdmEntitySet getTargetEntitySet();

  public EdmFunctionImport getFunctionImport();

  public EdmType getTargetType();

  public List<KeyPredicate> getKeyPredicates();

  public List<NavigationSegment> getNavigationSegments();

  public List<EdmProperty> getPropertyPath();

  public boolean isCount();

  public boolean isValue();

  public boolean isLinks();

  public Format getFormat();

  public String getCustomFormat();

  public String getFilter();

  public InlineCount getInlineCount();

  public String getOrderBy();

  public String getSkipToken();

  public int getSkip();

  public Integer getTop();

  public List<ArrayList<NavigationPropertySegment>> getExpand();

  public List<SelectItem> getSelect();

  public Map<String, UriLiteral> getFunctionImportParameters();

  public Map<String, String> getCustomQueryOptions();
}
