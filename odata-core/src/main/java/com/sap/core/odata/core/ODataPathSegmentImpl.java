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
package com.sap.core.odata.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.uri.PathSegment;

/**
 * @author SAP AG
 */
public class ODataPathSegmentImpl implements PathSegment {

  private String path;
  private Map<String, List<String>> matrixParameter;

  public ODataPathSegmentImpl(final String path, final Map<String, List<String>> matrixParameters) {
    this.path = path;

    Map<String, List<String>> unmodifiableMap = new HashMap<String, List<String>>();
    if (matrixParameters != null) {
      for (String key : matrixParameters.keySet()) {
        List<String> values = Collections.unmodifiableList(matrixParameters.get(key));
        unmodifiableMap.put(key, values);
      }
    }

    matrixParameter = Collections.unmodifiableMap(unmodifiableMap);
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public Map<String, List<String>> getMatrixParameters() {
    return matrixParameter;
  }

}
