package com.sap.core.odata.api.ep;

public class ODataEpOptionalProperties {

  // Feed: GetEntitySetView entitySetView, List<Map<String, Object>> data, String mediaResourceMimeType, int
  // inlinecount, String nextSkiptoken
  // Entry: EdmEntitySet entitySet, Map<String, Object> data, String mediaResourceMimeType
  // Property: EdmProperty edmProperty, Object value
  // Text: EdmProperty edmProperty, Object value
  // Media: String mimeType, byte [] data

  private String mediaResourceMimeType;
  private int inlineCount;
  private String nextSkipToken;

  private ODataEpOptionalProperties() { }
  
  /**
   * @return the mediaResourceMimeType
   */
  public final String getMediaResourceMimeType() {
    return mediaResourceMimeType;
  }

  /**
   * @return the inlineCount
   */
  public final int getInlineCount() {
    return inlineCount;
  }

  /**
   * @return the nextSkipToken
   */
  public final String getNextSkipToken() {
    return nextSkipToken;
  }

  public static ODataEpOptionalPropertiesBuilder builder() {
    return new ODataEpOptionalPropertiesBuilder();
  }

  public static class ODataEpOptionalPropertiesBuilder {
    private ODataEpOptionalProperties epOptProperties = new ODataEpOptionalProperties();

    /**
     * @param mediaResourceMimeType
     *          the mediaResourceMimeType to set
     */
    public final ODataEpOptionalPropertiesBuilder mediaResourceMimeType(String mediaResourceMimeType) {
      epOptProperties.mediaResourceMimeType = mediaResourceMimeType;
      return this;
    }

    /**
     * @param inlineCount
     *          the inlineCount to set
     */
    public final ODataEpOptionalPropertiesBuilder inlineCount(int inlineCount) {
      epOptProperties.inlineCount = inlineCount;
      return this;
    }

    /**
     * @param nextSkipToken
     *          the nextSkipToken to set
     */
    public final ODataEpOptionalPropertiesBuilder nextSkipToken(String nextSkipToken) {
      epOptProperties.nextSkipToken = nextSkipToken;
      return this;
    }

    public final ODataEpOptionalProperties build() {
      return epOptProperties;
    }
  }
}
