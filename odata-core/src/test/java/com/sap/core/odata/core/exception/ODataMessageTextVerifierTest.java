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
package com.sap.core.odata.core.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.helper.ODataMessageTextVerifier;
import com.sap.core.odata.testutil.mock.SampleClassForInvalidMessageReferences;

/**
 * This class tests the {@link ODataMessageTextVerifier}
 * 
 * @Author SAP AG
 */
public class ODataMessageTextVerifierTest extends BaseTest {

  @Test
  public void TestExceptionText() {
    ODataMessageTextVerifier tool = new ODataMessageTextVerifier();
    tool.CheckMessagesOfClass(SampleClassForInvalidMessageReferences.class);

    List<Throwable> ec = tool.getErrorCollector();

    assertEquals("!!!Error in testtool", 2, ec.size());

    assertNotNull("!!!Error in testtool", ec.get(0));
    assertEquals("Error", "Error-->Messagetext for key:\"com.sap.core.odata.testutil.mock.SampleClassForInvalidMessageReferences.DOES_NOT_EXIST\" missing", ec.get(0).getMessage());

    assertNotNull("!!!Error in testtool", ec.get(1));
    assertEquals("Error", "Error-->Messagetext for key:\"com.sap.core.odata.testutil.mock.SampleClassForInvalidMessageReferences.EXITS_BUT_EMPTY\" empty", ec.get(1).getMessage());

  }

}
