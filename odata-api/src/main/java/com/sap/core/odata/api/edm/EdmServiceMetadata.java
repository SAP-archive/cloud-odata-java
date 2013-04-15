/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm;

import java.io.InputStream;

import com.sap.core.odata.api.exception.ODataException;

/**
 * @com.sap.core.odata.DoNotImplement
 * This interface gives access to the metadata of a service as well as the calculated Data Service Version.
 * @author SAP AG
 *
 */
public interface EdmServiceMetadata {

  /**
   * @return {@link InputStream} containing the metadata document
   * @throws ODataException
   */
  InputStream getMetadata() throws ODataException;

  /**
   * @return <b>String</b> data service version of this service
   * @throws ODataException
   */
  String getDataServiceVersion() throws ODataException;
}