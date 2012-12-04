package com.sap.core.odata.core.uri.expression.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import org.junit.Test;
import org.junit.rules.ErrorCollector;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.core.exception.MessageService;

public class ExceptionTextsTool {

  /**
   * Same as define in {@link MessageService}
   */
  private static final String BUNDLE_NAME = "i18n"; //$NON-NLS-1$
  private static final Locale locale = Locale.ROOT;

  public ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
  private Vector<Throwable> errorCollector;

  public ExceptionTextsTool()
  {
    errorCollector = new Vector<Throwable>();
  }

  private void failCollector(String text)
  {
    try {
      System.out.println(text);
      fail(text);
    } catch (AssertionError ae)
    {
      errorCollector.add(ae);
    }
  }

  private String getMessage(MessageReference msgRef) {
    String value = null;

    try
    {
      String key = msgRef.getKey();
      value = resourceBundle.getString(key);
      return value;
    } catch (java.util.MissingResourceException ex)
    {
      failCollector("Error-->Messagetext for key:\"" + msgRef.getKey() + "\" missing");
    }
    return null;
  }

  private void assertExistMessage(MessageReference msgRef)
  {
    String text = getMessage(msgRef);
    if (text == null)
      return; //checked in getMessage

    if (text.length() == 0)
    {
      failCollector("Error-->Messagetext for key:\"" + msgRef.getKey() + "\" empty");
    }
  }

  public void CheckMessagesOfClass(Class<?> exceptionClassToBeTested)
  {
    Class<?> testClass = exceptionClassToBeTested;
    //try {

    for (Field field : testClass.getDeclaredFields())
    {
      //if field from type  MessageReference
      if (field.getType().isAssignableFrom(MessageReference.class))
      {
        //field should be final
        assertEquals(true, Modifier.isFinal(field.getModifiers()));

        MessageReference msgRef = null;
        try {
          msgRef = (MessageReference) field.get(null);
        } catch (IllegalArgumentException e) {
          failCollector("MsgRef Error--> Error: MsgRef " + field.getName() + " of class \"" + testClass.getSimpleName() + "\"");
          break;
        } catch (IllegalAccessException e) {
          failCollector("MsgRef Error--> Not public: MsgRef " + field.getName() + " of class \"" + testClass.getSimpleName() + "\"");
          break;
        }

        if (msgRef == null)
        {
          failCollector("MsgRef Error--> Not assigned: MsgRef " + field.getName() + " of class \"" + testClass.getSimpleName() + "\"");
          break;
        }

        assertExistMessage(msgRef);
      }
    }
  }

  public Vector<Throwable> getErrorCollector() {
    return errorCollector;
  }

  static public void TestClass(Class<?> exceptionClassToBeTested)
  {
    ExceptionTextsTool tool = new ExceptionTextsTool();
    tool.CheckMessagesOfClass(exceptionClassToBeTested);
    Vector<Throwable> errors = tool.getErrorCollector();

    for (Throwable throwable : errors)
    {
      fail(throwable.getMessage());
    }
  }
}