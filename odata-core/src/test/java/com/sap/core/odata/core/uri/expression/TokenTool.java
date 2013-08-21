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
package com.sap.core.odata.core.uri.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.uri.expression.ExpressionParserException;
import com.sap.core.odata.core.exception.MessageService;
import com.sap.core.odata.core.exception.MessageService.Message;

public class TokenTool {
  protected Token token;
  protected TokenList tokens = null;
  private Exception curException;
  private Exception exception;
  private String expression;
  private static boolean debug = false;

  private static final Logger log = LoggerFactory.getLogger(ParserTool.class);
  private static final Locale DEFAULT_LANGUAGE = new Locale("test", "SAP");

  public TokenTool(final String expression, final boolean wsp) {
    dout("TokenTool - Testing: " + expression);
    this.expression = expression;

    Tokenizer tokenizer = new Tokenizer(expression).setFlagWhiteSpace(wsp);
    try {
      tokens = tokenizer.tokenize();
      at(0);
    } catch (TokenizerException e) {
      exception = e;
    } catch (ExpressionParserException e) {
      exception = e;
    }

    curException = exception;
  }

  /**
   * Set the token to be check to the token at position <code>index</code>
   * 
   * @param index Index of the token to be checked
   * @return Returns <code>this</code>
   * @throws AssertionError
   */
  public TokenTool at(final int index) {
    token = tokens.elementAt(index);
    return this;
  }

  /**
   * Checks that the Type of the token matches the <code>kind</code>
   *      
   * @param kind Kind to be compared with the token type
   * @return Returns <code>this</code>
   * @throws AssertionError 
   */
  public TokenTool aKind(final TokenKind kind) {
    assertEquals(kind, token.getKind());
    return this;
  }

  /**
   * Checks that the EDM Type of the token matches the <code>edmType</code>
   * 
   * @param edmType EDM Type to be compared with the token type
   * @return Returns <code>this</code>
   * @throws AssertionError
   */
  public TokenTool aEdmType(final int edmType) {
    assertEquals(edmType, token.getEdmType());
    return this;
  }

  /**
   * Checks that the Value of the token matches the <code>stringValue</code>
   * 
   * @param stringValue Value to be compared with the token value 
   * @return Returns <code>this</code>
   * @throws AssertionError
   */
  public TokenTool aUriLiteral(final String stringValue) {
    assertEquals(stringValue, token.getUriLiteral());
    return this;
  }

  public TokenTool aPosition(final int position) {
    assertEquals(position, token.getPosition());
    return this;
  }

  public static void dout(final String out) {
    if (debug) {
      log.debug(out);
    }
  }

  public static void out(final String out) {
    log.debug(out);
  }

  /**
   * Verifies that all place holders in the message text definition of the thrown exception are provided with content
   * @return TokenTool
   */
  public TokenTool aExMsgContentAllSet() {
    String info = "aExMessageTextNoEmptyTag(" + expression + ")-->";
    if (curException == null) {
      fail("Error in aExMessageText: Expected exception.");
    }

    ODataMessageException messageException;

    try {
      messageException = (ODataMessageException) curException;
    } catch (ClassCastException ex) {
      fail("Error in aExNext: curException not an ODataMessageException");
      return this;
    }

    Message ms = MessageService.getMessage(DEFAULT_LANGUAGE, messageException.getMessageReference());
    info = "  " + info + "Messagetext: '" + ms.getText() + "contains [%";

    dout(info);

    if (ms.getText().contains("[%")) {
      fail(info);
    }
    return this;
  }

  /**
   * Verifies that the message text of the thrown exception is not empty
   * @return TokenTool
   */
  public TokenTool aExMsgNotEmpty() {
    String info = "aExTextNotEmpty(" + expression + ")-->";
    if (curException == null) {
      fail("Error in aExMessageText: Expected exception.");
    }

    ODataMessageException messageException;

    try {
      messageException = (ODataMessageException) curException;
    } catch (ClassCastException ex) {
      fail("Error in aExNext: curException not an ODataMessageException");
      return this;
    }

    Message ms = MessageService.getMessage(DEFAULT_LANGUAGE, messageException.getMessageReference());
    info = "  " + info + "check if Messagetext is empty";
    dout(info);

    if (ms.getText().length() == 0) {
      fail(info);
    }
    return this;
  }

  public TokenTool aExKey(final MessageReference expressionExpectedAtPos) {
    String expectedKey = expressionExpectedAtPos.getKey();
    ODataMessageException messageException;

    String info = "GetExceptionType(" + expression + ")-->";

    if (curException == null) {
      fail("Error in aExType: Expected exception");
    }

    try {
      messageException = (ODataMessageException) curException;
    } catch (ClassCastException ex) {
      fail("Error in aExNext: curException not an ODataMessageException");
      return this;
    }

    String actualKey = messageException.getMessageReference().getKey();
    dout("  " + info + "Expected key: " + expectedKey + " Actual: " + actualKey);

    if (expectedKey != actualKey) {
      fail("  " + info + "Expected: " + expectedKey + " Actual: " + actualKey);
    }
    return this;
  }

  public TokenTool printExMessage() {
    ODataMessageException messageException;

    if (curException == null) {
      fail("Error in aExMsgPrint: Expected exception");
    }

    try {
      messageException = (ODataMessageException) curException;
    } catch (ClassCastException ex) {
      fail("Error in aExNext: curException not an ODataMessageException");
      return this;
    }

    Message ms = MessageService.getMessage(DEFAULT_LANGUAGE, messageException.getMessageReference());
    out("Messge --> ");
    out("  " + ms.getText());
    out("Messge <-- ");

    return this;
  }

  /**
   * Verifies that the message text of the thrown exception serialized is {@paramref messageText}
   * @param messageText  
   *   Expected message text 
   * @return  this
   */
  public TokenTool aExMsgText(final String messageText) {
    String info = "aExMessageText(" + expression + ")-->";
    if (curException == null) {
      fail("Error in aExMessageText: Expected exception.");
    }

    ODataMessageException messageException;

    try {
      messageException = (ODataMessageException) curException;
    } catch (ClassCastException ex) {
      fail("Error in aExNext: curException not an ODataMessageException");
      return this;
    }

    Message ms = MessageService.getMessage(DEFAULT_LANGUAGE, messageException.getMessageReference());
    info = "  " + info + "Expected: '" + messageText + "' Actual: '" + ms.getText() + "'";

    dout(info);
    assertEquals(info, messageText, ms.getText());

    return this;
  }
}
