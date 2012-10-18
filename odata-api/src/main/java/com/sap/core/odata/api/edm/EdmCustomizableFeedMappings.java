package com.sap.core.odata.api.edm;

public interface EdmCustomizableFeedMappings {

  public Boolean isFcKeepInContent();

  public EdmContentKind getFcContentKind();

  public String getFcNsPrefix();

  public String getFcNsUri();

  public String getFcSourcePath();

  public EdmTargetPath getFcTargetPath();
}
