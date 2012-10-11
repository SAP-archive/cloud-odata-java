package com.sap.core.odata.core.edm;

public interface EdmCustomizableFeedMappings {

  // TODO: review defaults
  boolean fcKeepInContent = true;
  EdmContentKind fcContentKind = null;
  String fNsPrefix = null;
  String fcNsUri = null;
  String fcSourcePath = null;
  EdmTargetPath fcTargetPath = null;
}
