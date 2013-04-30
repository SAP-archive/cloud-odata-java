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
package com.sap.core.odata.api.edm;

import java.io.InputStream;
import java.util.List;

import com.sap.core.odata.api.exception.ODataException;

/**
 * @com.sap.core.odata.DoNotImplement
 * This interface gives access to the metadata of a service, the calculated Data Service Version and an info list of all entity sets inside this EntityDataModel.
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

  /**
   * @return a list of {@link EdmEntitySetInfo} objects of all entity sets in this data model
   * @throws ODataException
   */
  List<EdmEntitySetInfo> getEntitySetInfos() throws ODataException;;
}
