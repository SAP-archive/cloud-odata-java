package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.exception.ODataException;

/**
 * A <code>ODataProcessor</code> is the root interface for processor implementation. A processor handles OData requests like reading or writing entities. All possible
 * actions are defined in the {@link com.sap.core.odata.api.processor.feature} package.
 * @author SAP AG
 * @DoNotImplement
 */
public interface ODataProcessor {

  /**
   * @param context A request context object which is usually injected by the OData library itself.
   * @throws ODataException
   */
  void setContext(ODataContext context) throws ODataException;

  /**
   * @return A request context object.
   * @throws ODataException
   */
  ODataContext getContext() throws ODataException;

}
