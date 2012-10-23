package com.sap.core.odata.core.experimental.edm.adapter;

import com.sap.core.odata.api.edm.EdmContentKind;
import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmTargetPath;

public class EdmCustomizableFeedMappingsAdapter implements EdmCustomizableFeedMappings {

  private Boolean fcKeepInContent;
  private EdmContentKind fcContentKind;
  private String fcNsPrefix;
  private String fcNsUri;
  private String fcSourcePath;
  private EdmTargetPath fcTargetPath;

  public EdmCustomizableFeedMappingsAdapter(Boolean fcKeepInContent, EdmContentKind fcContentKind, String fcNsPrefix, String fcNsUri, String fcSourcePath, EdmTargetPath fcTargetPath) {
    this.fcKeepInContent = fcKeepInContent;
    this.fcContentKind = fcContentKind;
    this.fcNsPrefix = fcNsPrefix;
    this.fcNsUri = fcNsUri;
    this.fcSourcePath = fcSourcePath;
    this.fcTargetPath = fcTargetPath;
  }

  @Override
  public Boolean isFcKeepInContent() {
    return this.fcKeepInContent;
  }

  @Override
  public EdmContentKind getFcContentKind() {
    return this.fcContentKind;
  }

  @Override
  public String getFcNsPrefix() {
    return this.fcNsPrefix;
  }

  @Override
  public String getFcNsUri() {
    return this.fcNsUri;
  }

  @Override
  public String getFcSourcePath() {
    return this.fcSourcePath;
  }

  @Override
  public EdmTargetPath getFcTargetPath() {
    return this.fcTargetPath;
  }
}
