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

import java.util.Locale;

import org.junit.Test;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.core.exception.MessageService.Message;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class MessageServiceTest extends BaseTest {

  private static final Locale DEFAULT_LANGUAGE = new Locale("test", "SAP");

  @Test
  public void testResourceBundleException() throws Exception {
    MessageReference context = MessageReference.create(ODataMessageException.class, "COMMON");
    Message ms = MessageService.getMessage(null, context);

    assertEquals("MessageService could not be created because of exception 'IllegalArgumentException with message 'Parameter locale MUST NOT be NULL.'.", ms.getText());
  }

  @Test
  public void test() throws Exception {
    MessageReference context = MessageReference.create(ODataMessageException.class, "COMMON");
    Message ms = MessageService.getMessage(DEFAULT_LANGUAGE, context);

    assertEquals("Common exception", ms.getText());
  }

  @Test
  public void testParameter() throws Exception {
    MessageReference context = MessageReference.create(ODataMessageException.class, "ONE_REPLACEMENTS").addContent("first");
    Message ms = MessageService.getMessage(DEFAULT_LANGUAGE, context);

    assertEquals("Only replacement is [first]!", ms.getText());
  }

  @Test
  public void testOneParameterForTwo() throws Exception {
    MessageReference context = MessageReference.create(ODataMessageException.class, "TWO_REPLACEMENTS").addContent("first");
    Message ms = MessageService.getMessage(DEFAULT_LANGUAGE, context);

    assertEquals("Missing replacement for place holder in value 'First was [%1$s] and second was [%2$s]!' for following arguments '[first]'!", ms.getText());
  }

  @Test
  public void testTwoParameters() throws Exception {
    MessageReference context = MessageReference.create(ODataMessageException.class, "TWO_REPLACEMENTS").addContent("first", "second");
    Message ms = MessageService.getMessage(DEFAULT_LANGUAGE, context);

    assertEquals("First was [first] and second was [second]!", ms.getText());
  }

  @Test
  public void testThreeParametersForTwo() throws Exception {
    MessageReference context = MessageReference.create(ODataMessageException.class, "TWO_REPLACEMENTS").addContent("first", "second", "third");
    Message ms = MessageService.getMessage(DEFAULT_LANGUAGE, context);

    assertEquals("First was [first] and second was [second]!", ms.getText());
  }
}
