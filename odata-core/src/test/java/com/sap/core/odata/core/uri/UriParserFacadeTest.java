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
package com.sap.core.odata.core.uri;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class UriParserFacadeTest extends BaseTest {

  @Test
  public void parseWithFacade() throws Exception {
    UriParser.parse(MockFacade.getMockEdm(),
        MockFacade.getPathSegmentsAsODataPathSegmentMock(Arrays.asList("$metadata")),
        Collections.<String, String> emptyMap());
  }

}
