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
package com.sap.core.odata.core.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.batch.BatchPart;
import com.sap.core.odata.api.processor.ODataRequest;

public class BatchPartImpl implements BatchPart {

  private List<ODataRequest> requests = new ArrayList<ODataRequest>();
  private boolean isChangeSet;

  public BatchPartImpl() {}

  public BatchPartImpl(final boolean isChangeSet, final List<ODataRequest> requests) {
    this.isChangeSet = isChangeSet;
    this.requests = requests;
  }

  @Override
  public boolean isChangeSet() {
    return isChangeSet;
  }

  public void setChangeSet(final boolean isChangeSet) {
    this.isChangeSet = isChangeSet;
  }

  @Override
  public List<ODataRequest> getRequests() {
    return Collections.unmodifiableList(requests);
  }

  public void setRequests(final List<ODataRequest> requests) {
    this.requests = requests;
  }

}
