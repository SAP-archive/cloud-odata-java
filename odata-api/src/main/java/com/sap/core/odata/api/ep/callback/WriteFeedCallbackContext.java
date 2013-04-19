package com.sap.core.odata.api.ep.callback;

import java.net.URI;

/**
 * Context given if the target of an expand is a feed. It contains the source entity set, the navigation property pointing to the entry which has to be expanded, the current expand select tree node and the data of the source entry.
 * @author SAP AG
 *
 */
public class WriteFeedCallbackContext extends WriteCallbackContext {

  private URI selfLink;

  /**
   * Sets the self Link for this feed.
   * @param selfLink
   */
  public void setSelfLink(final URI selfLink) {
    this.selfLink = selfLink;
  }

  /**
   * This self link is the same as the link displayed for the navigation property e.g. Rooms(1)/nr_Buildings.
   * @return the self link calculated by the library
   */
  public URI getSelfLink() {
    return selfLink;
  }

}
