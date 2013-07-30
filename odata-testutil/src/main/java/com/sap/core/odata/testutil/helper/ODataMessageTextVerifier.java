package com.sap.core.odata.testutil.helper;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.sap.core.odata.api.exception.MessageReference;

/**
 * This class is a helper for writing proper error messages.
 * Please use the static method {@link #TestClass(Class)} to
 * test whether all fields of type {@link MessageReference} of
 * the tested (Exception) class are provided in the <b>i18n.properties</b> file.
 * 
 * @author SAP AG
 */
public class ODataMessageTextVerifier {

  /**
   * Same as define in {@link MessageService}
   */
  private static final String BUNDLE_NAME = "i18n"; //$NON-NLS-1$
  private static final Locale locale = Locale.ROOT;

  public ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
  private final List<Throwable> errorCollector;

  public ODataMessageTextVerifier() {
    errorCollector = new ArrayList<Throwable>();
  }

  private void failCollector(final String text) {
    try {
      fail(text);
    } catch (final AssertionError ae) {
      errorCollector.add(ae);
    }
  }

  private String getMessage(final MessageReference msgRef) {
    try {
      final String key = msgRef.getKey();
      final String value = resourceBundle.getString(key);
      return value;
    } catch (final MissingResourceException e) {
      failCollector("Error-->Messagetext for key:\"" + msgRef.getKey() + "\" missing");
    }
    return null;
  }

  private void assertExistMessage(final MessageReference msgRef) {
    final String text = getMessage(msgRef);
    if (text == null) {
      return; // checked in getMessage
    }

    if (text.length() == 0) {
      failCollector("Error-->Messagetext for key:\"" + msgRef.getKey() + "\" empty");
    }
  }

  public void CheckMessagesOfClass(final Class<? extends Exception> exceptionClassToBeTested) {
    final Class<? extends Exception> testClass = exceptionClassToBeTested;

    for (final Field field : testClass.getDeclaredFields()) {
      // if field from type MessageReference
      if (field.getType().isAssignableFrom(MessageReference.class)) {
        final int modifiers = field.getModifiers();
        if (!Modifier.isStatic(modifiers)) {
          continue;
        }
        // field should be public
        assertTrue("MsgRef Error--> Error: field should be public.", Modifier.isPublic(modifiers));
        // field should be final
        assertTrue("MsgRef Error--> Error: field should be final.", Modifier.isFinal(modifiers));

        MessageReference msgRef = null;
        try {
          msgRef = (MessageReference) field.get(null);
        } catch (final IllegalArgumentException e) {
          failCollector("MsgRef Error--> Error: MsgRef " + field.getName() + " of class \"" + testClass.getSimpleName() + "\"");
          break;
        } catch (final IllegalAccessException e) {
          failCollector("MsgRef Error--> Not public: MsgRef " + field.getName() + " of class \"" + testClass.getSimpleName() + "\"");
          break;
        }

        if (msgRef == null) {
          failCollector("MsgRef Error--> Not assigned: MsgRef " + field.getName() + " of class \"" + testClass.getSimpleName() + "\"");
          break;
        }

        assertExistMessage(msgRef);
      }
    }
  }

  public List<Throwable> getErrorCollector() {
    return errorCollector;
  }

  static public void TestClass(final Class<? extends Exception> exceptionClassToBeTested) {
    final ODataMessageTextVerifier tool = new ODataMessageTextVerifier();
    tool.CheckMessagesOfClass(exceptionClassToBeTested);
    for (final Throwable throwable : tool.getErrorCollector()) {
      fail(throwable.getMessage());
    }
  }
}