/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.exception.ODataException;

/**
 * An <code>ODataProcessor</code> is the root interface for processor implementation.
 * A processor handles OData requests like reading or writing entities. All possible
 * actions are defined in the {@link com.sap.core.odata.api.processor.feature} package.
 * @author SAP AG
 * @com.sap.core.odata.DoNotImplement
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
