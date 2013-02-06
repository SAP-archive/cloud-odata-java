package com.sap.core.odata.api.ep;

import java.net.URI;

import com.sap.core.odata.api.commons.InlineCount;

/**
 * @author SAP AG
 */
public class EntityProviderProperties {

  private URI serviceRoot;
  private String mediaResourceMimeType;
  private InlineCount inlineCountType;
  private Integer inlineCount;
  public String nextLink;

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
   * @return a next link used for server side paging of feeds 
   */
  public final String getNextLink() {
    return nextLink;
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
     * @param serviceRoot
     */
    private final ODataEntityProviderPropertiesBuilder serviceRoot(URI serviceRoot) {
      properties.serviceRoot = serviceRoot;
      return this;
    }

    public final EntityProviderProperties build() {
      return properties;
    }

    /**
     * @param nextLink Next link to render feeds with server side paging. Should usually contain a skiptoken.
     */
    public ODataEntityProviderPropertiesBuilder nextLink(String nextLink) {
      properties.nextLink = nextLink;
      return this;
    }
  }
}
