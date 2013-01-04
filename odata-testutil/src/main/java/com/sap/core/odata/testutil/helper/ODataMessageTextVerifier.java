package com.sap.core.odata.testutil.helper;

import static org.junit.Assert.assertEquals;
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
 * the tested (Exception) class  are provided in the <b>i18n.properties</b> file.
 * @author SAP AG
 */
public class ODataMessageTextVerifier {

  /**
   * Same as define in {@link MessageService}
   */
  private static final String BUNDLE_NAME = "i18n"; //$NON-NLS-1$
  private static final Locale locale = Locale.ROOT;

  public ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
  private List<Throwable> errorCollector;

  public ODataMessageTextVerifier()
  {
    errorCollector = new ArrayList<Throwable>();
  }

  private void failCollector(String text)
  {
    try {
      //System.out.println(text);
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
    } catch (MissingResourceException ex)
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

  public void CheckMessagesOfClass(Class<? extends Exception> exceptionClassToBeTested)
  {
    Class<? extends Exception> testClass = exceptionClassToBeTested;
    //try {

    for (Field field : testClass.getDeclaredFields())
    {
      //if field from type  MessageReference
      if (field.getType().isAssignableFrom(MessageReference.class))
      {
        //field should be final
        assertEquals("MsgRef Error--> Error: field should be final. ", true, Modifier.isFinal(field.getModifiers()));

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

  public List<Throwable> getErrorCollector() {
    return errorCollector;
  }

  static public void TestClass(Class<? extends Exception> exceptionClassToBeTested)
  {
    ODataMessageTextVerifier tool = new ODataMessageTextVerifier();
    tool.CheckMessagesOfClass(exceptionClassToBeTested);
    for (Throwable throwable : tool.getErrorCollector())
    {
      fail(throwable.getMessage());
    }
  }
}