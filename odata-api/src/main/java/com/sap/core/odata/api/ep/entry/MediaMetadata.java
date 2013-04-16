package com.sap.core.odata.api.ep.entry;

/**
 * {@link MediaMetadata} contains all metadata for media related entries.
 */
public interface MediaMetadata {

  /**
   * Get <code>edit link</code>.
   * 
   * @return <code>edit link</code>.
   */
  public abstract String getEditLink();

  /**
   * Get <code>content type</code> in as specified in 
   * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html">RFC 2616 Section 14</a>.
   * 
   * @return <code>content type</code>.
   */
  public abstract String getContentType();

  /**
   * Get <code>etag</code>.
   * 
   * @return <code>etag</code>.
   */
  public abstract String getEtag();

  /**
   * Get <code>source link</code>.
   * 
   * @return <code>source link</code>.
   */
  public abstract String getSourceLink();
}
