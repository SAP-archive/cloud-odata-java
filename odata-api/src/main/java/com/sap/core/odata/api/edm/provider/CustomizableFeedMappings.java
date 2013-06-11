package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmContentKind;
import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;

/**
 * Objects of this class represent customizable feed mappings.
 * @author SAP AG
 */
public class CustomizableFeedMappings implements EdmCustomizableFeedMappings {

  private Boolean fcKeepInContent;
  private EdmContentKind fcContentKind;
  private String fcNsPrefix;
  private String fcNsUri;
  private String fcSourcePath;
  private String fcTargetPath;

  @Override
  public Boolean isFcKeepInContent() {
    return fcKeepInContent;
  }

  @Override
  public EdmContentKind getFcContentKind() {
    return fcContentKind;
  }

  @Override
  public String getFcNsPrefix() {
    return fcNsPrefix;
  }

  @Override
  public String getFcNsUri() {
    return fcNsUri;
  }

  @Override
  public String getFcSourcePath() {
    return fcSourcePath;
  }

  @Override
  public String getFcTargetPath() {
    return fcTargetPath;
  }

  /**
   * @return <b>boolean</b>
   */
  public Boolean getFcKeepInContent() {
    return fcKeepInContent;
  }

  /**
   * Sets if this is kept in content.
   * @param fcKeepInContent
   * @return {@link CustomizableFeedMappings} for method chaining
   */
  public CustomizableFeedMappings setFcKeepInContent(final Boolean fcKeepInContent) {
    this.fcKeepInContent = fcKeepInContent;
    return this;
  }

  /**
   * Sets the {@link EdmContentKind}.
   * @param fcContentKind
   * @return {@link CustomizableFeedMappings} for method chaining
   */
  public CustomizableFeedMappings setFcContentKind(final EdmContentKind fcContentKind) {
    this.fcContentKind = fcContentKind;
    return this;
  }

  /**
   * Sets the prefix.
   * @param fcNsPrefix
   * @return {@link CustomizableFeedMappings} for method chaining
   */
  public CustomizableFeedMappings setFcNsPrefix(final String fcNsPrefix) {
    this.fcNsPrefix = fcNsPrefix;
    return this;
  }

  /**
   * Sets the Uri.
   * @param fcNsUri
   * @return {@link CustomizableFeedMappings} for method chaining
   */
  public CustomizableFeedMappings setFcNsUri(final String fcNsUri) {
    this.fcNsUri = fcNsUri;
    return this;
  }

  /**
   * Sets the source path.
   * @param fcSourcePath
   * @return {@link CustomizableFeedMappings} for method chaining
   */
  public CustomizableFeedMappings setFcSourcePath(final String fcSourcePath) {
    this.fcSourcePath = fcSourcePath;
    return this;
  }

  /**
   * <p>Sets the target path.</p>
   * <p>For standard Atom elements, constants are available in
   * {@link com.sap.core.odata.api.edm.EdmTargetPath EdmTargetPath}.</p>
   * @param fcTargetPath
   * @return {@link CustomizableFeedMappings} for method chaining
   */
  public CustomizableFeedMappings setFcTargetPath(final String fcTargetPath) {
    this.fcTargetPath = fcTargetPath;
    return this;
  }
}