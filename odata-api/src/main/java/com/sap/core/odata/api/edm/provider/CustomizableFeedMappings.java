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

  public Boolean getFcKeepInContent() {
    return fcKeepInContent;
  }

  public CustomizableFeedMappings setFcKeepInContent(Boolean fcKeepInContent) {
    this.fcKeepInContent = fcKeepInContent;
    return this;
  }

  public CustomizableFeedMappings setFcContentKind(EdmContentKind fcContentKind) {
    this.fcContentKind = fcContentKind;
    return this;
  }

  public CustomizableFeedMappings setFcNsPrefix(String fcNsPrefix) {
    this.fcNsPrefix = fcNsPrefix;
    return this;
  }

  public CustomizableFeedMappings setFcNsUri(String fcNsUri) {
    this.fcNsUri = fcNsUri;
    return this;
  }

  public CustomizableFeedMappings setFcSourcePath(String fcSourcePath) {
    this.fcSourcePath = fcSourcePath;
    return this;
  }

  public CustomizableFeedMappings setFcTargetPath(EdmTargetPath fcTargetPath) {
    this.fcTargetPath = fcTargetPath;
    return this;
  }

  public CustomizableFeedMappings setCustomTargetPath(String customTargetPath) {
    this.customTargetPath = customTargetPath;
    return this;
  }

}