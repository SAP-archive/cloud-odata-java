package com.sap.core.odata.processor.api.jpa.model;

import com.sap.core.odata.api.edm.provider.Key;

/**
 * A view on Java Persistence Entity Key Attributes and EDM Key properties. Java
 * Persistence Key Attributes of type
 * <ol>
 * <li>embedded ID</li>
 * <li>ID</li>
 * </ol>
 * are converted into EDM keys. Embedded IDs are expanded into simple EDM
 * properties.
 * <p>
 * The implementation of the view provides access to EDM key properties for a
 * given JPA EDM entity type. The view acts as a container for consistent EDM
 * key property of an EDM entity type.
 * 
 * @author SAP AG
 *         <p>
 * @DoNotImplement
 * @see com.sap.core.odata.processor.api.jpa.model.JPAEdmPropertyView
 * 
 */
public interface JPAEdmKeyView extends JPAEdmBaseView {
  /**
   * The method returns an instance of EDM key for the given JPA EDM Entity
   * type.
   * 
   * @return an instance of type
   *         {@link com.sap.core.odata.api.edm.provider.Key}
   */
  public Key getEdmKey();
}
