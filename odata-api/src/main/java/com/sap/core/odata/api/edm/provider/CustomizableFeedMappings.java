package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmContentKind;
import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmTargetPath;

public class CustomizableFeedMappings implements EdmCustomizableFeedMappings {

  private Boolean fcKeepInContent;
  private EdmContentKind fcContentKind;
  private String fcNsPrefix;
  private String fcNsUri;
  private String fcSourcePath;
  private EdmTargetPath fcTargetPath;
  private String customTargetPath;

  public CustomizableFeedMappings(Boolean fcKeepInContent, EdmContentKind fcContentKind, String fcNsPrefix, String fcNsUri, String fcSourcePath, EdmTargetPath fcTargetPath, String customTargetPath) {
    this.fcKeepInContent = fcKeepInContent;
    this.fcContentKind = fcContentKind;
    this.fcNsPrefix = fcNsPrefix;
    this.fcNsUri = fcNsUri;
    this.fcSourcePath = fcSourcePath;
    this.fcTargetPath = fcTargetPath;
    this.customTargetPath = customTargetPath;
  }

  public Boolean isFcKeepInContent() {
    return fcKeepInContent;
  }

  public EdmContentKind getFcContentKind() {
    return fcContentKind;
  }

  public String getFcNsPrefix() {
    return fcNsPrefix;
  }

  public String getFcNsUri() {
    return fcNsUri;
  }

  public String getFcSourcePath() {
    return fcSourcePath;
  }

  public EdmTargetPath getFcTargetPath() {
    return fcTargetPath;
  }

  public String getCustomTargetPath() {
    return customTargetPath;
  }
}