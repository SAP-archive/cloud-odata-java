package org.odata4j.edm;

/**
 *  Source for service metadata.
 *
 *  @see EdmDataServices
 */
public interface EdmDataServicesProvider {

  /** Gets the service metadata. */
  EdmDataServices getMetadata();

}
