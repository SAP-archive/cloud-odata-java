package com.sap.core.odata.core.uri.expression.test;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

@SuppressWarnings("serial")
public class SampleClassForMessageReference extends ODataMessageException
{
  public SampleClassForMessageReference(MessageReference messageReference) {
    super(messageReference);
  }

  public static final MessageReference EXIST = createMessageReference(SampleClassForMessageReference.class, "EXIST");
  public static final MessageReference DOES_NOT_EXIST = createMessageReference(SampleClassForMessageReference.class, "DOES_NOT_EXIST");
  public static final MessageReference EXITS_BUT_EMPTY = createMessageReference(SampleClassForMessageReference.class, "EXITS_BUT_EMPTY");
}
