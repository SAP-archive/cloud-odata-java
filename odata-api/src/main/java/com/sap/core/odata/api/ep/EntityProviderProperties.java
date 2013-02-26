package com.sap.core.odata.api.ep;

import java.net.URI;

import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.ep.entry.ODataEntry;

/**
 * {@link EntityProviderProperties} contains all additional properties which are necessary to <b>write (serialize)</b> an
 * {@link ODataEntry} into an specific format (e.g. <code>XML</code> or <code>JSON</code> or ...).
 */
public class EntityProviderProperties {

  private URI serviceRoot;
  private String mediaResourceMimeType;
  private InlineCount inlineCountType;
  private Integer inlineCount;
  private String nextLink;
  private boolean hasLocationHeader;

  private EntityProviderProperties() {}

  /**
   * Gets the service root.
   * @return the service root
   */
  public final URI getServiceRoot() {
    return serviceRoot;
  }

  /**
   * Gets the MIME type of the media resource.
   * @return the MIME type of the media resource
   */
  public final String getMediaResourceMimeType() {
    return mediaResourceMimeType;
  }

  /**
  * Gets the type of the inlinecount request from the system query option.
  * @return the type of the inlinecount request from the system query option
  */
  public final InlineCount getInlineCountType() {
    return inlineCountType;
  }

  /**
  * Gets the inlinecount.
   * @return the inlinecount as Integer
   * @see #getInlineCountType
   */
  public final Integer getInlineCount() {
    return inlineCount;
  }

  /**
   * Gets the next link used for server-side paging of feeds.
   * @return the next link
   */
  public final String getNextLink() {
    return nextLink;
  }

  /**
   * Gets the option whether the response should have a Location HTTP header
   * with the canonical URI of the entity.
   */
  public final boolean hasLocationHeader() {
    return hasLocationHeader;
  }

  public static ODataEntityProviderPropertiesBuilder serviceRoot(final URI serviceRoot) {
    return new ODataEntityProviderPropertiesBuilder().serviceRoot(serviceRoot);
  }

  public static class ODataEntityProviderPropertiesBuilder {
    private final EntityProviderProperties properties = new EntityProviderProperties();

    /**
     * @param mediaResourceMimeType  the mediaResourceMimeType to set
     */
    public final ODataEntityProviderPropertiesBuilder mediaResourceMimeType(final String mediaResourceMimeType) {
      properties.mediaResourceMimeType = mediaResourceMimeType;
      return this;
    }

    /**
     * @param inlineCountType  the inlineCountType to set
     */
    public final ODataEntityProviderPropertiesBuilder inlineCountType(final InlineCount inlineCountType) {
      properties.inlineCountType = inlineCountType;
      return this;
    }

    /**
     * @param inlineCount  the inlineCount to set
     */
    public final ODataEntityProviderPropertiesBuilder inlineCount(final Integer inlineCount) {
      properties.inlineCount = inlineCount;
      return this;
    }

    /**
     * @param serviceRoot
     */
    private final ODataEntityProviderPropertiesBuilder serviceRoot(final URI serviceRoot) {
      properties.serviceRoot = serviceRoot;
      return this;
    }

    /**
     * @param nextLink Next link to render feeds with server side paging. Should usually contain a skiptoken.
     */
    public ODataEntityProviderPropertiesBuilder nextLink(final String nextLink) {
      properties.nextLink = nextLink;
      return this;
    }

    /**
     * Requests that the response will have a Location HTTP header with the canonical URI of the entity.
     * @see HttpHeaders#LOCATION
     */
    public ODataEntityProviderPropertiesBuilder hasLocationHeader() {
      properties.hasLocationHeader = true;
      return this;
    }

    public final EntityProviderProperties build() {
      return properties;
    }

  }
}
