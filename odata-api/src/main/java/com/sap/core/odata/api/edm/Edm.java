package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * Entity Data Model (EDM)
 * <p>Interface representing a Entity Data Model as described in the Conceptual Schema Definition.
 * @author SAP AG
 */
public interface Edm {

  public static final String NAMESPACE_EDM_2008_09 = "http://schemas.microsoft.com/ado/2008/09/edm";
  public static final String NAMESPACE_APP_2007 = "http://www.w3.org/2007/app";
  public static final String NAMESPACE_ATOM_2005 = "http://www.w3.org/2005/Atom";
  public static final String NAMESPACE_D_2007_08 = "http://schemas.microsoft.com/ado/2007/08/dataservices";
  public static final String NAMESPACE_M_2007_08 = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata";
  public static final String NAMESPACE_EDMX_2007_06 = "http://schemas.microsoft.com/ado/2007/06/edmx";
  public static final String NAMESPACE_REL_2007_08 = "http://schemas.microsoft.com/ado/2007/08/dataservices/related/";
  public static final String NAMESPACE_REL_ASSOC_2007_08 = "http://schemas.microsoft.com/ado/2007/08/dataservices/relatedlinks/";
  public static final String NAMESPACE_SCHEME_2007_08 = "http://schemas.microsoft.com/ado/2007/08/dataservices/scheme";
  public static final String NAMESPACE_XML_1998 = "http://www.w3.org/XML/1998/namespace";
  public static final String PREFIX_EDM = "edm";
  public static final String PREFIX_APP = "app";
  public static final String PREFIX_ATOM = "atom";
  public static final String PREFIX_D = "d";
  public static final String PREFIX_M = "m";
  public static final String PREFIX_XML = "xml";
  public static final String PREFIX_EDMX = "edmx";
  public static final String LINK_REL_SELF = "self";
  public static final String LINK_REL_EDIT_MEDIA = "edit-media";
  public static final String LINK_REL_EDIT = "edit";
  public static final String LINK_REL_NEXT = "next";
  public static final String DELIMITER = ".";

  /**
   * Get entity container by name
   * <p>See {@link EdmEntityContainer} for more information.
   * @param name
   * @return {@link EdmEntityContainer}
   * @throws EdmException
   */
  EdmEntityContainer getEntityContainer(String name) throws EdmException;

  /**
   * Get entity type by full qualified name
   * <p>See {@link EdmEntityType} for more information.
   * @param namespace
   * @param name
   * @return {@link EdmEntityType}
   * @throws EdmException
   */
  EdmEntityType getEntityType(String namespace, String name) throws EdmException;

  /**
   * Get complex type by full qualified name
   * <p>See {@link EdmComplexType} for more information.
   * @param namespace
   * @param name
   * @return {@link EdmComplexType}
   * @throws EdmException
   */
  EdmComplexType getComplexType(String namespace, String name) throws EdmException;

  /**
   * Get association by full qualified name
   * <p>See {@link EdmAssociation} for more information.
   * @param namespace
   * @param name
   * @return {@link EdmAssociation}
   * @throws EdmException
   */
  EdmAssociation getAssociation(String namespace, String name) throws EdmException;

  /**
   * Get service metadata
   * <p>See {@link EdmServiceMetadata} for more information.
   * @return {@link EdmServiceMetadata}
   */
  EdmServiceMetadata getServiceMetadata();

  /**
   * Get default entity container
   * <p>See {@link EdmEntityContainer} for more information.
   * @return {@link EdmEntityContainer}
   * @throws EdmException
   */
  EdmEntityContainer getDefaultEntityContainer() throws EdmException;
}