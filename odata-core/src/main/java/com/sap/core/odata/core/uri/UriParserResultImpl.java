package com.sap.core.odata.core.uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.NavigationPropertySegment;
import com.sap.core.odata.api.uri.NavigationSegment;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.UriLiteral;
import com.sap.core.odata.api.uri.UriParserResult;
import com.sap.core.odata.api.uri.enums.Format;
import com.sap.core.odata.api.uri.enums.InlineCount;
import com.sap.core.odata.api.uri.enums.UriType;

public class UriParserResultImpl implements UriParserResult {

  private UriType uriType;

  private EdmEntityContainer entityContainer;
  private EdmEntitySet entitySet;
  private EdmEntitySet targetEntitySet;
  private EdmFunctionImport functionImport;
  private EdmType targetType;
  private List<KeyPredicate> keyPredicates = Collections.emptyList();
  private List<NavigationSegment> navigationSegments = Collections.emptyList();
  private List<EdmProperty> propertyPath = Collections.emptyList();
  private boolean count;
  private boolean value;
  private boolean links;

  private Format format;
  private String customFormat;
  private String filter;
  private InlineCount inlineCount;
  private String orderBy;
  private String skipToken;
  private int skip;
  private Integer top;
  private List<ArrayList<NavigationPropertySegment>> expand = Collections.emptyList();
  private List<SelectItem> select = Collections.emptyList();
  private Map<String, UriLiteral> functionImportParameters;
  private Map<String, String> customQueryOptions;

  public void setUriType(UriType uriType) {
    this.uriType = uriType;
  }

  @Override
  public UriType getUriType() {
    return uriType;
  }

  public void setEntityContainer(EdmEntityContainer entityContainer) {
    this.entityContainer = entityContainer;
  }

  @Override
  public EdmEntityContainer getEntityContainer() {
    return entityContainer;
  }

  public void setEntitySet(EdmEntitySet edmEntitySet) {
    this.entitySet = edmEntitySet;
  }

  @Override
  public EdmEntitySet getEntitySet() {
    return entitySet;
  }

  public void setTargetEntitySet(EdmEntitySet targetEntitySet) {
    this.targetEntitySet = targetEntitySet;
  }

  @Override
  public EdmEntitySet getTargetEntitySet() {
    return targetEntitySet;
  }

  public void setFunctionImport(EdmFunctionImport functionImport) {
    this.functionImport = functionImport;
  }

  @Override
  public EdmFunctionImport getFunctionImport() {
    return functionImport;
  }

  public void setTargetType(EdmType targetType) {
    this.targetType = targetType;
  }

  @Override
  public EdmType getTargetType() {
    return targetType;
  }

  public void setKeyPredicates(List<KeyPredicate> keyPredicates) {
    this.keyPredicates = keyPredicates;
  }

  @Override
  public List<KeyPredicate> getKeyPredicates() {
    return keyPredicates;
  }

  public void addNavigationSegment(NavigationSegment navigationSegment) {
    if (navigationSegments.equals(Collections.EMPTY_LIST))
      navigationSegments = new ArrayList<NavigationSegment>();

    navigationSegments.add(navigationSegment);
  }

  @Override
  public List<NavigationSegment> getNavigationSegments() {
    return navigationSegments;
  }

  public void addProperty(EdmProperty property) {
    if (propertyPath.equals(Collections.EMPTY_LIST))
      propertyPath = new ArrayList<EdmProperty>();

    propertyPath.add(property);
  }

  @Override
  public List<EdmProperty> getPropertyPath() {
    return propertyPath;
  }

  public void setCount(boolean count) {
    this.count = count;
  }

  @Override
  public boolean isCount() {
    return count;
  }

  public void setValue(boolean value) {
    this.value = value;
  }

  @Override
  public boolean isValue() {
    return value;
  }

  public void setLinks(boolean links) {
    this.links = links;
  }

  @Override
  public boolean isLinks() {
    return links;
  }

  public void setFormat(Format format) {
    this.format = format;
  }

  public void setFormat(String customFormat) {
    this.customFormat = customFormat;
  }

  @Override
  public Format getFormat() {
    return format;
  }

  @Override
  public String getCustomFormat() {
    return customFormat;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  @Override
  public String getFilter() {
    return filter;
  }

  public void setInlineCount(InlineCount inlineCount) {
    this.inlineCount = inlineCount;
  }

  @Override
  public InlineCount getInlineCount() {
    return inlineCount;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  @Override
  public String getOrderBy() {
    return orderBy;
  }

  public void setSkipToken(String skipToken) {
    this.skipToken = skipToken;
  }

  @Override
  public String getSkipToken() {
    return skipToken;
  }

  public void setSkip(int skip) {
    this.skip = skip;
  }

  @Override
  public int getSkip() {
    return skip;
  }

  public void setTop(Integer top) {
    this.top = top;
  }

  @Override
  public Integer getTop() {
    return top;
  }

  public void setExpand(final List<ArrayList<NavigationPropertySegment>> expand) {
    this.expand = expand;
  }

  @Override
  public List<ArrayList<NavigationPropertySegment>> getExpand() {
    return expand;
  }

  public void setSelect(final List<SelectItem> select) {
    this.select = select;
  }

  @Override
  public List<SelectItem> getSelect() {
    return select;
  }

  public void addFunctionImportParameter(final String name, final UriLiteral value) {
    if (functionImportParameters == null)
      functionImportParameters = new HashMap<String, UriLiteral>();

    functionImportParameters.put(name, value);
  }

  @Override
  public Map<String, UriLiteral> getFunctionImportParameters() {
    return functionImportParameters;
  }

  @Override
  public Map<String, String> getCustomQueryOptions() {
    return customQueryOptions;
  }

  public void setCustomQueryOptions(final Map<String, String> customQueryOptions) {
    this.customQueryOptions = customQueryOptions;
  }

  @Override
  public String toString() {
    return "UriParserResult: uriType=" + uriType + ", "
        + "entityContainer=" + this.entityContainer + ", "
        + "entitySet=" + this.entitySet + ", "
        + "targetEntitySet=" + this.targetEntitySet + ", "
        + "functionImport=" + this.functionImport + ", "
        + "targetType=" + this.targetType + ", "
        + "keyPredicates=" + keyPredicates + ", "
        + "navigationSegments=" + navigationSegments + ", "
        + "propertyPath=" + propertyPath + ", "
        + "isCount=" + count + ", "
        + "isValue=" + value + ", "
        + "isLinks=" + links + ", "
        + "format=" + format + ", customFormat=" + customFormat + ", "
        + "filter=" + filter + ", "
        + "inlineCount=" + inlineCount + ", "
        + "orderBy=" + orderBy + ", "
        + "skipToken=" + skipToken + ", "
        + "skip=" + skip + ", "
        + "top=" + top + ", "
        + "expand=" + expand + ", "
        + "select=" + select + ", "
        + "functionImportParameters=" + functionImportParameters
        + "customQueryOptions=" + customQueryOptions;
  }
}
