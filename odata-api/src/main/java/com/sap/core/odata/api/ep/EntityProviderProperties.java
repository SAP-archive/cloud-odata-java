package com.sap.core.odata.api.ep;

import java.net.URI;

import com.sap.core.odata.api.commons.InlineCount;

/**
 * 
 */
public class EntityProviderProperties {

  // Feed: GetEntitySetView entitySetView, List<Map<String, Object>> data, String mediaResourceMimeType, int inlinecount, String nextSkiptoken
  // Entry: EdmEntitySet entitySet, Map<String, Object> data, String mediaResourceMimeType
  // Property: EdmProperty edmProperty, Object value
  // Text: EdmProperty edmProperty, Object value
  // Media: String mimeType, byte [] data

  private URI baseUri;
  private String mediaResourceMimeType;
  private String skipToken;
  private Integer inlineCount;
  private InlineCount inlineCountType;

  private EntityProviderProperties() {}

  /**
   * @return the mediaResourceMimeType
   */
  public final String getMediaResourceMimeType() {
    return mediaResourceMimeType;
  }

  /**
  * @return the inlineCountType
  */
  public final InlineCount getInlineCountType() {
    return inlineCountType;
  }

  /**
   * @return the inlineCount
   */
  public final Integer getInlineCount() {
    return inlineCount;
  }

  /**
   * @return the nextSkipToken
   */
  public final String getSkipToken() {
    return skipToken;
  }

  /**
   * @return the baseUri
   */
  public final URI getBaseUri() {
    return baseUri;
  }

  public static ODataEntityProviderPropertiesBuilder baseUri(URI baseUri) {
    return new ODataEntityProviderPropertiesBuilder().baseUri(baseUri);
  }

  public static class ODataEntityProviderPropertiesBuilder {
    private EntityProviderProperties epOptProperties = new EntityProviderProperties();

    /**
     * @param mediaResourceMimeType
     *          the mediaResourceMimeType to set
     */
    public final ODataEntityProviderPropertiesBuilder mediaResourceMimeType(String mediaResourceMimeType) {
      epOptProperties.mediaResourceMimeType = mediaResourceMimeType;
      return this;
    }
    
    /**
     * @param inlineCountType
     *          the inlineCount to set
     */
    public final ODataEntityProviderPropertiesBuilder inlineCountType(InlineCount inlineCountType) {
      epOptProperties.inlineCountType = inlineCountType;
      return this;
    }


    /**
     * @param inlineCount
     *          the inlineCount to set
     */
    public final ODataEntityProviderPropertiesBuilder inlineCount(Integer inlineCount) {
      epOptProperties.inlineCount = inlineCount;
      return this;
    }

    /**
     * @param skipToken
     *          the skipToken to set
     */
    public final ODataEntityProviderPropertiesBuilder skipToken(String skipToken) {
      epOptProperties.skipToken = skipToken;
      return this;
    }

    /**
     * 
     * @param baseUri
     * @return
     */
    private final ODataEntityProviderPropertiesBuilder baseUri(URI baseUri) {
      epOptProperties.baseUri = baseUri;
      return this;
    }

    public final EntityProviderProperties build() {
      return epOptProperties;
    }
  }
}
