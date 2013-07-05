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

import java.util.List;

import com.sap.core.odata.api.batch.BatchResponsePart;
import com.sap.core.odata.api.processor.ODataResponse;

public class BatchResponsePartImpl extends BatchResponsePart {
  private List<ODataResponse> responses;
  private boolean isChangeSet;

  @Override
  public List<ODataResponse> getResponses() {
    return responses;
  }

  @Override
  public boolean isChangeSet() {
    return isChangeSet;
  }

  public class BatchResponsePartBuilderImpl extends BatchResponsePartBuilder {
    private List<ODataResponse> responses;
    private boolean isChangeSet;

    @Override
    public BatchResponsePart build() {
      BatchResponsePartImpl.this.responses = responses;
      BatchResponsePartImpl.this.isChangeSet = isChangeSet;
      return BatchResponsePartImpl.this;
    }

    @Override
    public BatchResponsePartBuilder responses(final List<ODataResponse> responses) {
      this.responses = responses;
      return this;
    }

    @Override
    public BatchResponsePartBuilder changeSet(final boolean isChangeSet) {
      this.isChangeSet = isChangeSet;
      return this;
    }
  }
}
