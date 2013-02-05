package com.sap.core.odata.api.ep;

import java.net.URI;
import java.util.Map;

import com.sap.core.odata.api.commons.InlineCount;

/**
 * @author SAP AG
 */
public class EntityProviderProperties {

  private URI serviceRoot;
  private String mediaResourceMimeType;
  private InlineCount inlineCountType;
  private Integer inlineCount;
  private String skipToken;
  private Map<String, String> nextLinkQueryOptions;

  private EntityProviderProperties() {}

  /**
   * @return the service root
   */
  public final URI getServiceRoot() {
    return serviceRoot;
  }

  /**
   * @return the MIME type of the media resource
   */
  public final String getMediaResourceMimeType() {
    return mediaResourceMimeType;
  }

  /**
  * @return the type of the inlinecount request from the system query option
  */
  public final InlineCount getInlineCountType() {
    return inlineCountType;
  }

  /**
   * @return the inlinecount as Integer
   */
  public final Integer getInlineCount() {
    return inlineCount;
  }

  /**
   * @return the skip token for the "next" link
   */
  public final String getSkipToken() {
    return skipToken;
  }

  /**
   * @return query options for extending next link
   */
  public final Map<String, String> getNextLinkQueryOptions() {
    return nextLinkQueryOptions;
  }
  
  public static ODataEntityProviderPropertiesBuilder serviceRoot(URI serviceRoot) {
    return new ODataEntityProviderPropertiesBuilder().serviceRoot(serviceRoot);
  }

  public static class ODataEntityProviderPropertiesBuilder {
    private final EntityProviderProperties properties = new EntityProviderProperties();

    /**
     * @param mediaResourceMimeType  the mediaResourceMimeType to set
     */
    public final ODataEntityProviderPropertiesBuilder mediaResourceMimeType(String mediaResourceMimeType) {
      properties.mediaResourceMimeType = mediaResourceMimeType;
      return this;
    }

    /**
     * @param inlineCountType  the inlineCountType to set
     */
    public final ODataEntityProviderPropertiesBuilder inlineCountType(InlineCount inlineCountType) {
      properties.inlineCountType = inlineCountType;
      return this;
    }

    /**
     * @param inlineCount  the inlineCount to set
     */
    public final ODataEntityProviderPropertiesBuilder inlineCount(Integer inlineCount) {
      properties.inlineCount = inlineCount;
      return this;
    }

    /**
     * @param skipToken  the skipToken to set
     */
    public final ODataEntityProviderPropertiesBuilder skipToken(String skipToken) {
      properties.skipToken = skipToken;
      return this;
    }

    /**
     * @param serviceRoot
     */
    private final ODataEntityProviderPropertiesBuilder serviceRoot(URI serviceRoot) {
      properties.serviceRoot = serviceRoot;
      return this;
    }

    public final EntityProviderProperties build() {
      return properties;
    }

    public ODataEntityProviderPropertiesBuilder nextLinkQueryOptions(Map<String, String> query) {
      properties.nextLinkQueryOptions = query;
      return this;
    }
  }
}
