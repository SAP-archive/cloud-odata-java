package com.sap.core.odata.core.uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.sap.core.odata.core.edm.EdmEntityContainer;
import com.sap.core.odata.core.edm.EdmEntitySet;
import com.sap.core.odata.core.edm.EdmFunctionImport;
import com.sap.core.odata.core.edm.EdmProperty;
import com.sap.core.odata.core.edm.EdmType;
import com.sap.core.odata.core.uri.enums.Format;
import com.sap.core.odata.core.uri.enums.InlineCount;
import com.sap.core.odata.core.uri.enums.UriType;

public class UriParserResult {

  private UriType uriType;

  private EdmEntityContainer entityContainer;
  private EdmEntitySet entitySet;
  private EdmEntitySet targetEntitySet;
  private EdmFunctionImport functionImport;
  private EdmType targetType;
  private List<KeyPredicate> keyPredicates = Collections.emptyList();
  private List<NavigationSegment> navigationSegments = Collections.emptyList();
  private List<EdmProperty> propertyPath = Collections.emptyList();
  private List<SelectItem> select = Collections.emptyList();
  private boolean count;
  private boolean value;
  private boolean links;

  private Format format;
  private InlineCount inlineCount;
  private String skipToken;
  private int skip;
  private Integer top;
  private HashMap<String, UriLiteral> functionImportParameters;

  public void setUriType(UriType uriType) {
    this.uriType = uriType;
  }

  public UriType getUriType() {
    return uriType;
  }

  public void setEntityContainer(EdmEntityContainer entityContainer) {
    this.entityContainer = entityContainer;
  }

  public EdmEntityContainer getEntityContainer() {
    return entityContainer;
  }

  public void setEntitySet(EdmEntitySet edmEntitySet) {
    this.entitySet = edmEntitySet;
  }

  public EdmEntitySet getEntitySet() {
    return entitySet;
  }

  public void setTargetEntitySet(EdmEntitySet targetEntitySet) {
    this.targetEntitySet = targetEntitySet;
  }

  public EdmEntitySet getTargetEntitySet() {
    return targetEntitySet;
  }

  public void setFunctionImport(EdmFunctionImport functionImport) {
    this.functionImport = functionImport;
  }

  public EdmFunctionImport getFunctionImport() {
    return functionImport;
  }

  public void setTargetType(EdmType targetType) {
    this.targetType = targetType;
  }

  public EdmType getTargetType() {
    return targetType;
  }

  public void setKeyPredicates(List<KeyPredicate> keyPredicates) {
    this.keyPredicates = keyPredicates;
  }

  public List<KeyPredicate> getKeyPredicates() {
    return keyPredicates;
  }

  public void addNavigationSegment(NavigationSegment navigationSegment) {
    if (navigationSegments.equals(Collections.EMPTY_LIST))
      navigationSegments = new ArrayList<NavigationSegment>();

    navigationSegments.add(navigationSegment);
  }

  public List<NavigationSegment> getNavigationSegments() {
    return navigationSegments;
  }

  public void addProperty(EdmProperty property) {
    if (propertyPath.equals(Collections.EMPTY_LIST))
      propertyPath = new ArrayList<EdmProperty>();

    propertyPath.add(property);
  }

  public List<EdmProperty> getPropertyPath() {
    return propertyPath;
  }

  public void setCount(boolean count) {
    this.count = count;
  }

  public boolean isCount() {
    return count;
  }

  public void setValue(boolean value) {
    this.value = value;
  }

  public boolean isValue() {
    return value;
  }

  public void setLinks(boolean links) {
    this.links = links;
  }

  public boolean isLinks() {
    return links;
  }

  public void setFormat(Format format) {
    this.format = format;
  }

  public Format getFormat() {
    return format;
  }

  public void setInlineCount(InlineCount inlineCount) {
    this.inlineCount = inlineCount;
  }

  public InlineCount getInlineCount() {
    return inlineCount;
  }

  public void setSkipToken(String skipToken) {
    this.skipToken = skipToken;
  }

  public String getSkipToken() {
    return skipToken;
  }

  public void setSkip(int skip) {
    this.skip = skip;
  }

  public int getSkip() {
    return skip;
  }

  public void setTop(Integer top) {
    this.top = top;
  }

  public Integer getTop() {
    return top;
  }

  public void addFunctionImportParameter(final String name, final UriLiteral value) {
    if (functionImportParameters == null)
      functionImportParameters = new HashMap<String, UriLiteral>();

    functionImportParameters.put(name, value);
  }

  public HashMap<String, UriLiteral> getFunctionImportParameters() {
    return functionImportParameters;
  }

  @Override
  public String toString() {
    String entityContainerName = this.entityContainer == null ? "null" : this.entityContainer.getName();
    String entitySetName = this.entitySet == null ? "null" : this.entitySet.getName();
    String targetEntitySetName = this.targetEntitySet == null ? "null" : this.targetEntitySet.getName();
    String targetTypeName = this.targetType == null ? "null" : this.targetType.getName();
    String functionImportName = this.functionImport == null ? "null" : this.functionImport.getName();

    return "UriParserResult: uriType=" + uriType + ", "
        + "entityContainer=" + entityContainerName + ", "
        + "entitySet=" + entitySetName + ", "
        + "targetEntitySet=" + targetEntitySetName + ", "
        + "functionImport=" + functionImportName + ", "
        + "targetType=" + targetTypeName + ", "
        + "keyPredicates=" + keyPredicates + ", "
        + "navigationSegments=" + navigationSegments + ", "
        + "propertyPath=" + propertyPath + ", "
        + "isCount=" + count + ", "
        + "isValue=" + value + ", "
        + "isLinks=" + links + ", "
        + "format=" + format + ", "
        + "inlineCount=" + inlineCount + ", "
        + "skipToken=" + skipToken + ", "
        + "skip=" + skip + ", "
        + "top=" + top + ", "
        + "FunctionImportParameters=" + functionImportParameters;
  }

  public List<SelectItem> getSelect() {
    return select;
  }

  public void setSelect(List<SelectItem> select) {
    this.select = select;
  }
}
